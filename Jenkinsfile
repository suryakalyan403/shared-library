// This is Jenkinsfile for Test Project 
pipeline {

  agent any

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

  }
}

