package evaluation

import swarm.Individual
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

const val R_PUNISHMENT_VALUE = 500000.0
const val MIN_X_POSITION = 100.0

class RestrictedRastriginTest {

    fun evaluateIndividual(individual: Individual) : Double {

        var sumFactor = 10.0 * individual.coords.size * 2
        individual.coords.forEach { (x, y) ->
            sumFactor += (x.pow(2) + y.pow(2) - 10 * cos(2 * PI * x ) - 10 * cos(2 * PI * y)).toFloat()
            if (x < MIN_X_POSITION ) {
                sumFactor += R_PUNISHMENT_VALUE
            }
        }

        return sumFactor
    }
}