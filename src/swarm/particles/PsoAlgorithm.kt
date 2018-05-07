package swarm.particles

import evaluation.GenerationStatistics
import swarm.Individual
import swarm.SwarmAlgorithm

/**
 * Created by r.makowiecki on 07/05/2018.
 */
class PsoAlgorithm(
        private val swarmInertia: Double = 1.0,
        private val particlePersonalAcceleration: Double = 2.0,
        private val particleSocialAcceleration: Double = 2.0
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
                updateParticleGlobalBestIfNeeded(i)
            }

            iterationCount++
        }

        return population[0]
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

    private fun updateParticlePersonalBestIfNeeded(i: Int) {
        if (population[i].intensity > population[i].personalBestIntensity) {
            population[i].personalBestIntensity = population[i].intensity
            population[i].personalBestCoords = population[i].coords.
        }
    }

    private fun updateParticleGlobalBestIfNeeded(i: Int) {

    }

    override fun getBestIndividual(): Individual {
        return population.maxBy { it.intensity }!!
    }
}