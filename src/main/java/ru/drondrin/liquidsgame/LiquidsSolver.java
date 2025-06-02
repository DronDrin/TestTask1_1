package ru.drondrin.liquidsgame;

import java.util.Deque;
import java.util.Optional;

public interface LiquidsSolver {
    Optional<Deque<Move>> solve(LiquidsState state);
}
