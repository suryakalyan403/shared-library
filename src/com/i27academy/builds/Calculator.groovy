package com.i27academy.builds

class Calculator implements Serializable {
    def jenkins

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

    def dockerImgExtract(filePath, fileName) {
        try {
            steps.sh "docker extract -i ${filePath}/${fileName}"
            steps.echo "$fileName: Image Extracted Successfully"
        } catch (Exception e) {
            steps.error "Failed to extract image: ${e.message}"
        }
    }


}



