package swarm

import model.FurniturePiece
import model.Room
import testing.RastriginTest
import java.lang.Math.random
import kotlin.math.exp

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class FireflyAlgorithm {

    val alpha = 0.8f
    val beta = 0.8f
    val gamma = 0.001f

    val populationSize = 200
    val furnitureCount = 5
    val generationCount = 1000
    val roomWidth = 150f
    val roomHeight = 100f

    val population: MutableList<Individual> = mutableListOf()
    val testFunction = RastriginTest()

    fun runOptimisation() {
        generateInitialPopulation()
        evaluatePopulation()

        var iteration = 0

        while (iteration < generationCount) {
            (0 until populationSize).forEach { i ->
                (0 until populationSize).forEach { j ->
                    if (population[j].intensity > population[i].intensity) {
//                        println("Moving $i towards $j")
//                        println(population[i].coords)
                        moveTowards(population[i], population[j])
//                        println(population[i].coords)
                        evaluateIndividual(population[i])
                    }
                }
            }
            collectStatistics()
            iteration++
//            println("_________")
        }
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
        furnitureList.add(FurniturePiece(20f, 15f))
        furnitureList.add(FurniturePiece(25f, 10f))
//        furnitureList.add(FurniturePiece(10f, 5f))
//        furnitureList.add(FurniturePiece(17f, 12f))
//        furnitureList.add(FurniturePiece(10f, 10f))

        return Room(furnitureList, roomWidth, roomHeight)
    }

    private fun moveTowards(recessive: Individual, dominant: Individual) {
        val distance = recessive.distanceTo(dominant)
        val dominantIndividualAttractiveness = beta * exp(-gamma * distance)

        recessive.coords.forEachIndexed { index, (x, y) ->
            val newX = x + (dominantIndividualAttractiveness * (dominant.coords[index].first - x) + alpha * (2 * random() - 1).toFloat())
            val newY = y + (dominantIndividualAttractiveness * (dominant.coords[index].second - y) + alpha * (2 * random() - 1).toFloat())
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

    fun collectStatistics() {
        var bestScore = 0f
        var bestIndividual: Individual? = null
        population.forEach {
            if (it.intensity > bestScore) {
                bestScore = it.intensity
                bestIndividual = it
            }
        }

        println(bestIndividual?.coords)
        println("Best score ${bestIndividual?.intensity}")
    }
}