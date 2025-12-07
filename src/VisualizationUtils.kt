import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

const val AOC_BACKGROUND_COLOR = "#0f0f23"
const val AOC_COLOR_LIGHT_GREEN = "#00cc00"
const val AOC_COLOR_DARK_GREEN = "#009900"
const val FRAME_BORDER = 50

fun createFrame(panel: JPanel, input: List<List<Any>>, pointSize: Int, title: String) {
    val width = input[0].size * pointSize + (FRAME_BORDER * 2)
    val height = input.size * pointSize + (FRAME_BORDER * 2)
    val frame = JFrame(title)

    frame.setSize(width, height)
    frame.isVisible = true
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.add(panel)
    frame.validate()
}

