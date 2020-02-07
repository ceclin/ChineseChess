package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameInfoTest {

    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        game = ChessGame.initial();
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
        game.movePiece(ChessMove.parse("a9b9"));
        assertEquals("1rbakabnr/9/1c5c1/p1p1p1p2/8p/P8/2P1P1P1P/7C1/9/RNBAKABNR w - - 0 3",
                game.getFEN());
        game.retract();
        assertEquals("rCbakabnr/9/1c5c1/p1p1p1p2/8p/P8/2P1P1P1P/7C1/9/RNBAKABNR b - - 0 2",
                game.getFEN());
        game.retract();
        assertEquals("rnbakabnr/9/1c5c1/p1p1p1p2/8p/P8/2P1P1P1P/1C5C1/9/RNBAKABNR w - - 2 2",
                game.getFEN());
    }

    @Test
    public void history() {
        assertEquals("", game.getHistory());
        game.movePiece(ChessMove.parse("a3a4"));
        assertEquals("a3a4", game.getHistory());
        game.movePiece(ChessMove.parse("i6i5"));
        assertEquals("a3a4 i6i5", game.getHistory());
        game.movePiece(ChessMove.parse("b2b9"));
        assertEquals("a3a4 i6i5 b2b9-n", game.getHistory());
        game.movePiece(ChessMove.parse("a9b9"));
        assertEquals("a3a4 i6i5 b2b9-n a9b9-C", game.getHistory());
        game.retract();
        assertEquals("a3a4 i6i5 b2b9-n", game.getHistory());
        game.retract();
        assertEquals("a3a4 i6i5", game.getHistory());
    }

    @Test
    public void loadFEN() {
        ChessGame loaded = ChessGame.fromFEN("rnb1kabnr/4a4/1c5c1/p1p1C1p2/8p/9/P1P1P1P1P/7C1/9/RNBAKABNR w - - 1 3");
        assertEquals(GameState.IN_PROGRESS, loaded.getState());
        loaded.movePiece(ChessMove.parse("e6e9"));
        assertEquals(GameState.FINISHED, loaded.getState());
        assertEquals(Player.RED, loaded.getWinner());
        assertEquals(Player.BLACK, loaded.getLoser());
        ChessGame another = ChessGame.fromFEN(loaded.getFEN(), loaded.getHistory());
        assertEquals(GameState.FINISHED, another.getState());
    }
}
