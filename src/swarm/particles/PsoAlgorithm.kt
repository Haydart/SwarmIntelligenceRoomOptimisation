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
            (0 until populationSize).forEach { i ->
                population[i].velocity = swarmInertia *
            }

            iterationCount++
        }

        return population[0]
    }

    override fun getBestIndividual(): Individual {
        return population.maxBy { it.intensity }!!
    }
}