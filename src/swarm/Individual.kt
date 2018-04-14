package swarm

import model.Room

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class Individual(var room: Room) {
    val coords: MutableList<Pair<Float, Float>> = mutableListOf()

}