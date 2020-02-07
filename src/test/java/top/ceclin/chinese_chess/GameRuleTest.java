package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;
import top.ceclin.chinese_chess.exception.InvalidMoveException;

import static org.junit.Assert.*;

// TODO: Write tests for all the Chinese Chess pieces.
public class GameRuleTest {

    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        game = ChessGame.initial();
    }

    @Test
    public void gameOver() {
        game.movePiece(ChessMove.parse("b2e2"));
        game.movePiece(ChessMove.parse("i6i5"));
        game.movePiece(ChessMove.parse("e2e6"));
        game.movePiece(ChessMove.parse("d9e8"));
        game.movePiece(ChessMove.parse("e6e9"));
        assertEquals(GameState.FINISHED, game.getState());
        assertEquals(Player.RED, game.getWinner());
        assertEquals(Player.BLACK, game.getLoser());
        assertFalse(game.isDraw());
    }

    @Test
    public void tryMove() {
        assertFalse(game.tryMove(ChessMove.parse("a0a3")));
        assertTrue(game.tryMove(ChessMove.parse("b2c2")));
        assertTrue(game.tryMove(ChessMove.parse("i6i5")));
        assertFalse(game.tryMove(ChessMove.parse("c2c9")));
    }

    @Test(expected = InvalidMoveException.class)
    public void cannotEatPieceOfYourOwn() {
        game.movePiece(ChessMove.parse("a0a3"));
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidRookMove() {
        game.movePiece(ChessMove.parse("a0a2"));
        game.movePiece(ChessMove.parse("i6i5"));
        game.movePiece(ChessMove.parse("a2b2"));
        game.movePiece(ChessMove.parse("i5i4"));
        game.movePiece(ChessMove.parse("b2b9"));
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidCannonMove() {
        game.movePiece(ChessMove.parse("b2b8"));
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidCannonMove2() {
        game.movePiece(ChessMove.parse("b2c2"));
        game.movePiece(ChessMove.parse("i6i5"));
        game.movePiece(ChessMove.parse("c2c9"));
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidPawnMove() {
        game.movePiece(ChessMove.parse("a3b3"));
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidPawnMove2() {
        game.movePiece(ChessMove.parse("a3a5"));
    }

    @Test(expected = InvalidMoveException.class)
    public void invalidPawnMove3() {
        game.movePiece(ChessMove.parse("a3a4"));
        game.movePiece(ChessMove.parse("i6i5"));
        game.movePiece(ChessMove.parse("a4a3"));
    }
}
