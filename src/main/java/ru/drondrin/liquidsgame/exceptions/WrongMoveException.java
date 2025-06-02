package ru.drondrin.liquidsgame.exceptions;

public class WrongMoveException extends Exception {
    private final int from, to;

    public WrongMoveException(int from, int to, String message) {
        super(message);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getMessage() {
        return "Wrong move from %d to %d. Reason: %s".formatted(from, to, super.getMessage());
    }
}
