package model

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class Room(
        val furnitureList: MutableList<FurniturePiece>,
        val obstacleList: MutableList<RoomObstacle>,
        val width: Double,
        val height: Double
)