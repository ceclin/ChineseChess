package top.ceclin.chinese_chess

import top.ceclin.chinese_chess.exception.InvalidMoveException
import top.ceclin.chinese_chess.exception.PieceNotFoundException
import java.lang.Error
import java.lang.UnsupportedOperationException
import kotlin.IllegalArgumentException
import kotlin.math.absoluteValue

/**
 * This class represents the status of a chessboard.
 */
internal class Chessboard private constructor() {

    companion object {
        fun default(): Chessboard {
            return Chessboard().apply {
                placePiece(RookPiece(false, ChessPosition('a', '0')))
                placePiece(RookPiece(false, ChessPosition('i', '0')))
                placePiece(HorsePiece(false, ChessPosition('b', '0')))
                placePiece(HorsePiece(false, ChessPosition('h', '0')))
                placePiece(ElephantPiece(false, ChessPosition('c', '0')))
                placePiece(ElephantPiece(false, ChessPosition('g', '0')))
                placePiece(AdviserPiece(false, ChessPosition('d', '0')))
                placePiece(AdviserPiece(false, ChessPosition('f', '0')))
                placePiece(KingPiece(false, ChessPosition('e', '0')))
                placePiece(CannonPiece(false, ChessPosition('b', '2')))
                placePiece(CannonPiece(false, ChessPosition('h', '2')))
                placePiece(PawnPiece(false, ChessPosition('a', '3')))
                placePiece(PawnPiece(false, ChessPosition('c', '3')))
                placePiece(PawnPiece(false, ChessPosition('e', '3')))
                placePiece(PawnPiece(false, ChessPosition('g', '3')))
                placePiece(PawnPiece(false, ChessPosition('i', '3')))

                placePiece(RookPiece(true, ChessPosition('a', '9')))
                placePiece(RookPiece(true, ChessPosition('i', '9')))
                placePiece(HorsePiece(true, ChessPosition('b', '9')))
                placePiece(HorsePiece(true, ChessPosition('h', '9')))
                placePiece(ElephantPiece(true, ChessPosition('c', '9')))
                placePiece(ElephantPiece(true, ChessPosition('g', '9')))
                placePiece(AdviserPiece(true, ChessPosition('d', '9')))
                placePiece(AdviserPiece(true, ChessPosition('f', '9')))
                placePiece(KingPiece(true, ChessPosition('e', '9')))
                placePiece(CannonPiece(true, ChessPosition('b', '7')))
                placePiece(CannonPiece(true, ChessPosition('h', '7')))
                placePiece(PawnPiece(true, ChessPosition('a', '6')))
                placePiece(PawnPiece(true, ChessPosition('c', '6')))
                placePiece(PawnPiece(true, ChessPosition('e', '6')))
                placePiece(PawnPiece(true, ChessPosition('g', '6')))
                placePiece(PawnPiece(true, ChessPosition('i', '6')))
            }
        }
    }

    private val board: Array<Array<ChessPiece>> = Array(9) { Array(10) { EmptyPiece as ChessPiece } }

    fun toFEN(): String = buildString {
        var count = 0
        fun appendCountIfNecessary() {
            if (count != 0) {
                append(count)
                count = 0
            }
        }
        for (y in 9 downTo 0) {
            for (x in 0..8) {
                val piece = board[x][y]
                if (piece == EmptyPiece) {
                    count++
                } else {
                    appendCountIfNecessary()
                    // It is better to extract it as a member function of ChessPiece
                    when (piece) {
                        is RookPiece -> append('r'.let { if (piece.isBlack) it else it.toUpperCase() })
                        is HorsePiece -> append('n'.let { if (piece.isBlack) it else it.toUpperCase() })
                        is ElephantPiece -> append('b'.let { if (piece.isBlack) it else it.toUpperCase() })
                        is AdviserPiece -> append('a'.let { if (piece.isBlack) it else it.toUpperCase() })
                        is KingPiece -> append('k'.let { if (piece.isBlack) it else it.toUpperCase() })
                        is CannonPiece -> append('c'.let { if (piece.isBlack) it else it.toUpperCase() })
                        is PawnPiece -> append('p'.let { if (piece.isBlack) it else it.toUpperCase() })
                    }
                }
            }
            appendCountIfNecessary()
            if (y != 0) {
                append('/')
            }
        }
    }

    private fun placePiece(piece: ChessPiece) {
        val co = piece.position.toCoordinate()
        board[co.first][co.second] = piece
    }

    fun isEmpty(pos: ChessPosition): Boolean = pos.toCoordinate().let {
        board[it.first][it.second] == EmptyPiece
    }

    fun isNotEmpty(pos: ChessPosition): Boolean = !isEmpty(pos)

    fun pieceAt(pos: ChessPosition): ChessPiece = pos.toCoordinate().let {
        board[it.first][it.second]
    }

    /**
     * If there is an available piece at [pos], return it, otherwise throw an [PieceNotFoundException].
     *
     * @exception PieceNotFoundException
     */
    fun ensureNotEmpty(pos: ChessPosition): ChessPiece =
        pieceAt(pos).takeUnless { it == EmptyPiece } ?: throw PieceNotFoundException(pos)


    private object EmptyPiece : ChessPiece {

        override val chessboard: Chessboard
            get() = throw UnsupportedOperationException()

        override val isBlack: Boolean
            get() = throw UnsupportedOperationException()

        override val position: ChessPosition
            get() = throw UnsupportedOperationException()

        override fun moveTo(target: ChessPosition): ChessPiece? {
            throw UnsupportedOperationException()
        }

        override fun retractTo(previous: ChessPosition, eaten: ChessPiece?) {
            throw UnsupportedOperationException()
        }
    }

    internal abstract inner class AbstractPiece(
        override val isBlack: Boolean,
        private var pos: ChessPosition
    ) : ChessPiece {

        override val chessboard: Chessboard
            get() = this@Chessboard

        override val position: ChessPosition
            get() = pos

        override fun moveTo(target: ChessPosition): ChessPiece? {
            val next = target.toCoordinate()
            val piece = board[next.first][next.second].takeUnless { it == EmptyPiece }
            if (piece != null && piece.isBlack == isBlack)
                throw InvalidMoveException(ChessMove(position, target))
            board[next.first][next.second] = this
            val current = pos.toCoordinate()
            board[current.first][current.second] = EmptyPiece
            pos = target
            return piece
        }

        override fun retractTo(previous: ChessPosition, eaten: ChessPiece?) {
            val current = pos.toCoordinate()
            board[current.first][current.second] = eaten ?: EmptyPiece
            pos = previous
            val pre = previous.toCoordinate()
            board[pre.first][pre.second] = this
        }
    }

    internal inner class KingPiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {

        private fun validX(pos: ChessPosition) = pos.x in 'd'..'f'

        private fun validY(pos: ChessPosition) = if (isBlack) pos.y in '7'..'9' else pos.y in '0'..'2'

        init {
            if (!(validX(pos) && validY(pos)))
                throw IllegalArgumentException("Invalid position $pos for King chess piece")
        }

        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target)
                throw InvalidMoveException(ChessMove(position, target))
            if (!(validX(target) && validY(target)))
                throw InvalidMoveException(ChessMove(position, target))
            val diff = when {
                position.x == target.x -> (target.y - position.y).absoluteValue
                position.y == target.y -> (target.x - position.x).absoluteValue
                else -> 0
            }
            if (diff != 1)
                throw InvalidMoveException(ChessMove(position, target))
            return super.moveTo(target)
        }
    }

    private inner class AdviserPiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {

        private fun valid(pos: ChessPosition) =
            pos in if (isBlack) arrayOf(
                ChessPosition('d', '9'),
                ChessPosition('d', '7'),
                ChessPosition('f', '9'),
                ChessPosition('f', '7'),
                ChessPosition('e', '8')
            ) else arrayOf(
                ChessPosition('d', '0'),
                ChessPosition('d', '2'),
                ChessPosition('f', '0'),
                ChessPosition('f', '2'),
                ChessPosition('e', '1')
            )

        init {
            if (!valid(pos))
                throw IllegalArgumentException("Invalid position $pos for Adviser chess piece")
        }

        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target || !valid(target))
                throw InvalidMoveException(ChessMove(position, target))
            val ok = when (position.x) {
                'e' -> target.x in arrayOf('d', 'f')
                else -> target.x == 'e'
            }
            if (!ok)
                throw InvalidMoveException(ChessMove(position, target))
            return super.moveTo(target)
        }
    }

    private inner class ElephantPiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {

        private fun valid(pos: ChessPosition) =
            pos in if (isBlack) arrayOf(
                ChessPosition('c', '9'),
                ChessPosition('g', '9'),
                ChessPosition('e', '7'),
                ChessPosition('a', '7'),
                ChessPosition('i', '7'),
                ChessPosition('c', '5'),
                ChessPosition('g', '5')
            ) else arrayOf(
                ChessPosition('c', '0'),
                ChessPosition('g', '0'),
                ChessPosition('e', '2'),
                ChessPosition('a', '2'),
                ChessPosition('i', '2'),
                ChessPosition('c', '4'),
                ChessPosition('g', '4')
            )

        init {
            if (!valid(pos))
                throw IllegalArgumentException("Invalid position $pos for Elephant chess piece")
        }

        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target || !valid(target))
                throw InvalidMoveException(ChessMove(position, target))
            val ok = when (position.x) {
                'c' -> target.x in arrayOf('a', 'e')
                'g' -> target.x in arrayOf('i', 'e')
                'e' -> target.x in arrayOf('c', 'g')
                'a' -> target.x == 'c'
                'i' -> target.x == 'g'
                else -> throw Error()
            }
            if (!ok)
                throw InvalidMoveException(ChessMove(position, target))
            val middle = ChessPosition(
                ((position.x.toInt() + target.x.toInt()) / 2).toChar(),
                ((position.y.toInt() + target.y.toInt()) / 2).toChar()
            )
            if (isNotEmpty(middle))
                throw InvalidMoveException(ChessMove(position, target))
            return super.moveTo(target)
        }
    }

    private inner class HorsePiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {

        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target)
                throw InvalidMoveException(ChessMove(position, target))
            val ok = when ((target.x - position.x).absoluteValue) {
                1 -> {
                    (target.y - position.y).absoluteValue == 2
                            && isEmpty(
                        ChessPosition(
                            position.x,
                            ((position.y.toInt() + target.y.toInt()) / 2).toChar()
                        )
                    )
                }
                2 -> {
                    (target.y - position.y).absoluteValue == 1
                            && isEmpty(
                        ChessPosition(
                            ((position.x.toInt() + target.x.toInt()) / 2).toChar(),
                            position.y
                        )
                    )
                }
                else -> false
            }
            if (!ok)
                throw InvalidMoveException(ChessMove(position, target))
            return super.moveTo(target)
        }
    }

    private inner class RookPiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {

        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target)
                throw InvalidMoveException(ChessMove(position, target))
            when {
                position.x == target.x -> {
                    val range =
                        if (position.y < target.y) (position.y + 1) until target.y
                        else (target.y + 1) until position.y
                    if (range.any { isNotEmpty(ChessPosition(position.x, it)) })
                        throw InvalidMoveException(ChessMove(position, target))
                }
                position.y == target.y -> {
                    val range =
                        if (position.x < target.x) (position.x + 1) until target.x
                        else (target.x + 1) until position.x
                    if (range.any { isNotEmpty(ChessPosition(it, position.y)) })
                        throw InvalidMoveException(ChessMove(position, target))
                }
                else -> throw InvalidMoveException(ChessMove(position, target))
            }
            return super.moveTo(target)
        }
    }

    private inner class CannonPiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {

        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target)
                throw InvalidMoveException(ChessMove(position, target))
            when {
                position.x == target.x -> {
                    val range =
                        if (position.y < target.y) (position.y + 1) until target.y
                        else (target.y + 1) until position.y
                    val count = range.map { isNotEmpty(ChessPosition(position.x, it)) }.count { it }
                    if (!(count == 0 || count == 1 && isNotEmpty(target)))
                        throw InvalidMoveException(ChessMove(position, target))
                }
                position.y == target.y -> {
                    val range =
                        if (position.x < target.x) (position.x + 1) until target.x
                        else (target.x + 1) until position.x
                    val count = range.map { isNotEmpty(ChessPosition(it, position.y)) }.count { it }
                    if (!(count == 0 || count == 1 && isNotEmpty(target)))
                        throw InvalidMoveException(ChessMove(position, target))
                }
                else -> throw InvalidMoveException(ChessMove(position, target))
            }
            return super.moveTo(target)
        }
    }

    private inner class PawnPiece(isBlack: Boolean, pos: ChessPosition) : AbstractPiece(isBlack, pos) {
        override fun moveTo(target: ChessPosition): ChessPiece? {
            if (position == target)
                throw InvalidMoveException(ChessMove(position, target))
            when {
                position.x == target.x -> {
                    val ok =
                        if (isBlack) target.y - position.y == -1
                        else target.y - position.y == 1
                    if (!ok)
                        throw InvalidMoveException(ChessMove(position, target))
                }
                position.y == target.y -> {
                    val ok = (if (isBlack) position.y in '0'..'4' else position.y in '5'..'9')
                            && (target.x - position.x).absoluteValue == 1
                    if (!ok)
                        throw InvalidMoveException(ChessMove(position, target))
                }
                else -> throw InvalidMoveException(ChessMove(position, target))
            }
            return super.moveTo(target)
        }
    }
}
