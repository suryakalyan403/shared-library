import com.eureka.builds.Calculator

def call(Map pipelineparams) {
    pipeline {
        agent {
            label "k8s-jenkins-slave"
        }

        environment {
            APP_NAME = "${pipelineparams.appName}"
        }

        stages {
            stage('AdditionStage') {
                steps {
                    script {
                        echo "********** Test **********"

                        // Create Calculator object
                        def calculator = new Calculator(this)

                        echo "Printing Sum of 2 Numbers"
                        echo "Result: ${calculator.add(3, 4)}"

                        echo "******** Microservice Name is: ${APP_NAME}"
                    }
                }
            }
        }
    }
}

