package swarm

import model.FurniturePiece
import model.Room

abstract class SwarmAlgorithm {

    val roomWidth = 150.0
    val roomHeight = 100.0

    abstract fun runOptimisation(): Individual

    abstract fun getBestIndividual(): Individual

    protected fun initRoom(): Room {
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
}