data class Machine(val diagram: Map<Int, Boolean>, val buttons: List<List<Int>>, val joltageRequirements: List<Int>)
data class LightPath(val lights: Map<Int, Boolean>, val numberOfPressedButtons: Int)
data class JoltagePath(val joltages: List<Int>, val numberOfPressedButtons: Int, val estimatedCost: Int) {
    val cost: Int get() = numberOfPressedButtons + estimatedCost
}
const val ON = '#'

fun main() {

    // Dijkstra
    fun getMinNumberOfPressedButtons(
        possiblePaths: MutableList<LightPath>,
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

            // If the newLights are already present in the possiblePaths with a lower cost, don't add a new path.
            if (possiblePaths.filter{it.lights == newLights}.any{it.numberOfPressedButtons <= newCost}) {
                continue
            }

            // If the diagram is found, return the number of pressed buttons.
            if (newLights == machine.diagram) {
                return newCost
            }

            // Else, add the path to the list of possible paths.
            possiblePaths.add(LightPath(newLights, newCost))
        }

        return getMinNumberOfPressedButtons(possiblePaths, machine)
    }

    // A*
    fun getMinNumberOfPressedButtonsForJoltages(
        possiblePaths: MutableList<JoltagePath>,
        machine: Machine
    ): Int {
        // Find the path with the lowest cost (number of pressed buttons)
        val currentPath = possiblePaths.minBy{it.cost}
        possiblePaths.remove(currentPath)
        println(possiblePaths.size)

        // Find out which joltages should be counted upwards and which buttons could do that.
        val possibleButtons = mutableSetOf<List<Int>>()
        for (button in machine.buttons) {
            if (button.all{currentPath.joltages[it] < machine.joltageRequirements[it]}){
                possibleButtons.add(button)
            }
        }

        for (button in possibleButtons) {
            // Add one joltage to the counter according to the button.
            val newJoltages = currentPath.joltages.mapIndexed {index, it ->
                if (button.contains(index)) {
                    it + 1
                } else {
                    it
                }
            }
            val newCost = currentPath.numberOfPressedButtons + 1

            val newEstimatedCost =  (machine.joltageRequirements.sum() - newJoltages.sum()) / machine.buttons.maxOf{it.size}

            // If any of the joltage counters is higher than the joltage requirement, don't add the new path.
            if (newJoltages.mapIndexed { index, it -> it > machine.joltageRequirements[index]}.any{ it }) {
                continue
            }

            // If the newJoltages are already present in the possiblePaths with a lower or equal cost, don't add the new path.
            // If they are present in the possiblePaths with a higher cost, delete them from the possiblePaths.
            val presentPathsWithSameJoltages = possiblePaths.filter{it.joltages == newJoltages}
            if (presentPathsWithSameJoltages.isNotEmpty()) {
                if (presentPathsWithSameJoltages.any{it.numberOfPressedButtons <= newCost}) {
                    continue
                }
                // Greedy algorithm
                if (presentPathsWithSameJoltages.any{it.numberOfPressedButtons + it.estimatedCost <= newCost + newEstimatedCost}) {
                    continue
                }
                possiblePaths.removeAll(presentPathsWithSameJoltages)
            }

            // If the joltage requirement is met, return the number of pressed buttons.
            if (newJoltages == machine.joltageRequirements) {
                return newCost
            }

            // Else, add the path to the list of possible paths.
            possiblePaths.add(JoltagePath(newJoltages, newCost, newEstimatedCost))
        }

        return getMinNumberOfPressedButtonsForJoltages(possiblePaths, machine)
    }

    fun part1(input: List<Machine>): Long {
        var sum = 0L
        for (machine in input) {
            val possiblePaths = mutableListOf<LightPath>()
            val startLights = machine.diagram.mapValues { false }
            possiblePaths.add(LightPath(startLights, 0))
            sum += getMinNumberOfPressedButtons(possiblePaths, machine)
        }
        return sum
    }

    fun part2(input: List<Machine>): Long {
        var sum = 0L
        for (machine in input) {
            val possiblePaths = mutableListOf<JoltagePath>()
            val startJoltages = MutableList(machine.joltageRequirements.size) { 0 }
            val estimatedCost = machine.joltageRequirements.sum() / machine.buttons.maxOf{it.size}
            possiblePaths.add(JoltagePath(startJoltages, 0, estimatedCost))
            sum += getMinNumberOfPressedButtonsForJoltages(possiblePaths, machine)
        }
        return sum
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
