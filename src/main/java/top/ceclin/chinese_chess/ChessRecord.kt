package top.ceclin.chinese_chess

import java.util.*

internal class ChessRecord(holder: Deque<Item> = LinkedList<Item>()) : Deque<ChessRecord.Item> by holder {
    class Item(val move: ChessMove, val eaten: ChessPiece?)
}