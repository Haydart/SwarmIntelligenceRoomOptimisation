package swarm

import model.Room
import java.lang.Math.random

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class Individual(room: Room) {

    val coords: MutableList<Pair<Float, Float>> = mutableListOf()

    init {
        room.furnitureList.forEach {
            val x = random() * (room.width - it.width) + (it.width / 2)
            val y = random() * (room.height - it.height) + (it.height / 2)

            coords.add(Pair(x.toFloat(), y.toFloat()))
        }
    }
}