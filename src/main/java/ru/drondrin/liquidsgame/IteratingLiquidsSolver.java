package ru.drondrin.liquidsgame;

import ru.drondrin.liquidsgame.exceptions.WrongMoveException;

import java.util.*;

public class IteratingLiquidsSolver implements LiquidsSolver {
    private final HashSet<String> pastStates = new HashSet<>();

    @Override
    public synchronized Optional<Deque<Move>> solve(LiquidsState state) {
        pastStates.clear();
        Optional<Deque<Move>> res = solveRecursively(state);
        pastStates.clear();
        return res;
    }

    private Optional<Deque<Move>> solveRecursively(LiquidsState state) {
        pastStates.add(state.toUniqueString());
        if (state.isSolved())
            return Optional.of(new ArrayDeque<>());
        for (int from = 0; from < state.getTubesCount(); from++) {
            List<Integer> fromTube = state.getTubes().get(from);
            if (fromTube.isEmpty())
                continue;
            for (int to = 0; to < state.getTubesCount(); to++) {
                List<Integer> toTube = state.getTubes().get(to);
                if (from == to
                        || toTube.size() >= state.getTubeVolume()
                        || (!toTube.isEmpty() && !fromTube.getLast().equals(toTube.getLast())))
                    continue;
                var move = new Move(from, to);
                try {
                    LiquidsState.MoveResult moveRes = state.move(move);
                    if (pastStates.contains(state.toUniqueString())) {
                        state.revertMove(moveRes);
                        continue;
                    }
                    var res = solveRecursively(state);
                    state.revertMove(moveRes);
                    if (res.isPresent()) {
                        return res.map(r -> {
                            r.addLast(move);
                            return r;
                        });
                    }
                } catch (WrongMoveException e) {
                    System.err.println("Solver failed: " + e.getMessage());
                    throw new RuntimeException(e);
                }

            }
        }
        return Optional.empty();
    }
}
