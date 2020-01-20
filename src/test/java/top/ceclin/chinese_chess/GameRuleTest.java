package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;
import top.ceclin.chinese_chess.exception.InvalidMoveException;

import static org.junit.Assert.*;

// TODO: Write tests for all the Chinese Chess pieces.
public class GameRuleTest {

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
    public void gameOver() {
        game.movePiece(ChessMove.parse("b2e2"));
        game.movePiece(ChessMove.parse("i6i5"));
        game.movePiece(ChessMove.parse("e2e6"));
        game.movePiece(ChessMove.parse("d9e8"));
        game.movePiece(ChessMove.parse("e6e9"));
        assertEquals(GameState.FINISHED, game.getState());
        assertEquals(game.getRedPlayer(), game.getWinner());
        assertEquals(game.getBlackPlayer(), game.getLoser());
        assertFalse(game.isDraw());
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
