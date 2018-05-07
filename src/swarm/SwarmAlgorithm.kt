package swarm

import evaluation.GenerationStatistics
import evaluation.RoomConfigurationEvaluator
import model.FurniturePiece
import model.Room

private const val DEFAULT_POPULATION_SIZE = 100
private const val DEFAULT_GENERATION_COUNT = 1000

abstract class SwarmAlgorithm(
        val populationSize: Int = DEFAULT_POPULATION_SIZE,
        val generationCount: Int = DEFAULT_GENERATION_COUNT
) {

    val roomWidth = 150.0
    val roomHeight = 100.0

    private val testFunction = RoomConfigurationEvaluator()

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

        return Room(furnitureList, roomWidth, roomHeight)
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