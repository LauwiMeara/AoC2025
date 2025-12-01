@file:Suppress("unused")

import java.io.File
import kotlin.math.abs

/**
 * Reads from the given input txt file as one string.
 */
fun readInput(name: String): String = File("src", "$name.txt")
    .readText()

/**
 * Reads lines from the given input txt file as integers.
 */
fun readInputAsInts(name: String): List<Int> = File("src", "$name.txt")
    .readLines()
    .map { it.toInt() }

/**
 * Reads from the given input txt file as strings split by the given delimiter.
 */
fun readInputSplitByDelimiter(name: String, delimiter: String): List<String> = File("src", "$name.txt")
    .readText()
    .split(delimiter)

/**
 * Reads lines from the given input txt file as strings. Can be used to read the input as a grid of characters.
 */
fun readInputAsStrings(name: String): List<String> = File("src", "$name.txt")
    .readLines()

/**
 * Adds a border with the given character to a grid. Useful when looking for neighbours of points in the input grid.
 */
fun addBorderToGrid(input: List<String>, borderCharacter: Char): List<String> {
    val emptyRow = borderCharacter.toString().repeat(input.first().length)
    val borderedInput = mutableListOf(emptyRow)
    for (line in input) {
        val borderedLine = "$borderCharacter$line$borderCharacter"
        borderedInput.add(borderedLine)
    }
    borderedInput.add(emptyRow)
    return borderedInput
}

/**
 * Calculates the absolute difference between two integers.
 */
fun distanceTo(a: Int, b: Int): Int = abs(a - b)

/**
 * Checks if the given x and y fit in the grid. The grid can be specified by min (optional, default is 0) and max (required) values.
 */
fun fitsInGrid(x: Int, y: Int, maxX: Int, maxY: Int, minX: Int = 0, minY: Int = 0): Boolean {
    return x in minX..maxX && y in minY..maxY
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * The cleaner shorthand for splitting strings and filtering non-empty values.
 */
fun CharSequence.splitIgnoreEmpty(vararg delimiters: String): List<String> {
    return this.split(*delimiters).filter {
        it.isNotEmpty()
    }
}

/**
 * Get all possible permutations of a string. Usage: "abc".permute()
 */
fun String.permute(result: String = ""): List<String> =
    if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }.distinct()
