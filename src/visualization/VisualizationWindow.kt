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

    private lateinit var boardRoot: BorderPane
    private lateinit var roomGraphicContext: GraphicsContext
    private lateinit var generationHistorySlider: Slider
    private var currentHistoryFrame: Int = 0

    private var initialBestIndividual: Individual? = null
    private var lastGlobalBestIndividual: Individual? = null
    private var lastHistoryData: MutableList<MutableList<Individual>> = mutableListOf()
    private val lastRunStatistics: MutableList<GenerationStatistics> = mutableListOf()

    private val furnitureColor: Color = Color.CORNFLOWERBLUE
    private val initFurnitureColor: Color = Color.CORAL

    override fun start(primaryStage: Stage) {
        initUI(primaryStage)
    }

    private fun updateRoomVisualization() {
        gAlgorithm.let {
            val gc = roomGraphicContext
            gc.clearRect(0.0, 0.0, CANVAS_WIDTH, CANVAS_HEIGHT)
            drawRoomBounds(gc, it, 0.0, 0.0)
            if (initialBestIndividual != null) {
                drawFurniturePieces(gc, initialBestIndividual!!, initFurnitureColor, 0.0, 0.0)
            }
            if (lastGlobalBestIndividual != null) {
                drawFurniturePieces(gc, lastGlobalBestIndividual!!, furnitureColor, 0.0, 0.0)
            }

            if (lastHistoryData.size >= currentHistoryFrame && currentHistoryFrame > 0) {
                val currentIterationData = lastHistoryData[currentHistoryFrame - 1]
                (0 until HIST_IND_VIS_COUNT).forEach { i ->
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
                    furniturePiece.width, furniturePiece.height)
        }
    }

    private fun initUI(stage: Stage) {

        boardRoot = BorderPane()

        initButtonsPanelUI()
        initRoomVisUI()
        gAlgorithm.let {
            initialBestIndividual = it.getBestIndividual().deepCopy()
            updateRoomVisualization()
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
            lastGlobalBestIndividual = (gAlgorithm.runOptimisation(lastHistoryData, lastRunStatistics)).deepCopy()
            updateRoomVisualization()
            generationHistorySlider.max = lastHistoryData.size.toDouble()
            if (lastHistoryData.size > 0) {
                generationHistorySlider.majorTickUnit = (lastHistoryData.size / 50).toDouble()
            }
            ChartWindow().drawChart(lastRunStatistics)
        })
        buttonsPanel.children.add(startBtn)

        generationHistorySlider = Slider(0.0, 0.0, 0.0)
        generationHistorySlider.isShowTickLabels = true
        generationHistorySlider.isShowTickMarks = true
        //generationHistorySlider.isSnapToTicks = true
        generationHistorySlider.prefWidth = SLIDER_WIDTH

        generationHistorySlider.valueProperty().addListener({ _, _, new_val ->
            if (new_val.toInt() != currentHistoryFrame) {
                currentHistoryFrame = new_val.toInt()
                updateRoomVisualization()
            }
        })
        buttonsPanel.children.add(generationHistorySlider)

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