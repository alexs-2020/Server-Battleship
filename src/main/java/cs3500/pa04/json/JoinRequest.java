package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class to represent the JSON format of the coordinates.
 *
 * @param githubUserName The name of the user.
 * @param type The type of game.
 */
public record JoinRequest(
    @JsonProperty("name") String githubUserName,
    @JsonProperty("game-type") String type) {
}
