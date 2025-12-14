data class Graph<T> (val graph: Map<T, List<T>>) {

    fun getNumberOfPaths(
        start: T,
        end: T,
        memory: MutableMap<T, Long> = mutableMapOf()
    ): Long {
        // If the end is found, count the path
        if (start == end) return 1L

        // Look for the start in memory.
        // If there is a start memorized, use the counted number of paths.
        // If there isn't a start memorized, count the number of paths to the end and memorize it.
        // If start can't be found in input, there is no path to the end.
        return memory.getOrPut(start) {
            graph[start]?.sumOf { next ->
                getNumberOfPaths(next, end, memory)
            } ?: 0
        }
    }
}