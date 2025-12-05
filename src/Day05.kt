fun main() {
    fun getFreshRanges(input: List<String>): List<LongRange> {
        return input.first().split(System.lineSeparator()).map{
            val firstNumber = it.substringBefore('-').toLong()
            val lastNumber = it.substringAfter('-').toLong()
            firstNumber.rangeTo(lastNumber)
        }
    }

    fun getIngredients(input:List<String>): List<Long> {
        return input.last().split(System.lineSeparator()).map{it.toLong()}
    }

    fun getCombinedFreshRanges(ranges: List<LongRange>): List<LongRange> {
        val combinedRanges = mutableListOf<LongRange>()

        for (range in ranges.sortedBy { it.first() }) {
            if (combinedRanges.isEmpty() || combinedRanges.last().last() < range.first()) {
                combinedRanges.add(range)
            } else if (combinedRanges.last().last() < range.last()) {
                combinedRanges.add(combinedRanges.last().first..range.last())
                combinedRanges.removeAt(combinedRanges.size - 2)
            }
        }

        return combinedRanges
    }

    fun getNumberOfIngredients(range: LongRange): Long {
        return range.last() - range.first() + 1
    }

    fun part1(input: List<String>): Long {
        val ranges = getFreshRanges(input)
        val ingredients = getIngredients(input)
        var sum = 0L
        ingredients.forEach{ingredient ->
            if (ranges.any{it.contains(ingredient)}) {
                sum++
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val ranges = getFreshRanges(input)
        val combinedRanges = getCombinedFreshRanges(ranges)
        return combinedRanges.fold(0L){acc, it -> acc + getNumberOfIngredients(it)}
    }

    val input = readInputSplitByDelimiter("Day05", System.lineSeparator() + System.lineSeparator())
    part1(input).println()
    part2(input).println()
}
