package visualization

import evaluation.GenerationStatistics
import gAlgorithm
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import swarm.Individual
import swarm.SwarmAlgorithm

/**
 * Created by r.makowiecki on 14/04/2018.
 */

const val SCENE_WIDTH = 1280.0
const val SCENE_HEIGHT = 768.0

const val CANVAS_WIDTH = 1200.0
const val CANVAS_HEIGHT = 1000.0

const val HIST_IND_VIS_COUNT = 40
const val HIST_IND_IN_ROW = 5
const val HIST_IND_IN_COL = 5

const val SLIDER_WIDTH = 1000.0

class VisualizationWindow : Application() {

    lateinit var boardRoot: BorderPane
    lateinit var roomGraphicContext: GraphicsContext
    lateinit var historySlider: Slider
    var currentHistoryFrame: Int = 0

    var initBestIndividual: Individual? = null
    var lastGlobalBestIndividual: Individual? = null
    var lastHistoryData: MutableList<MutableList<Individual>> = mutableListOf()
    val lastRunStatistics: MutableList<GenerationStatistics> = mutableListOf()

    val furnitureColor: Color = Color.CORNFLOWERBLUE
    val initFurnitureColor: Color = Color.CORAL

    override fun start(primaryStage: Stage) {
        initUI(primaryStage)
    }

    fun updateRoomVis() {
        gAlgorithm?.let {
            val gc = roomGraphicContext
            gc.clearRect(0.0, 0.0, CANVAS_WIDTH, CANVAS_HEIGHT)
            drawRoomBounds(gc, it, 0.0, 0.0)
            if (initBestIndividual != null) {
                drawFurniturePieces(gc, initBestIndividual!!, initFurnitureColor, 0.0, 0.0)
            }
            if (lastGlobalBestIndividual != null) {
                drawFurniturePieces(gc, lastGlobalBestIndividual!!, furnitureColor, 0.0, 0.0)
            }

            if (lastHistoryData.size >= currentHistoryFrame && currentHistoryFrame > 0) {
                val currentIterationData = lastHistoryData[currentHistoryFrame - 1]
                (0 until HIST_IND_VIS_COUNT).forEach {i ->
                    drawRoomBounds(gc, it, (i / HIST_IND_IN_ROW) * it.roomWidth,
                            (i % HIST_IND_IN_COL + 1) * it.roomHeight)
                    drawFurniturePieces(gc, currentIterationData[i], furnitureColor, (i / HIST_IND_IN_ROW) * it.roomWidth,
                            (i % HIST_IND_IN_COL + 1) * it.roomHeight)
                }
            }
        }
    }

    private fun drawRoomBounds(gc: GraphicsContext, it: SwarmAlgorithm, offsetX: Double, offsetY: Double) {
        gc.stroke = Color.BLACK
        gc.lineWidth = 4.0
        gc.strokeRect(0.0 + offsetX, 0.0 + offsetY, it.roomWidth, it.roomHeight)
    }

    private fun drawFurniturePieces(gc: GraphicsContext, individual: Individual, color: Color, offsetX: Double, offsetY: Double) {
        gc.stroke = color
        gc.lineWidth = 2.0
        individual.coords.forEachIndexed { index, (x, y) ->
            val furniturePiece = individual.room.furnitureList[index]
            gc.strokeRect(x - furniturePiece.width / 2 + offsetX, y - furniturePiece.height / 2 + offsetY,
                    furniturePiece.width,  furniturePiece.height)
        }
    }

    fun initUI(stage: Stage) {

        boardRoot = BorderPane()

        initButtonsPanelUI()
        initRoomVisUI()
        gAlgorithm?.let {
            initBestIndividual = it.getBestIndividual().deepCopy()
            updateRoomVis()
        }

        val scene = Scene(boardRoot, SCENE_WIDTH, SCENE_HEIGHT)

        stage.title = "Room Optimizer 3000"
        stage.scene = scene
        stage.show()
    }

    private fun initButtonsPanelUI() {
        val buttonsPanel = HBox(10.0)

        val startBtn = Button("Begin!")
        startBtn.setOnAction({
            lastHistoryData.clear()
            lastRunStatistics.clear()
            lastGlobalBestIndividual = (gAlgorithm?.runOptimisation(lastHistoryData, lastRunStatistics))!!.deepCopy()
            updateRoomVis()
            historySlider.max = lastHistoryData.size.toDouble()
            if (lastHistoryData.size  > 0) {
                historySlider.majorTickUnit = (lastHistoryData.size / 50).toDouble()
            }
            ChartWindow().drawChart(lastRunStatistics)
        })
        buttonsPanel.children.add(startBtn)

        historySlider = Slider(0.0, 0.0, 0.0)
        historySlider.isShowTickLabels = true
        historySlider.isShowTickMarks = true
        //historySlider.isSnapToTicks = true
        historySlider.prefWidth = SLIDER_WIDTH

        historySlider.valueProperty().addListener({ _, _, new_val ->
            if (new_val.toInt() != currentHistoryFrame) {
                currentHistoryFrame = new_val.toInt()
                updateRoomVis()
            }
        })
        buttonsPanel.children.add(historySlider)

        boardRoot.top = buttonsPanel
    }

    private fun initRoomVisUI() {
        val canvas = Canvas(CANVAS_WIDTH, CANVAS_HEIGHT)
        roomGraphicContext = canvas.graphicsContext2D
        boardRoot.center = canvas

    }

    fun launchWindow(args: Array<String>) {
        Application.launch(*args)
    }
}