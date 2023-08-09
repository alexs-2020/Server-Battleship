package cs3500.pa04;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa03.AiPlayerMock;
import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.player.AiPlayer;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.model.player.PlayerImp;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.ShipType;
import cs3500.pa03.view.DisplayImp;
import cs3500.pa04.json.CoordinatesJson;
import cs3500.pa04.json.EmptyJson;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.proxy.ProxyController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test correct responses for different requests from the socket using a Mock Socket (mocket)
 */
public class ProxyControllerTest {
  private ByteArrayOutputStream testLog;
  private ProxyController dealer;
  private ObjectMapper objectMapper = new ObjectMapper();

  GridImp aiBoard;
  GridImp enemyBoard;
  Player ai;
  Player enemy;
  DisplayImp view;

  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
    aiBoard = new GridImp();
    enemyBoard = new GridImp();
    enemy = new PlayerImp(enemyBoard);
    ai = new AiPlayer(aiBoard);
    view = new DisplayImp(aiBoard, enemyBoard);
  }

  /**
   * Check that the server returns a guess when given a hint.
   */
  @Test
  public void testJoin() {
    // Prepare sample message


    JsonNode emptyJsonNode = objectMapper.createObjectNode();
    MessageJson messageJson = new MessageJson("join", emptyJsonNode);
    JsonNode sampleMessage = createSampleMessage("join", messageJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      this.dealer =  new ProxyController(socket, ai, enemy, view);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }

    // run the dealer and verify the response
    this.dealer.run();

    String expected = "{\"method-name\":\"join\",\"arguments\":{\"name\":\"alexs-2020\","
        + "\"game-type\":\"SINGLE\"}}" + System.lineSeparator();;
    assertEquals(expected, logToString());
  }

  /**
   * Check that the server returns a guess when given a hint.
   */
  @Test
  public void testSetup() {
    ObjectNode fleetRequest = objectMapper.createObjectNode();
    fleetRequest.put("width", 10);
    fleetRequest.put("height", 10);
    ObjectNode fleetData = objectMapper.createObjectNode();
    fleetData.put("CARRIER", 2);
    fleetData.put("BATTLESHIP", 4);
    fleetData.put("DESTROYER", 1);
    fleetData.put("SUBMARINE", 3);
    JsonNode y = fleetData;
    fleetRequest.set("fleet-spec", y);
    JsonNode x = fleetRequest;
    MessageJson messageJson = new MessageJson("setup", x);
    JsonNode sampleMessage = JsonUtils.serializeRecord(messageJson);

    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    // Create a Dealer
    try {
      GridImp aiBoard = new GridImp();
      GridImp enemyBoard = new GridImp();
      Player ai = new AiPlayer(aiBoard);
      Player enemy = new PlayerImp(enemyBoard);
      DisplayImp view = new DisplayImp(aiBoard, enemyBoard);
      this.dealer =  new ProxyController(socket, ai, enemy, view);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }

    // run the dealer and verify the response
    this.dealer.run();
    Map<ShipType, Integer> fleetFromServer = new HashMap<>();
    Iterator<Map.Entry<String, JsonNode>> fields = fleetRequest.get("fleet-spec").fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();
      ShipType key = new ShipType(field.getKey());
      int value = field.getValue().asInt();
      fleetFromServer.put(key, value);
    }
    List<Integer> fleetSize = fleetFromServer.values().stream().toList();
    int sum = 0;
    for (Integer fleetNum :
        fleetSize) {
      sum += fleetNum;
    }
    assertEquals(10, sum);
  }

  /**
   * test for response from damage received
   *
   */

  @Test
  public void testDamageReceived() {
    List<Coord> shots = new ArrayList<>();
    shots.add(new Coord(5, 5));
    CoordinatesJson shotsFromAi = new CoordinatesJson(shots);
    JsonNode sampleMessage = createSampleMessage("report-damage", shotsFromAi);
    System.out.println(sampleMessage);
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    AiPlayerMock   ai = new AiPlayerMock(aiBoard);
    try {
      this.dealer =  new ProxyController(socket, ai, enemy, view);
      ai.initializeBoards(6, 6);
      ai.setShipSize();
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    this.dealer.run();
    //order is not guaranteed
    assertTrue(testShots("{\"method-name\":\"report-damage\",\"arguments\""
        + ":{\"coordinates\":[{\"x\":5,\"y\":5}]}}" + System.lineSeparator(),
        "{\"method-name\":\"report-damage\",\"arguments\""
        + ":{\"coordinates\":[{\"y\":5,\"x\":5}]}}" + System.lineSeparator(), logToString()));
  }
  /**
   * Test shot response, makes sure it can be read as a volley and appropriate number of shots
   *
   */

  @Test
  public void testShots() {
    EmptyJson x = new EmptyJson();
    JsonNode sampleMessage = createSampleMessage("take-shots", x);
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    AiPlayerMock   ai = new AiPlayerMock(aiBoard);
    try {
      this.dealer =  new ProxyController(socket, ai, enemy, view);
      ai.initializeBoards(6, 6);
      ai.setShipSize();
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    // run the dealer and verify the response
    this.dealer.run();
    //make sure it can be read as a volley
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(logToString());
      MessageJson message = jsonParser.readValueAs(MessageJson.class);
      JsonParser j = new ObjectMapper().createParser(message.arguments().toString());
      JsonNode coordinates = j.readValueAs(JsonNode.class);
      List<Coord> shots = new ArrayList<>();
      for (JsonNode shotCord : coordinates.get("coordinates")) {
        System.out.println(shotCord);
        Coord coord = new Coord(shotCord.get("x").asInt(), shotCord.get("y").asInt());
        shots.add(coord);
      }
      //num shots hsould equal number of boats
      assertEquals(shots.size(), 4);
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }
  }
  /**
   * test for receving succesful shots from server
   *
   */

  @Test
  public void testSuccHits() {
    List<Coord> shotsThatHit = new ArrayList<>();
    shotsThatHit.add(new Coord(5, 5));
    CoordinatesJson shotsFromAi = new CoordinatesJson(shotsThatHit);
    JsonNode sampleMessage = createSampleMessage("successful-hits", shotsFromAi);
    System.out.println(sampleMessage);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));

    AiPlayerMock ai = new AiPlayerMock(aiBoard);
    try {
      this.dealer = new ProxyController(socket, ai, enemy, view);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    this.dealer.run();
    //correct order is not guaranteed
    assertTrue(testShots("x: 5 y: 5\n", "y: 5 x: 5\n", ai.getSuccShots().get(0).toString()));
  }
  /**
   * test for response from game ending
   *
   */

  @Test
  public void testEndGame() {
    EndGameJson engGame = new EndGameJson("LOSE", "You Lost");
    JsonNode sampleMessage = createSampleMessage("end-game", engGame);

    Mocket socket = new Mocket(this.testLog, List.of(sampleMessage.toString()));
    try {
      this.dealer = new ProxyController(socket, ai, enemy, view);
    } catch (IOException e) {
      fail(); // fail if the dealer can't be created
    }
    this.dealer.run();
    String expected = "{\"method-name\":\"end-game\",\"arguments\":{}}" + System.lineSeparator();
    assertEquals(expected, logToString());
    assertEquals("\"You Lost\"", view.getAppendable().toString());
  }
  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */

  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }
  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */

  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,
        JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
  /**
   * Helper since response is sometimes x: y: or y: x:
   *
   * @return boolean if the coords are equal in either order
   */

  private boolean testShots(String xtheny, String ythenx, String shot) {
    if (xtheny.equals(shot) || ythenx.equals(shot)) {
      return true;
    } else {
      return false;
    }
  }
}

