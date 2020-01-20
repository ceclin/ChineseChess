package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameInfoTest {

    private final Player foo = new Player(1234);

    private final Player bar = new Player(7890);

    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        game = new ChessGame();
        game.addPlayer(foo);
        game.addPlayer(bar);
        game.start();
    }

    @Test
    public void toFEN() {
        assertEquals("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w - - 0 1",
                game.getFEN());
        game.movePiece(ChessMove.parse("a3a4"));
        assertEquals("rnbakabnr/9/1c5c1/p1p1p1p1p/9/P8/2P1P1P1P/1C5C1/9/RNBAKABNR b - - 1 1",
                game.getFEN());
        game.movePiece(ChessMove.parse("i6i5"));
        assertEquals("rnbakabnr/9/1c5c1/p1p1p1p2/8p/P8/2P1P1P1P/1C5C1/9/RNBAKABNR w - - 2 2",
                game.getFEN());
        game.movePiece(ChessMove.parse("b2b9"));
        assertEquals("rCbakabnr/9/1c5c1/p1p1p1p2/8p/P8/2P1P1P1P/7C1/9/RNBAKABNR b - - 0 2",
                game.getFEN());
        game.retract();
        assertEquals("rnbakabnr/9/1c5c1/p1p1p1p2/8p/P8/2P1P1P1P/1C5C1/9/RNBAKABNR w - - 2 2",
                game.getFEN());
    }
}
