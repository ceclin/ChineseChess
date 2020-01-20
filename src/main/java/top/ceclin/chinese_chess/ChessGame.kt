package top.ceclin.chinese_chess

import top.ceclin.chinese_chess.exception.InvalidMoveException

class ChessGame {

    var state: GameState = GameState.PREPARING
        private set

    private val players = arrayOfNulls<Player>(2)

    val playerCount: Int
        get() = players.count { it != null }

    val blackPlayer: Player?
        get() = players[0]

    val redPlayer: Player?
        get() = players[1]

    fun addPlayer(player: Player) {
        check(state == GameState.PREPARING)
        require(players.all { it == null || it.id != player.id }) { "Cannot add the same player twice" }
        val index = players.indexOfFirst { it == null }
        if (index == -1) {
            throw IllegalStateException("Chinese-Chess cannot have more than 2 players")
        }
        players[index] = player
    }

    fun exchangePlayers() {
        check(state == GameState.PREPARING)
        players[0] = players[1].also {
            players[1] = players[0]
        }
    }

    fun start() {
        check(state == GameState.PREPARING)
        if (playerCount != 2) {
            throw  IllegalStateException("Chinese-Chess should have 2 players to play")
        }
        state = GameState.IN_PROGRESS
    }

    private val board: Chessboard = Chessboard.default()

    private val record: ChessRecord = ChessRecord()

    /**
     * Get current count of rounds that are already completed.
     */
    val roundCount: Int
        get() = record.size / 2

    var peacefulStepCount: Int = 0
        private set

    val nextPlayer: Player
        get() {
            check(state == GameState.IN_PROGRESS)
            return when (record.size % 2) {
                0 -> redPlayer
                else -> blackPlayer
            } as Player
        }

    fun movePiece(move: ChessMove) {
        check(state == GameState.IN_PROGRESS)
        val piece = board.ensureNotEmpty(move.from)
        if (piece.isBlack == (record.size % 2 == 0))
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
                loser = blackPlayer
                winner = redPlayer
            } else {
                loser = redPlayer
                winner = blackPlayer
            }
            state = GameState.FINISHED
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
            if (peacefulStepCount == 0) {
                peacefulStepCount = record.size
            }
        }
    }

    var winner: Player? = null
        private set

    var loser: Player? = null
        private set

    fun resign(playerId: Long) {
        check(state == GameState.IN_PROGRESS)
        val p = players.partition { (it as Player).id == playerId }
        loser = p.first.single()
        winner = p.second.single()
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
            append(if (nextPlayer == blackPlayer) 'b' else 'w')
            append(" - - ")
            append(peacefulStepCount)
            append(' ')
            append(roundCount + 1)
        }
}