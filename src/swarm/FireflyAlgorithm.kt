package swarm

import model.FurniturePiece
import model.Room
import java.lang.Math.random
import kotlin.math.exp

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class FireflyAlgorithm {

    val alpha = 1f
    val beta = 1f
    val gamma = 1f

    val populationSize = 50
    val furnitureCount = 5
    val generationCount = 100
    val roomWidth = 150f
    val roomHeight = 100f

    val population: MutableList<Individual> = mutableListOf()

    fun runOptimisation() {
        generateInitialPopulation()

        var iteration = 0

        while (iteration < generationCount) {
            (0 until populationSize).forEach { i ->
                (0 until populationSize).forEach { j ->
                    if (population[j].intensity > population[i].intensity) {
                        moveTowards(population[i], population[j])
                    }
                }
            }
        }
    }

    private fun moveTowards(recessive: Individual, dominant: Individual) {
        val distance = recessive.distanceTo(dominant)
        val dominantIndividualAttractiveness = beta * exp(-gamma * distance)

        recessive.coords.forEachIndexed { index, (x, y) ->
            val newX = x + (dominantIndividualAttractiveness * (dominant.coords[index].first - x) + alpha * random()).toFloat()
            val newY = y + (dominantIndividualAttractiveness * (dominant.coords[index].second - y) + alpha * random()).toFloat()
            recessive.coords[index] = Pair(newX, newY)
        }
    }

    private fun generateInitialPopulation() {
        var room = initRoom()

        (0 until populationSize).forEach {
            population.add(Individual(room))
        }

        println(population[0])
    }

    private fun initRoom(): Room {
        val furnitureList = mutableListOf<FurniturePiece>()
        furnitureList.add(FurniturePiece(20f, 15f))
        furnitureList.add(FurniturePiece(25f, 10f))
        furnitureList.add(FurniturePiece(10f, 5f))
        furnitureList.add(FurniturePiece(17f, 12f))
        furnitureList.add(FurniturePiece(10f, 10f))

        return Room(furnitureList, roomWidth, roomHeight)
    }
}