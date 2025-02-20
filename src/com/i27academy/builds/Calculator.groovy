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

        docker extract -i filePath/fileName

        return "$fileName: Image Extracted Successfully"
    }


}

