import swarm.FireflyAlgorithm
import visualization.VisualizationWindow

/**
 * Created by r.makowiecki on 14/04/2018.
 */

var gAlgorithm: FireflyAlgorithm? = null

fun main(args: Array<String>) {
    gAlgorithm = FireflyAlgorithm()
    val visWindow = VisualizationWindow()
    visWindow.launchWindow(args)
}