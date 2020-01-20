package top.ceclin.chinese_chess.exception

import top.ceclin.chinese_chess.ChessException
import top.ceclin.chinese_chess.ChessMove

class InvalidMoveException(move: ChessMove) :
    ChessException("The move (${move.from.x},${move.from.y}) -> (${move.to.x},${move.to.y}) is invalid")