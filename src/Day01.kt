data class SafeRotation (val direction: String, val turns: Int)
const val START_DIAL = 50
const val MIN_DIAL = 0
const val MAX_DIAL = 99

fun main() {
    fun rotate(safeRotation: SafeRotation, currentDial: Int): Int {
        val turns = safeRotation.turns % (MAX_DIAL + 1)
        var dial: Int

        if (safeRotation.direction == "L") {
            dial = currentDial - turns

            if (dial < MIN_DIAL) {
                dial += (MAX_DIAL + 1)
            }
        } else {
            dial = currentDial + turns

            if (dial > MAX_DIAL) {
                dial -= (MAX_DIAL + 1)
            }
        }

        return dial
    }

    fun rotatePart2(safeRotation: SafeRotation, currentDial: Int): Pair<Int,Int> {
        val turns = safeRotation.turns % (MAX_DIAL + 1)
        var passing0 = (safeRotation.turns / (MAX_DIAL + 1))
        var dial: Int

        if (safeRotation.direction == "L") {
            dial = currentDial - turns

            if (dial < MIN_DIAL) {
                dial += (MAX_DIAL + 1)

                // If the dial before turning is 0, it has already been counted.
                if (currentDial != 0) {
                    passing0++
                }
            }
        } else {
            dial = currentDial + turns

            if (dial > MAX_DIAL) {
                dial -= (MAX_DIAL + 1)

                // If the dial after turning is 0, it will be counted later.
                if (dial != 0) {
                    passing0++
                }
            }
        }

        return Pair(dial, passing0)
    }

    fun part1(input: List<SafeRotation>): Long {
        var currentDial = START_DIAL
        var numberOfTimesDialAt0 = 0L

        for (safeRotation in input) {
            currentDial = rotate(safeRotation = safeRotation, currentDial = currentDial)
            if (currentDial == 0) {
                numberOfTimesDialAt0++
            }
        }

        return numberOfTimesDialAt0
    }

    fun part2(input: List<SafeRotation>): Long {
        var currentDial = START_DIAL
        var numberOfTimesDialPassing0 = 0L

        for (safeRotation in input) {
            val (dial, passing0) = rotatePart2(safeRotation = safeRotation, currentDial = currentDial)
            currentDial = dial
            numberOfTimesDialPassing0 += passing0

            if (currentDial == 0) {
                numberOfTimesDialPassing0++
            }
        }

        return numberOfTimesDialPassing0
    }

    val input = readInputAsStrings("Day01").map { SafeRotation(direction = it.substring(0, 1), turns = it.substring(1).toInt())}
    part1(input).println()
    part2(input).println()
}
