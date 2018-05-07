package swarm.fireflies

import model.Room
import swarm.Individual

/**
 * Created by r.makowiecki on 07/05/2018.
 */
class FireflyIndividual(room: Room) : Individual(room) {

    override fun deepCopy(): Individual {
        val coordsCopy = mutableListOf<Pair<Double, Double>>()
        coords.forEach { (x, y) ->
            coordsCopy.add(Pair(x, y))
        }

        val result = FireflyIndividual(room)
        result.intensity = intensity
        result.coords = coordsCopy
        return result
    }
}