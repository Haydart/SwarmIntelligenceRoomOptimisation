package evaluation

import model.FurniturePiece
import swarm.Individual
import kotlin.math.pow

/**
 * Created by r.makowiecki on 14/04/2018.
 */

const val PUNISHMENT_VALUE = 1000.0

class RoomConfigurationEvaluator {

    fun evaluateIndividual(individual: Individual): Double {
        var result = 0.0001
        val furnitureCount = individual.coords.size

        for (i in 0 until furnitureCount) {
            result += 50.0 * 1.0 / (0.0001 + distance2ToMiddle(individual.coords[i], individual.room.width / 2, individual.room.height / 2))

            result += punishIfFurnitureOutsideRoom(individual, i)

            for (j in i + 1 until furnitureCount) {
                if (isFurnitureOverlapping(individual.coords[i], individual.room.furnitureList[i], individual.coords[j], individual.room.furnitureList[j])) {
                    result += PUNISHMENT_VALUE
                }
            }
        }
        return result
    }

    private fun distance2ToMiddle(coords: Pair<Double, Double>, roomMiddleX: Double, roomMiddleY: Double): Double {
        return (roomMiddleX - coords.first).pow(2) + (roomMiddleY - coords.second).pow(2)
    }

    private fun punishIfFurnitureOutsideRoom(individual: Individual, furnitureIndex: Int): Double {
        val roomWidth = individual.room.width
        val roomHeight = individual.room.height
        val furnitureCoords = individual.coords[furnitureIndex]
        val furnitureHalfWidth = individual.room.furnitureList[furnitureIndex].width / 2
        val furnitureHalfHeight = individual.room.furnitureList[furnitureIndex].height / 2

        return if (furnitureCoords.first - furnitureHalfWidth < 0 || furnitureCoords.second - furnitureHalfHeight < 0
                || furnitureCoords.first + furnitureHalfWidth > roomWidth || furnitureCoords.second + furnitureHalfHeight > roomHeight)
            PUNISHMENT_VALUE else 0.0
    }

    private fun isFurnitureOverlapping(furn1Pos: Pair<Double, Double>, furn1Info: FurniturePiece, furn2Pos: Pair<Double, Double>, furn2Info: FurniturePiece): Boolean {
        val leftA = furn1Pos.first - furn1Info.width / 2
        val rightA = furn1Pos.first + furn1Info.width / 2
        val topA = furn1Pos.second - furn1Info.height / 2
        val bottomA = furn1Pos.second + furn1Info.height / 2

        val leftB = furn2Pos.first - furn2Info.width / 2
        val rightB = furn2Pos.first + furn2Info.width / 2
        val topB = furn2Pos.second - furn2Info.height / 2
        val bottomB = furn2Pos.second + furn2Info.height / 2

        return (leftA < rightB && rightA > leftB && topA < bottomB && bottomA > topB)
    }
}