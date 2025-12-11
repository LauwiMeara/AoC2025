const val START_SERVER_PART_1 = "you"
const val START_SERVER_PART_2 = "svr"
const val DAC_SERVER = "dac"
const val FFT_SERVER = "fft"
const val END_SERVER = "out"

fun main() {
    fun getPaths(input: Map<String, List<String>>, start: String): List<List<String>> {
        val paths = mutableListOf(listOf(start))
        val endedPaths = mutableListOf<List<String>>()

        while (paths.size != 0) {
            println(paths.size)
            println(endedPaths.size)

            val currentPath = paths.maxBy{it.size}
            paths.remove(currentPath)

            val nextServers = input[currentPath.last()] ?: emptyList()
            for (nextServer in nextServers) {
                // Don't circle through the paths.
                if (currentPath.contains(nextServer)) continue
                // Add to endedPaths if the end is reached.
                if (nextServer == END_SERVER) {
                    endedPaths.add(currentPath + END_SERVER)
                    continue
                }
                // Add the next server to the path.
                paths.add(currentPath + nextServer)
            }
        }

        return endedPaths
    }

    fun part1(input: Map<String, List<String>>): Long {
        return getPaths(input, START_SERVER_PART_1).size.toLong()
    }

    fun part2(input: Map<String, List<String>>): Long {
        return getPaths(input, START_SERVER_PART_2)
            .filter{it.contains(DAC_SERVER) && it.contains(FFT_SERVER)}
            .size.toLong()
    }

    val input = readInputAsStrings("Day11")
        .map { it.split(" ").map { string -> string.filter { c -> c != ':' } } }
        .associate { Pair(it.first(), it.subList(1, it.size)) }
    part1(input).println()
    part2(input).println()
}
