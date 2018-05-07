package swarm.particles

import model.Room
import swarm.Individual

class ParticleIndividual(room: Room) : Individual(room) {

    var velocity: MutableList<Pair<Double, Double>> = MutableList(coords.size) { Pair(0.0, 0.0) }
    var personalBestCoords: MutableList<Pair<Double, Double>> = coords
    var personalBestIntensity = intensity

    override fun deepCopy(): Individual {
        //no-op
        return this
    }
}