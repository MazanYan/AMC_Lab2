package com.example.amclab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var arrays: Array<Array<Double>> = Array(1,{i->Array(1, {j-> 0.0})})
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun generateArrOnClick (view: View) {
        val generated: Array<Array<Double>> = generateArr()
        var res = ""
        for (i in generated) {
            var res_single: String = "["
            for (j in i)
                res_single+="$j, "
            res+= "${res_single.dropLast(2)}]\n\n"
        }
        this.arrays = generated
        textViewGenerated.text = res
        val params: ViewGroup.LayoutParams = textViewGenerated.getLayoutParams()
        params.height = 300
        textViewGenerated.setLayoutParams(params)
    }

    fun generateSortedArr (view: View) {
        val sorted: Array<ShakerSort> = Array(arrays.size, { i -> ShakerSort(arrays[i]) })
        var res = ""
        for (i in sorted) {
            var res_single: String = "["
            for (j in i.sorted)
                res_single+="$j, "
            res+= "${res_single.dropLast(2)}] time: ${i.time} s\n\n"
        }
        textViewSorted.text = res
        val params: ViewGroup.LayoutParams = textViewSorted.getLayoutParams()
        params.height = 300
        textViewSorted.setLayoutParams(params)
    }

    fun plot(view: View) {

    }
}
