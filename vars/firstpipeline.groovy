import com.i27academy.builds.Calculator

def call(Map pipelineparams) {
    // No need to pass 'this' unless required by your logic
    Calculator calculator = new Calculator() 

    pipeline {
        agent any
        stages {
            stage('Addition') {
                steps {
                    script {
                        echo "Sum: ${calculator.add(3, 4)}"
                        echo "Microservice: ${pipelineparams.APP_NAME}"
                    }
                }
            }
        }
    }
}
