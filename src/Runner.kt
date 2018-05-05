import swarm.BatAlgorithm
import swarm.FireflyAlgorithm
import swarm.SwarmAlgorithm
import visualization.VisualizationWindow

/**
 * Created by r.makowiecki on 14/04/2018.
 */

var gAlgorithm: SwarmAlgorithm? = null

fun main(args: Array<String>) {
    gAlgorithm = BatAlgorithm()
    val visWindow = VisualizationWindow()
    visWindow.launchWindow(args)
}