package top.ceclin.chinese_chess

data class ChessPosition(val x: Char, val y: Char) {

    init {
        require(x in 'a'..'i' && y in '0'..'9') { "Position ($x,$y) is out of range" }
    }

    fun toCoordinate() = Pair(x - 'a', y - '0')
}