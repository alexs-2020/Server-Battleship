package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Class to represent the JSON format of the coordinates.
 *
 * @param listShip The list of ships.
 */
public record FleetJson(
    @JsonProperty("fleet") ArrayList<ShipJson> listShip) {
}
