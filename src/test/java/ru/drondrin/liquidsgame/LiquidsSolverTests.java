package ru.drondrin.liquidsgame;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.drondrin.liquidsgame.exceptions.WrongMoveException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.shuffle;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 5, unit = SECONDS)
public class LiquidsSolverTests {
    @Test
    public void iteratingSolverSimple() {
        var solver = new IteratingLiquidsSolver();
        // check for state erasing, so twice
        simpleTest(solver);
        simpleTest(solver);
    }

    @Test
    public void iteratingSolverHardTest() {
        LiquidsState state = new LiquidsState(4, 14, new int[][]{
                {4, 4, 10, 2},
                {8, 12, 8, 1},
                {9, 5, 7, 10},
                {5, 2, 3, 5},
                {7, 8, 11, 6},
                {2, 1, 12, 12},
                {11, 8, 7, 4},
                {1, 3, 11, 10},
                {9, 9, 7, 10},
                {11, 6, 2, 6},
                {3, 9, 5, 4},
                {1, 12, 3, 6}
        });
        test(new IteratingLiquidsSolver(), state);
    }

    @Test
    public void iteratingSolverHardTest2() {
        LiquidsState state = new LiquidsState(7, 11, new int[][]{
                {5, 0, 7, 3, 8, 2, 4},
                {8, 1, 4, 1, 5, 4, 2},
                {6, 0, 5, 0, 0, 3, 5},
                {1, 5, 2, 4, 0, 6, 7},
                {6, 5, 7, 2, 2, 8, 3},
                {3, 4, 3, 2, 2, 7, 5},
                {0, 3, 0, 8, 7, 6, 6},
                {6, 4, 1, 7, 6, 1, 3},
                {7, 1, 4, 1, 8, 8, 8}
        });
        test(new IteratingLiquidsSolver(), state);
    }

    @Disabled               // do not use randomized stress tests in normal testing
    @RepeatedTest(100000)
    public void iteratingSolverRandomTest() {
        randomTest(new IteratingLiquidsSolver());
    }

    private static void simpleTest(LiquidsSolver solver) {
        LiquidsState state = new LiquidsState(3, 4, new int[][]{
                {1, 2, 3},
                {1, 3, 2},
                {3, 1, 2}
        });
        test(solver, state);
    }

    private static void randomTest(LiquidsSolver solver) {
        Random rnd = new Random(System.nanoTime());
        int tubeVolume = rnd.nextInt(3, 7);
        int tubesCount = rnd.nextInt(4, 16);
        int liquidsCount = tubesCount - 2;

        var liquids = range(0, liquidsCount)
                .flatMap(liquid -> range(0, tubeVolume).map(j -> liquid))
                .boxed()
                .collect(Collectors.toList());
        shuffle(liquids, rnd);
        var tubes = new int[liquidsCount][tubeVolume];
        for (int i = 0; i < liquidsCount; i++)
            for (int j = 0; j < tubeVolume; j++) {
                tubes[i][j] = liquids.getLast();
                liquids.removeLast();
            }
        var state = new LiquidsState(tubeVolume, tubesCount, tubes);
        System.out.println("Random test:");
        System.out.printf("tubeVolume = %d; tubesCount = %d; liquidsCount = %d;%n", tubeVolume, tubesCount, liquidsCount);
        System.out.println(state);
        Optional<Deque<Move>> solveOptional = solver.solve(state);
        solveOptional.ifPresent(moves -> verifySolve(state, moves.reversed()));
        System.out.printf("%n(has%s solution)", solveOptional.isPresent() ? "" : " not");
        System.out.println("\n\n\n");
    }

    private static void test(LiquidsSolver solver, LiquidsState state) {
        Optional<Deque<Move>> solveRaw = solver.solve(state);
        assertTrue(solveRaw.isPresent());
        var solve = solveRaw.get().reversed();

        verifySolve(state, solve);
    }

    private static void verifySolve(LiquidsState state, Deque<Move> solve) {
        for (Move move : solve) {
            try {
                state.move(move);
            } catch (WrongMoveException e) {
                fail(e.getMessage());
            }
        }
        assertTrue(state.isSolved());
    }
}
