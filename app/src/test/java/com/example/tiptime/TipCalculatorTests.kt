package com.example.tiptime

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.text.NumberFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TipCalculatorTests {

    @Test
    fun calculateTip_20PercentNoRoundUp() {
        val amount = 10.0
        val tipPercent = 20.0
        val roundUp = false
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        val actualTip =
            calculateTip(billAmount = amount, tipPercent = tipPercent, roundUp = roundUp)
        assertEquals(expectedTip, actualTip)

    }

    @Test
    fun calculateTip_21PercentRoundUp() {
        val amount = 10.0
        val tipPercent = 21.0
        val roundUp = true
        val expectedTip = NumberFormat.getCurrencyInstance().format(2.00)

        val actualTip =
            calculateTip(billAmount = amount, tipPercent = tipPercent, roundUp = roundUp)
        assertEquals(expectedTip, actualTip)

    }


}