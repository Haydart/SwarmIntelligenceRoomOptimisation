package visualization

import evaluation.GenerationStatistics
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage

private const val SCREEN_WIDTH = 800.0
private const val SCREEN_HEIGHT = 600.0

class ChartWindow {

    lateinit var stage: Stage

    fun drawChart(statistics: MutableList<GenerationStatistics>) {
        stage = Stage()
        stage.title = "Statistics"
        val xAxis = NumberAxis()
        val yAxis = NumberAxis()
        xAxis.label = "Generation Number"
        yAxis.label = "Fitness"

        val lineChart = LineChart(xAxis, yAxis)

        lineChart.title = "Result Chart"
        lineChart.createSymbols = false // no dots

        //worst
        val seriesWorst = XYChart.Series<Number, Number>()
        seriesWorst.name = "Worst result"

        //avg
        val seriesAvg = XYChart.Series<Number, Number>()
        seriesAvg.name = "Avg result"

        // best
        val seriesBest = XYChart.Series<Number, Number>()
        seriesBest.name = "Best result"

        // data
        for (s in statistics) {
            seriesWorst.data.add(XYChart.Data(s.generationNumber, s.worst))
            seriesAvg.data.add(XYChart.Data(s.generationNumber, s.avg))
            seriesBest.data.add(XYChart.Data(s.generationNumber, s.best))
        }

        val scene = Scene(lineChart, SCREEN_WIDTH, SCREEN_HEIGHT)
        lineChart.data.add(seriesWorst)
        lineChart.data.add(seriesAvg)
        lineChart.data.add(seriesBest)

        stage.scene = scene
        stage.show()
    }
}