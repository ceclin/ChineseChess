package top.ceclin.chinese_chess;

import kotlin.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChessMoveTest {
    @Test(expected = IllegalArgumentException.class)
    public void lengthCheck() {
        ChessMove.parse("h1");
    }

    @Test
    public void parseMove() {
        ChessMove move = ChessMove.parse("h2e2");
        assertEquals(new Pair<>(7, 2), move.getFrom().toCoordinate());
        assertEquals(new Pair<>(4, 2), move.getTo().toCoordinate());
    }
}
