import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

const val BEAM_POINT_SIZE = 4

class BeamPanel(var input: List<List<String>>) : JPanel() {
    init {
        this.background = Color.decode(AOC_BACKGROUND_COLOR)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        for (x in input.indices) {
            for (y in input[x].indices) {
                when (input[x][y]) {
                    START -> {
                        g.color = Color.YELLOW
                        g.fillOval(y * BEAM_POINT_SIZE + FRAME_BORDER, x * BEAM_POINT_SIZE + FRAME_BORDER, BEAM_POINT_SIZE, BEAM_POINT_SIZE)
                    }
                    SPLITTER -> {
                        g.color = Color.decode(AOC_COLOR_DARK_GREEN)
                        g.fillPolygon(
                            intArrayOf(
                                y * BEAM_POINT_SIZE + FRAME_BORDER,
                                y * BEAM_POINT_SIZE + (BEAM_POINT_SIZE / 2) + FRAME_BORDER,
                                y * BEAM_POINT_SIZE + BEAM_POINT_SIZE + FRAME_BORDER
                            ),
                            intArrayOf(
                                x * BEAM_POINT_SIZE + BEAM_POINT_SIZE + FRAME_BORDER,
                                x * BEAM_POINT_SIZE + FRAME_BORDER,
                                x * BEAM_POINT_SIZE + BEAM_POINT_SIZE + FRAME_BORDER
                            ),
                            3
                        )
                    }
                    BEAM -> {
                        g.color = Color.decode(AOC_COLOR_LIGHT_GREEN)
                        g.fillRect(y * BEAM_POINT_SIZE + (BEAM_POINT_SIZE / 4) + FRAME_BORDER, x * BEAM_POINT_SIZE + FRAME_BORDER, BEAM_POINT_SIZE / 2, BEAM_POINT_SIZE)
                    }
                    else -> continue
                }
            }
        }
    }
}

fun BeamPanel.repaint(input: List<List<String>>) {
    Thread.sleep(100L)
    this.input = input
    this.repaint()
}

