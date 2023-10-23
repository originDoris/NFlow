package com.doris.nflow.api

/**
 * 表达式解析器
 * @author xhz
 */

static def calculateSum(a, b) {
    def sum = a + b
    if (sum > 10) {
        return "Sum is greater than 10"
    } else {
        return sum
    }
}
static void main(String[] args) {

    def result = calculateSum(5, 6)
    println "result = $result"
}
