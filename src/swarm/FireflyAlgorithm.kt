package swarm

import evaluation.RastriginTest
import evaluation.RestrictedRastriginTest
import model.FurniturePiece
import model.Room
import evaluation.RoomConfigurationEvaluator
import java.lang.Math.random
import kotlin.math.exp

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class FireflyAlgorithm {

    val alpha = 0.05
    val beta = 0.09
    val gamma = 0.0001

    val populationSize = 100
    val furnitureCount = 5
    val generationCount = 1000
    val roomWidth = 150.0
    val roomHeight = 100.0

    val population: MutableList<Individual> = mutableListOf()
    val testFunction = RestrictedRastriginTest()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    fun runOptimisation(): Individual {

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

            val currentGenerationBestIntensityIndividual = assignIntensityAndReturnBestIndividual()
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

    private fun initRoom(): Room {
        val furnitureList = mutableListOf<FurniturePiece>()
        furnitureList.add(FurniturePiece(20.0, 15.0))
        furnitureList.add(FurniturePiece(25.0, 10.0))
        furnitureList.add(FurniturePiece(10.0, 5.0))
        furnitureList.add(FurniturePiece(17.0, 12.0))
        furnitureList.add(FurniturePiece(10.0, 10.0))
        furnitureList.add(FurniturePiece(20.0, 5.0))
        furnitureList.add(FurniturePiece(25.0, 10.0))
        furnitureList.add(FurniturePiece(5.0, 15.0))
        furnitureList.add(FurniturePiece(12.0, 17.0))
        furnitureList.add(FurniturePiece(10.0, 25.0))

        return Room(furnitureList, roomWidth, roomHeight)
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

    fun assignIntensityAndReturnBestIndividual(): Individual {
        var bestIntensity = -Double.MAX_VALUE
        var bestIndividual: Individual? = null
        population.forEach {
            if (it.intensity > bestIntensity) {
                bestIntensity = it.intensity
                bestIndividual = it
            }
        }

        println(bestIndividual?.coords)
        println("Best score ${bestIndividual?.intensity}")
        return bestIndividual!!
    }
}