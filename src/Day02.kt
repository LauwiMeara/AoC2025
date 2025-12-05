fun main() {
    fun checkIfSubstringIsRepeatedTwice(string: String): Boolean {
        // If the length of the string is uneven, it can't consist of two equal parts.
        if (string.length % 2 != 0) {
            return false
        }

        val firstPart = string.substring(0, string.length / 2)
        val secondPart = string.substring(string.length/2)
        return firstPart == secondPart
    }

    fun checkIfSubstringIsRepeated(string: String): Boolean {
        for (i in 1..string.length / 2) {
            val substrings = string.chunked(i)
            if (substrings.all{it == substrings.first()}) {
                return true
            }
        }
        return false
    }

    fun part1(input: List<LongRange>): Long {
        var sum = 0L
        input.forEach{range -> range.forEach{
            if (checkIfSubstringIsRepeatedTwice(it.toString())) {
                sum += it
            }
        }}
        return sum
    }

    fun part2(input: List<LongRange>): Long {
        var sum = 0L
        input.forEach{range -> range.forEach{
            if (checkIfSubstringIsRepeated(it.toString())) {
                sum += it
            }
        }}
        return sum
    }

    val input = readInputSplitByDelimiter("Day02", ",").toListOfLongRange()
    part1(input).println()
    part2(input).println()
}
