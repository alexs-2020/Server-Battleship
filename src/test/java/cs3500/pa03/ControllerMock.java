package cs3500.pa03;

import cs3500.pa03.controllers.GameController;
import cs3500.pa03.model.player.AiPlayer;
import cs3500.pa03.model.player.PlayerImp;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import cs3500.pa03.view.DisplayImp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Moke for GameController
 * Provides additional methods to access and manipulate the board dimensions
 * and ship configurations for testing purposes.
 */

public class ControllerMock extends GameController {
  public ControllerMock(PlayerImp player,
                        AiPlayer ai, DisplayImp display) {
    super(player, ai, display);
  }

  /**
   * Getter for board dimensions
   *
   * @return an array representing the board dimensions [height, width]
   */

  public   int[] getBoardDims() {
    return boardDimensions;
  }
  /**
   * Getter for User fleet/boat hashmap
   *
   * @return a map containing the ship types and their quantities of the user
   */

  public Map<ShipType, Integer> getuBoats() {
    return uboats;
  }

  /**
   * Getter for User Ship list
   *
   * @return a list of Ship representing the users boat placements
   */

  public List<Ship> getUserBoats() {
    return userBoats;
  }
  /**
   * Getter for Ai fleet/boat hashmap
   *
   * @return a map containing the ship types and quantities of AI
   */

  public Map<ShipType, Integer> getAiBoats() {
    return aiBoat;
  }
  /**
   * Setter for board dimensions
   */

  public void setDims() {
    boardDimensions = new int[] {6, 6};
  }
  /**
   * Creates a boat config for both User and Ai.
   */

  public void setBoats() {
    uboats = new HashMap<>();
    uboats.put(new ShipType("Carrier"), 1);
    uboats.put(new ShipType("Battleship"), 1);
    uboats.put(new ShipType("Destroyer"), 1);
    uboats.put(new ShipType("Submarine"), 1);

    aiBoat = new HashMap<>();
    aiBoat.put(new ShipType("Carrier"), 1);
    aiBoat.put(new ShipType("Battleship"), 1);
    aiBoat.put(new ShipType("Destroyer"), 1);
    aiBoat.put(new ShipType("Submarine"), 1);
  }
}
