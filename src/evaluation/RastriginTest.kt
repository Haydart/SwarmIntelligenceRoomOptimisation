package evaluation

import swarm.Individual
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

/**
 * Created by r.makowiecki on 14/04/2018.
 */
class RastriginTest {

    fun evaluateIndividual(individual: Individual): Double {

        var sumFactor = 10.0 * individual.coords.size * 2
        individual.coords.forEach { (x, y) ->
            sumFactor += (x.pow(2) + y.pow(2) - 10 * cos(2 * PI * x) - 10 * cos(2 * PI * y)).toFloat()
        }
        return sumFactor
    }
}