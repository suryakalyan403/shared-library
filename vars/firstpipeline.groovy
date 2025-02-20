import com.i27academy.builds.Calculator

def call(Map pipelineparams) {
    pipeline {
        agent any
        stages {
            stage('Addition') {
                steps {
                    script {
                        // Pass 'steps' to the Calculator class
                        Calculator calculator = new Calculator(steps)

                        // Call the dockerImgExtract method
                        def result = calculator.dockerImgExtract("/home/rsoni/base/0.4.9/images", "base-0.4.9.tar.gz")
                        echo result

                        // Call the add method
                        echo "Sum: ${calculator.add(3, 4)}"
                        echo "Microservice: ${pipelineparams.APP_NAME}"
                    }
                }
            }
        }
    }
}
