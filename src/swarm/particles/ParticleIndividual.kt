package swarm.particles

import model.Room
import swarm.Individual

class ParticleIndividual(room: Room) : Individual(room) {

    var velocity: MutableList<Pair<Double, Double>> = MutableList(coords.size) { Pair(0.0, 0.0) }
    var personalBestCoords: MutableList<Pair<Double, Double>> = coords
    var personalBestIntensity = intensity

    override fun deepCopy(): Individual {
        val coordsCopy = mutableListOf<Pair<Double, Double>>()
        coords.forEach { (x, y) ->
            coordsCopy.add(Pair(x, y))
        }

        val velocityCopy = mutableListOf<Pair<Double, Double>>()
        velocity.forEach { (x, y) ->
            velocityCopy.add(Pair(x, y))
        }

        val personalBestCoordsCopy = mutableListOf<Pair<Double, Double>>()
        personalBestCoords.forEach { (x, y) ->
            personalBestCoordsCopy.add(Pair(x, y))
        }

        val result = ParticleIndividual(room)
        result.intensity = intensity
        result.personalBestIntensity = personalBestIntensity
        result.coords = coordsCopy
        result.velocity = velocityCopy
        result.personalBestCoords = personalBestCoordsCopy
        return result
    }
}