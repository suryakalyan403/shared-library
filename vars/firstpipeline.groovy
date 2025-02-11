// import the calculator class from the package co.i27academy.build

import com.i27academy.builds.Calculator


def call(Map pipelineparams){

    Calculator calculator = new com.i27academy.builds.Calculator(this)


    pipeline {

      stages{

        stage('Addition'){

          steps {

            script {

              echo "Printing the sum of two numbers"

              println calculator.add(3,4)
                
              echo "**** Microservice Name is: ${APP_NAME}"

            }

           }

         }
       }

     }

  }

