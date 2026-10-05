package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvHistory: TextView
    private lateinit var tvResult: TextView

    private var currentExpression = ""
    private var isNewCalculation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvHistory = findViewById(R.id.tvHistory)
        tvResult = findViewById(R.id.tvResult)

        tvResult.text = "0"
        tvHistory.text = ""

        setupNumberButtons()
        setupOperatorButtons()
        setupActionButtons()
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            R.id.btn0 to "0",
            R.id.btn1 to "1",
            R.id.btn2 to "2",
            R.id.btn3 to "3",
            R.id.btn4 to "4",
            R.id.btn5 to "5",
            R.id.btn6 to "6",
            R.id.btn7 to "7",
            R.id.btn8 to "8",
            R.id.btn9 to "9",
            R.id.btnDot to "."
        )

        for ((id, value) in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                if (isNewCalculation) {
                    currentExpression = ""
                    isNewCalculation = false
                }
                if (currentExpression == "0" && value != ".") {
                    currentExpression = value
                } else {
                    currentExpression += value
                }
                tvResult.text = currentExpression
            }
        }
    }

    private fun setupOperatorButtons() {
        val operatorButtons = listOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷",
            R.id.btnOpenBracket to "("
        )

        for ((id, value) in operatorButtons) {
            findViewById<Button>(id).setOnClickListener {
                if (isNewCalculation) {
                    isNewCalculation = false
                }
                currentExpression += value
                tvResult.text = currentExpression
            }
        }
    }

    private fun setupActionButtons() {
        // Clear (C)
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentExpression = ""
            tvResult.text = "0"
            tvHistory.text = ""
            isNewCalculation = false
        }

        // Backspace (⌫)
        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            if (currentExpression.isNotEmpty()) {
                currentExpression = currentExpression.dropLast(1)
                tvResult.text = if (currentExpression.isEmpty()) "0" else currentExpression
            }
        }

        // Equals (=)
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            if (currentExpression.isEmpty()) return@setOnClickListener
            try {
                val result = evaluateExpression(currentExpression)
                val formattedResult = formatResult(result)
                tvHistory.text = currentExpression
                tvResult.text = formattedResult
                currentExpression = formattedResult
                isNewCalculation = true
            } catch (_: Exception) {
                tvResult.text = "錯誤"
                isNewCalculation = true
            }
        }
    }

    private fun formatResult(d: Double): String {
        return if (d == d.toLong().toDouble()) {
            d.toLong().toString()
        } else {
            d.toString()
        }
    }

    private fun evaluateExpression(expr: String): Double {
        val formatted = expr.replace("×", "*").replace("÷", "/")
        return ExpressionEvaluator(formatted).parse()
    }

    private class ExpressionEvaluator(private val input: String) {
        private var pos = -1
        private var ch = 0

        private fun nextChar() {
            ch = if (++pos < input.length) input[pos].code else -1
        }

        private fun eat(charToEat: Int): Boolean {
            while (ch == ' '.code) nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < input.length) throw RuntimeException("Unexpected: " + ch.toChar())
            return x
        }

        private fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                if (eat('+'.code)) x += parseTerm()
                else if (eat('-'.code)) x -= parseTerm()
                else return x
            }
        }

        private fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                if (eat('*'.code)) x *= parseFactor()
                else if (eat('/'.code)) {
                    val denominator = parseFactor()
                    if (denominator == 0.0) throw ArithmeticException("Division by zero")
                    x /= denominator
                } else return x
            }
        }

        private fun parseFactor(): Double {
            if (eat('+'.code)) return parseFactor()
            if (eat('-'.code)) return -parseFactor()

            val x: Double
            val startPos = pos
            if (eat('('.code)) {
                x = parseExpression()
                eat(')'.code)
            } else if ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) {
                while ((ch >= '0'.code && ch <= '9'.code) || ch == '.'.code) nextChar()
                x = input.substring(startPos, pos).toDouble()
            } else {
                throw RuntimeException("Unexpected: " + ch.toChar())
            }

            return x
        }
    }
}
