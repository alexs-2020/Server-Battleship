package cs3500.pa03.model.types;

/**
 * The GameResult class represents the result of a game.
 */
public class GameResult {
  private boolean isWinner = false;
  private boolean isLoser = false;
  private boolean isDraw = false;

  /**
   * Sets the game result as a winner.
   */
  public void setWinner() {
    this.isDraw = false;
    this.isLoser = false;
    this.isWinner = true;
  }

  /**
   * Sets the game result as a loser.
   */
  public void setLoser() {
    this.isLoser = true;
    this.isDraw = false;
    this.isWinner = false;
  }

  /**
   * Sets the game result as a draw.
   */
  public void setDraw() {
    this.isDraw = true;
    this.isLoser = false;
    this.isWinner = false;
  }

  /**
   * Returns whether the game result is a draw.
   *
   * @return true if the game is a draw, false otherwise
   */
  public boolean getDraw() {
    return this.isDraw;
  }

  /**
   * Returns whether the game result is a loss.
   *
   * @return true if the game is a loss, false otherwise
   */
  public boolean getLoss() {
    return this.isLoser;
  }

  /**
   * Returns whether the game result is a win.
   *
   * @return true if the game is a win, false otherwise
   */
  public boolean getWin() {
    return this.isWinner;
  }
}
