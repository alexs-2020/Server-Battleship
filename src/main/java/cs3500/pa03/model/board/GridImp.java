package cs3500.pa03.model.board;

import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.Ship;
import java.util.List;

/**
 * The GridImp class implements the GridInterface and represents the board for the game
 */

public class GridImp implements GridInterface {
  /**
   * 2d battleboard representing a players board
   */
  public BattleBoard[][] grid;

  /**
   * Initializes the game grid with the specified dimensions and sets all cells to empty state: "O"
   *
   * @param width The width of the grid.
   * @param height The height of the grid.
   */

  public void initializeGrid(int height, int width) {
    grid = new BattleBoard[width][height];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = BattleBoard.O;
      }
    }
  }
  /**
   * Getter for grid
   *
   * @return 2d battleboard representing a players board
   */

  @Override
  public BattleBoard[][] getGrid() {
    return grid;
  }

  /**
   * Sets up battleboard by placing corrisponding marker for each ship
   *
   * @param boats The list of ships to be placed on the grid.
   */

  @Override
  public void userGridSetup(List<Ship> boats) {
    for (Ship boat : boats) {
      for (Coord boatCoords : boat.getCoords()) {
        for (int i = 0; i < grid.length; i++) {
          for (int j = 0; j < grid[i].length; j++) {
            if (j == boatCoords.getX() && i == boatCoords.getY()) {
              switch (boat.name()) {
                case "destroyer" -> grid[i][j] = BattleBoard.D;
                case "submarine" -> grid[i][j] = BattleBoard.S;
                case "carrier" -> grid[i][j] = BattleBoard.C;
                case "battleship" -> grid[i][j] = BattleBoard.B;
                default -> System.err.println("There has been an error placing ships");
              }
            }
          }
        }
      }
    }
  }
  /**
   * Updates the game grid to mark the specified coordinates as a hit.
   *
   * @param shotsThatHit The list of coordinates representing the shots that hit.
   */

  @Override
  public void setHit(List<Coord> shotsThatHit) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        for (Coord shot : shotsThatHit) {
          if (j == shot.getX() && i == shot.getY()) {
            grid[i][j] = BattleBoard.X;
          }
        }
      }
    }
  }
}
