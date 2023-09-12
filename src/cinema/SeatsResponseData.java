package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

record SeatsResponseData(
        @JsonProperty("total_rows") int totalRows,
        @JsonProperty("total_columns")
        int totalColumns,
        @JsonProperty("available_seats")
        List<SeatResponseData> availableSeats) {
}
