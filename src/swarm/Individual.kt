package swarm

import model.Room
import java.lang.Math.random
import kotlin.math.pow

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class Individual(val room: Room) {

    val coords: MutableList<Pair<Double, Double>> = mutableListOf()
    var intensity = 0.0

    init {
        room.furnitureList.forEach {
            val x = random() * (room.width - it.width) + (it.width / 2)
            val y = random() * (room.height - it.height) + (it.height / 2)

            coords.add(Pair(x, y))
        }
    }

    fun distanceTo(other: Individual): Double {
        var distance = 0.0

        (0 until other.coords.size).forEach { index ->
            distance += (other.coords[index].first - this.coords[index].first).pow(2)
            distance += (other.coords[index].second - this.coords[index].second).pow(2)
        }

        return distance
    }
//
//    fun vectorTo(other: Individual) : MutableList<Pair<Float, Float>> {
//        val result = mutableListOf<Pair<Float, Float>>()
//        (0 until other.coords.size).forEach { index ->
//            val vecX = other.coords[index].first - this.coords[index].first
//            val vecY = other.coords[index].second - this.coords[index].second
//            result.add(Pair(vecX, vecY))
//        }
//        return result
//    }

    override fun toString(): String {
        return coords.toString()
    }
}