fun main() {
    fun getHighestDigitWithIndex(string: String, neededDigitsAfterIndex: Int): Pair<Char, Int> {
        for (digit in '9' downTo '0') {
            val index = string.indexOf(digit)

            // If the index is found and there are enough digits left, return the digit with index.
            if (index != -1 && neededDigitsAfterIndex <= string.length - 1 - index) {
                return Pair(digit, index)
            }
        }

        return Pair('x', -1) // Shouldn't happen
    }

    fun part1(input: List<String>): Long {
        val joltages = mutableListOf<Long>()

        input.forEach{
            val (firstDigit, firstIndex) = getHighestDigitWithIndex(string = it, neededDigitsAfterIndex = 1)
            val (secondDigit, _) = getHighestDigitWithIndex(string = it.substring(firstIndex + 1), neededDigitsAfterIndex = 0)
            val joltage = "" + firstDigit + secondDigit
            joltages.add(joltage.toLong())
        }

        return joltages.reduce{acc, it -> acc + it}
    }

    fun part2(input: List<String>): Long {
        val joltages = mutableListOf<Long>()

        input.forEach {
            var joltage = ""
            var fromIndex = 0

            for (neededDigits in 11 downTo 0) {
                val (digit, index) = getHighestDigitWithIndex(string = it.substring(fromIndex), neededDigitsAfterIndex = neededDigits)

                // If there are just enough digits left, add the rest of the string to the joltage.
                if (index == it.length - 1 - neededDigits) {
                    joltage += it.substring(index)
                    break
                }

                fromIndex += index + 1
                joltage += digit
            }

            joltages.add(joltage.toLong())
        }

        return joltages.reduce{acc, it -> acc + it}
    }

    val input = readInputAsStrings("Day03")
    part1(input).println()
    part2(input).println()
}
