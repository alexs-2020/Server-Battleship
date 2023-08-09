package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.model.player.PlayerImp;
import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.GameResult;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
/**
 * Test class for the "Types"
 * Verify the functionality of the Coord, ShipType, Ship, GameResult,
 * BattleBoard, and GridImp classes.
 */

public class TypesTest {
  /**
   * Test case to verify the Coord class.
   */

  @Test
  public void testCoords() {
    Coord sample1 = new Coord(1, 2);
    assertEquals("x: 1 y: 2\n", sample1.toString());

    sample1.setNull();
    assertEquals(-1, sample1.getX());
    assertEquals(-1, sample1.getY());

  }

  @Test
  public void testShipType() {
    ShipType test = new ShipType("destroyer");
    assertEquals("destroyer", test.toString());
    assertEquals(4, test.getLength());
  }
  /**
   * Test case to verify the ShipType class.
   */

  @Test
  public void testShip() {
    ShipType shipType = new ShipType("carrier");
    List<Coord> shipCoords = new ArrayList<>();
    shipCoords.add(new Coord(0, 0));
    shipCoords.add(new Coord(1, 1));
    shipCoords.add(new Coord(2, 2));
    Ship sample = new Ship(shipType, shipCoords, true);
    assertEquals(shipCoords, sample.getCoords());
    shipCoords.remove(shipCoords.get(0));
    sample.deleteCoord(shipCoords.get(0));
    assertEquals(shipCoords, sample.getCoords());
    assertEquals(shipType.toString(), sample.name());
  }
  /**
   * Test case to verify the Ship class.
   */

  @Test
  public void testBattleBoard() {
    BattleBoard empty = BattleBoard.O;
    assertEquals(BattleBoard.valueOf("O"), empty);
  }
  /**
   * Test case to verify the BattleBoard enum.
   */

  @Test
  public void testBoardSetup() {
    List<Ship> fakeShips = new ArrayList<>();
    List<Coord> center = new ArrayList<>();
    center.add(new Coord(0, 0));
    fakeShips.add(new Ship(new ShipType("carrier"), center, true));
    GridImp grid = new GridImp();
    Player temp = new PlayerImp(grid);
    temp.initializeBoards(2, 2);
    grid.userGridSetup(fakeShips);
    BattleBoard[][] returned = {{BattleBoard.C, BattleBoard.O}, {BattleBoard.O, BattleBoard.O}};
    assertEquals(returned[0][0], grid.getGrid()[0][0]);

    grid.setHit(center);
    returned = new BattleBoard[][] {{BattleBoard.X, BattleBoard.O},
        {BattleBoard.O, BattleBoard.O}};
    assertEquals(returned[0][0], grid.getGrid()[0][0]);
  }
  /**
   * Test case to verify the GameResult class.
   * Creates a GameResult object and tests the setter and getter
   * methods for winner, loser, and draw.
   */

  @Test
  public void testGameResult() {
    GameResult test = new GameResult();
    test.setWinner();
    assertTrue(test.getWin());
    test.setLoser();
    assertTrue(test.getLoss());
    test.setDraw();
    assertTrue(test.getDraw());
  }

}
