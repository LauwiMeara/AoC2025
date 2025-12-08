import kotlin.math.sqrt

const val NUMBER_OF_CONNECTIONS = 1000

fun main() {
    // Euclidian distance: square the differences, sum and square root the sum
    fun getEuclidianDistance(box1: Grid3D.Position, box2: Grid3D.Position): Long {
        return sqrt( (box1.x - box2.x).square() + (box1.y - box2.y).square() + (box1.z - box2.z).square().toDouble()).toLong()
    }

    fun getEuclidianDistances(input: List<Grid3D.Position>): Map<Pair<Grid3D.Position, Grid3D.Position>, Long> {
        val distances = mutableMapOf<Pair<Grid3D.Position, Grid3D.Position>, Long>()
        for (boxIndex in input.indices) {
            for (otherBoxIndex in input.indices) {
                if (boxIndex == otherBoxIndex) continue // This is the same box.
                if (distances[Pair(input[otherBoxIndex], input[boxIndex])] != null) continue // The distance is already calculated from the other box.
                distances[Pair(input[boxIndex], input[otherBoxIndex])] = getEuclidianDistance(input[boxIndex], input[otherBoxIndex])
            }
        }
        return distances
    }

    fun getCircuits(boxPairsSortedByDistance: List<Pair<Grid3D.Position, Grid3D.Position>>): List<List<Grid3D.Position>> {
        val circuits = mutableListOf<List<Grid3D.Position>>()
        var connectionsMade = 0

        for (boxPair in boxPairsSortedByDistance) {
            if (connectionsMade == NUMBER_OF_CONNECTIONS) break
            connectionsMade++

            val existingCircuits = circuits.filter { it.contains(boxPair.first) || it.contains(boxPair.second) }

            if (existingCircuits.isEmpty()) {
                circuits.add(listOf(boxPair.first, boxPair.second))
                continue
            }

            if (existingCircuits.size == 1) {
                val existingCircuit = existingCircuits.first().toMutableList()
                val containsBox1 = existingCircuit.contains(boxPair.first)
                val containsBox2 = existingCircuit.contains(boxPair.second)
                if (containsBox1 && !containsBox2) {
                    circuits.remove(existingCircuit)
                    existingCircuit.add(boxPair.second)
                    circuits.add(existingCircuit)
                } else if (containsBox2 && !containsBox1) {
                    circuits.remove(existingCircuit)
                    existingCircuit.add(boxPair.first)
                    circuits.add(existingCircuit)
                }
                continue
            }

            if (existingCircuits.size == 2) {
                circuits.removeAll(existingCircuits)
                circuits.add(existingCircuits[0] + existingCircuits[1])
            }
        }

        return circuits
    }

    fun part1(input: List<Grid3D.Position>): Long {
        val boxPairsSortedByDistance = getEuclidianDistances(input).toList().sortedBy { it.second }.map{it.first}
        val circuits = getCircuits(boxPairsSortedByDistance)
        return circuits.map{it.size}.sortedDescending().subList(0,3).fold(1){acc, it -> acc * it}
    }

    fun part2(input: List<Grid3D.Position>): Long {
        val boxPairsSortedByDistance = getEuclidianDistances(input).toList().sortedBy { it.second }.map{it.first}

        // The last box to connect is the one with the latest appearance in the list boxPairsSortedByDistance.
        val lastBoxToConnect = input.map{box -> Pair(box, boxPairsSortedByDistance.indexOfFirst{it.first == box || it.second == box})}.maxBy { it.second }.first
        val lastBoxPair = boxPairsSortedByDistance.first{it.first == lastBoxToConnect || it.second == lastBoxToConnect}
        return lastBoxPair.first.x.toLong() * lastBoxPair.second.x
    }

    val input = readInputAsStrings("Day08")
        .map{it.split(',')}
        .map{Grid3D.Position(it[0].toInt(), it[1].toInt(), it[2].toInt())}
    part1(input).println()
    part2(input).println()
}
