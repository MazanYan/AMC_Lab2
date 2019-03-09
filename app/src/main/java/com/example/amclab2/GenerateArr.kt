package com.example.amclab2

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @return 10 arrays of different size with random double numbers in range [0,1] rounded to 4 decimal points
 */
fun generateArr() : Array<Array<Double>>{
    val doubleFormat = DecimalFormat("#.####")
    doubleFormat.roundingMode = RoundingMode.CEILING
    return Array(10, {i ->
        Array(5+i*i*5, {j ->
            doubleFormat.format(kotlin.random.Random.nextDouble()).toDouble()})})
}