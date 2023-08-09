package cs3500.pa03.model.player;

import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.GameResult;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import java.util.List;
import java.util.Map;


/**
 * Represents a single player in a game of BattleSalvo.
 */
public interface Player {

  BattleBoard[][] getGrid();


  void initializeBoards(int x, int y);



  /**
  * Get the player's name.
  *
  * @return the player's name
  */
  String name();

  /**
  * Given the specifications for a BattleSalvo board, return a list of ships with their locations
  * on the board.
  *
  * @param height the height of the board, range: [6, 15] inclusive
  * @param width the width of the board, range: [6, 15] inclusive
  * @param specifications a map of ship type to the number of occurrences each ship should
  *                       appear on the board
  * @return the placements of each ship on the board
  */

  List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications);

  //temporary
  //  void placeShips(List<Ship> ships);

  //temporary
  void placeShips(List<Ship> ships);

  /**
  * Returns this player's shots on the opponent's board. The number of shots returned should
    * equal the number of ships on this player's board that have not sunk.
  *
  * @return the locations of shots on the opponent's board
  */
  List<Coord> takeShots();

  int getNumShips();

  /**
   * Given the list of shots the opponent has fired on this player's board,
   * report which shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots
   *         that hit a ship on this board
   */

  List<Coord> reportDamage(List<Coord> opponentShotsOnBoard);
  /**
  * Reports to this player what shots in their previous volley returned from takeShots()
    * successfully hit an opponent's ship.
  *
  * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
  */

  void successfulHits(List<Coord> shotsThatHitOpponentShips);

  /**
  * Notifies the player that the game is over.
  * Win, lose, and draw should all be supported.
  *
  * @param result if the player has won, lost, or forced a draw
  * @param reason the reason for the game ending
  */
  void endGame(GameResult result, String reason);

  void setFleetFromServer(Map<ShipType, Integer> fleetFromServer);
}
