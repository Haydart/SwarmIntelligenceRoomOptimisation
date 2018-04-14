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
import swarm.FireflyAlgorithm
import swarm.Individual


/**
 * Created by r.makowiecki on 14/04/2018.
 */


const val CANVAS_HEIGHT = 1000.0
const val CANVAS_WIDTH = 500.0

const val SCENE_WIDTH = 1280.0
const val SCENE_HEIGHT = 768.0

class VisualizationWindow : Application() {

    lateinit var boardRoot: BorderPane
    lateinit var roomGraphicContext: GraphicsContext

    override fun start(primaryStage: Stage) {
        initUI(primaryStage)
    }

    fun updateRoomVis(color: Color) {
        gAlgorithm?.let {
            val gc = roomGraphicContext
            drawRoomBounds(gc, it)
            drawFurniturePieces(gc, it.population[0], color)
            gc.fill = Color.RED
            gc.fillOval(0.0, 0.0, 15.0, 15.0)
        }
    }

    private fun drawRoomBounds(gc: GraphicsContext, it: FireflyAlgorithm) {
        gc.stroke = Color.BLACK
        gc.lineWidth = 4.0
        gc.strokeRect(0.0, 0.0, it.roomWidth, it.roomHeight)
    }

    private fun drawFurniturePieces(gc: GraphicsContext, individual: Individual, color: Color) {
        gc.stroke = color
        gc.lineWidth = 2.0
        individual.coords.forEachIndexed { index, (x, y) ->
            val furniturePiece = individual.room.furnitureList[index]
            gc.strokeRect(x - furniturePiece.width / 2, y - furniturePiece.height / 2, furniturePiece.width,  furniturePiece.height)
        }
    }

    fun initUI(stage: Stage) {

        boardRoot = BorderPane()

        initButtonsPanelUI()
        initRoomVisUI()
        updateRoomVis(Color.CORAL)

        val scene = Scene(boardRoot, SCENE_WIDTH, SCENE_HEIGHT)

        stage.title = "Room Optimizer 3000"
        stage.scene = scene
        stage.show()
    }

    private fun initButtonsPanelUI() {
        val buttonsPanel = HBox(10.0)

        val startBtn = Button("Begin!")
        startBtn.setOnAction({
            gAlgorithm?.runOptimisation()
            updateRoomVis(Color.CORNFLOWERBLUE)
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