package swarm

import evaluation.RastriginTest
import evaluation.RestrictedRastriginTest
import model.FurniturePiece
import model.Room
import evaluation.RoomConfigurationEvaluator
import java.lang.Math.random
import kotlin.math.exp

class BatAlgorithm {

    val fMin = 0.0
    val fMax = 10.0

    //val initAMin = 1.0
    //val initAMax = 2.0
    val aMin = 0.0

    val alpha = 0.9
    val gamma = 0.9

    //val rMin = 0.0
    //val rMax = 1.0

    val randomFlyMin = 0.0
    val randomFlyMax = 1.0

    val populationSize = 100
    val generationCount = 10000
    val roomWidth = 150.0
    val roomHeight = 100.0

    val population: MutableList<BatIndividual> = mutableListOf()
    val testFunction = RastriginTest()

    init {
        generateInitialPopulation()
        evaluatePopulation()
    }

    fun runOptimisation(): Individual {

        var iteration = 0
        var bestIndividualInAllGenerations = population[0]
        var currentBestIndividual: BatIndividual = getBestIndividual()

        while (iteration < generationCount) {
            (0 until populationSize).forEach { i ->
                updateBatVelocityAndPosition(population[i], currentBestIndividual)

                if (Math.random() > population[i].r) {
                    moveTowardsBest(population[i], currentBestIndividual)
                }

                evaluateIndividual(population[i])

                // Generate random solution by flying randomly
                if (Math.random() < population[i].A) {
                    val newPotentialSolution = population[i].deepCopy()
                    flyRandomly(newPotentialSolution)
                    evaluateIndividual(newPotentialSolution)
                    if (newPotentialSolution.intensity > currentBestIndividual.intensity) {
                        population[i] = newPotentialSolution
                        population[i].A = population[i].A * alpha
                        population[i].r = population[i].r * (1 - Math.exp(-gamma * iteration)) // not sure about this!!!
                    }
                }
            }

            currentBestIndividual = getBestIndividual()

            if (currentBestIndividual.intensity > bestIndividualInAllGenerations.intensity) {
                bestIndividualInAllGenerations = currentBestIndividual.deepCopy()
            }
            iteration++
        }

        return bestIndividualInAllGenerations
    }

    private fun generateInitialPopulation() {
        val room = initRoom()

        (0 until populationSize).forEach {
            population.add(BatIndividual(room))
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

    private fun flyRandomly(currentBat: BatIndividual) {
        currentBat.coords.forEachIndexed { index, (x, y) ->
            val newX = x + randomFlyMin + (randomFlyMax - randomFlyMin) * Math.random()
            val newY = y + randomFlyMin + (randomFlyMax - randomFlyMin) * Math.random()

            currentBat.coords[index] = Pair(newX, newY)
        }
    }

    private fun moveTowardsBest(currentBat: BatIndividual, currentGlobalBestBat: BatIndividual) {
        currentBat.coords.forEachIndexed { index, (x, y) ->
            val newX = x + (Math.random() * 2.0 - 1.0) * currentGlobalBestBat.A
            val newY = y + (Math.random() * 2.0 - 1.0) * currentGlobalBestBat.A

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

    fun getBestIndividual(): BatIndividual {
        return population.maxBy { it.intensity }!!
    }
}