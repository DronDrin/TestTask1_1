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
                {1, 12, 3, 5}
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

    @Test
    public void iteratingSolverHardTest3() {
        LiquidsState state = new LiquidsState(7, 23, new int[][]{
                {7, 13, 10, 1, 19, 3, 19},
                {3, 8, 2, 10, 3, 3, 0},
                {18, 4, 5, 15, 7, 9, 14},
                {4, 9, 18, 17, 13, 10, 19},
                {6, 2, 3, 5, 20, 12, 1},
                {7, 16, 2, 14, 18, 17, 6},
                {8, 10, 2, 9, 7, 18, 12},
                {14, 7, 13, 20, 6, 11, 4},
                {16, 16, 15, 1, 8, 17, 17},
                {16, 5, 11, 8, 0, 18, 14},
                {8, 9, 1, 4, 14, 4, 0},
                {9, 3, 17, 12, 12, 11, 12},
                {0, 20, 1, 15, 5, 16, 16},
                {9, 20, 6, 15, 7, 18, 15},
                {13, 20, 11, 1, 3, 4, 0},
                {13, 10, 11, 10, 2, 8, 14},
                {8, 5, 13, 15, 19, 6, 10},
                {13, 19, 11, 20, 12, 19, 7},
                {1, 17, 6, 20, 19, 15, 17},
                {0, 2, 2, 11, 16, 14, 9},
                {5, 4, 12, 6, 18, 0, 5}
        });
        test(new IteratingLiquidsSolver(), state);
    }

    @Test
    public void iteratingSolverHardTest4() {
        LiquidsState state = new LiquidsState(6, 28, new int[][]{
                {14, 20, 23, 5, 16, 3},
                {18, 18, 4, 1, 5, 19},
                {7, 4, 19, 16, 14, 18},
                {10, 15, 21, 7, 9, 20},
                {17, 24, 21, 15, 14, 11},
                {13, 8, 9, 0, 23, 3},
                {18, 1, 17, 8, 10, 2},
                {22, 1, 12, 2, 10, 15},
                {12, 4, 24, 7, 8, 24},
                {22, 19, 5, 22, 18, 17},
                {20, 0, 2, 23, 6, 1},
                {4, 22, 24, 11, 21, 11},
                {16, 17, 9, 3, 7, 7},
                {25, 25, 23, 5, 1, 25},
                {0, 13, 9, 9, 19, 14},
                {11, 19, 6, 16, 4, 16},
                {10, 21, 4, 18, 25, 8},
                {9, 10, 13, 24, 25, 20},
                {1, 5, 15, 2, 2, 0},
                {13, 23, 20, 7, 21, 25},
                {6, 16, 3, 2, 3, 20},
                {6, 14, 11, 15, 22, 6},
                {14, 13, 17, 3, 24, 19},
                {5, 12, 6, 8, 12, 22},
                {21, 8, 13, 23, 0, 12},
                {11, 15, 10, 0, 12, 17}
        });
        test(new IteratingLiquidsSolver(), state);
    }

    // has solution
    @Test
    public void iteratingSolverHardTest5() {
        LiquidsState state = new LiquidsState(8, 14, new int[][]{
                {7, 4, 10, 6, 11, 5, 0, 7},
                {4, 6, 4, 7, 0, 8, 1, 5},
                {6, 6, 10, 2, 8, 1, 1, 1},
                {0, 8, 10, 0, 11, 7, 3, 4},
                {3, 6, 10, 5, 0, 2, 5, 2},
                {8, 3, 2, 6, 2, 9, 1, 0},
                {11, 9, 1, 0, 9, 7, 11, 3},
                {5, 3, 9, 4, 8, 7, 4, 2},
                {7, 9, 6, 11, 8, 1, 3, 8},
                {8, 9, 10, 5, 3, 0, 7, 5},
                {5, 3, 1, 10, 6, 4, 4, 11},
                {10, 9, 10, 11, 2, 9, 2, 11}
        });
        test(new IteratingLiquidsSolver(), state);
    }

    @Test
    public void iteratingSolverHardTest6() {
        LiquidsState state = new LiquidsState(7, 15, new int[][]{
                {0, 5, 11, 2, 12, 1, 8},
                {11, 0, 1, 5, 3, 2, 12},
                {8, 10, 8, 10, 9, 3, 6},
                {7, 5, 9, 10, 8, 6, 3},
                {4, 7, 9, 2, 2, 1, 6},
                {12, 6, 10, 7, 4, 9, 7},
                {0, 10, 5, 7, 8, 2, 0},
                {4, 6, 11, 9, 3, 2, 12},
                {12, 5, 1, 11, 0, 1, 12},
                {4, 7, 3, 0, 4, 6, 2},
                {5, 7, 9, 3, 1, 10, 4},
                {4, 0, 8, 6, 11, 8, 1},
                {5, 9, 11, 11, 10, 12, 3}
        });
        test(new IteratingLiquidsSolver(), state);
    }

    @Disabled               // do not use randomized stress tests in normal testing
    @RepeatedTest(10000)
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
        int tubeVolume = rnd.nextInt(3, 9);
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
