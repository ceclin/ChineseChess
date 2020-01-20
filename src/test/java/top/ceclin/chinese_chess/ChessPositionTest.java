package top.ceclin.chinese_chess;

import kotlin.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChessPositionTest {
    @Test(expected = IllegalArgumentException.class)
    public void outOfRange() {
        new ChessPosition('0', 'a');
    }

    @Test
    public void toCoordinate() {
        assertEquals(new Pair<>(0, 0), new ChessPosition('a', '0').toCoordinate());
        assertEquals(new Pair<>(0, 9), new ChessPosition('a', '9').toCoordinate());
        assertEquals(new Pair<>(8, 0), new ChessPosition('i', '0').toCoordinate());
        assertEquals(new Pair<>(8, 9), new ChessPosition('i', '9').toCoordinate());
    }
}
