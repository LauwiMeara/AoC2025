const val PAPER_ROLL = '@'

fun main() {
    fun getIndicesOf(input: List<String>, letter: Char): List<Grid2D.Position> {
        return input.flatMapIndexed { indexY, it ->
            it.mapIndexedNotNull { indexX, c -> if (c == letter) Grid2D.Position(indexX, indexY) else null }
        }
    }

    fun getAdjacentPaperRolls(it: Grid2D.Position, indices: List<Grid2D.Position>): Int {
        var adjacentPaperRolls = 0
        Grid2D.cardinalsAndDiagonals.forEach { direction ->
            val x = it.x + direction.value.x
            val y = it.y + direction.value.y
            if (indices.contains(Grid2D.Position(x, y))) {
                adjacentPaperRolls += 1
            }
        }
        return adjacentPaperRolls
    }

    fun part1(input: List<String>): Long {
        var accessiblePaperRolls = 0L
        val indices = getIndicesOf(input, PAPER_ROLL)
        indices.forEach {
            val adjacentPaperRolls = getAdjacentPaperRolls(it, indices)
            if (adjacentPaperRolls < 4) {
                accessiblePaperRolls++
            }
        }
        return accessiblePaperRolls
    }

    fun part2(input: List<String>): Long {
        var accessiblePaperRolls = 0L
        val indices = getIndicesOf(input, PAPER_ROLL).toMutableList()
        var removedPaperRolls: MutableList<Grid2D.Position>

        while (true) {
            removedPaperRolls = mutableListOf()
            indices.forEach {
                val adjacentPaperRolls = getAdjacentPaperRolls(it, indices)
                if (adjacentPaperRolls < 4) {
                    removedPaperRolls.add(it)
                    accessiblePaperRolls++
                }
            }

            if (removedPaperRolls.size == 0) break
            indices.removeAll(removedPaperRolls)
        }

        return accessiblePaperRolls
    }

    val input = readInputAsStrings("Day04")
    part1(input).println()
    part2(input).println()
}
