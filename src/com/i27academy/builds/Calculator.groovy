package com.i27academy.builds

class Calculator implements Serializable {
    def steps

    Calculator(steps) {
        this.steps = steps
    }

    // Addition Method
    def add(n1, n2) {
        return n1 + n2
    }

    // Subtraction Method
    def sub(n1, n2) {
        return n1 - n2
    }

    // Multiplication Method
    def mul(n1, n2) {
        return n1 * n2
    }

    // Division Method
    def div(n1, n2) {
        return n1 / n2
    }

    def dockerLogin(user, password, registry_url) {

        try {

        steps.sh "echo ${password} | docker login -u ${user} --password-stdin ${registry_url}" 
        return "Successfully Login to Registry: ${registry_url}" 
        }
        catch (Exception e) { 
            steps.error "Failed to login: ${e.message}"
        
        }
         

   }


    def dockerImgExtract(filePath, fileName) {
        try {
            steps.sh "docker load -i ${filePath}/${fileName}"
            return "$fileName: Image Extracted Successfully"
        } catch (Exception e) {
            steps.error "Failed to extract image: ${e.message}"
        }
    }

   def dockerPushImgToRegistry(local_img_name,registry_url,img_name,tag) {

    try {

         steps.sh "docker tag ${local_img_name}:${tag} ${registry_url}/${img_name}:${tag}"
         steps.sh "docker push ${registry_url}/${img_name}:${tag}"
         return "Image pulled successfully: ${registry_url}/${img_name}:${tag}"

    }
    catch (Exception e) {
   
          steps.error "Failed to push image image: ${e.message}"

    }

   }

}
