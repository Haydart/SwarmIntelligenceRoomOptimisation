import swarm.SwarmAlgorithm
import swarm.particles.PsoAlgorithm
import visualization.VisualizationWindow

/**
 * Created by r.makowiecki on 14/04/2018.
 */

lateinit var gAlgorithm: SwarmAlgorithm

fun main(args: Array<String>) {
    gAlgorithm = PsoAlgorithm()
    val visWindow = VisualizationWindow()
    visWindow.launchWindow(args)
}