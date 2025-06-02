package ru.drondrin.liquidsgame;

public record Move(int from, int to) {
    public Move reverse() {
        return new Move(to, from);
    }
}
