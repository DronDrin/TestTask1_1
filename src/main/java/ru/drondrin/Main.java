package ru.drondrin;

import ru.drondrin.liquidsgame.IteratingLiquidsSolver;
import ru.drondrin.liquidsgame.LiquidsSolver;
import ru.drondrin.liquidsgame.LiquidsState;
import ru.drondrin.liquidsgame.Move;
import ru.drondrin.ui.UserInputHandler;

import java.util.Deque;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.MatchResult;

import static ru.drondrin.ui.StringLiterals.COLOR_NAME_REGEX;
import static ru.drondrin.ui.StringLiterals.TUBES_PROMPT;
import static ru.drondrin.ui.UserInputHandler.invalidInput;

public class Main {
    public static void main(String[] args) {
        LiquidsSolver solver = new IteratingLiquidsSolver();
        var in = new UserInputHandler(new Scanner(System.in), System.out);

        System.out.println("Welcome!");
        int tubesCount = in.getInt("Enter number of tubes: ", 3, 15);
        int tubeVolume = in.getInt("Enter the volume of tubes: ", 3, 6);

        in.get(TUBES_PROMPT.formatted(tubeVolume, tubesCount), input -> {
            HashMap<String, Integer> colorIds = new HashMap<>();

            var droplets = COLOR_NAME_REGEX.matcher(input).results().map(MatchResult::group).toList();
            if (droplets.size() % tubeVolume > 0)
                invalidInput("Wrong number of droplets.");

            var tubes = new int[droplets.size() / tubeVolume][tubeVolume];

            int colorI = 0;
            for (int i = 0; i < droplets.size(); i++) {
                if (!colorIds.containsKey(droplets.get(i)))
                    colorIds.put(droplets.get(i), colorI++);
                tubes[i / tubeVolume][i % tubeVolume] = colorIds.get(droplets.get(i));
            }

            try {
                var state = new LiquidsState(tubeVolume, tubesCount, tubes);
                System.out.println("\n\nSolving...");
                Optional<Deque<Move>> solveOptional = solver.solve(state);

                System.out.println();
                if (solveOptional.isPresent()) {
                    var moves = solveOptional.get().reversed();
                    System.out.printf("Solved in %d moves (showing indexes of tubes):%n", moves.size());
                    for (var move : moves)
                        System.out.printf("%d -> %d; ", move.from(), move.to());
                    System.out.println();
                } else {
                    System.out.println("Unfortunately, for this starting position, no solutions were found.");
                }
            } catch (IllegalArgumentException ex) {
                invalidInput("Wrong starting position. (Reason: %s)".formatted(ex.getMessage()));
            }
            return tubes;
        }, true);

        System.out.println("Thank you. Bye!");
    }
}