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

    fun getPaths(
        graph: Map<Grid2D.Position, List<Grid2D.Position>>): MutableList<List<Grid2D.Position>> {
        val startSplitter = graph.minBy { it.key.x }.key
        val pathsInProgress = mutableListOf(listOf(startSplitter))
        val pathsWithEnds = mutableListOf<List<Grid2D.Position>>()

        while (true) {
            println("In progress: ${pathsInProgress.size}, ended: ${pathsWithEnds.size}")
            // If all paths have reached the end (no next splitters), stop searching.
            if (pathsInProgress.isEmpty()) break

            // Get path that hasn't reached the end yet.
            val currentPath = pathsInProgress.first().toMutableList()
            pathsInProgress.removeAt(0)

            val lastSplitter = currentPath.last()
            val nextSplitters = graph[lastSplitter] ?: break
            if (nextSplitters.isEmpty()) {
                // Add the path twice, as there will be two paths after the last splitter and both will reach the end.
                pathsWithEnds.add(currentPath)
                pathsWithEnds.add(currentPath)
                continue
            }
            if (nextSplitters.size == 1) {
                // Add the path once, as one path will reach another splitter and one path will reach the end.
                pathsWithEnds.add(currentPath)
            }
            for (nextSplitter in nextSplitters) {
                // Update the path with the next splitter.
                val newPath = currentPath + nextSplitter
                pathsInProgress.add(newPath)
            }
        }
        return pathsWithEnds
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
        val paths = getPaths(graph)
        return paths.size.toLong()
    }

    val input = readInputAsStrings("Day07").map{it.splitIgnoreEmpty("").toMutableList()}
    part1(input).println()
    part2(input).println()
}
