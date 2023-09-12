package cinema;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
    private final SeatsManager seatsManager;

    public Mapper(SeatsManager seatsManager) {
        this.seatsManager = seatsManager;
    }

    public List<SeatResponseData> toDto(List<Seat> seats) {
        return seats.stream().map(
                seat -> new SeatResponseData(
                        seat.row(),
                        seat.column(),
                        seatsManager.getSeatPrice(seat).amount()
                )
        ).toList();
    }

    public SeatResponseData toDto(Seat seat) {
        return new SeatResponseData(
                seat.row(),
                seat.column(),
                seatsManager.getSeatPrice(seat).amount()
        );
    }
}
