package swarm

import evaluation.GenerationStatistics
import evaluation.RastriginTest
import evaluation.RestrictedRastriginTest
import evaluation.RoomConfigurationEvaluator

class BatAlgorithm : SwarmAlgorithm(){

    val fMin = 0.0
    val fMax = 10.0

    val alpha = 0.9
    val gamma = 0.9

    val populationSize = 200
    val generationCount = 1000

    val population: MutableList<BatIndividual> = mutableListOf()
    val testFunction = RoomConfigurationEvaluator()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    override fun runOptimisation(historyData: MutableList<MutableList<Individual>>?,
                                 lastRunStatistics: MutableList<GenerationStatistics>?): Individual {

        var iteration = 0
        var currentBestIndividual: BatIndividual = getBestIndividual()
        var bestIndividualInAllGenerations = currentBestIndividual

        lastRunStatistics?.add(getPopulationStatistics(population as MutableList<Individual>, iteration))

        while (iteration < generationCount) {
            (0 until populationSize).forEach { i ->
                val originalCoords = mutableListOf<Pair<Double, Double>>()
                population[i].coords.forEach { (x, y) ->
                    originalCoords.add(Pair(x, y))
                }
                val originalIntensivity = population[i].intensity

                updateBatVelocityAndPosition(population[i], bestIndividualInAllGenerations)

                val avgA = calcAvgA()
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

            lastRunStatistics?.add(getPopulationStatistics(population as MutableList<Individual>, iteration))
        }

        return bestIndividualInAllGenerations
    }

    private fun calcAvgA(): Double {
        var result = 0.0
        population.forEach {
            result += it.A
        }
        return result / populationSize.toDouble()
    }

    private fun generateInitialPopulation() {
        val room = initRoom()

        (0 until populationSize).forEach {
            population.add(BatIndividual(room))
        }

        println(population[0])
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

    fun evaluatePopulation() {
        population.forEach {
            evaluateIndividual(it)
            println("Individual intensity: ${it.intensity}")
        }
    }

    fun evaluateIndividual(individual: Individual) {
        individual.intensity = 1 / testFunction.evaluateIndividual(individual)
    }

    override fun getBestIndividual(): BatIndividual {
        return population.maxBy { it.intensity }!!
    }
}