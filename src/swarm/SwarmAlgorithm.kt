package swarm

import evaluation.GenerationStatistics
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

    abstract fun runOptimisation(historyData: MutableList<MutableList<Individual>>? = null,
                                 lastRunStatistics: MutableList<GenerationStatistics>? = null): Individual

    abstract fun getBestIndividual(): Individual

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

    fun getPopulationStatistics(population: MutableList<Individual>, generationNumber: Int): GenerationStatistics {
        var best = population[0].intensity
        var worst = population[0].intensity
        var avg = 0.0

        for (ind in population) {
            if (ind.intensity > best) {
                best = ind.intensity
            }
            if (ind.intensity < worst) {
                worst = ind.intensity
            }
            avg += ind.intensity
        }
        avg /= population.size.toDouble()

        return GenerationStatistics(best, worst, avg, generationNumber)
    }
}