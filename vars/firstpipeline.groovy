import com.i27academy.builds.Calculator

def call(Map pipelineparams) {
    pipeline {
        agent any
 
        //Paramter Definition
        parameters{
         string(name: 'IMG_FILEPATH', defaultValue: "/var/lib/jenkins/workspace/images", description:"Path where Img exists")
         string(name: 'IMG_FILENAME', defaultValue: "base-0.4.9.tar.gz", description:"Img Name")
         string(name: 'LOC_IMG_NAME', defaultValue: "aip/base", description: "Local Img Name")
         string(name: 'IMG_NAME', defaultValue: "base", description: "API Image Name")
         string(name: 'REGISTRY_URL', defaultValue: '192.168.100.176:5000', description: "Name of the Registry")
         string(name: 'TAG', defaultValue: '0.4.9', description: "Describe the version of the Image")

        }
        stages {
            stage('Docker Img Extract') {
                steps {
                    script {
                        // Pass 'steps' to the Calculator class
                        Calculator calculator = new Calculator(steps)

                        // Call the dockerImgExtract method
                        //def result = calculator.dockerImgExtract(params.IMG_FILEPATH, params.IMG_FILENAME)
                        def result = calculator.dockerPushImgToRegistry(params.LOC_IMG_NAME, params.REGISTRY_URL,params.IMG_NAME,params.IMG_NAME,params.TAG)
                        echo result

                    }
                }
            }
        }
    }
}
