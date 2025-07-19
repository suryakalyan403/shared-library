// This is Jenkinsfile for Test Project 
pipeline {

  agent {
    kubernetes {
      label 'docker-agent'
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: docker
spec:
  containers:
    - name: docker
      image: 7981689475/jenkins-docker-agent:latest
      command:
        - cat
      tty: true
      volumeMounts:
        - name: docker-sock
          mountPath: /var/run/docker.sock
  volumes:
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
"""
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

