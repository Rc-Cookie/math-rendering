package com.github.rccookie.math;

public final class Precedence {

    private Precedence() { }


    public static final int MIN = Integer.MIN_VALUE;
    public static final int MAX = Integer.MAX_VALUE;

    public static final int BRACKETS = MAX;
    public static final int COMMA = MIN;

    public static final int SINGLE_BRACKET = 20;

//    public static final int LEFT_PARENTHESIS = -99;
//    public static final int RIGHT_PARENTHESIS = 100;
//    public static final int LEFT_BRACKET = LEFT_PARENTHESIS;
//    public static final int RIGHT_BRACKET = RIGHT_PARENTHESIS;

    public static final int PLUS = 100;
    public static final int MINUS = PLUS;
    public static final int MULTIPLY = 200;
    public static final int DIVIDE = MULTIPLY;
    public static final int POWER = 300;
    public static final int MODULO = DIVIDE;

    public static final int NEGATE = 110;
    public static final int FRACTION = DIVIDE + 10;
    public static final int FACTORIAL = 400;

    public static final int DEGREE = 1200;
    public static final int PERCENT = DEGREE;
    public static final int SQUARE = POWER;
    public static final int CUBE = POWER;
    public static final int ROOT = MAX;

    public static final int DEFINE = 0;
    public static final int LAMBDA = 10;

    public static final int EQUALS = 50;
    public static final int LESS = EQUALS;
    public static final int LESS_OR_EQUAL = EQUALS;
    public static final int GREATER = EQUALS;
    public static final int GREATER_OR_EQUAL = EQUALS;

    public static final int IN = 60;
    public static final int AND = 25;
    public static final int OR = AND;
    public static final int NOT = 30;

    public static final int IMPLICIT = MULTIPLY + 1;
    public static final int FUNCTION_CALL = IMPLICIT;

    public static final int MID = 75;
    public static final int GRID = 80;
    public static final int ITERATION = IMPLICIT;
    public static final int SUBSCRIPT = MAX;
    public static final int SUPERSCRIPT = POWER;
}
