package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.types.Coord;
import java.util.ArrayList;

/**
 * Class to represent the JSON format of the coordinates.
 *
 * @param shipCoords The list of coordinates.
 * @param shipLength The length of ship
 * @param dir The direction of ship
 */
public record ShipJson(
    @JsonProperty("coord")  Coord shipCoords,
    @JsonProperty("length") int shipLength,
    @JsonProperty("direction") String dir
){

}
