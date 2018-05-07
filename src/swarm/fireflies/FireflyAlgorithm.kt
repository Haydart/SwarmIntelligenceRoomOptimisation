package swarm.fireflies

import evaluation.GenerationStatistics
import swarm.Individual
import swarm.SwarmAlgorithm
import java.lang.Math.random
import kotlin.math.exp

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class FireflyAlgorithm(
        private val alpha: Double = 0.05,
        private val beta: Double = 0.09,
        private val gamma: Double = 0.0001
) : SwarmAlgorithm() {

    override val population: MutableList<FireflyIndividual> = mutableListOf()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    override fun generateInitialPopulation() {
        val room = initRoom()

        (0 until populationSize).forEach {
            population.add(FireflyIndividual(room))
        }
    }

    override fun runOptimisation(
            historyData: MutableList<MutableList<Individual>>?,
            lastRunStatistics: MutableList<GenerationStatistics>?): Individual {

        var iterationCount = 0
        var bestIndividualInAllGenerations = population[0]

        lastRunStatistics?.add(getPopulationStatistics(population, iterationCount))

        while (iterationCount < generationCount) {
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
                bestIndividualInAllGenerations = currentGenerationBestIntensityIndividual.deepCopy() as FireflyIndividual
            }

            iterationCount++

            // Update historical data
            if (historyData != null) {
                val currentIterationHistory = mutableListOf<Individual>()
                population.forEach {
                    currentIterationHistory.add(it.deepCopy())
                }
                historyData.add(currentIterationHistory)
            }

            lastRunStatistics?.add(getPopulationStatistics(population, iterationCount))
        }

        return bestIndividualInAllGenerations
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

    override fun getBestIndividual(): Individual {
        return population.maxBy { it.intensity }!!
    }
}