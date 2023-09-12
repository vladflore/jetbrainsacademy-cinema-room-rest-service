package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

record PurchaseSeatResponseData(
        String token,
        @JsonProperty("ticket")
        SeatResponseData seatResponseData) {
}