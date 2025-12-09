import kotlin.math.abs

fun main() {
    fun getRectangleArea(position1: Grid2D.Position, position2: Grid2D.Position): Long {
        return (abs(position1.x.toLong() - position2.x) + 1) * (abs(position1.y.toLong() - position2.y) + 1)
    }

    fun getRectangleAreas(input: List<Grid2D.Position>): Map<Pair<Grid2D.Position, Grid2D.Position>, Long> {
        val distances = mutableMapOf<Pair<Grid2D.Position, Grid2D.Position>, Long>()
        for (position1Index in input.indices) {
            for (position2Index in input.indices) {
                if (position1Index == position2Index) continue // This is the same position.
                if (distances[Pair(input[position2Index], input[position1Index])] != null) continue // The area is already calculated from the other position.
                distances[Pair(input[position1Index], input[position2Index])] = getRectangleArea(input[position1Index], input[position2Index])
            }
        }
        return distances
    }

    fun part1(input: List<Grid2D.Position>): Long {
        return getRectangleAreas(input).maxOf { it.value }
    }

    fun part2(input: List<Grid2D.Position>): Long {
        return 0
    }

    val input = readInputAsStrings("Day09")
        .map{it.split(",")}
        .map{Grid2D.Position(it.first().toInt(), it.last().toInt())}
    part1(input).println()
    part2(input).println()
}
