package com.eureka.builds

// Calculator class with basic arithmetic methods
class Docker {
    def jenkins

    Docker(jenkins) {
        this.jenkins = jenkins
    }

    // Addition Method
    def buildApp(appName) {
        jenkins.sh """
           echo 'Building the $appName Application'
           mvn clean package -DskipTests=true
        """
    }

}
