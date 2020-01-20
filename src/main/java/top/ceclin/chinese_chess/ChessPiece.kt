package top.ceclin.chinese_chess

internal interface ChessPiece {

    /**
     * Get the chessboard to which the instance belongs.
     */
    val chessboard: Chessboard

    /**
     * Whether the instance is a black piece or not.
     */
    val isBlack: Boolean

    /**
     * Get the current position.
     */
    val position: ChessPosition

    /**
     * Move the instance to target position if possible.
     *
     * This function only changes the internal state of the instance.
     *
     * @return the eaten chess piece caused by this move or null
     * @exception top.ceclin.chinese_chess.exception.InvalidMoveException
     */
    fun moveTo(target: ChessPosition): ChessPiece?

    /**
     * Move the instance back to [previous] position without checking.
     */
    fun retractTo(previous: ChessPosition, eaten: ChessPiece?)
}