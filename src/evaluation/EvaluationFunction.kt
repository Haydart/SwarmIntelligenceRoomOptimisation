package evaluation

import swarm.Individual

abstract class EvaluationFunction {

    abstract fun evaluateIndividual(individual: Individual): Double
}