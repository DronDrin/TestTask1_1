package ru.drondrin.liquidsgame;

import ru.drondrin.liquidsgame.exceptions.WrongMoveException;

import java.util.ArrayList;
import java.util.List;

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
                this.tubes.get(i).add(tubes[i][j]);
            }
        }
    }

    private static void checkTubesInit(int tubeVolume, int tubesCount, int[][] tubes) {
        if (tubes.length >= tubesCount)
            throw new IllegalArgumentException("LiquidsState init: tubes.length >= tubesCount");
        for (var tube : tubes)
            if (tube.length != tubeVolume)
                throw new IllegalArgumentException("LiquidsState init: tubes[i].length != tubeVolume");
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

    public void move(Move move) throws WrongMoveException {
        validateTubeIDs(move);
        var fromTube = tubes.get(move.from());
        var toTube = tubes.get(move.to());
        validateMove(move, fromTube, toTube);

        toTube.add(fromTube.getLast());
        fromTube.removeLast();
    }

    private void validateMove(Move move, List<Integer> fromTube, List<Integer> toTube) throws WrongMoveException {
        if (fromTube.isEmpty() || toTube.size() >= tubeVolume)
            throw new WrongMoveException(move.from(), move.to(),
                    "Tube filling error (sizes: %d -> %d). Volume: %d".formatted(fromTube.size(), toTube.size(), tubeVolume));
        if (!fromTube.getLast().equals(toTube.getLast()))
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
}
