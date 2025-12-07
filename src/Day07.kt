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

    fun getBeamPaths(input: List<MutableList<String>>): List<List<Grid2D.Position>> {
        val indexOfStart = Grid2D.Position(0, input[0].indexOf(START))
        val paths = mutableListOf(listOf(indexOfStart))
        val endedPaths = mutableListOf<List<Grid2D.Position>>()
        val indicesOfSplitters = getIndicesOfSplitters(input).sortedBy{it.x}

        while(paths.size > 0) {
            val path = paths.removeAt(0)
            val nextSplitter = indicesOfSplitters.firstOrNull{it.y == path.last().y && it.x > path.last().x}

            // End path if there isn't a next splitter
            if (nextSplitter == null) {
                endedPaths.add(path)
                continue
            }

            // Add left and right  if there is a next splitter
            val leftPath = path.toList() + Grid2D.Position(nextSplitter.x + 1, nextSplitter.y - 1)
            val rightPath = path.toList() + Grid2D.Position(nextSplitter.x + 1, nextSplitter.y + 1)
            paths.add(leftPath)
            paths.add(rightPath)
            paths.sortedBy{it.last().y}
            println(paths.size)
        }

        return endedPaths
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
        return getBeamPaths(input).size.toLong()
    }

    val input = readInputAsStrings("Day07").map{it.splitIgnoreEmpty("").toMutableList()}
    part1(input).println()
    part2(input).println()
}
