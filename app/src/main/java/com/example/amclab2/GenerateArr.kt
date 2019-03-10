package com.example.amclab2




/**
 * @input Unit
 * @return 10 arrays of different size with random double numbers in range [-100, 100] rounded to 0 decimal points
 */
fun generateArr() : Array<Array<Double>> {
    return Array(10, {i -> Array(5+i*i*20, {j -> Math.round((Math.random()-0.5)*200).toDouble()})})
}




/**
 * @input string representation of a file with lines connected by \n
 * @return 10 arrays of different size with random double numbers in range [0,1] rounded to 4 decimal points
 */
fun parseInputFromFile(src: String) : Array<Array<Double>> {
    val split: List<String> = src.split("\n").filter({i -> i != ""})
    val lines_length: Int = split.size
    val comment_line_begin: Int?
    if (split.contains("/*"))
        comment_line_begin = split.indexOf("/*")+1
    else
        comment_line_begin = split.size
    //delete everything after comment sign
    val array: List<List<Double>> = split.dropLast(lines_length-comment_line_begin+1).map({ s -> s.split(" ").map { i -> i.toDouble() } })
    return array.map({e -> e.toTypedArray()}).toTypedArray()
}