package swarm.particles

import evaluation.GenerationStatistics
import swarm.Individual
import swarm.SwarmAlgorithm

/**
 * Created by r.makowiecki on 07/05/2018.
 */
class PsoAlgorithm(
        private val swarmInertia: Double = .8,
        private val particlePersonalAcceleration: Double = 1.4,
        private val particleSocialAcceleration: Double = 0.6
) : SwarmAlgorithm() {

    lateinit var globalBest: ParticleIndividual

    override val population: MutableList<ParticleIndividual> = mutableListOf()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    override fun generateInitialPopulation() {
        val room = initRoom()

        (0 until populationSize).forEach {
            population.add(ParticleIndividual(room))
        }
    }

    override fun runOptimisation(
            historyData: MutableList<MutableList<Individual>>?,
            lastRunStatistics: MutableList<GenerationStatistics>?
    ): Individual {

        var iterationCount = 0
        globalBest = population[0].deepCopy() as ParticleIndividual

        lastRunStatistics?.add(getPopulationStatistics(population, iterationCount))

        while (iterationCount < generationCount) {
            (0 until populationSize).forEach { i ->
                updateParticleVelocity(i)
                updateParticlePosition(i)
                evaluateIndividual(population[i])
                updateParticlePersonalBestIfNeeded(i)
                updateGlobalBestIfNeeded(i)
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

        return globalBest
    }

    private fun updateParticleVelocity(individualIndex: Int) {
        val currentVelocity = population[individualIndex].velocity

        population[individualIndex].velocity = MutableList(currentVelocity.size) { dimensionIndex ->
            val newXVelocity = swarmInertia * currentVelocity[dimensionIndex].first +
                    particlePersonalAcceleration * Math.random() *
                            (population[individualIndex].personalBestCoords[dimensionIndex].first - population[individualIndex].coords[dimensionIndex].first) +
                    particleSocialAcceleration * Math.random() *
                            (globalBest.coords[dimensionIndex].first - population[individualIndex].coords[dimensionIndex].first)

            val newYVelocity = swarmInertia * currentVelocity[dimensionIndex].second +
                    particlePersonalAcceleration * Math.random() *
                            (population[individualIndex].personalBestCoords[dimensionIndex].second - population[individualIndex].coords[dimensionIndex].second) +
                    particleSocialAcceleration * Math.random() *
                            (globalBest.coords[dimensionIndex].second - population[individualIndex].coords[dimensionIndex].second)

            Pair(newXVelocity, newYVelocity)
        }
    }

    private fun updateParticlePosition(individualIndex: Int) {
        val currentVelocity = population[individualIndex].velocity
        val currentPosition = population[individualIndex].coords

        population[individualIndex].coords = MutableList(currentPosition.size) { dimensionIndex ->
            val newXPosition = currentPosition[dimensionIndex].first + currentVelocity[dimensionIndex].first

            val newYPosition = currentPosition[dimensionIndex].second + currentVelocity[dimensionIndex].second

            Pair(newXPosition, newYPosition)
        }
    }

    private fun updateParticlePersonalBestIfNeeded(index: Int) {
        if (population[index].intensity > population[index].personalBestIntensity) {
            population[index].personalBestIntensity = population[index].intensity
            population[index].personalBestCoords = population[index].coords.deepCopy()
        }
    }

    private fun updateGlobalBestIfNeeded(index: Int) {
        if (population[index].intensity > globalBest.intensity) {
            globalBest = population[index].deepCopy() as ParticleIndividual
        }
    }

    override fun getBestIndividual(): Individual {
        return population.maxBy { it.intensity }!!
    }
}

fun <T> MutableList<T>.deepCopy(): MutableList<T> {
    return this.toMutableList()
}