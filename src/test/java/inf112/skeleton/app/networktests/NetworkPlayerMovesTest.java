package inf112.skeleton.app.networktests;

import inf112.skeleton.app.cards.Card;
import inf112.skeleton.app.net.PlayerMoves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NetworkPlayerMovesTest {

    private PlayerMoves playerMoves;
    private ArrayList<Card> cards;

    @BeforeEach
    public void setUp(){
        playerMoves = new PlayerMoves();
        cards = new ArrayList<>();
    }

    @Test
    public void moveSetGetTest(){
        for(int i = 0; i<3; i++){
            cards.add(new Card());
        }
        playerMoves.setMoves(cards);
        assertEquals(cards, playerMoves.getMoves(), "Lists of cards should be equal, but was not");
    }
}
