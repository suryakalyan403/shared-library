// This is Jenkinsfile for Test Project 
pipeline {

  agent {
  docker {
    image 'docker:24.0.0.1'
    args '-v /var/run/docker.sock:/var/run/docker.sock'
    }
  }


  stages {

    stage('Build') {
      steps {
        echo "***** Initializing the first pipeline ********"
      }
    }

    stage('Cat README') {
      when {
        branch pattern: "fix-.*", comparator: "REGEXP"
      }
      steps {
        sh '''
          cat README.md
        '''
      }
    }

   stage("Docker Login"){
     steps {
     sh '''
        docker login -u 7981689475 -p rsghios@1458 
        '''
    }
    }
  }
}

