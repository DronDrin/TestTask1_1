package ru.drondrin.ui;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class StringLiterals {
    public static final String TUBES_PROMPT = """
            Enter the start position.
            
            Use any digits and letters or words for color names.
            For example: 6, 0, A, a, hello15, blue, red, DarkBlue
            
            For empty spots, use '_' (underscore) symbol.
            Also, you can skip empty tubes. They will be added to the end automatically.
            
            You can use almost any format for input. Here are some examples:
            1)   Table:
                 B R G 4     <-- the top of the tube is on the right
                 4 R R G
                 4 4 B B
                 _ _ _ _     <-- empty tube
            
            2)   Array:
                 {
                    {B, R, G, 4},
                    {4, R, R, G},
                    {4, 4, B, B}
                 }                      <-- the empty tube is omitted
            
            3)   Or go ahead and put it on one line:
                 B R G 4 4 R R G 4 4 B B
            
            Don't forget that every tube must contain exactly %d values, and you can't enter more than %d tubes.
            
            Here you go:
            """;

    public static Pattern COLOR_NAME_REGEX = compile("\\w+");
}
