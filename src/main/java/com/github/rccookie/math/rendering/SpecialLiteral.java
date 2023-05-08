package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SpecialLiteral implements Expression {

    public static final Expression PI = new SpecialLiteral(false, "\u03C0", "pi", "\u03C0", "\\pi");
    public static final Expression CAP_PI = new SpecialLiteral(false, "\u03A0", "Pi", "\u03A0", "\\Pi");

    public static final Expression NATURAL_NUMS = new SpecialLiteral(false, "\u2115", "N", "\u2115", "\\NN");
    public static final Expression INT_NUMS = new SpecialLiteral(false, "\u211A", "Q", "\u211A", "\\QQ");
    public static final Expression RATIONAL_NUMS = new SpecialLiteral(false, "\u211A", "Q", "\u211A", "\\QQ");
    public static final Expression REAL_NUMS = new SpecialLiteral(false, "\u211D", "R", "\u211D", "\\RR");
    public static final Expression COMPLEX_NUMS = new SpecialLiteral(false, "\u2102", "C", "\u2102", "\\CC");

    public static final Expression INFINITY = new SpecialLiteral(false, "\u221E", "oo", "\u221E", "\\infty");

    public static final Expression SUM = new SpecialLiteral(false, "\u03A3", "__\n\\\n/_", "__\n\u2572\n\u2571_", "\\sum", "\u2211");
    public static final Expression PRODUCT = new SpecialLiteral(false, "\u03A0", "___\n| |\n| |", "___\n\u2502 \u2502\n\u2502 \u2502", "\\prod", "\u220F");
    public static final Expression LIMES = new SpecialLiteral(false, "lim", "lim", "lim", "\\lim");

    public static final Expression PLUS = new Literal(true, "+");
    public static final Expression MINUS = new SpecialLiteral(true, "-", "-", "-", "-", "\u2212");
    public static final Expression MULTIPLY = new SpecialLiteral(true, "\u00B7", "*", "\u00B7", "\\cdot");
    public static final Expression DIVIDE = new Literal(true, "/");
    public static final Expression CROSS = new SpecialLiteral(true, "\u2A2F", "x", "\u2A2F", "\\times");
    public static final Expression EQUALS = new Literal(true, "=");
    public static final Expression NOT_EQUALS = new SpecialLiteral(true, "!=", "!=", "\u2260", "\\not=");
    public static final Expression APPROXIMATELY = new SpecialLiteral(true, "\u2248", "~", "\u2248", "\\approx");
    public static final Expression NOT_APPROXIMATELY = new SpecialLiteral(true, "\u2249", "!~", "\u2249", "\\not\\approx");
    public static final Expression LESS = new Literal(true, "<");
    public static final Expression NOT_LESS = new SpecialLiteral(true, "\u226E", "!<", "\u226E", "\\not<");
    public static final Expression GREATER = new Literal(true, ">");
    public static final Expression NOT_GREATER = new SpecialLiteral(true, "\u226F", "!>", "\u226F", "\\not>");
    public static final Expression LESS_OR_EQUAL = new SpecialLiteral(true, "<=", "<=", "\u2A7D", "\\leq");
    public static final Expression NOT_LESS_OR_EQUAL = new SpecialLiteral(true, "\u2270", "!<=", "\u2270", "\\not\\leq");
    public static final Expression GREATER_OR_EQUAL = new SpecialLiteral(true, ">=", ">=", "\u2A7E", "\\geq");
    public static final Expression NOT_GREATER_OR_EQUAL = new SpecialLiteral(true, "\u2271", "!<=", "\u2271", "\\not\\geq");
    public static final Expression DEFINE = new SpecialLiteral(true, " \u2254 ", " := ", " \u2254 ", " := ", "\u2254");
    public static final Expression DEFINE_REVERSE = new SpecialLiteral(true, " \u2255 ", " =: ", " \u2255 ", " =: ", "\u2255");
    public static final Expression IN = new SpecialLiteral(" \u2208 ", new AsciiArt("  __ \n /__ \n \\__ ").setCenter(1), new AsciiArt(" \u2208 "), "\\in", new Literal(true, "\u2208").renderMathMLNode());
    public static final Expression NOT_IN = new SpecialLiteral(" \u2209 ", new AsciiArt("  __/\n /_/ \n \\/_ \n /").setCenter(1), new AsciiArt(" \u2209 "), "\\not\\in", new Literal(true, "\u2209").renderMathMLNode());
    public static final Expression CONTAINS = new SpecialLiteral(" \u220B ", new AsciiArt(" __/\n _/\\ \n /_/\n/").setCenter(1), new AsciiArt(" \u220B "), "\\ni", new Literal(true, "\u220B").renderMathMLNode());
    public static final Expression CONTAINS_NOT = new SpecialLiteral(" \u220C ", new AsciiArt("  __/\n /_/ \n \\/_ \n /").setCenter(1), new AsciiArt(" \u220C "), "\\not\\ni", new Literal(true, "\u220C").renderMathMLNode());
    public static final Expression AND = new SpecialLiteral(true, "\u2227", "&", "\u2227", "\\land");
    public static final Expression OR = new SpecialLiteral(true, "\u2228", " || ", "\u2228", "\\lor");
    public static final Expression NEGATE = new Literal(true, "-");
    public static final Expression NOT = new SpecialLiteral(true, "\u00AC", "!", "\u00AC", "\\lnot");
    public static final Expression FACTORIAL = new Literal(true, "!");
    public static final Expression PERCENT = new SpecialLiteral(true, "%", "%", "%", "\\%");
    public static final Expression DEGREE = new SpecialLiteral(true, "°", "°", "°", "^\\circ");

    public static final Expression LEFT_ARROW = new SpecialLiteral(true, "<-", "<-", "\u2190", "\\leftarrow");
    public static final Expression D_LEFT_ARROW = new SpecialLiteral(true, "<==", "<==", "\u21D0", "\\Leftarrow");
    public static final Expression RIGHT_ARROW = new SpecialLiteral(true, "->", "->", "\u2192", "\\rightarrow");
    public static final Expression D_RIGHT_ARROW = new SpecialLiteral(true, "=>", "=>", "\u21D2", "\\Rightarrow");
    public static final Expression LEFT_RIGHT_ARROW = new SpecialLiteral(true, "<->", "<->", "\u2194", "\\leftrightarrow");
    public static final Expression D_LEFT_RIGHT_ARROW = new SpecialLiteral(true, "<=>", "<=>", "\u21D4", "\\Leftrightarrow");

    final String inline;
    final AsciiArt ascii;
    final AsciiArt unicode;
    final String latex;
    final Node mathML;

    SpecialLiteral(boolean operator, String inline, String ascii, String unicode, String latex) {
        this(operator, inline, ascii, unicode, latex, unicode);
    }

    SpecialLiteral(boolean operator, String inline, String ascii, String unicode, String latex, String mathML) {
        this(inline, new AsciiArt(ascii), new AsciiArt(unicode), latex, new Literal(operator, mathML).renderMathMLNode());
    }

    SpecialLiteral(String inline, AsciiArt ascii, AsciiArt unicode, String latex, Node mathML) {
        this.inline = Arguments.checkNull(inline, "inline");
        this.ascii = Arguments.checkNull(ascii, "ascii");
        this.unicode = Arguments.checkNull(unicode, "unicode");
        this.latex = Arguments.checkNull(latex, "latex");
        this.mathML = Arguments.checkNull(mathML, "mathML");
    }

    @Override
    public String toString() {
        return inline;
    }

    @Override
    public String renderInline() {
        return inline;
    }

    @Override
    public AsciiArt renderAscii() {
        return ascii;
    }

    @Override
    public AsciiArt renderUnicode() {
        return unicode;
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return charset.orFallback(unicode, ascii);
    }

    @Override
    public String renderLatex() {
        return latex;
    }

    @Override
    public Node renderMathMLNode() {
        return mathML.clone();
    }
}
