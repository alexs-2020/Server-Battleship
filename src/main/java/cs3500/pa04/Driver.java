package cs3500.pa04;


// TODO: document


import cs3500.pa03.controllers.GameController;
import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.AiPlayer;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.model.player.PlayerImp;
import cs3500.pa03.view.DisplayImp;
import cs3500.pa04.proxy.ProxyController;
import java.io.IOException;
import java.net.Socket;

/**
 * The main class to run the game.
 */
public class Driver {

  private static void runClient(String host, int port) throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);
    GridImp aiBoard = new GridImp();
    GridImp enemyBoard = new GridImp();
    Player ai = new AiPlayer(aiBoard);
    Player enemy = new PlayerImp(enemyBoard);
    DisplayImp view = new DisplayImp(aiBoard, enemyBoard);
    ProxyController proxyDealer = new ProxyController(server, ai, enemy, view);
    proxyDealer.run();
  }

  /**
   * Main method to run the game.
   *
   * @param args The arguments.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      GridImp userBoard = new GridImp();
      GridImp aiBoard = new GridImp();
      PlayerImp user = new PlayerImp(userBoard);
      AiPlayer ai = new AiPlayer(aiBoard);
      DisplayImp view = new DisplayImp(userBoard, aiBoard);
      cs3500.pa03.controllers.GameController game = new GameController(user, ai, view);
      game.run();
    } else {
      String host = "0.0.0.0";
      int port = 35001;
      try {
        Driver.runClient(host, port);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }


  }

}
