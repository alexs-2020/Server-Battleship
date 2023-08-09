package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.PlayerImp;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.GameResult;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the PlayerImp class.
 */
public class PlayerTest {

  PlayerMock testPlayer;

  /**
   * Test case to verify the setup method of the PlayerImp class.
   * Creates a PlayerImp object, sets up boats with specified dimensions,
   * and checks for valid boat coordinates and overlaps.
   */
  @BeforeEach

  public void setup() {
    GridImp tesGrid = new GridImp();
    tesGrid.initializeGrid(6, 6);
    testPlayer = new PlayerMock(tesGrid);
  }

  @Test
  public void testSetup() {
    Map<ShipType, Integer> boats = new HashMap<>();
    boats.put(new ShipType("Carrier"), 1);
    boats.put(new ShipType("Battleship"), 1);
    boats.put(new ShipType("Destroyer"), 1);
    boats.put(new ShipType("Submarine"), 1);
    //test overlap
    List<Coord> testOverlap = new ArrayList<>();
    List<Ship> shipsToTest = testPlayer.setup(6, 6, boats);
    assertEquals(4, shipsToTest.size());
    for (Ship inList : shipsToTest) {
      for (Coord test : inList.getCoords()) {
        //make sure coords of boats are all valid according to board size
        assertTrue(test.getX() < 6 && test.getX() >= 0);
        assertTrue(test.getY() < 6 && test.getY() >= 0);
        testOverlap.add(test);
      }
    }
    //confirm no overlaps
    for (int i = 0; i < testOverlap.size(); i++) {
      for (int j = i + 1; j < testOverlap.size(); j++) {
        assertNotEquals(testOverlap.get(i).toString(), testOverlap.get(j).toString(), "no overlap");
      }
    }
  }
  /**
   * Test case to verify the damage reporting method of the PlayerImp class.
   */

  @Test
  public void testDamage() {

    Map<ShipType, Integer> boats = new HashMap<>();
    boats.put(new ShipType("Carrier"), 1);
    boats.put(new ShipType("Battleship"), 1);
    boats.put(new ShipType("Destroyer"), 1);
    boats.put(new ShipType("Submarine"), 1);
    //test overlap
    List<Ship> placedShips = testPlayer.setup(6, 6, boats);
    placedShips.get(0).getCoords().set(0, new Coord(0, 0)); //manually set coord of first ship

    List<Coord> testShots = new ArrayList<>();
    testShots.add(new Coord(0, 0));
    List<Coord> shotThatHit = new ArrayList<>();
    shotThatHit.add(new Coord(0, 0));
    assertEquals(shotThatHit.get(0).toString(),
        testPlayer.reportDamage(testShots).get(0).toString());
  }
  /**
   * Test case to verify the successful hits method using PlayerMock
   */

  @Test
  public void testShots() {
    List<Coord> testShots = new ArrayList<>();
    testShots.add(new Coord(0, 0));
    testPlayer.successfulHits(testShots);

  }
  /**
   * Test case to verify the name method
   */

  @Test
  public void testName() {
    String testInput =  "joe";
    ByteArrayInputStream testIn;
    testIn = new ByteArrayInputStream(testInput.getBytes());
    System.setIn(testIn);
    assertEquals("joe", testPlayer.name());

    testInput =  "";
    testIn = new ByteArrayInputStream(testInput.getBytes());
    System.setIn(testIn);
    assertThrows(IllegalArgumentException.class, testPlayer::name);
  }

  /**
   * Test case to verify the endGame trigger
   */

  @Test
  public void testEndGame() {
    GameResult result = new GameResult();
    result.setWinner();
    testPlayer.endGame(result, "winner");
    //    assertEquals("winner", testPlayer.getView().toString());
  }
}
