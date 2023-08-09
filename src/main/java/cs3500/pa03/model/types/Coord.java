package cs3500.pa03.model.types;

/**
 * The Coord class represents a coordinate in a two-dimensional Board
 */
public class Coord {
  private int coordX;
  private int coordY;

  /**
   * Constructor with X and Y coordinates
   *
   * @param xcoord the x coordinate
   * @param ycoord the y coordinate
   */

  public Coord(int xcoord, int ycoord) {
    this.coordX = xcoord;
    this.coordY = ycoord;
  }

  /**
   * Returns a string representation of the Coord object.
   *
   * @return a string representation of the x and y coordinates
   */

  public String toString() {
    return "x: " + this.coordX + " y: " + this.coordY + "\n";
  }

  /**
   * Getter for x Coord
   *
   * @return the x coordinate
   */

  public int getX() {
    return this.coordX;
  }

  /**
   * Getter for Y Coord
   *
   * @return the y coordinate
   */

  public int getY() {
    return this.coordY;
  }

  /**
   * Setting the values as -1 representing a Coord that has been hit
   */

  public void setNull() {
    this.coordX = -1;
    this.coordY = -1;
  }
}

