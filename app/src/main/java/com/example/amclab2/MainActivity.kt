package com.example.amclab2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View;
import android.widget.Toast
import java.io.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var arrays: Array<Array<Double>> = Array(1,{_->Array(1, {_-> 0.0})})
    var filePath: String? = null
    val fileName: String = "AMC_Lab2.txt"
    val baseFile: String = """
-13.4 4 -10 0
1 2 3 -8 5
2 14 -13 26 145.5 16.04

/*
    EVERYTHING AFTER A COMMENT SIGN ISN'T READ
    the input is a bunch of arrays with numbers delimited by single space. Each array starts from a new line
    EG:
-13.4 4 -10 0
1 2 3 -8 5
2 14 -13 26 145.5 16.04
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filePath = getExternalFilesDir(null).absolutePath.toString()
        pathToFile.text="$filePath/$fileName - path to data file to edit"
        if (!File("$filePath/$fileName").exists())
            initialFileCreate()
    }




    fun plot(view: View) {

    }




    fun initialFileCreate() {
        Toast.makeText(this, "The initial file didn't exist before and has been automatically created", Toast.LENGTH_SHORT).show()
        val initialFile: File = File(filePath+"/"+fileName)
        PrintWriter(initialFile).use { out -> out.println(baseFile) }
//        baseContext.openFileOutput(filePath+"/"+fileName, Context.MODE_PRIVATE).use {
//            it.write((baseFile.toByteArray()))
//        }
    }
    fun generateFromFile(view: View) {
        try {
            val sdPath = File(filePath + "/" + fileName)
            if (!sdPath.exists())
                initialFileCreate()
            val stringSdcardPath: String = File(sdPath.absolutePath.toString().plus("/")).toString()
            val file: File = File(stringSdcardPath)
            try {
                val reader = BufferedReader(FileReader(file))
                var str = ""
                var line: String? = ""
                while (line != null) {
                    line = reader.readLine()
                    str += line.plus("\n")
                }
                this.arrays = parseInputFromFile(str)
                showOnCanvas(view, parseForTextView(arrays))
                button3.setOnClickListener({ v -> generateSortedArr(v) })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        catch (e: FileNotFoundException) {
            Toast.makeText(this, "The input file has been deleted!", Toast.LENGTH_SHORT).show()
        }
    }




    fun generateArrOnClick (view: View) {
        val generated: Array<Array<Double>> = generateArr()
        this.arrays = generated
        showOnCanvas(view,parseForTextView(arrays))
        button3.setOnClickListener({v -> generateSortedArr(v)})
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
