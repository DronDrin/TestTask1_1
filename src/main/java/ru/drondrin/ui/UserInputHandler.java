package ru.drondrin.ui;

import ru.drondrin.ui.exceptions.InvalidUserInputException;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class UserInputHandler {
    private final Scanner in;
    private final PrintStream out;

    public UserInputHandler(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Both min and max are inclusive boundaries.
     */
    public int getInt(String prompt, int min, int max) {
        return get(prompt, line -> {
            try {
                int result = parseInt(line);
                if (result < min || result > max)
                    invalidInput("This number must be between %d and %d.%nPlease, try again.".formatted(min, max));
                return result;
            } catch (NumberFormatException e) {
                invalidInput("This is not an integer.\nPlease try again.");
                return 0;
            }
        });
    }

    /**
     * @param checker Function that checks if user input is correct and converts it to needed type.
     *                Throw a {@link InvalidUserInputException} with a message for user
     *                if the input is malformed.
     * @param <R>     Requested type
     */
    public <R> R get(String prompt, Function<String, R> checker) {
        return get(prompt, checker, false);
    }

    /**
     * @param checker Function that checks if user input is correct and converts it to needed type.
     *                Throw a {@link InvalidUserInputException} with a message for user
     *                if the input is malformed.
     * @param <R>     Requested type
     */
    public <R> R get(String prompt, Function<String, R> checker, boolean multiLine) {
        while (true) {
            out.println();
            out.print(prompt);

            StringBuilder input = new StringBuilder();
            if (multiLine) {
                out.println("(type FINISH <enter> to end the multiline input)");
                String last;
                while (true) {
                    last = in.nextLine();
                    if (last.trim().equalsIgnoreCase("finish"))
                        break;
                    input.append(last).append("\n");
                }
            } else
                input.append(in.nextLine());

            try {
                return checker.apply(input.toString());
            } catch (InvalidUserInputException e) {
                out.println(e.getMessage());
            }
        }
    }

    /**
     * Short exception factory method.
     * Always throws {@link InvalidUserInputException} with specified message.
     */
    public static void invalidInput(String message) {
        throw new InvalidUserInputException(message);
    }
}
