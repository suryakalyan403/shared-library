import com.i27academy.builds.Calculator

def call(Map pipelineparams) {
    pipeline {
        agent any
 
        //Paramter Definition
        parameters{
         string(name: 'IMG_FILEPATH', defaultValue: "/var/lib/jenkins/workspace/images", description:"Path where Img exists")
         string(name: 'IMG_FILENAME', defaultValue: "base-0.4.9.tar.gz", description:"Img Name")

        }
        stages {
            stage('Docker Img Extract') {
                steps {
                    script {
                        // Pass 'steps' to the Calculator class
                        Calculator calculator = new Calculator(steps)

                        // Call the dockerImgExtract method
                        def result = calculator.dockerImgExtract(params.IMG_FILEPATH, params.IMG_FILENAME)
                        echo result

                    }
                }
            }
        }
    }
}
