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

                             def result = calculator.dockerImgExtract("/home/rsoni/base/0.4.9/images", "base-0.4.9.tar.gz")
                             echo result

                             echo "Sum: ${calculator.add(3, 4)}"
                             echo "Microservice: ${pipelineparams.APP_NAME}"
                    }
                }
            }
        }
    }
}
