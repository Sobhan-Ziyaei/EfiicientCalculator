package com.efiicientcalculator

import java.util.*

class Expression(var infixExpression: MutableList<String>) {
    private var postfix: String = ""
    private fun infixToPostfix() {

        var result = ""
        var stack = Stack<String>()
        for (element in infixExpression) {

            //"123" -> check for Digit or No : element.all { it.isDigit() }
            //"1.2" -> check for Digit or No : element.any { it == '.' })
            if (element.all { it.isDigit() } || element.any { it == '.' }) {

                //space after element because of Double Number
                result += "$element "
            } else if (element == "(") {
                stack.push(element)
            } else if (element == ")") {
                while (element != "(" && element.isNotEmpty()) {
                    result += "${stack.pop()} "
                }
                // this if for delete "("
                if (element.isNotEmpty()) {
                    stack.pop()
                }
            } else {
                while (stack.isNotEmpty() && precedence(stack.peek()) >= precedence(element)) {
                    result += "${stack.pop()} "
                }
                stack.push(element)
            }
        }
        while (stack.isNotEmpty()) {
            result += "${stack.pop()} "
        }
        postfix = result
    }

    private fun precedence(operator: String): Int {
        return when (operator) {
            "×", "÷" -> 2
            "+", "-" -> 1
            else -> -1
        }
    }

    fun evaluateExpression(): Number {
        infixToPostfix()
        var i = 0
        val stack = Stack<Double>()
        while (i < postfix.length) {
            if (postfix[i] == ' ') {
                i++
                continue
            } else if (Character.isDigit(postfix[i])) {
                var number = ""
                while (Character.isDigit(postfix[i]) || postfix[i] == '.') {
                    number += postfix[i]
                    i++
                }
                stack.push(number.toDouble())
            } else {
                val x = stack.pop()
                val y = stack.pop()
                when (postfix[i]) {
                    '×' -> stack.push(x * y)
                    '÷' -> stack.push(y / x)
                    '+' -> stack.push(x + y)
                    '-' -> stack.push(y - x)
                }
            }
            i++
        }
        return if (stack.peek() / stack.peek().toInt() == 1.0) {
            stack.peek().toInt()
        } else {
            stack.peek()
        }
    }
}