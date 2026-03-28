import com.mobileum.builds.AipDeployments

def call(Map pipelineparams) {

    // Create an instance of the Aipdeployments Class
    AipDeployments aipdeployments = new AipDeployments(this)

    pipeline {
        agent any

        parameters {
            choice(
                name: 'METHODS',
                choices: ["install", "upgrade", "restart", "deploy-to-portal", "update-secret"],
                description: "Select the deployment method"
            )
            choice(
                name: 'MICROSERVICES',
                choices: ["ALL", "portal", "t00", "t50t51", "t52t53", "t54", "t55", "t56", "t80"],
                description: "Select the Microservice for the deployment"
            )
            choice(
                name: 'SATELLITE',
                choices: ['', 'satellite'],
                description: 'Select the satellite key to deploy satellite pods'
            )
            choice(
                name: 'SATMICROSERVICE',
                choices: ['', 'sat1', 'sat2', 'sat3', 'sat4', 'sat5'],
                description: "Select the satellite microservice"
            )
            choice(
                name: 'SECRETS',
                choices: ['', 'license', 'product-config'],
                description: "Select the secret (required for update-secret)"
            )
            booleanParam(
                name: 'DRY_RUN',
                defaultValue: false,
                description: 'Run in Dry-Run mode'
            )
        }

        environment {
            TERM = 'xterm'
            DEPLOYMENT_DIR = "${WORKSPACE}/../src/bin"
            MICROSERVICES = 'portal t00 t50t51 t52t53 t54 t55 t56 t80'
        }

        options {
           skipDefaultCheckout()
           timeout(time: 20, unit: 'MINUTES')
        }

        stages {

            stage('Pre-deployments Checks') {
                steps {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh """
                            which mc || true
                            export KUBECONFIG=\$KUBECONFIG
                            echo "Kubeconfig File: \$KUBECONFIG"
                            kubectl cluster-info || true
                        """
                    }
                }
            }

            stage('Checkout Code') {
                steps {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/suryakalyan403/Mobileum.git',
                            credentialsId: 'git-creds'
                        ]],
                        extensions: [[
                            $class: 'CloneOption',
                            depth: 1,
                            noTags: true,
                            shallow: true
                        ]]
                    ])
                }
            }

            stage('Select & Validate Inputs') {
                steps {
                    script {
                        // Normalize inputs
                        def method = params.METHODS?.trim()
                        def microParam = params.MICROSERVICES?.trim()
                        def selectSat = params.SATELLITE?.trim()
                        def selectSatService = params.SATMICROSERVICE?.trim()
                        def selectSecret = params.SECRETS?.trim()
                        def dryRunFlag = params.DRY_RUN ? 'dry-run' : ''

                        // Determine services to deploy (list)
                        def servicesToDeploy = (microParam == 'ALL') ?
                                env.MICROSERVICES.split() : [microParam]

                        // ----- PRE-VALIDATIONS (interactive-style messages) -----

                        // 1) deploy-to-portal should NOT be used with satellite selection
                        if (method == 'deploy-to-portal' && selectSat) {
                            error """
                                 ❗ Validation Error
                                  You selected 'deploy-to-portal' but also selected 'SATELLITE'.
                                  👉 deploy-to-portal is only applicable for application microservices, not satellites.
                                  Please deselect 'SATELLITE' and rerun the pipeline.
                                  """
                        }

                        // 2) update-secret requires a secret value
                        if (method == 'update-secret' && !selectSecret) {
                            error """
                                 ❗ Validation Error
                                  You selected 'update-secret' but did not provide a secret.
                                 👉 Please select either 'license' or 'product-config' in the SECRETS parameter and rerun the pipeline.
                                  """
                        }

                        // 3) If satellite key was selected, satellite microservice must be provided
                        if (selectSat && !selectSatService) {
                            error """
                                 ❗ Validation Error
                                  You selected 'SATELLITE' but did not select the satellite microservice (SATMICROSERVICE).
                                  👉 Please select sat1/sat2/sat3/sat4/sat5 and rerun the pipeline.
                                  """
                        }

                        // 4) Sanity for method value (future-proofing)
                        def validMethods = ['install', 'upgrade', 'restart', 'deploy-to-portal', 'update-secret']
                        if (!validMethods.contains(method)) {
                            error "❗ Validation Error - Invalid method selected: ${method}"
                        }

                        // 5) Check there is at least one service to deploy
                        if (!servicesToDeploy || servicesToDeploy.size() == 0) {
                            error "❗ Validation Error - No microservices found to deploy. Check MICROSERVICES environment or parameter."
                        }

                        // Store normalized values for later steps (keeps block tidy)
                        // Using script-binding variables so later steps inside this script have access:
                        env.__METHOD = method
                        env.__SELECT_SAT = selectSat ?: ''
                        env.__SELECT_SAT_SVC = selectSatService ?: ''
                        env.__SELECT_SECRET = selectSecret ?: ''
                        env.__DRY_RUN_FLAG = dryRunFlag
                        env.__SERVICES = servicesToDeploy.join(',')
                    }
                }
            }

            stage('Deploy MicroServices') {
                steps {
                    script {
                        // Rehydrate values from environment/script variables
                        def method = env.__METHOD
                        def selectSat = env.__SELECT_SAT
                        def selectSatService = env.__SELECT_SAT_SVC
                        def selectSecret = env.__SELECT_SECRET
                        def dryRunFlag = env.__DRY_RUN_FLAG
                        def servicesToDeploy = env.__SERVICES.split(',')

                        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                            servicesToDeploy.each { service ->
                                // Create a visible stage per-service
                                stage("Deploy ${service}") {
                                    try {
                                        echo "Building risk-man.sh command for ${service}"
                                        def cmdParts = ['bash', 'risk-man.sh', method, service]

                                        if (selectSat) {
                                            cmdParts << selectSat
                                            // selectSatService validated earlier
                                            cmdParts << selectSatService
                                        }

                                        if (method == 'update-secret') {
                                            // selectSecret validated earlier
                                            cmdParts << selectSecret
                                        }

                                        if (params.DRY_RUN) {
                                            cmdParts << dryRunFlag
                                        }

                                        def finalCmd = cmdParts.join(' ')
                                        echo "Generated Command: ${finalCmd}"

                                        sh """
                                            cd ${env.DEPLOYMENT_DIR}
                                            export TERM=${env.TERM}
                                            export KUBECONFIG=\$KUBECONFIG
                                            echo 'y' | ${finalCmd}
                                        """
                                        echo "✅ Completed deployment command for ${service}"
                                    } catch (Exception e) {
                                        error "❌ Failed to deploy ${service}: ${e.getMessage()}"
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } // end stages

        post {
            success {
                echo "✅ Successful deployment: ${params.MICROSERVICES} - ${env.BUILD_URL}"
            }
            failure {
                echo "❌ Deployment failed: ${params.MICROSERVICES} - ${env.BUILD_URL}"
                archiveArtifacts artifacts: '**/*.log', allowEmptyArchive: true
                
                emailext (
                    subject: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                    body: """
                    Build Failed.

                    Job: ${env.JOB_NAME}
                    Build Number: ${env.BUILD_NUMBER}
                    URL: ${env.BUILD_URL}
                    """,
                    to: "learngcpwithkalyan@gmail.com"
                )  
              
            }
            always {
                cleanWs()
            }

        }

    } // end pipeline
}

