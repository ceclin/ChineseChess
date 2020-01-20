package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameProgressTest {

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
    public void roundCount() {
        assertEquals(0, game.getRoundCount());
        game.movePiece(ChessMove.parse("a0a2"));
        assertEquals(0, game.getRoundCount());
        game.movePiece(ChessMove.parse("i9i7"));
        assertEquals(1, game.getRoundCount());
        game.movePiece(ChessMove.parse("i0i2"));
        assertEquals(1, game.getRoundCount());
        game.retract();
        assertEquals(1, game.getRoundCount());
        game.retract();
        assertEquals(0, game.getRoundCount());
    }

    @Test
    public void nextPlayer() {
        assertEquals(game.getRedPlayer(), game.getNextPlayer());
        game.movePiece(ChessMove.parse("a0a2"));
        assertEquals(game.getBlackPlayer(), game.getNextPlayer());
        game.movePiece(ChessMove.parse("i9i7"));
        assertEquals(game.getRedPlayer(), game.getNextPlayer());
        game.movePiece(ChessMove.parse("i0i2"));
        assertEquals(game.getBlackPlayer(), game.getNextPlayer());
        game.retract();
        assertEquals(game.getRedPlayer(), game.getNextPlayer());
        game.retract();
        assertEquals(game.getBlackPlayer(), game.getNextPlayer());
    }

    @Test
    public void resign() {
        game.resign(bar.getId());
        assertEquals(GameState.FINISHED, game.getState());
        assertFalse(game.isDraw());
        assertEquals(bar, game.getLoser());
        assertEquals(foo, game.getWinner());
    }

    @Test
    public void draw() {
        game.draw();
        assertEquals(GameState.FINISHED, game.getState());
        assertTrue(game.isDraw());
        assertNull(game.getLoser());
        assertNull(game.getWinner());
    }
}
