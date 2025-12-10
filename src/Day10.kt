data class Machine(val diagram: Map<Int, Boolean>, val buttons: List<List<Int>>, val joltageRequirements: List<Int>)
data class Path(val lights: Map<Int, Boolean>, val numberOfPressedButtons: Int)
const val ON = '#'

fun main() {
    fun getMinNumberOfPressedButtons(
        possiblePaths: MutableList<Path>,
        machine: Machine
    ): Int {
        // Find the path with the lowest cost (number of pressed buttons)
        val minCost = possiblePaths.minOf{it.numberOfPressedButtons}
        val currentPath = possiblePaths.first { it.numberOfPressedButtons == minCost }
        possiblePaths.remove(currentPath)

        // Find out which lights should be triggered and which buttons could do that.
        val possibleButtons = mutableSetOf<List<Int>>()
        for (index in currentPath.lights.keys) {
            if (currentPath.lights[index] != machine.diagram[index]) {
                possibleButtons.addAll(machine.buttons.filter { it.contains(index) })
            }
        }

        for (button in possibleButtons) {
            // Toggle the lights on/off according to the button.
            val newLights = currentPath.lights.mapValues {
                if (button.contains(it.key)) {
                    !it.value
                } else {
                    it.value
                }
            }
            val newCost = currentPath.numberOfPressedButtons + 1

            // If the newLights are already present to the possiblePaths with a lower cost, don't add a new path.
            if (possiblePaths.filter{it.lights == newLights}.any{it.numberOfPressedButtons <= newCost}) {
                continue
            }

            // If the diagram is found, return the number of pressed buttons.
            if (newLights == machine.diagram) {
                return newCost
            }

            // Else, add the path to the list of possible paths.
            possiblePaths.add(Path(newLights, newCost))
        }

        return getMinNumberOfPressedButtons(possiblePaths, machine)
    }

    fun part1(input: List<Machine>): Long {
        var sum = 0L
        for (machine in input) {
            val possiblePaths = mutableListOf<Path>()
            val startLights = machine.diagram.mapValues { false }
            possiblePaths.add(Path(startLights, 0))
            sum += getMinNumberOfPressedButtons(possiblePaths, machine)
        }
        return sum
    }

    fun part2(input: List<Machine>): Long {
        return 0
    }

    val brackets = listOf('(', ')', '[', ']', '{', '}')
    val input = readInputAsStrings("Day10")
        .map{it.filter{c -> !brackets.contains(c)}.split(" ")}
        .map{machine ->
            val diagram = machine.first().mapIndexed { index, it -> Pair(index, it == ON) }.toMap()
            val joltageRequirements = machine.last().split(",").map{it.toInt()}
            val buttons = machine.subList(1, machine.size - 1).map{it.split(",").map{button -> button.toInt()}}
            Machine(diagram, buttons, joltageRequirements)
        }
    part1(input).println()
    part2(input).println()
}
