package swarm

import evaluation.EvaluationFunction
import evaluation.MIN_X_POSITION
import evaluation.RestrictedRastriginTest
import evaluation.RoomConfigurationEvaluationFunction
import model.FurniturePiece
import model.GenerationStatistics
import model.Room
import model.RoomObstacle

const val DEFAULT_POPULATION_SIZE = 100
const val DEFAULT_GENERATION_COUNT = 1000

abstract class SwarmAlgorithm(
        private val testFunction: EvaluationFunction = RoomConfigurationEvaluationFunction(),
        protected val populationSize: Int = DEFAULT_POPULATION_SIZE,
        protected val generationCount: Int = DEFAULT_GENERATION_COUNT,
        private val hasObstacles: Boolean = false,
        val roomWidth: Double = 150.0,
        val roomHeight: Double = 100.0
) {

    abstract val population: MutableList<out Individual>

    abstract fun generateInitialPopulation()

    protected fun initRoom(): Room {
        val furnitureList = mutableListOf<FurniturePiece>()
        furnitureList.add(FurniturePiece(20.0, 15.0))
        furnitureList.add(FurniturePiece(25.0, 10.0))
        furnitureList.add(FurniturePiece(10.0, 5.0))
        furnitureList.add(FurniturePiece(17.0, 12.0))
        furnitureList.add(FurniturePiece(10.0, 10.0))
        furnitureList.add(FurniturePiece(20.0, 5.0))
        furnitureList.add(FurniturePiece(25.0, 10.0))
        furnitureList.add(FurniturePiece(5.0, 15.0))
        furnitureList.add(FurniturePiece(12.0, 17.0))
        furnitureList.add(FurniturePiece(10.0, 25.0))

        val obstacleList = mutableListOf<RoomObstacle>()

        if (hasObstacles) {
            obstacleList.add(RoomObstacle(width = 36.0, height = 36.0, x = 18.0, y = 18.0))
            obstacleList.add(RoomObstacle(width = 30.0, height = 20.0, x = 15.0, y = 55.0))
            obstacleList.add(RoomObstacle(width = 35.0, height = 20.0, x = 40.0, y = 90.0))
            obstacleList.add(RoomObstacle(width = 25.0, height = 20.0, x = 100.0, y = 90.0))
        }

        if (testFunction is RestrictedRastriginTest) {
            obstacleList.add(RoomObstacle(width = MIN_X_POSITION, height = roomHeight, x = MIN_X_POSITION / 2.0, y = roomHeight / 2.0))
        }

        return Room(furnitureList, obstacleList, roomWidth, roomHeight)
    }

    abstract fun runOptimisation(
            historyData: MutableList<MutableList<Individual>>? = null,
            lastRunStatistics: MutableList<GenerationStatistics>? = null
    ): Individual

    abstract fun getBestIndividual(): Individual

    fun getPopulationStatistics(population: MutableList<out Individual>, generationNumber: Int): GenerationStatistics {
        var bestIntensity = population[0].intensity
        var worstIntensity = population[0].intensity
        var averageIntensity = 0.0

        for (individual in population) {
            if (individual.intensity > bestIntensity) {
                bestIntensity = individual.intensity
            }
            if (individual.intensity < worstIntensity) {
                worstIntensity = individual.intensity
            }
            averageIntensity += individual.intensity
        }
        averageIntensity /= population.size.toDouble()

        return GenerationStatistics(bestIntensity, worstIntensity, averageIntensity, generationNumber)
    }

    protected fun evaluatePopulation() {
        population.forEach {
            evaluateIndividual(it)
            println("Individual intensity: ${it.intensity}")
        }
    }

    protected fun evaluateIndividual(individual: Individual) {
        individual.intensity = 1 / testFunction.evaluateIndividual(individual)
    }
}