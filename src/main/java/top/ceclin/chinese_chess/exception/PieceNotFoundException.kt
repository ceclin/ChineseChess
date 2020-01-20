package top.ceclin.chinese_chess.exception

import top.ceclin.chinese_chess.ChessException
import top.ceclin.chinese_chess.ChessPosition

class PieceNotFoundException(pos: ChessPosition) : ChessException("There is no chess piece at (${pos.x},${pos.y})")