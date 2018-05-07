package swarm.particles

import model.Room
import swarm.Individual

class ParticleIndividual(room: Room) : Individual(room) {

    override fun deepCopy(): Individual {
        //no-op
        return this
    }
}