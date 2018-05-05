package swarm

import evaluation.RastriginTest
import evaluation.RestrictedRastriginTest
import evaluation.RoomConfigurationEvaluator
import java.lang.Math.random
import kotlin.math.exp

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class FireflyAlgorithm : SwarmAlgorithm() {

    val alpha = 0.05
    val beta = 0.09
    val gamma = 0.0001

    val populationSize = 100
    val generationCount = 1000

    val population: MutableList<Individual> = mutableListOf()
    val testFunction = RoomConfigurationEvaluator()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    override fun runOptimisation(): Individual {

        var iteration = 0
        var bestIndividualInAllGenerations = population[0]

        while (iteration < generationCount) {
            (0 until populationSize).forEach { i ->
                (0 until populationSize).forEach { j ->
                    if (population[j].intensity > population[i].intensity) {
                        moveTowards(population[i], population[j])
                        evaluateIndividual(population[i])
                    }
                }
            }

            val currentGenerationBestIntensityIndividual = getBestIndividual()
            if (currentGenerationBestIntensityIndividual.intensity > bestIndividualInAllGenerations.intensity) {
                bestIndividualInAllGenerations = currentGenerationBestIntensityIndividual.deepCopy()
            }
            iteration++
        }

        return bestIndividualInAllGenerations
    }

    private fun generateInitialPopulation() {
        val room = initRoom()

        (0 until populationSize).forEach {
            population.add(Individual(room))
        }

        println(population[0])
    }

    private fun moveTowards(recessive: Individual, dominant: Individual) {
        val distance = recessive.distanceTo(dominant)
        val dominantIndividualAttractiveness = beta * exp(-gamma * distance)

        recessive.coords.forEachIndexed { index, (x, y) ->
            val newX = x + dominantIndividualAttractiveness * (dominant.coords[index].first - x) + alpha * (2 * random() - 1)
            val newY = y + dominantIndividualAttractiveness * (dominant.coords[index].second - y) + alpha * (2 * random() - 1)
            recessive.coords[index] = Pair(newX, newY)
        }
    }

    fun evaluatePopulation() {
        population.forEach {
            it.intensity = 1 / testFunction.evaluateIndividual(it)
            println("Individual intensity: ${it.intensity}")
        }
    }

    fun evaluateIndividual(individual: Individual) {
        val priorIntensity = individual.intensity
        individual.intensity = 1 / testFunction.evaluateIndividual(individual)
//        println("Posterior intensity: ${individual.intensity}, prior intensity: $priorIntensity")
    }

    override fun getBestIndividual(): Individual {
        return population.maxBy { it.intensity }!!
    }
}