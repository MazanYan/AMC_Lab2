package com.example.amclab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View;
import java.io.*


class MainActivity : AppCompatActivity() {

    var arrays: Array<Array<Double>> = Array(1,{_->Array(1, {_-> 0.0})})
    var generateState: Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }




    /**
     * state == 0 -> generates array
     * state == 1 -> sorts array
     */
    fun switchButtonState(view: View) {
        if (generateState.equals(0)) {
            generateState = 1
            button.text = "Відсортувати масиви"
            button.setOnClickListener { v -> generateSortedArr(v) }
        }
        else {
            generateState = 0
            button.text = "Згенерувати масиви"
            button.setOnClickListener { v -> generateArrOnClick(v) }
        }
    }




    fun plot(view: View) {

    }




    fun generateFromFile(view: View) {
        val filename: String = "AMC_Lab2.txt"

        try {
            // открываем поток для чтения
            val br = BufferedReader(
                InputStreamReader(
                    openFileInput(filename)
                )
            )
            var str = ""
            // читаем содержимое
            while ((br.readLine()) != null) {
                str+=br.readLine()
            }
            this.arrays = parseInputFromFile(str)
            showOnCanvas(view,parseForTextView(arrays))
//            var res = ""
//            for (i in arrays) {
//                var res_single: String = "["
//                for (j in i)
//                    res_single += "$j, "
//                res += "${res_single.dropLast(2)}]\n\n"
//            }
//            textView.text = res
//            val params: ViewGroup.LayoutParams = textView.getLayoutParams()
//            params.height = 300
//            textView.setLayoutParams(params)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }




    fun generateArrOnClick (view: View) {
        val generated: Array<Array<Double>> = generateArr()
        this.arrays = generated
        showOnCanvas(view,parseForTextView(arrays))
        switchButtonState(view)
    }




    fun generateSortedArr (view: View) {
        val sorted: Array<ShakerSort> = Array(arrays.size, { i -> ShakerSort(arrays[i]) })
        showOnCanvas(view,parseForTextView(sorted))
    }

    fun parseForTextView(array: Array<Array<Double>>) : String {
        var res = ""
        for (i in array) {
            var res_single: String = "["
            for (j in i)
                res_single += "$j, "
            res += "${res_single.dropLast(2)}]\n\n"
        }
        return res
    }

    fun parseForTextView(sortedArr: Array<ShakerSort>) : String {
        var res = ""
        for (i in sortedArr) {
            var res_single: String = "["
            for (j in i.sorted)
                res_single+="$j, "
            res+= "${res_single.dropLast(2)}] time: ${i.time} s\n\n"
        }
        return res
    }

    fun showOnCanvas(view: View, text: String) {
        textView.text = text
        val params: ViewGroup.LayoutParams = textView.getLayoutParams()
        params.height = 300
        textView.setLayoutParams(params)
    }
}
