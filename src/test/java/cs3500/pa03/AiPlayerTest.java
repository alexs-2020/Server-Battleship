package cs3500.pa03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.AiPlayer;
import cs3500.pa03.model.types.Coord;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
/**
 * Test for AiPlayer
 */

public class AiPlayerTest {
  /**
   * The testShots method tests the shot generation algorithm of the AiPlayerMock class.
   * It verifies that the generated shots fall within the valid range of the game grid and
   */

  @Test
  public void testShots() {
    GridImp u = new GridImp();
    AiPlayerMock computer = new AiPlayerMock(u);
    computer.setShipSize();
    computer.initializeBoards(6, 6);

    List<Coord> fakePreviousShots = new ArrayList<>();
    fakePreviousShots.add(new Coord(1, 1));
    fakePreviousShots.add(new Coord(2, 2));
    fakePreviousShots.add(new Coord(3, 3));
    fakePreviousShots.add(new Coord(4, 4));
    computer.setPreviousSuccessfulShots(fakePreviousShots);


    List<Coord> duplicates = new ArrayList<>();
    duplicates.add(new Coord(1, 1));
    assertFalse(AiPlayer.hasDoub(duplicates));
    duplicates.add(new Coord(1, 1));
    assertTrue(AiPlayer.hasDoub(duplicates));

    computer.setPreviousSuccessfulShots(duplicates);
    assertTrue(computer.prevContain(duplicates));

    assertTrue(computer.check(new Coord(-1, -1)));
    assertTrue(computer.check(new Coord(1, 1)));
    assertFalse(computer.check(new Coord(2, 1)));

    assertTrue(computer.shotContain(new Coord(1, 1), duplicates));
    assertFalse(computer.shotContain(new Coord(3, 1), duplicates));
    assertEquals(Coord.class, computer.generateGuess().getClass());

    List<Coord> generatedShots = computer.takeShots();
    for (Coord computerShot : generatedShots) {
      //valid on board size
      assertTrue(computerShot.getX() < 6 && computerShot.getX() >= 0);
      assertTrue(computerShot.getY() < 6 && computerShot.getY() >= 0);
    }
  }

  @Test
  public void testDamage() {
    GridImp u = new GridImp();
    AiPlayerMock computer = new AiPlayerMock(u);
    computer.initializeBoards(6, 6);
    computer.setShipSize();
    List<Coord> fakeShots = new ArrayList<>();
    fakeShots.add(new Coord(5, 5));
    List<Coord> succShot = computer.reportDamage(fakeShots);
    assertEquals("x: 5 y: 5\n", succShot.get(0).toString());
  }

}
