package cs3500.pa03.model.board;

import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.Ship;
import java.util.List;

/**
 * Interface for player grid
 */
public interface GridInterface {
  /**
   * Getter for grid
   *
   * @return 2d battleboard representing a players board
   */

  public BattleBoard[][] getGrid();
  /**
   * Sets up battleboard by placing corrisponding marker for each ship
   *
   * @param boats The list of ships to be placed on the grid.
   */


  public void userGridSetup(List<Ship> boats);

  /**
   * Updates the game grid to mark the specified coordinates as a hit.
   *
   * @param shotsThatHit The list of coordinates representing the shots that hit.
   */

  public void setHit(List<Coord> shotsThatHit);

}
