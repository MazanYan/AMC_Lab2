package com.example.amclab2

import java.math.RoundingMode
import java.text.DecimalFormat
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.view.ViewGroup
//import kotlinx.android.synthetic.main.activity_main.*
//import java.io.File
//import java.io.FileInputStream

/**
 * @return 10 arrays of different size with random double numbers in range [0,1] rounded to 4 decimal points
 */
fun generateArr() : Array<Array<Double>> {
    val doubleFormat = DecimalFormat("#.####")
    doubleFormat.roundingMode = RoundingMode.CEILING
    return Array(10, {i ->
        Array(5+i*i*5, {_ ->
            doubleFormat.format(kotlin.random.Random.nextDouble()).toDouble()})})
}

fun parseInputFromFile(src: String) : Array<Array<Double>> {
    val split: List<String> = src.split("\n").filter({i -> i != ""})
    val lines_length: Int = split.size
    var comment_line_begin: Int = 0
    var comment_line_end: Int = 0
    if (split.contains("/*"))
        comment_line_begin = split.indexOf("/*")+1
    else
        comment_line_begin = split.size
    val array: List<List<Double>> = split.dropLast(lines_length-comment_line_begin+1).map({ s -> s.split(" ").map { i -> i.toDouble() } })
    return array.map({e -> e.toTypedArray()}).toTypedArray()
}