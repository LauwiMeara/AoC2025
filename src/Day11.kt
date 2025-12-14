const val START_SERVER_PART_1 = "you"
const val START_SERVER_PART_2 = "svr"
const val DAC_SERVER = "dac"
const val FFT_SERVER = "fft"
const val END_SERVER = "out"

fun main() {
    fun part1(input: Map<String, List<String>>): Long {
        return Graph(input).getNumberOfPaths(START_SERVER_PART_1, END_SERVER)
    }

    fun part2(input: Map<String, List<String>>): Long {
        // The possible paths are svr -> dac -> fft -> out and svr -> fft -> dac -> out.
        // So the answer is the possible paths between (svr -> dac * dac -> fft * fft -> out) + (svr -> fft * fft -> dac * dac -> out)
        val graph = Graph(input)
        val pathsSvrToDac = graph.getNumberOfPaths(START_SERVER_PART_2, DAC_SERVER)
        val pathsDacToFft = graph.getNumberOfPaths(DAC_SERVER, FFT_SERVER)
        val pathsFftToOut = graph.getNumberOfPaths(FFT_SERVER, END_SERVER)
        val pathsSvrToFft = graph.getNumberOfPaths(START_SERVER_PART_2, FFT_SERVER)
        val pathsFftToDac = graph.getNumberOfPaths(FFT_SERVER, DAC_SERVER)
        val pathsDacToOut = graph.getNumberOfPaths(DAC_SERVER, END_SERVER)
        return (pathsSvrToDac * pathsDacToFft * pathsFftToOut) + (pathsSvrToFft * pathsFftToDac * pathsDacToOut)
    }

    val input = readInputAsStrings("Day11")
        .map { it.split(" ").map { string -> string.filter { c -> c != ':' } } }
        .associate { Pair(it.first(), it.subList(1, it.size)) }
    part1(input).println()
    part2(input).println()
}
