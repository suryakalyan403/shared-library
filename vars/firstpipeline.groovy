import com.eureka.builds.Calculator

def call(Map pipelineparams) {
    pipeline {
        agent {
        label "k8s-jenkins-slave" }

        environment {

            APP_NAME = "${pipelineparams.appName}"

        } 
        stages {
            stage('AdditionStage') {
                steps {
                    script {
                        // Use withCredentials to securely inject Docker credentials
                        echo "Printing Sum of 2 Numbers"
                        println calculator.add(3,4)
                        echo "******** Microservice Name is: ${APP_NAme}"
                        
                        }
                    }
                }
            }
        }
    }

