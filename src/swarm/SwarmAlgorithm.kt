package swarm

abstract class SwarmAlgorithm {

    val roomWidth = 150.0
    val roomHeight = 100.0

    abstract fun runOptimisation(): Individual

    abstract fun getBestIndividual(): Individual
}