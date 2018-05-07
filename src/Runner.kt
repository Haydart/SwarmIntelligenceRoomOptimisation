import swarm.SwarmAlgorithm
import swarm.fireflies.FireflyAlgorithm
import visualization.VisualizationWindow

/**
 * Created by r.makowiecki on 14/04/2018.
 */

lateinit var gAlgorithm: SwarmAlgorithm

fun main(args: Array<String>) {
    gAlgorithm = FireflyAlgorithm()
    val visWindow = VisualizationWindow()
    visWindow.launchWindow(args)
}