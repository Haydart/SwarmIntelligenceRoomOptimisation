package swarm

import model.Room

class BatIndividual(room: Room) : Individual(room){

    var velocity: MutableList<Pair<Double, Double>> = mutableListOf()
    var r = 0.9
    var A = 0.9

    init {
        room.furnitureList.forEach {
            val x = 0.0
            val y = 0.0

            velocity.add(Pair(x, y))
        }
    }

    override fun deepCopy(): BatIndividual {
        val velocityCopy = mutableListOf<Pair<Double, Double>>()
        velocity.forEach { (x, y) ->
            velocityCopy.add(Pair(x, y))
        }

        val coordsCopy = mutableListOf<Pair<Double, Double>>()
        coords.forEach { (x, y) ->
            coordsCopy.add(Pair(x, y))
        }

        val result = BatIndividual(room)
        result.intensity = intensity
        result.coords = coordsCopy
        result.velocity = velocityCopy
        return result
    }
}