package ru.drondrin.liquidsgame;

import ru.drondrin.liquidsgame.exceptions.WrongMoveException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public class LiquidsState {
    private final List<List<Integer>> tubes;
    private final int tubeVolume, tubesCount;

    public LiquidsState(int tubeVolume, int tubesCount) {
        this.tubeVolume = tubeVolume;
        this.tubesCount = tubesCount;
        tubes = new ArrayList<>();
        for (int i = 0; i < tubesCount; i++)
            tubes.add(new ArrayList<>(tubeVolume));
    }

    public LiquidsState(int tubeVolume, int tubesCount, int[][] tubes) {
        this(tubeVolume, tubesCount);
        checkTubesInit(tubeVolume, tubesCount, tubes);
        for (int i = 0; i < tubes.length; i++) {
            for (int j = 0; j < tubeVolume; j++) {
                if (tubes[i][j] != -1)
                    this.tubes.get(i).add(tubes[i][j]);
            }
        }
    }

    private static void checkTubesInit(int tubeVolume, int tubesCount, int[][] tubes) {
        if (tubes.length > tubesCount)
            throw new IllegalArgumentException("Too many tubes");
        for (var tube : tubes)
            if (tube.length != tubeVolume)
                throw new IllegalArgumentException("Wrong tube volume");

        int[] colorsCnt = new int[tubesCount + 1];
        for (var tube : tubes)
            for (int color : tube)
                if (color != -1 && color < colorsCnt.length) colorsCnt[color]++;

        for (int color : colorsCnt) {
            if (colorsCnt[color] != 0 && colorsCnt[color] != tubeVolume) {
                throw new IllegalArgumentException("Wrong coloring");
            }
        }
    }

    public int getTubeVolume() {
        return tubeVolume;
    }

    public int getTubesCount() {
        return tubesCount;
    }

    public List<List<Integer>> getTubes() {
        return tubes;
    }

    private boolean isTubeInvalid(int i) {
        return i < 0 || i >= tubes.size();
    }

    public MoveResult move(Move move) throws WrongMoveException {
        validateTubeIDs(move);
        var fromTube = tubes.get(move.from());
        var toTube = tubes.get(move.to());
        validateMove(move, fromTube, toTube);

        int count = 0;
        while (!fromTube.isEmpty() && toTube.size() < tubeVolume &&
                (toTube.isEmpty() || fromTube.getLast().equals(toTube.getLast()))) {
            toTube.add(fromTube.getLast());
            fromTube.removeLast();
            count++;
        }
        return new MoveResult(move.from(), move.to(), count);
    }

    public void revertMove(MoveResult move) {
        var fromTubeRev = tubes.get(move.to);
        var toTubeRev = tubes.get(move.from);
        for (int i = 0; i < move.count; ++i) {
            toTubeRev.add(fromTubeRev.getLast());
            fromTubeRev.removeLast();
        }
    }

    private void validateMove(Move move, List<Integer> fromTube, List<Integer> toTube) throws WrongMoveException {
        if (fromTube.isEmpty() || toTube.size() >= tubeVolume)
            throw new WrongMoveException(move.from(), move.to(),
                    "Tube filling error (sizes: %d -> %d). Volume: %d".formatted(fromTube.size(), toTube.size(), tubeVolume));
        if (!toTube.isEmpty() && !fromTube.getLast().equals(toTube.getLast()))
            throw new WrongMoveException(move.from(), move.to(), "Wrong coloring");
    }

    private void validateTubeIDs(Move move) throws WrongMoveException {
        if (isTubeInvalid(move.to()) || isTubeInvalid(move.from()) ||
                move.from() == move.to())
            throw new WrongMoveException(move.from(), move.to(), "Invalid index");
    }

    public boolean isSolved() {
        return tubes.stream().allMatch(t -> t.stream().distinct().count() <= 1);
    }

    public static class MoveResult {
        private final int from, to, count;

        private MoveResult(int from, int to, int count) {
            this.from = from;
            this.to = to;
            this.count = count;
        }
    }

    @Override
    public String toString() {
        return tubes.stream().map(this::tubeToString).collect(joining("\n"));
    }

    public String toUniqueString() {
        return tubes.stream()
                .filter(t -> !t.isEmpty())
                .map(this::tubeToString)
                .sorted()
                .collect(joining("\n"));
    }

    private String tubeToString(List<Integer> tube) {
        return range(0, tubeVolume)
                .mapToObj(i -> i < tube.size() ? String.valueOf(tube.get(i)) : "_")
                .collect(joining(" "));
    }
}
