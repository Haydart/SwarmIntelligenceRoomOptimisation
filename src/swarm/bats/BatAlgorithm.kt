package swarm.bats

import evaluation.GenerationStatistics
import swarm.Individual
import swarm.SwarmAlgorithm

class BatAlgorithm(
        private val fMin: Double = 0.0,
        private val fMax: Double = 8.0,
        private val alpha: Double = 0.9,
        private val gamma: Double = 0.9
) : SwarmAlgorithm() {


    override val population: MutableList<BatIndividual> = mutableListOf()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    override fun generateInitialPopulation() {
        val room = initRoom()

        (0 until populationSize).forEach {
            population.add(BatIndividual(room))
        }

        println(population[0])
    }

    override fun runOptimisation(
            historyData: MutableList<MutableList<Individual>>?,
            lastRunStatistics: MutableList<GenerationStatistics>?
    ): Individual {

        var iteration = 0
        var currentBestIndividual: BatIndividual = getBestIndividual()
        var bestIndividualInAllGenerations = currentBestIndividual

        lastRunStatistics?.add(getPopulationStatistics(population, iteration))

        while (iteration < generationCount) {
            (0 until populationSize).forEach { i ->
                val originalCoords = mutableListOf<Pair<Double, Double>>()
                population[i].coords.forEach { (x, y) ->
                    originalCoords.add(Pair(x, y))
                }
                val originalIntensivity = population[i].intensity

                updateBatVelocityAndPosition(population[i], bestIndividualInAllGenerations)

                val avgA = calculateAverageA()
                if (Math.random() > population[i].r) {
                    moveTowardsBest(population[i], bestIndividualInAllGenerations, avgA)
                }

                evaluateIndividual(population[i])

                if (Math.random() < population[i].A && population[i].intensity > originalIntensivity) {
                    population[i].A = population[i].A * alpha
                    population[i].r = population[i].r * (1 - Math.exp(-gamma * iteration))
                }
                else {
                    // Revert
                    population[i].coords = originalCoords
                    population[i].intensity = originalIntensivity
                }
            }

            currentBestIndividual = getBestIndividual()

            if (currentBestIndividual.intensity > bestIndividualInAllGenerations.intensity) {
                bestIndividualInAllGenerations = currentBestIndividual.deepCopy()
            }
            iteration++


            // Update historical data
            if (historyData != null) {
                val currentIterationHistory = mutableListOf<Individual>()
                population.forEach {
                    currentIterationHistory.add(it.deepCopy())
                }
                historyData.add(currentIterationHistory)
            }

            lastRunStatistics?.add(getPopulationStatistics(population, iteration))
        }

        return bestIndividualInAllGenerations
    }

    private fun calculateAverageA(): Double {
        var result = 0.0
        population.forEach {
            result += it.A
        }
        return result / populationSize.toDouble()
    }

    private fun updateBatVelocityAndPosition(currentBat: BatIndividual, currentGlobalBestBat: BatIndividual) {
        val freq = fMin + (fMax - fMin) * Math.random()
        currentBat.velocity.forEachIndexed { index, (vx, vy) ->
            val x = currentBat.coords[index].first
            val y = currentBat.coords[index].second

            val newVX = vx + (x - currentGlobalBestBat.coords[index].first) * freq
            val newVY = vy + (y - currentGlobalBestBat.coords[index].second) * freq

            val newX = x + vx
            val newY = y + vy

            currentBat.velocity[index] = Pair(newVX, newVY)
            currentBat.coords[index] = Pair(newX, newY)
        }
    }

    private fun moveTowardsBest(currentBat: BatIndividual, currentGlobalBestBat: BatIndividual, avgA: Double) {
        currentBat.coords.forEachIndexed { index, (_, _) ->
            val newX = currentGlobalBestBat.coords[index].first + (Math.random() * 2.0 - 1.0) * avgA
            val newY = currentGlobalBestBat.coords[index].second + (Math.random() * 2.0 - 1.0) * avgA

            currentBat.coords[index] = Pair(newX, newY)
        }
    }

    override fun getBestIndividual(): BatIndividual {
        return population.maxBy { it.intensity }!!
    }
}