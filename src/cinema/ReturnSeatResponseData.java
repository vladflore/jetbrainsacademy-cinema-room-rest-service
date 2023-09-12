package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReturnSeatResponseData(
        @JsonProperty("returned_ticket")
        SeatResponseData seat
) {
}
