package visualization

import evaluation.GenerationStatistics
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage

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
        seriesWorst.setName("Worst result")

        //avg
        val seriesAvg = XYChart.Series<Number, Number>()
        seriesAvg.setName("Avg result")

        // best
        val seriesBest = XYChart.Series<Number, Number>()
        seriesBest.setName("Best result")

        // datas
        for (s in statistics) {
            seriesWorst.getData().add(XYChart.Data(s.generationNumber, s.worst))
            seriesAvg.getData().add(XYChart.Data(s.generationNumber, s.avg))
            seriesBest.getData().add(XYChart.Data(s.generationNumber, s.best))
        }

        val scene = Scene(lineChart, 800.0, 600.0)
        lineChart.data.add(seriesWorst)
        lineChart.data.add(seriesAvg)
        lineChart.data.add(seriesBest)

        stage.scene = scene
        stage.show()
    }
}