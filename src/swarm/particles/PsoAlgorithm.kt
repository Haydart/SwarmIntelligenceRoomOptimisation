package swarm.particles

import evaluation.GenerationStatistics
import swarm.Individual
import swarm.SwarmAlgorithm

/**
 * Created by r.makowiecki on 07/05/2018.
 */
class PsoAlgorithm : SwarmAlgorithm() {

    override val population: MutableList<ParticleIndividual> = mutableListOf()

    override fun generateInitialPopulation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun runOptimisation(
            historyData: MutableList<MutableList<Individual>>?,
            lastRunStatistics: MutableList<GenerationStatistics>?): Individual {

        var iterationCount = 0
        var bestIndividualInAllGenerations = population[0]

        lastRunStatistics?.add(getPopulationStatistics(population, iterationCount))

        while (iterationCount < generationCount) {
            (0 until populationSize).forEach { i ->

            }

            iterationCount++
        }
    }

    override fun getBestIndividual(): Individual {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}