package com.example.amclab2

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View;
import android.widget.Toast
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.DataPointInterface
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import java.io.*
import java.lang.Exception
import android.icu.lang.UCharacter.DecompositionType.CIRCLE



class MainActivity : AppCompatActivity() {

    var arrays: Array<Array<Double>> = Array(1,{_->Array(1, {_-> 0.0})})
    var plotVals: MutableMap<Int, Double> = mutableMapOf()
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



//    object Graph {
//        var graph: GraphView? = null
//        fun create(context: Context){
//            graph = GraphView(context)
//        }
//    }




    fun interpolateOmicronSquare() : (x:Double) -> Double {
        val valsList = if (plotVals == null) return {x:Double -> 0.0} else plotVals
        /**
         * size, time - size and sort time for the biggest test array
         * k*size^2 = time
         * k = time/(size^2)
         */
        val maxSize: Int? = valsList?.keys!!.max()
        val maxTime: Double? = valsList.get(maxSize)!!.toDouble()
        val maxSizePow2: Double = if (maxSize == null) 1.0 else Math.pow(maxSize.toDouble(), 2.0)
        val maxTimeSet: Double = if (maxTime == null) 0.0 else maxTime
        val k: Double = maxTimeSet/maxSizePow2
        return {x:Double -> k*x*x}
    }
    fun plot(view: View) {
        graph.removeAllSeries()
        graph.visibility = GraphView.VISIBLE
        if(plotVals == mutableMapOf<Int,Double>()) {
            Toast.makeText(
                this,
                "Please sort the array first!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        else {
            //tested arrays
            val pts = plotVals
            val max: Int? = pts.keys.max()
            val maxSize: Int = if (max == null) 1 else max
            val graphPoints: Array<DataPoint> = pts.map({key -> DataPoint(key.key.toDouble(),key.value)}).toTypedArray()
            val series: PointsGraphSeries<DataPointInterface> = PointsGraphSeries(graphPoints)

            //interpolated O(n^2)
            val function = interpolateOmicronSquare()
            val squarePlotData: Array<DataPoint> = (0..(if (maxSize == null) 1 else maxSize) step 1).map({ it -> DataPoint(it.toDouble(),function(it.toDouble()))}).toTypedArray()
            val interpolatedPlot: LineGraphSeries<DataPointInterface> = LineGraphSeries(squarePlotData)

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMaxY(squarePlotData.last().y+0.05)
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMaxX(squarePlotData.last().x+100)
            series.setShape(PointsGraphSeries.Shape.RECTANGLE)
            series.setSize(5.0f)
            series.setColor(Color.rgb(215,0,36))
            graph.addSeries(series)
            interpolatedPlot.setColor(Color.BLUE)
            graph.addSeries(interpolatedPlot)

        }
    }




    fun initialFileCreate() {
        Toast.makeText(
            this,
            "The initial file didn't exist before and has been automatically created",
            Toast.LENGTH_SHORT
        ).show()
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

        fun add (sum: MutableMap<Int,Double>, el:ShakerSort) : MutableMap<Int,Double> {
            sum.put(el.arr.size, el.time)
            return sum
        }
        plotVals = sorted.fold(mutableMapOf(), {sum,el -> add(sum,el)})
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
