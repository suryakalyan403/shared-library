package com.i27academy.builds

class Calculator implements Serializable {
    def jenkins

    Calculator(jenkins) {
        this.jenkins = jenkins
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


    def dockerImgExtract(filePath, fileName) {
    // Ensure the script is running in a Jenkins Pipeline context
    if (!binding.hasVariable('steps')) {
        throw new IllegalStateException("This method must be called from a Jenkins Pipeline context.")
    }

    try {
        // Use the 'sh' step to execute the docker command
        steps.sh "docker extract -i ${filePath}/${fileName}"
        return "$fileName: Image Extracted Successfully"
    } catch (Exception e) {
        // Use the 'error' step to fail the pipeline with a custom message
        steps.error "Failed to extract image: ${e.message}"
    }
}



}

}

