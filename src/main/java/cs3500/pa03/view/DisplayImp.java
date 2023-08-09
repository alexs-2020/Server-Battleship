package cs3500.pa03.view;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The DisplayImp class implements the Display interface and
 * provides methods for displaying appropriate information
 */
public class DisplayImp implements Display {
  private Scanner input;
  private Appendable appendable;
  BattleBoard[][] board;
  BattleBoard[][] enemyBoard;
  GridImp userGrid;
  GridImp enemyGrid;

  public DisplayImp(GridImp userBoard, GridImp enemyBoard) {
    this.userGrid = userBoard;
    this.enemyGrid = enemyBoard;
  }

  /**
  * Retrieves user's input for the shots they want to take.
  *
  * @param phrase   the prompt phrase to display
  * @param numShots the number of shots the user is able to take
  * @return a list of coordinates representing the users shots
  */

  @Override
  public List<Coord> getUserShots(String phrase, int numShots) {
    Scanner scanner;
    displayString(phrase);
    List<Coord> shotCoords = new ArrayList<>();
    int count = 0;
    while (count < numShots) {
      scanner = new Scanner(System.in);
      shotCoords.add(new Coord(scanner.nextInt(), scanner.nextInt()));
      count++;
    }
    return shotCoords;
  }


  @Override
  public void displayUserBoard() {
    displayString("Your Board\n");
    disBoard(this.userGrid.getGrid());
  }

  @Override
  public void displayEnemyBoard() {
    displayString("Enemy Board\n");
    disBoard(this.enemyGrid.getGrid());
  }

  /**
   * Displays the game board grid.
   *
   * @param board the grid representing the game board
   */
  public void disBoard(BattleBoard[][] board) {
    appendable = new StringBuilder();
    try {
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
          appendable.append(board[i][j] + " ");
        }
        appendable.append("\n");
      }
      System.out.println(appendable);
    } catch (IOException e) {
      System.err.println("There was an error generating the board.");
    }
  }

  /**
  * Displays a string message.
  *
  * @param phrase the message to be displayed
  */

  @Override
  public void displayString(String phrase) {
    appendable = new StringBuilder();
    try {
      appendable.append(phrase);
      System.out.println(appendable.toString());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
  * Gets the fleet from the user
  *
  * @param maxSize the maximum size allowed for the fleet
  * @return an array of fleet sizes index representing #[Carrier, Battleship, Destroyer, Submarine]
  */

  @Override
  public int[] getFleet(int maxSize) {
    displayString("Please enter your fleet in the order "
        + "[Carrier, Battleship, Destroyer, Submarine]. "
        + "Remember, your fleet may not exceed size " + maxSize + ".\n");
    int[] fleet = new int[4];
    input = new Scanner(System.in);
    fleet[0] = input.nextInt();
    fleet[1] = input.nextInt();
    fleet[2] = input.nextInt();
    fleet[3] = input.nextInt();
    return fleet;
  }

  /**
  * Gets users dimension input
  *
  * @return an array of board dimensions
  */

  @Override
  public int[] getBoardSize() {
    displayString("Please enter a valid height and width below:\n");
    int[] dimensions = new int[2];
    input = new Scanner(System.in);
    dimensions[0] = input.nextInt();
    dimensions[1] = input.nextInt();
    return dimensions;
  }

  /**
  * Returns the appendable object used for displaying messages.
  *
  * @return appendable
  */

  public Appendable getAppendable() {
    return appendable;
  }
}
