const val PRESENT_AREA = '#'

data class Tree (val areaSize: Int, val numberOfPresents: List<Int>)

fun main() {
    // The given puzzle input can be solved by only accounting for the size of the present, disregarding its shape.
    fun getSizePerPresent(presentInput: List<String>): List<Int> {
        return presentInput.map{present -> present.count{it == PRESENT_AREA}}
    }

    fun getTrees(treeInput: String): List<Tree> {
        return treeInput
            .split(System.lineSeparator())
            .map{it.splitIgnoreEmpty("x", ":", " ").map{c -> c.toInt()}}
            .map{Tree(areaSize = it[0] * it[1], numberOfPresents = it.subList(2, it.size))}
    }

    fun parseInput(input: List<String>): Pair<List<Int>, List<Tree>> {
        return Pair(getSizePerPresent(input.dropLast(1)), getTrees(input.last()))
    }

    fun part1(input: Pair<List<Int>, List<Tree>>): Long {
        val (presentSizes, trees) = input
        var sum = 0L
        for (tree in trees) {
            val neededAreaSize = presentSizes
                .zip(tree.numberOfPresents)
                .map{it.first * it.second}
                .fold(0){acc, it -> acc + it}
            if (tree.areaSize >= neededAreaSize) {
                sum++
            }
        }
        return sum
    }

    val input = parseInput(readInput("Day12").split(System.lineSeparator() + System.lineSeparator()))
    part1(input).println()
}
