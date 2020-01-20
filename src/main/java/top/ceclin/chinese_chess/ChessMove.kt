package top.ceclin.chinese_chess

data class ChessMove(val from: ChessPosition, val to: ChessPosition) {
    companion object {
        @JvmStatic
        fun parse(move: String): ChessMove {
            require(move.length == 4) { "Move command should be 4-length but \"$move\"" }
            return ChessMove(ChessPosition(move[0], move[1]), ChessPosition(move[2], move[3]))
        }
    }
}