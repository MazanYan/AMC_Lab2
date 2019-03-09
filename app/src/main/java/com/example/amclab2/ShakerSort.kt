package com.example.amclab2

class ShakerSort {

    val time: Double
    val arr: Array<Double>
    val sorted:  Array<Double>
    constructor (array:  Array<Double>) {
        this.arr = array
        val beginTime: Long = System.currentTimeMillis()
        /**
         * sort array
         */
        this.sorted = sort()
        val endTime: Long = System.currentTimeMillis()
        this.time = (endTime-beginTime)/Math.pow(10.0,3.0)
    }

    //sort by ascending
    fun sort() : Array<Double> {
        var sortedArr:  Array<Double> = arr
        var left: Int = 1
        var right: Int = arr.size - 1
        var k: Int = arr.size - 1
        do {
            for (j: Int in right downTo left) {
                if (sortedArr[j - 1] > sortedArr[j]) {
                    val t: Double = sortedArr[j - 1]
                    sortedArr[j - 1] = sortedArr[j]
                    sortedArr[j] = t
                    k = j
                }
                left = k + 1
            }
            for (j: Int in left..right) {
                if (sortedArr[j - 1] > sortedArr[j]) {
                    val t: Double = sortedArr[j - 1]
                    sortedArr[j - 1] = sortedArr[j]
                    sortedArr[j] = t
                    k = j
                }
                right = k - 1
            }
        } while(left<=right)
        return sortedArr
    }

}