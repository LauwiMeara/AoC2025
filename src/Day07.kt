const val START = "S"
const val SPLITTER = "^"
const val BEAM = "|"

fun main() {
    fun getIndicesOfSplitters(input: List<List<String>>): List<Grid2D.Position> {
        return input.flatMapIndexed { indexX, it ->
            it.mapIndexedNotNull { indexY, s -> if (s == SPLITTER) Grid2D.Position(indexX, indexY) else null }
        }
    }

    fun beam(input: List<MutableList<String>>, panel: BeamPanel? = null) {
        for (x in input.indices) {
            for (y in input[x].indices) {
                if ((input[x][y] == START || input[x][y] == BEAM) &&
                    x + 1 < input.size &&
                    input[x + 1][y] != SPLITTER
                ) {
                    // Beam downwards
                    input[x + 1][y] = BEAM
                } else if (input[x][y] == SPLITTER) {
                    if (x + 1 < input.size && y - 1 >= 0) {
                        // Split left
                        input[x + 1][y - 1] = BEAM
                    }
                    if (x + 1 < input.size && y + 1 < input[x].size) {
                        // Split right
                        input[x + 1][y + 1] = BEAM
                    }
                }
            }
            panel?.repaint(input)
        }
    }

    fun getGraph(input: List<MutableList<String>>): Map<Grid2D.Position, List<Grid2D.Position>> {
        val indicesOfSplitters = getIndicesOfSplitters(input).sortedBy { it.x }
        val graph = mutableMapOf<Grid2D.Position, List<Grid2D.Position>>()
        for (index in indicesOfSplitters) {
            val nextSplitters = mutableListOf<Grid2D.Position>()
            val nextLeftSplitter = indicesOfSplitters.firstOrNull { it.y == index.y - 1 && it.x > index.x }
            if (nextLeftSplitter != null) nextSplitters.add(nextLeftSplitter)
            val nextRightSplitter = indicesOfSplitters.firstOrNull { it.y == index.y + 1 && it.x > index.x }
            if (nextRightSplitter != null) nextSplitters.add(nextRightSplitter)
            graph[index] = nextSplitters
        }
        return graph
    }

    fun getNumberOfPaths(graph: Map<Grid2D.Position, List<Grid2D.Position>>, start: Grid2D.Position, knownPathsFromSplitter: MutableMap<Grid2D.Position, Long>): Long {
        if (knownPathsFromSplitter[start] != null) return knownPathsFromSplitter[start]!!

        val numberOfPaths: Long
        val nextSplitters = graph[start]

        when (nextSplitters?.size) {
            0 -> {
                // This is the last splitter. As the path will be split, add 2.
                numberOfPaths = 2L
            }
            1 -> {
                // The splitter has 1 nextSplitter and 1 end. Calculate the rest of the path and add 1 for the ended path.
                numberOfPaths = 1 + getNumberOfPaths(graph, nextSplitters.first(), knownPathsFromSplitter)
            }
            else -> {
                // The splitter has 2 nextSplitters. Calculate the rest of the path for both.
                val leftSplitter = nextSplitters?.firstOrNull { it.y < start.y }
                val rightSplitter = nextSplitters?.firstOrNull { it.y > start.y }
                numberOfPaths = getNumberOfPaths(graph, leftSplitter!!, knownPathsFromSplitter) + getNumberOfPaths(graph, rightSplitter!!, knownPathsFromSplitter)
            }
        }

        knownPathsFromSplitter[start] = numberOfPaths
        return numberOfPaths
    }

    fun part1(input: List<MutableList<String>>, visualize: Boolean = false): Long {
        val panel = if (visualize) BeamPanel(input) else null
        if (panel != null) {
            createFrame(
                panel = panel,
                input = input,
                pointSize = BEAM_POINT_SIZE,
                title = "Advent of Code, Day 7: Laboratories ")
            Thread.sleep(5000L)
        }

        beam(input, panel)

        val indicesOfSplitters = getIndicesOfSplitters(input)
        return indicesOfSplitters.fold(0L){acc, it ->
            if (input[it.x - 1][it.y] == BEAM) {
                acc + 1
            } else {
                acc + 0
            }
        }
    }

    fun part2(input: List<MutableList<String>>): Long {
        val graph = getGraph(input)
        val knownPathsFromSplitter = mutableMapOf<Grid2D.Position, Long>()
        return getNumberOfPaths(graph, graph.keys.minBy{it.x}, knownPathsFromSplitter)
    }

    val input = readInputAsStrings("Day07").map{it.splitIgnoreEmpty("").toMutableList()}
    part1(input).println()
    part2(input).println()
}
