package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
class SeatsController {

    private final SeatsManager seatsManager;
    private final Mapper mapper;

    SeatsController(SeatsManager seatsManager, Mapper mapper) {
        this.seatsManager = seatsManager;
        this.mapper = mapper;
    }

    @GetMapping("/seats")
    public SeatsResponseData allSeats() {
        return new SeatsResponseData(
                9,
                9,
                mapper.toDto(seatsManager.getAvailableSeats())
        );
    }

    @PostMapping("/purchase")
    public PurchaseSeatResponseData purchase(@RequestBody PurchaseSeatRequestData purchaseSeatRequestData) {
        var toPurchase = new Seat(purchaseSeatRequestData.row(), purchaseSeatRequestData.column());
        if (!seatsManager.isValidSeat(toPurchase)) {
            throw new InvalidSeatException("The number of a row or a column is out of bounds!");
        }
        if (!seatsManager.isSeatAvailable(toPurchase)) {
            throw new TicketAlreadyPurchasedException("The ticket has been already purchased!");
        }
        seatsManager.purchaseSeat(toPurchase);
        return new PurchaseSeatResponseData(
                seatsManager.getTokenBySeat(toPurchase).uuid().toString(),
                mapper.toDto(toPurchase)
        );
    }

    @PostMapping("/return")
    public ReturnSeatResponseData returnSeat(@RequestBody ReturnSeatRequestData returnSeatRequestData) {
        var token = new Token(UUID.fromString(returnSeatRequestData.token()));
        var seat = seatsManager.returnSeat(token);
        return new ReturnSeatResponseData(
                mapper.toDto(seat)
        );
    }

    @GetMapping("/stats")
    public StatsResponseData stats(@RequestParam(required = false) String password) {
        if (password == null || !password.equals("super_secret")) {
            throw new InvalidPasswordException("The password is wrong!");
        }
        return new StatsResponseData(
                seatsManager.calculateCurrentIncome(),
                seatsManager.getAvailableSeats().size(),
                seatsManager.getPurchasedSeats().size()
        );
    }

    @ExceptionHandler(value = {InvalidSeatException.class, TicketAlreadyPurchasedException.class, InvalidTokenException.class})
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("{\"error\": \"%s\"}".formatted(e.getMessage()));
    }

    @ExceptionHandler(value = {InvalidPasswordException.class})
    public ResponseEntity<String> handlePasswordException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"%s\"}".formatted(e.getMessage()));
    }
}


