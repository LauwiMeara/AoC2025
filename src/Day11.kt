import java.util.*

data class Path(val nodes: List<String>, val visited: Set<String>)

const val START_SERVER_PART_1 = "you"
const val START_SERVER_PART_2 = "svr"
const val DAC_SERVER = "dac"
const val FFT_SERVER = "fft"
const val END_SERVER = "out"

fun main() {
    fun getPaths(input: Map<String, List<String>>, start: String, end: String): List<List<String>> {
        val paths = PriorityQueue<Path>(
            compareBy { it.nodes.size }
        )
        paths.add(Path(nodes = listOf(start), visited = setOf(start)))
        val endedPaths = mutableListOf<List<String>>()

        while (paths.size != 0) {
            val currentPath = paths.poll()

            val nextServers = input[currentPath.nodes.last()] ?: emptyList()
            for (nextServer in nextServers) {
                // Don't circle through the paths.
                if (nextServer in currentPath.visited) continue
                // Add to endedPaths if the end is reached.
                if (nextServer == end) {
                    endedPaths.add(currentPath.nodes + end)
                    continue
                }
                // Add the next server to the path.
                paths.add(Path(nodes = currentPath.nodes + nextServer, visited = currentPath.visited + nextServer))
            }
        }

        return endedPaths
    }

    fun part1(input: Map<String, List<String>>): Long {
        return getPaths(input, START_SERVER_PART_1, END_SERVER).size.toLong()
    }

    fun part2(input: Map<String, List<String>>): Long {
        // The possible paths are svr -> dac -> fft -> out and svr -> fft -> dac -> out.
        // So the answer is the possible paths between (svr -> dac * dac -> fft * fft -> out) + (svr -> fft * fft -> dac * dac -> out)
        val pathsSvrToDac = getPaths(input, START_SERVER_PART_2, DAC_SERVER).size.toLong()
        println(pathsSvrToDac)
        val pathsDacToFft = getPaths(input, DAC_SERVER, FFT_SERVER).size
        println(pathsDacToFft)
        val pathsFftToOut = getPaths(input, FFT_SERVER, END_SERVER).size
        println(pathsFftToOut)
        val pathsSvrToFft = getPaths(input, START_SERVER_PART_2, FFT_SERVER).size.toLong()
        println(pathsSvrToFft)
        val pathsFftToDac = getPaths(input, FFT_SERVER, DAC_SERVER).size
        println(pathsFftToDac)
        val pathsDacToOut = getPaths(input, DAC_SERVER, END_SERVER).size
        println(pathsDacToOut)
        return (pathsSvrToDac * pathsDacToFft * pathsFftToOut) + (pathsSvrToFft * pathsFftToDac * pathsDacToOut)
    }

    val input = readInputAsStrings("Day11")
        .map { it.split(" ").map { string -> string.filter { c -> c != ':' } } }
        .associate { Pair(it.first(), it.subList(1, it.size)) }
    part1(input).println()
    part2(input).println()
}
