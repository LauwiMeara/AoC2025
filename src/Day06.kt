const val ADD = "+"
const val MULTIPLY = "*"

fun main() {
    fun turnLists(input: List<List<String>>): List<List<String>> {
        val newInput = mutableListOf<MutableList<String>>()
        for (indexOfProblem in 0..<input[0].size) {
            val problemList = mutableListOf<String>()
            for (indexOfList in input.indices) {
                problemList.add(input[indexOfList][indexOfProblem])
            }
            newInput.add(problemList)
        }
        return newInput
    }

    fun splitNumbersWhileKeepingTheBlankSpaces(input: List<String>): List<List<String>> {
        val regex = Regex("[${ADD}${MULTIPLY}]")
        val indexesOfOperators = regex.findAll(input.last()).map { it.range.first }.toList()
        val newInput = mutableListOf<MutableList<String>>()
        for (s in input.dropLast(1)) {
            var previousIndex = 0
            val newList = mutableListOf<String>()
            for (index in indexesOfOperators.drop(1)) {
                newList.add(s.substring(previousIndex, index - 1))
                previousIndex = index

                if (index == indexesOfOperators.last()) {
                    newList.add(s.substring(previousIndex))
                }
            }
            newInput.add(newList)
        }
        newInput.add(input.last().splitIgnoreEmpty(" ").toMutableList())
        return newInput
    }

    fun turnNumbers(input: List<List<String>>): List<List<String>> {
        val newInput = mutableListOf<MutableList<String>>()
        for (problem in input) {
            val problemList = mutableListOf<String>()
            for (index in problem.maxOf{it.length} - 1 downTo 0) {
                var newNumber = ""
                for (number in problem.dropLast(1)) {
                    if (number.length - 1 < index) {
                        continue
                    }
                    newNumber += number[index]
                }
                newNumber.filter{!it.isWhitespace()}
                problemList.add(newNumber)
            }
            problemList.add(problem.last())
            newInput.add(problemList)
        }
        return newInput
    }

    fun part1(input: List<List<String>>): Long {
        var sum = 0L
        for (problem in input) {
            val numbers = problem.dropLast(1).map{it.toLong()}
            val operator = problem.last()
            sum += if (operator == ADD) {
                numbers.reduce{acc, it -> acc + it}
            } else {
                numbers.reduce{acc, it -> acc * it}
            }
        }
        return sum
    }

    fun part2(input: List<List<String>>): Long {
        var sum = 0L
        for (problem in input) {
            val numbers = problem.dropLast(1).map{it.filter{c -> !c.isWhitespace()}.toLong()}
            val operator = problem.last()
            sum += if (operator == ADD) {
                numbers.reduce{acc, it -> acc + it}
            } else {
                numbers.reduce{acc, it -> acc * it}
            }
        }
        return sum
    }

    val inputPart1 = turnLists(readInputAsStrings("Day06").map{it.splitIgnoreEmpty(" ")})
    val inputPart2 = turnNumbers(turnLists(splitNumbersWhileKeepingTheBlankSpaces(readInputAsStrings("Day06"))))
    part1(inputPart1).println()
    part2(inputPart2).println()
}
