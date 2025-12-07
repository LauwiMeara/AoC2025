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
        return 0
    }

    val input = readInputAsStrings("Day07").map{it.splitIgnoreEmpty("").toMutableList()}
    part1(input, true).println()
    part2(input).println()
}
