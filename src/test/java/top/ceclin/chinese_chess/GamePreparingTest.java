package top.ceclin.chinese_chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GamePreparingTest {

    private final Player foo = new Player(1234);

    private final Player bar = new Player(7890);

    private ChessGame game;

    @Before
    public void setUp() throws Exception {
        game = ChessGame.initial();
    }

    @Test
    public void gameState() {
        assertEquals(GameState.PREPARING, game.getState());
    }

    @Test
    public void addPlayer() {
        assertEquals(0, game.getPlayerCount());
        game.addPlayer(foo);
        assertEquals(1, game.getPlayerCount());
        game.addPlayer(bar);
        assertEquals(2, game.getPlayerCount());
        assertNotNull(game.getBlackPlayer());
        assertNotNull(game.getRedPlayer());
    }

    @Test(expected = IllegalStateException.class)
    public void addTooMuchPlayers() {
        game.addPlayer(foo);
        game.addPlayer(bar);
        game.addPlayer(new Player(5678));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTheSamePlayerTwice() {
        game.addPlayer(foo);
        game.addPlayer(foo.copy(foo.getId()));
    }

    @Test
    public void exchangePlayers() {
        game.addPlayer(foo);
        game.addPlayer(bar);
        Player black = game.getBlackPlayer();
        Player red = game.getRedPlayer();
        game.exchangePlayers();
        assertEquals(black, game.getRedPlayer());
        assertEquals(red, game.getBlackPlayer());
    }

    @Test
    public void startGame() {
        game.addPlayer(foo);
        game.addPlayer(bar);
        game.start();
        assertEquals(GameState.IN_PROGRESS, game.getState());
    }

    @Test(expected = IllegalStateException.class)
    public void noEnoughPlayersToStart() {
        game.start();
    }
}
