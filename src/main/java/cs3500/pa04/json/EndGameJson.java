package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Class to represent the JSON format of the coordinates.
 *
 * @param result The result of the game.
 * @param reason The reason for the result.
 */
public record EndGameJson(
    @JsonProperty("result") String result,
    @JsonProperty("reason") String reason) {
}
