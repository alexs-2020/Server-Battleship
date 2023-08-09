package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.types.Coord;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the JSON format of the coordinates.
 *
 * @param coordArrayList The list of coordinates.
 */
public record CoordinatesJson(
    @JsonProperty("coordinates") List<Coord> coordArrayList) {
}

