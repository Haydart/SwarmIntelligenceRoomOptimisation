package evaluation

import model.FurniturePiece
import swarm.Individual

/**
 * Created by r.makowiecki on 14/04/2018.
 */

const val PUNISHMENT_VALUE = 100

class RoomConfigurationEvaluator {

    fun evaluateIndividual(individual: Individual): Double {
        var result = 0.01
        val furnitureCount = individual.coords.size

        for (i in 0 until furnitureCount) {
            for (j in i + 1 until furnitureCount) {
                if (isFurnitureOverlapping(individual.coords[i], individual.room.furnitureList[i], individual.coords[j], individual.room.furnitureList[j])) {
                    result += PUNISHMENT_VALUE
                }
            }
        }
        return result
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