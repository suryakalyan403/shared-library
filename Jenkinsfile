// This is Jenkinsfile for Test Project 

pipeline {

  agent any

  // stages

  stages {

    stage ('Build') {
     
      steps {

         echo "***** Intializing the first pipeline ********"
      }
     
    }

   }

   stage ('cat README'){
     when {
   
       branch "fix-*"

     }
     steps {
    
       sh ''' 

          cat Readme.md

         '''

     }

  }

}
