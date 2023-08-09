package cs3500.pa03.controllers;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.AiPlayer;
import cs3500.pa03.model.player.PlayerImp;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.GameResult;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import cs3500.pa03.view.DisplayImp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 The Game class controlling the game
 */

public class GameController {
  private final Random random = new Random();
  private DisplayImp view;
  private PlayerImp user;
  protected GridImp userBoard;
  protected List<Ship> userBoats;
  private AiPlayer ai;
  protected GridImp aiBoard;
  protected List<Ship> aiBoats;
  private GameResult result;
  protected Map<ShipType, Integer> aiBoat;
  protected int[] boardDimensions;
  private int smallestDim;
  protected Map<ShipType, Integer> uboats;
  private String name;

  /**
   * Constructor for the game controller
   *
   * @param player the user
   * @param ai the ai
   * @param display the display
   */
  public GameController(PlayerImp player, AiPlayer ai, DisplayImp display) {
    this.ai = ai;
    this.user = player;
    this.view = display;
  }

  /**
   Sets the board size based on user input.
   */
  public void setBoardSize() {
    boardDimensions = view.getBoardSize();
    while (boardDimensions[0] < 6 || boardDimensions[0] > 15 || boardDimensions[1] < 6
        || boardDimensions[1] > 15) {
      view.displayString("Uh Oh! You've entered invalid dimensions. Please remember that "
          + "the height and width of the game must be in the range (6, 15),"
          + " inclusive. Try again! \n");
      boardDimensions = view.getBoardSize();
    }
    this.user.initializeBoards(boardDimensions[0], boardDimensions[1]);

    smallestDim = Math.min(boardDimensions[0], boardDimensions[1]);
    if (smallestDim >= 8) {
      smallestDim = 8;
    }
  }

  /**
   Sets the user's fleet based on user input.
   */
  public void setFleet() {
    boolean validFleetSize = true;
    int [] userFleet = new int[4];
    while (validFleetSize) {
      int sum = 0;
      userFleet = view.getFleet(smallestDim);
      for (int num : userFleet) {
        sum += num;
      }
      if (sum > smallestDim) {
        view.displayString("Uh Oh! You've entered invalid fleet sizes.");
      } else {
        validFleetSize = false;
      }
    }
    //HASHMAP OF FLEET
    uboats = new HashMap<>();
    uboats.put(new ShipType("Carrier"), userFleet[0]);
    uboats.put(new ShipType("Battleship"), userFleet[1]);
    uboats.put(new ShipType("Destroyer"), userFleet[2]);
    uboats.put(new ShipType("Submarine"), userFleet[3]);
  }
  /**
   Sets the AI's fleet randomly.
   */

  public void setAiFleet() {
    int[] aiFleet = new int[4];
    do {
      aiFleet[0] = random.nextInt(smallestDim + 1);
      aiFleet[1] = random.nextInt(smallestDim - aiFleet[0] + 1);
      aiFleet[2] = random.nextInt(smallestDim - aiFleet[0] - aiFleet[1] + 1);
      aiFleet[3] = smallestDim - aiFleet[0] - aiFleet[1] - aiFleet[2];
    } while (aiFleet[3] < 0);
    aiBoat = new HashMap<>();
    aiBoat.put(new ShipType("Carrier"), aiFleet[0]);
    aiBoat.put(new ShipType("Battleship"), aiFleet[1]);
    aiBoat.put(new ShipType("Destroyer"), aiFleet[2]);
    aiBoat.put(new ShipType("Submarine"), aiFleet[3]);
  }
  /**
   * Sets up the user player and places their ships on the user board.
   */

  public void setupUser() {

    name = this.user.name();
    //CREATE LIST OF USER'S SHIPS
    userBoats = user.setup(boardDimensions[0], boardDimensions[1], uboats);
    //PLACE SHIPS
  }

  /**
   * Sets up the AI player and generates an empty grid for the AI.
   */

  public void setupAi() {
    //GET LIST OF AI SHIPS
    this.ai.initializeBoards(boardDimensions[0], boardDimensions[1]);
    aiBoats = ai.setup(boardDimensions[0], boardDimensions[1], aiBoat);
  }

  /**
   * Runs the game by calling the necessary methods accordingly
   */

  public void run() {
    //BOARD SETUP
    setBoardSize();
    //GET USER FLEET
    setFleet();
    //CREATE USER PLAYER
    setupUser();

    //COMPUTER SETUP
    //GENERATE AI FLEET
    setAiFleet();
    //CREATE AI PLAYER
    setupAi();

    //SETUP COMPLETE - DISPLAY BOTH BOARDS
    view.displayEnemyBoard();
    view.displayUserBoard();

    result = new GameResult();

    startGame();
  }

  /**
   * Starts the game loop.
   */

  public void startGame() {
    //start game loop
    boolean runGame = true;
    while (runGame) {
      boolean validShots = true;
      //change this back!      ------------------------------------------
      List<Coord> userShots = view.getUserShots("Please Enter " +  this.user.getNumShips()
          + " Shots: ",  this.user.getNumShips());
      while (validShots) {
        for (Coord shot : userShots) {
          if (shot.getX() >= boardDimensions[0] ||  shot.getY() >= boardDimensions[1]) {
            view.displayString("One or more of your shots is invalid, Please try again");
            userShots = view.getUserShots("", this.user.getNumShips());
          }
        }
        validShots = false;
      }
      //aiShots
      List<Coord> aiShots = ai.takeShots();
      //AI REPORT DAMAGE RECEIVED AND UPDATE BOARD
      List<Coord> shotsThatHitComputer = ai.reportDamage(userShots);
      user.successfulHits(shotsThatHitComputer);
      //USER REPORT DAMAGE RECEIVED AND UPDATE BOARD
      List<Coord> shotsThatHitUser = user.reportDamage(aiShots);
      //userBoard.setHit(shotsThatHitUser);


      //DISPLAY UPDATED BOARDS
      view.displayEnemyBoard();
      view.displayUserBoard();

      //check if over
      if (aiBoats.size() == 0 && userBoats.size() == 0) {
        result.setDraw();
        user.endGame(result, "ITS A DRAW!!");
        runGame = false;
      } else if (userBoats.size() == 0) {
        result.setLoser();
        user.endGame(result, "YOU HAVE LOST ALL YOUR SHIPS!!!!");
        runGame = false;
      } else if (aiBoats.size() == 0) {
        result.setWinner();
        user.endGame(result, "YOU HAVE DESTROYED ALL THE ENEMY SHIPS");
        runGame = false;
      }
    }
  }
}