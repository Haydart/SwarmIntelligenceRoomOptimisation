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
        var bestIndividualInAllGenerations = population[0]

        lastRunStatistics?.add(getPopulationStatistics(population, iterationCount))

        while (iterationCount < generationCount) {
            (0 until populationSize).forEach { individualIndex ->
                updateParticleVelocity(individualIndex)
                updateParticlePosition(individualIndex)
            }

            iterationCount++
        }

        return population[0]
    }

    private fun updateParticleVelocity(individualIndex: Int) {
        val i = individualIndex
        val currentVelocity = population[individualIndex].velocity

        population[i].velocity = MutableList(currentVelocity.size) { dimensionIndex ->
            val newXVelocity = swarmInertia * currentVelocity[dimensionIndex].first +
                    particlePersonalAcceleration * Math.random() * (population[i].personalBestCoords[dimensionIndex].first - population[i].coords[dimensionIndex].first) +
                    particleSocialAcceleration * Math.random() * (globalBest.coords[dimensionIndex].first - population[i].coords[dimensionIndex].first)
            val newYVelocity = swarmInertia * currentVelocity[dimensionIndex].second +
                    particlePersonalAcceleration * Math.random() * (population[i].personalBestCoords[dimensionIndex].second - population[i].coords[dimensionIndex].second) +
                    particleSocialAcceleration * Math.random() * (globalBest.coords[dimensionIndex].second - population[i].coords[dimensionIndex].second)
            Pair(newXVelocity, newYVelocity)
        }
    }

    private fun updateParticlePosition(individualIndex: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBestIndividual(): Individual {
        return population.maxBy { it.intensity }!!
    }
}