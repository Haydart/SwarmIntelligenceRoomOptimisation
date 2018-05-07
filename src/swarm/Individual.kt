package swarm

import model.Room
import java.lang.Math.random
import kotlin.math.pow

/**
 * Created by r.makowiecki on 14/04/2018.
 */
abstract class Individual(val room: Room) {

    var intensity = 0.0

    var coords: MutableList<Pair<Double, Double>> = mutableListOf()

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

    abstract fun deepCopy(): Individual

    override fun toString() = coords.toString()
}