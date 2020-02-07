package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameProgressTest {

    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        game = ChessGame.initial();
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
        assertEquals(Player.RED, game.getNextPlayer());
        game.movePiece(ChessMove.parse("a0a2"));
        assertEquals(Player.BLACK, game.getNextPlayer());
        game.movePiece(ChessMove.parse("i9i7"));
        assertEquals(Player.RED, game.getNextPlayer());
        game.movePiece(ChessMove.parse("i0i2"));
        assertEquals(Player.BLACK, game.getNextPlayer());
        game.retract();
        assertEquals(Player.RED, game.getNextPlayer());
        game.retract();
        assertEquals(Player.BLACK, game.getNextPlayer());
    }

    @Test
    public void resign() {
        game.resign(Player.BLACK);
        assertEquals(GameState.FINISHED, game.getState());
        assertFalse(game.isDraw());
        assertEquals(Player.BLACK, game.getLoser());
        assertEquals(Player.RED, game.getWinner());
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
