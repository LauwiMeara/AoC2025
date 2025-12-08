import java.util.*

class Grid3D {
    data class Position(val x: Int, val y: Int, val z: Int) {
        operator fun plus(other: Position): Position =
            Position(x + other.x, y + other.y, z + other.z)

        operator fun times(number: Int): Position =
            Position(x * number, y * number, z * number)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Position) return false
            return this.x == other.x && this.y == other.y && this.z == other.z
        }

        override fun hashCode(): Int {
            return Objects.hash(x, y, z)
        }
    }
}