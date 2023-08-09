package cs3500.pa04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.types.Coord;
import cs3500.pa04.json.CoordinatesJson;
import cs3500.pa04.json.EmptyJson;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.JoinRequest;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.ShipJson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test the JSON serialization and deserialization.
 */
public class JsonTests {
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testJoinRequest() {
    JoinRequest x = new JoinRequest("alexs-2020", "SINGLE");
    JsonNode y = JsonUtils.serializeRecord(x);
    MessageJson response = new MessageJson("join", y);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    try {
      JsonParser parser = objectMapper.createParser(jsonResponse.toString());
      MessageJson message = parser.readValueAs(MessageJson.class);
      String name = message.messageName();
      JsonNode arguments = message.arguments();
      System.out.println();
      assertEquals("join", name);
      assertEquals("SINGLE",
          arguments.get("game-type").toString().substring(1, 7));
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testEmptyJson() {
    EmptyJson originalRecord = new EmptyJson();

    JsonNode node = JsonUtils.serializeRecord(originalRecord);

    assertEquals("{}", node.toString());
  }

  @Test
  public void testEndGameJson() {
    EndGameJson x = new EndGameJson("LOSE", "You lost!");
    JsonNode y = JsonUtils.serializeRecord(x);
    MessageJson response = new MessageJson("endgame", y);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    try {
      JsonParser parser = objectMapper.createParser(jsonResponse.toString());
      MessageJson message = parser.readValueAs(MessageJson.class);

      String name = message.messageName();
      JsonNode arguments = message.arguments();

      assertEquals("endgame", name);
      assertEquals("LOSE", arguments.get("result").asText());
      assertEquals("You lost!", arguments.get("reason").asText());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test

  public void testCoordinatesJson() {
    List<Coord> coords = new ArrayList<>();
    coords.add(new Coord(0, 0));

    CoordinatesJson x = new CoordinatesJson(coords);
    JsonNode y = JsonUtils.serializeRecord(x);
    MessageJson response = new MessageJson("coordinates", y);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    try {
      JsonParser parser = objectMapper.createParser(jsonResponse.toString());
      MessageJson message = parser.readValueAs(MessageJson.class);

      String name = message.messageName();
      JsonNode arguments = message.arguments();

      assertEquals("coordinates", name);
      assertEquals(0, arguments.get("coordinates").get(0).get("x").asInt());
      assertEquals(0, arguments.get("coordinates").get(0).get("y").asInt());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testFleetJson() {
    ArrayList<ShipJson> ships = new ArrayList<>();
    ships.add(new ShipJson(new Coord(2, 2), 3, "horizontal"));

    FleetJson x = new FleetJson(ships);
    JsonNode y = JsonUtils.serializeRecord(x);
    MessageJson response = new MessageJson("fleet", y);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    try {
      JsonParser parser = objectMapper.createParser(jsonResponse.toString());
      MessageJson message = parser.readValueAs(MessageJson.class);

      String name = message.messageName();
      JsonNode arguments = message.arguments();

      assertEquals("fleet", name);
      assertEquals(3, arguments.get("fleet").get(0).get("length").asInt());
      assertEquals("horizontal", arguments.get("fleet").get(0).get("direction").asText());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }


  @Test
  public void testJsonUtils() {
    JoinRequest originalRecord = new JoinRequest("alexs-2020", "SINGLE");
    JsonNode node = JsonUtils.serializeRecord(originalRecord);
    assertEquals("alexs-2020", node.get("name").asText());
    assertEquals("SINGLE", node.get("game-type").asText());
  }


  @Test
  public void testMessageJson() {
    MessageJson x = new MessageJson("ready", null);
    JsonNode y = JsonUtils.serializeRecord(x);
    MessageJson response = new MessageJson("message", y);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    try {
      JsonParser parser = objectMapper.createParser(jsonResponse.toString());
      MessageJson message = parser.readValueAs(MessageJson.class);

      String name = message.messageName();
      JsonNode arguments = message.arguments();

      assertEquals("message", name);
      assertEquals("ready", arguments.get("method-name").asText());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testShipJson() {

    ShipJson x = new ShipJson(new Coord(2, 2), 6, "horizontal");
    JsonNode y = JsonUtils.serializeRecord(x);
    MessageJson response = new MessageJson("ship", y);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);

    try {
      JsonParser parser = objectMapper.createParser(jsonResponse.toString());
      MessageJson message = parser.readValueAs(MessageJson.class);

      String name = message.messageName();
      JsonNode arguments = message.arguments();

      assertEquals("ship", name);
      assertEquals(2, arguments.get("coord").get("x").asInt());
      assertEquals(2, arguments.get("coord").get("y").asInt());
      assertEquals(6, arguments.get("length").asInt());
      assertEquals("horizontal", arguments.get("direction").asText());
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
}

