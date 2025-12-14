const val START_SERVER_PART_1 = "you"
const val START_SERVER_PART_2 = "svr"
const val DAC_SERVER = "dac"
const val FFT_SERVER = "fft"
const val END_SERVER = "out"

fun main() {

    // Depth first search
    fun getNumberOfPaths(
        input: Map<String, List<String>>,
        start: String,
        end: String,
        memory: MutableMap<String, Long> = mutableMapOf()
    ): Long {
        // If the end is found, count the path
        if (start == end) return 1L

        // Look for the start in memory.
        // If there is a start memorized, use the counted number of paths.
        // If there isn't a start memorized, count the number of paths to the end and memorize it.
        // If start can't be found in input, there is no path to the end.
        return memory.getOrPut(start) {
            input[start]?.sumOf { next ->
                getNumberOfPaths(input, next, end, memory)
            } ?: 0
        }
    }

    fun part1(input: Map<String, List<String>>): Long {
        return getNumberOfPaths(input, START_SERVER_PART_1, END_SERVER)
    }

    fun part2(input: Map<String, List<String>>): Long {
        // The possible paths are svr -> dac -> fft -> out and svr -> fft -> dac -> out.
        // So the answer is the possible paths between (svr -> dac * dac -> fft * fft -> out) + (svr -> fft * fft -> dac * dac -> out)
        val pathsSvrToDac = getNumberOfPaths(input, START_SERVER_PART_2, DAC_SERVER)
        val pathsDacToFft = getNumberOfPaths(input, DAC_SERVER, FFT_SERVER)
        val pathsFftToOut = getNumberOfPaths(input, FFT_SERVER, END_SERVER)
        val pathsSvrToFft = getNumberOfPaths(input, START_SERVER_PART_2, FFT_SERVER)
        val pathsFftToDac = getNumberOfPaths(input, FFT_SERVER, DAC_SERVER)
        val pathsDacToOut = getNumberOfPaths(input, DAC_SERVER, END_SERVER)
        return (pathsSvrToDac * pathsDacToFft * pathsFftToOut) + (pathsSvrToFft * pathsFftToDac * pathsDacToOut)
    }

    val input = readInputAsStrings("Day11")
        .map { it.split(" ").map { string -> string.filter { c -> c != ':' } } }
        .associate { Pair(it.first(), it.subList(1, it.size)) }
    part1(input).println()
    part2(input).println()
}
