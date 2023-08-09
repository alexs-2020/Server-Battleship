package cs3500.pa04.proxy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import cs3500.pa03.controller.GameController;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import cs3500.pa03.view.DisplayImp;
import cs3500.pa04.json.CoordinatesJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.JoinRequest;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ShipJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The proxy controller.
 */
public class ProxyController {

  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final Player player;
  private final Player enemy;
  private DisplayImp view;
  //  private GameController localGame;
  private final ObjectMapper mapper = new ObjectMapper();

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Constructor for the proxy controller
   *
   * @param server The server.
   * @param player The player.
   * @param enemy The enemy.
   * @param display The display.
   * @throws IOException If the input or output is invalid.
   */
  public ProxyController(Socket server, Player player,
                         Player enemy, DisplayImp display) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
    this.enemy = enemy;
    this.view = display;
  }

  /**
   * Run the proxy controller.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);
      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message); //handle messege received from server
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  private void delegateMessage(MessageJson message) throws IOException {
    String name = message.messageName();
    JsonNode arguments = message.arguments();
    if ("join".equals(name)) {
      JoinRequest x = new JoinRequest("alexs-2020", "SINGLE");
      JsonNode y = JsonUtils.serializeRecord(x);
      MessageJson response = new MessageJson("join", y);
      JsonNode jsonResponse = JsonUtils.serializeRecord(response);
      out.println(jsonResponse);
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots();
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else {
      handleEndGame(arguments);
    }
  }

  /**
   * Handle the end of the game.
   *
   * @param fleet The fleet.
   */

  public void handleSetup(JsonNode fleet) {
    Map<ShipType, Integer> fleetFromServer = new HashMap<>();
    Iterator<Map.Entry<String, JsonNode>> fields = fleet.get("fleet-spec").fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();
      ShipType key = new ShipType(field.getKey());
      int value = field.getValue().asInt();
      fleetFromServer.put(key, value);
    }
    this.player.initializeBoards(fleet.get("width").asInt(),
        fleet.get("height").asInt());
    this.enemy.initializeBoards(fleet.get("width").asInt(),
        fleet.get("height").asInt());
    this.player.setFleetFromServer(fleetFromServer);
    List<Ship> playerShips = this.player.setup(fleet.get("width").asInt(),
        fleet.get("height").asInt(), fleetFromServer);

    this.player.placeShips(playerShips);

    ArrayList<ShipJson> listShip = new ArrayList<>();
    for (Ship placedShip : playerShips) {
      listShip.add(new ShipJson(placedShip.getCoords().get(0),
          placedShip.getLength(), placedShip.layout()));
    }
    FleetJson playerFleet = new FleetJson(listShip);
    JsonNode sendFleet = JsonUtils.serializeRecord(playerFleet);
    MessageJson response =
        new MessageJson("setup", sendFleet);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    out.println(jsonResponse);
  }

  /**
   * Handle the end of the game.
   */
  //ask user for shots and send to server
  public void handleTakeShots() {
    List<Coord> playerShots = this.player.takeShots();
    CoordinatesJson playerVolley = new CoordinatesJson(playerShots);
    JsonNode sendShots = JsonUtils.serializeRecord(playerVolley);
    MessageJson response =
        new MessageJson("take-shots", sendShots);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    out.println(jsonResponse);
  }

  /**
   * Handle the end of the game.
   *
   * @param coordinates The coordinates.
   */
  //update local enemy board with successful shots
  public void handleReportDamage(JsonNode coordinates) {
    List<Coord> shotsFromServer = new ArrayList<>();
    for (JsonNode coords : coordinates.get("coordinates")) {
      shotsFromServer.add(new Coord(coords.get("x").asInt(), coords.get("y").asInt()));
    }
    List<Coord> damageReceived = this.player.reportDamage(shotsFromServer);
    //start response
    CoordinatesJson reportLocalDamage = new CoordinatesJson(damageReceived);
    JsonNode sendDamage = JsonUtils.serializeRecord(reportLocalDamage);

    MessageJson response = new MessageJson("report-damage", sendDamage);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    out.println(jsonResponse);
  }

  /**
   * Handle the end of the game.
   *
   * @param coordinates The coordinates.
   */
  //send successful enemy shots and update local board

  public void handleSuccessfulHits(JsonNode coordinates) {
    List<Coord> succHits = new ArrayList<>();
    for (JsonNode shotCord : coordinates.get("coordinates")) {
      Coord coord = new Coord(shotCord.get("x").asInt(), shotCord.get("y").asInt());
      succHits.add(coord);
    }
    this.player.successfulHits(succHits);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode emptyJsonNode = objectMapper.createObjectNode();
    MessageJson response = new MessageJson("successful-hits", emptyJsonNode);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    out.println(jsonResponse);
  }

  /**
   * Handle the end of the game.
   *
   * @param end JsonNode that deals with end of the game
   */

  public void handleEndGame(JsonNode end) {
    view.displayString(end.get("result").toString());
    view.displayString(end.get("reason").toString());
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode emptyJsonNode = objectMapper.createObjectNode();
    MessageJson response = new MessageJson("end-game", emptyJsonNode);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    out.println(jsonResponse);
  }


}
