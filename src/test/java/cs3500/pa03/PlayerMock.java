package cs3500.pa03;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.PlayerImp;

/**
 * Mock for PlayerImp
 * Provides an additional method to retrieve the views appendable object for testing purposes.
 */
public class PlayerMock extends PlayerImp {
  public PlayerMock(GridImp userBoard) {
    super(userBoard);
  }

}
