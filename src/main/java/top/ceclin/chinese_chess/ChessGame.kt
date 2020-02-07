package top.ceclin.chinese_chess

import top.ceclin.chinese_chess.exception.InvalidMoveException

class ChessGame private constructor() {

    companion object {

        @JvmStatic
        fun initial(): ChessGame {
            return ChessGame().apply {
                board = Chessboard.initial()
            }
        }

        @JvmStatic
        @JvmOverloads
        fun fromFEN(fen: String, history: String? = null): ChessGame {
            return ChessGame().apply {
                board = Chessboard.fromFEN(fen)
                val steps = history?.takeIf { it.isNotEmpty() }?.split(Regex(" +")) ?: emptyList()
                for (step in steps) {
                    record.addLast(
                        ChessRecord.Item(
                            ChessMove.parse(step.slice(0..3)),
                            if (step.length == 6) board.makePieceFromCode(
                                step[5],
                                ChessPosition(step[2], step[3])
                            ) else null
                        )
                    )
                }
                val str = fen.split(Regex(" +"))
                peacefulStepCount = str[4].toInt()
                if (peacefulStepCount > steps.size) {
                    basePeacefulStepCount = peacefulStepCount - steps.size
                }
                val next = if (str[1] == "b") 1 else 0
                val round = str[5].toInt()
                val recordSize = 2 * (round - 1) + next
                if (recordSize > steps.size) {
                    baseRecordSize = recordSize - steps.size
                }
                val w = 'K' in str[0]
                val b = 'k' in str[0]
                require(w || b)
                if (!w || !b)
                    state = GameState.FINISHED
            }
        }
    }

    var state: GameState = GameState.IN_PROGRESS
        private set

    /**
     * It should be init in factory methods.
     */
    private lateinit var board: Chessboard

    /**
     * The size of unknown part in history record.
     */
    private var baseRecordSize: Int = 0

    /**
     * The size of unknown peaceful moves in history.
     */
    private var basePeacefulStepCount: Int = 0

    private val record: ChessRecord = ChessRecord()

    /**
     * Get current count of rounds that are already completed.
     */
    val roundCount: Int
        get() = (baseRecordSize + record.size) / 2

    var peacefulStepCount: Int = 0
        private set

    val nextPlayer: Player
        get() = when ((baseRecordSize + record.size) % 2) {
            0 -> Player.RED
            else -> Player.BLACK
        }

    fun movePiece(move: ChessMove) {
        check(state == GameState.IN_PROGRESS)
        val piece = board.ensureNotEmpty(move.from)
        if (piece.isBlack == ((baseRecordSize + record.size) % 2 == 0))
            throw InvalidMoveException(move)
        val eaten = piece.moveTo(move.to)
        record.addLast(ChessRecord.Item(move, eaten))
        if (eaten == null) {
            peacefulStepCount++
        } else {
            peacefulStepCount = 0
        }
        if (eaten is Chessboard.KingPiece) {
            if (eaten.isBlack) {
                loser = Player.BLACK
                winner = Player.RED
            } else {
                loser = Player.RED
                winner = Player.BLACK
            }
            state = GameState.FINISHED
        }
    }

    fun tryMove(move: ChessMove): Boolean {
        return try {
            movePiece(move)
            true
        } catch (e: ChessException) {
            false
        }
    }

    fun retract() {
        check(state == GameState.IN_PROGRESS)
        if (record.isEmpty())
            return
        val last = record.removeLast()
        val piece = board.pieceAt(last.move.to)
        piece.retractTo(last.move.from, last.eaten)
        if (peacefulStepCount > 0) {
            peacefulStepCount--
        } else {
            val iterator = record.descendingIterator().withIndex()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.value.eaten != null) {
                    peacefulStepCount = next.index
                    break
                }
            }
            // There is no record with non-null eaten piece.
            if (peacefulStepCount == 0 && record.lastOrNull()?.eaten == null) {
                peacefulStepCount = basePeacefulStepCount + record.size
            }
        }
    }

    var winner: Player? = null
        private set
        get() {
            check(state == GameState.FINISHED)
            return if (field == null && !isDraw)
                when ((baseRecordSize + record.size) % 2) {
                    1 -> Player.RED
                    else -> Player.BLACK
                }
            else field
        }

    var loser: Player? = null
        private set
        get() {
            check(state == GameState.FINISHED)
            return if (field == null && !isDraw) nextPlayer else field
        }

    fun resign(player: Player) {
        check(state == GameState.IN_PROGRESS)
        when (player) {
            Player.RED -> {
                loser = Player.RED
                winner = Player.BLACK
            }
            Player.BLACK -> {
                loser = Player.BLACK
                winner = Player.RED
            }
        }
        state = GameState.FINISHED
    }

    var isDraw: Boolean = false
        private set

    fun draw() {
        check(state == GameState.IN_PROGRESS)
        isDraw = true
        state = GameState.FINISHED
    }

    val FEN: String
        get() = buildString {
            append(board.toFEN())
            append(' ')
            append(if (nextPlayer == Player.BLACK) 'b' else 'w')
            append(" - - ")
            append(peacefulStepCount)
            append(' ')
            append(roundCount + 1)
        }

    val history: String
        get() = buildString {
            for ((index, step) in record.withIndex()) {
                if (index > 0)
                    append(' ')
                step.move.let {
                    append(it.from.x)
                    append(it.from.y)
                    append(it.to.x)
                    append(it.to.y)
                }
                step.eaten?.let {
                    append('-')
                    append(it.code)
                }
            }
        }
}