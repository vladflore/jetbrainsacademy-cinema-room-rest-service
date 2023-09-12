package cinema;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
class SeatsManager {
    private final List<Seat> seats;
    private final List<Seat> purchasedSeats;

    private final Map<Seat, Price> prices;

    private final Map<Token, Seat> tokens;

    private static final int ROWS = 9;
    private static final int COLUMNS = 9;

    public Price getSeatPrice(Seat seat) {
        return prices.get(seat);
    }

    SeatsManager() {
        seats = new ArrayList<>();
        purchasedSeats = new ArrayList<>();
        prices = new HashMap<>();
        tokens = new HashMap<>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Seat seat = new Seat(i + 1, j + 1);
                seats.add(seat);
                prices.put(seat, calculateSeatPrice(i));
            }
        }
    }

    private Price calculateSeatPrice(int row) {
        return new Price((row <= 4) ? 10 : 8);
    }

    public boolean isSeatAvailable(Seat seat) {
        return !purchasedSeats.contains(seat);
    }

    public void purchaseSeat(Seat seat) {
        purchasedSeats.add(seat);
        tokens.put(generateToken(), seat);
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream().filter(s -> !purchasedSeats.contains(s)).toList();
    }

    public boolean isValidSeat(Seat seat) {
        return 1 <= seat.row() && seat.row() <= ROWS && 1 <= seat.column() && seat.column() <= COLUMNS;
    }

    private Token generateToken() {
        return new Token(UUID.randomUUID());
    }

    public Seat returnSeat(Token token) {
        Seat seat = tokens.get(token);
        if (seat == null) {
            throw new InvalidTokenException("Wrong token!");
        }
        tokens.remove(token);
        purchasedSeats.remove(seat);
        return seat;
    }

    public Token getTokenBySeat(Seat toPurchase) {
        return tokens.entrySet().stream()
                .filter(e -> e.getValue().equals(toPurchase))
                .findFirst()
                .get()
                .getKey();
    }

    public List<Seat> getPurchasedSeats() {
        return Collections.unmodifiableList(purchasedSeats);
    }

    public int calculateCurrentIncome() {
        return purchasedSeats.stream().mapToInt(s -> prices.get(s).amount()).sum();
    }
}


