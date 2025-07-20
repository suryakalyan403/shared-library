// This is Jenkinsfile for Test Project 
pipeline {

  agent {
    kubernetes {
      label 'docker-agent'
      defaultContainer 'docker'
      yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: docker
spec:
  containers:
    - name: docker
      image: 7981689475/jenkins-docker-agent:v2.5.0
      env:
      - name: SKP_REG_URL
        value: "r.raid.cloud"
      - name: REG_URL
        value: "rl.raid.cloud"
      - name: IMG_NAME
        value: "base"
      - name: TAG
        value: "0.5.4"
      - name: MDS_ID
        value: mis_55fc977d56cb8851ff3722f4f1c1a2f4ccee42b0e9770e192cac9197217ffc71
      - name: MDS_SECRET
        value: aba13b9bc02f028bc647c63819dddd99eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpZCI6Im1pc181NWZjOTc3ZDU2Y2I4ODUxZmYzNzIyZjRmMWMxYTJmNGNjZWU0MmIwZTk3NzBlMTkyY2FjOTE5NzIxN2ZmYzcxIiwiZW1haWwiOiJTdXJ5YS5LYWx5YW5AbW9iaWxldW0uY29tIiwibXIiOiJwcmQtYWRtIiwiZXhwIjoxNzUzMDM1OTA2fQ.QXYInxUaVVEGs-6kG-I_VRbBh94oB-qdy3LnpWZdUOF6oL9v6c4tqA5yF_-n91VHvCNVJfybdXjc-8X-FjNdUw
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
         cat /opt/mds-docker.sh
         /opt/mds-docker.sh 
        '''
    }
    }
  }
}

