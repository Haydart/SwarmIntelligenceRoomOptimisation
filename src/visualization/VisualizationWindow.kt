package visualization

import gAlgorithm
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
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

class VisualizationWindow : Application() {

    lateinit var boardRoot: BorderPane
    lateinit var roomGraphicContext: GraphicsContext

    var lastHistoryData: MutableList<MutableList<Individual>> = mutableListOf()

    override fun start(primaryStage: Stage) {
        initUI(primaryStage)
    }

    fun updateRoomVis(color: Color, sampleIndividual: Individual, historyData: MutableList<MutableList<Individual>>? = null) {
        gAlgorithm?.let {
            val gc = roomGraphicContext
            drawRoomBounds(gc, it, 0.0, 0.0)
//            drawFurniturePieces(gc, it.population[0], color)
            drawFurniturePieces(gc, sampleIndividual, color, 0.0, 0.0)
//            gc.fill = Color.RED
//            gc.fillOval(0.0, 0.0, 15.0, 15.0)

            if (historyData != null && historyData.isNotEmpty()) {
                val lastIterationData = historyData.last()

                (0 until HIST_IND_VIS_COUNT).forEach {i ->
                    drawRoomBounds(gc, it, (i / HIST_IND_IN_ROW) * it.roomWidth,
                            (i % HIST_IND_IN_COL + 1) * it.roomHeight)
                    drawFurniturePieces(gc, lastIterationData[i], color, (i / HIST_IND_IN_ROW) * it.roomWidth,
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
            updateRoomVis(Color.CORAL, it.getBestIndividual())
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
            val globalBestIndividual = gAlgorithm?.runOptimisation(lastHistoryData)
            updateRoomVis(Color.CORNFLOWERBLUE, globalBestIndividual!!, lastHistoryData)
            print(lastHistoryData.size)
        })
        buttonsPanel.children.add(startBtn)

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