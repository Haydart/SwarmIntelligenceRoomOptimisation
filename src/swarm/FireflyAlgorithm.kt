package swarm

import model.FurniturePiece
import model.Room

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
    }

    private fun generateInitialPopulation() {
        var room = initRoom()

        (0 until populationSize).forEach {
            population.add(Individual(room))
            val pieceWidth = 0
            val pieceHeight = 0
        }
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