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

    fun addToPositionsSet(
        input: List<Grid2D.Position>,
        positionsSet: MutableSet<Grid2D.Position>
    ) {
        val yRangesForX = input.groupBy { it.x }
            .map { Pair(it.key, it.value.minOf { position -> position.y }..it.value.maxOf { position -> position.y }) }
        val xRangesForY = input.groupBy { it.y }
            .map { Pair(it.key, it.value.minOf { position -> position.x }..it.value.maxOf { position -> position.x }) }
        for (xWithYRange in yRangesForX) {
            for (y in xWithYRange.second) {
                positionsSet.add(Grid2D.Position(xWithYRange.first, y))
            }
        }
        for (yWithXRange in xRangesForY) {
            for (x in yWithXRange.second) {
                positionsSet.add(Grid2D.Position(x, yWithXRange.first))
            }
        }
    }

    fun part2(input: List<Grid2D.Position>): Long {
        val positionsSet = mutableSetOf<Grid2D.Position>()
        addToPositionsSet(input, positionsSet) // add outline
        addToPositionsSet(positionsSet.toList(), positionsSet) // fill shape

        val areas = getRectangleAreas(input).toList().sortedByDescending { it.second }
        for (area in areas) {
            val minX = area.first.toList().minOf{it.x}
            val maxX = area.first.toList().maxOf{it.x}
            val minY = area.first.toList().minOf{it.y}
            val maxY = area.first.toList().maxOf{it.y}
            var areaIsOutsideOfTiles = false
            for (x in minX..maxX) {
                if (areaIsOutsideOfTiles) break

                for (y in minY..maxY) {
                    if (!positionsSet.contains(Grid2D.Position(x, y))) {
                        areaIsOutsideOfTiles = true
                        break
                    }
                }
            }

            if (!areaIsOutsideOfTiles) {
                return area.second
            }
        }
        return 0
    }

    val input = readInputAsStrings("Day09")
        .map{it.split(",")}
        .map{Grid2D.Position(it.first().toInt(), it.last().toInt())}
    part1(input).println()
    part2(input).println()
}
