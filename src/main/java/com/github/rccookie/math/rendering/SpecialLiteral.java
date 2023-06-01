package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SpecialLiteral implements RenderableExpression {

    public static final RenderableExpression PI = new SpecialLiteral(false, "\u03C0", "pi", "\u03C0", "\\pi");
    public static final RenderableExpression CAP_PI = new SpecialLiteral(false, "\u03A0", "Pi", "\u03A0", "\\Pi");

    public static final RenderableExpression NATURAL_NUMS = new SpecialLiteral(false, "\u2115", "N", "\u2115", "\\NN");
    public static final RenderableExpression INT_NUMS = new SpecialLiteral(false, "\u211A", "Q", "\u211A", "\\QQ");
    public static final RenderableExpression RATIONAL_NUMS = new SpecialLiteral(false, "\u211A", "Q", "\u211A", "\\QQ");
    public static final RenderableExpression REAL_NUMS = new SpecialLiteral(false, "\u211D", "R", "\u211D", "\\RR");
    public static final RenderableExpression COMPLEX_NUMS = new SpecialLiteral(false, "\u2102", "C", "\u2102", "\\CC");

    public static final RenderableExpression INFINITY = new SpecialLiteral(false, "\u221E", "oo", "\u221E", "\\infty");

    public static final RenderableExpression SUM = new SpecialLiteral(false, "\u03A3", "__\n\\\n/_", "__\n\u2572\n\u2571_", "\\sum", "\u2211");
    public static final RenderableExpression PRODUCT = new SpecialLiteral(false, "\u03A0", "___\n| |\n| |", "___\n\u2502 \u2502\n\u2502 \u2502", "\\prod", "\u220F");
    public static final RenderableExpression LIMES = new SpecialLiteral(false, "lim", "lim", "lim", "\\lim");

    public static final RenderableExpression PLUS = new Literal(true, "+");
    public static final RenderableExpression MINUS = new SpecialLiteral(true, "-", "-", "-", "-", "\u2212");
    public static final RenderableExpression MULTIPLY = new SpecialLiteral(true, "\u00B7", "*", "\u00B7", "\\cdot");
    public static final RenderableExpression DIVIDE = new Literal(true, "/");
    public static final RenderableExpression CROSS = new SpecialLiteral(true, "\u2A2F", "x", "\u2A2F", "\\times");
    public static final RenderableExpression MODULO = new SpecialLiteral(true, " mod ", " mod ", " mod ", "\\mod");
    public static final RenderableExpression EQUALS = new Literal(true, "=");
    public static final RenderableExpression NOT_EQUALS = new SpecialLiteral(true, "!=", "!=", "\u2260", "\\not=");
    public static final RenderableExpression APPROXIMATELY = new SpecialLiteral(true, "\u2248", "~", "\u2248", "\\approx");
    public static final RenderableExpression NOT_APPROXIMATELY = new SpecialLiteral(true, "\u2249", "!~", "\u2249", "\\not\\approx");
    public static final RenderableExpression LESS = new Literal(true, "<");
    public static final RenderableExpression NOT_LESS = new SpecialLiteral(true, "\u226E", "!<", "\u226E", "\\not<");
    public static final RenderableExpression GREATER = new Literal(true, ">");
    public static final RenderableExpression NOT_GREATER = new SpecialLiteral(true, "\u226F", "!>", "\u226F", "\\not>");
    public static final RenderableExpression LESS_OR_EQUAL = new SpecialLiteral(true, "<=", "<=", "\u2A7D", "\\leq");
    public static final RenderableExpression NOT_LESS_OR_EQUAL = new SpecialLiteral(true, "\u2270", "!<=", "\u2270", "\\not\\leq");
    public static final RenderableExpression GREATER_OR_EQUAL = new SpecialLiteral(true, ">=", ">=", "\u2A7E", "\\geq");
    public static final RenderableExpression NOT_GREATER_OR_EQUAL = new SpecialLiteral(true, "\u2271", "!<=", "\u2271", "\\not\\geq");
    public static final RenderableExpression DEFINE = new SpecialLiteral(true, " := ", " := ", " \u2254 ", " := ", "\u2254");
    public static final RenderableExpression DEFINE_REVERSE = new SpecialLiteral(true, " =: ", " =: ", " \u2255 ", " =: ", "\u2255");
    public static final RenderableExpression IN = new SpecialLiteral(" \u2208 ", new AsciiArt("  __ \n /__ \n \\__ ").setCenter(1), new AsciiArt(" \u2208 "), "\\in", new Literal(true, "\u2208").renderMathMLNode(RenderOptions.DEFAULT));
    public static final RenderableExpression NOT_IN = new SpecialLiteral(" \u2209 ", new AsciiArt("  __/\n /_/ \n \\/_ \n /").setCenter(1), new AsciiArt(" \u2209 "), "\\not\\in", new Literal(true, "\u2209").renderMathMLNode(RenderOptions.DEFAULT));
    public static final RenderableExpression CONTAINS = new SpecialLiteral(" \u220B ", new AsciiArt(" __/\n _/\\ \n /_/\n/").setCenter(1), new AsciiArt(" \u220B "), "\\ni", new Literal(true, "\u220B").renderMathMLNode(RenderOptions.DEFAULT));
    public static final RenderableExpression CONTAINS_NOT = new SpecialLiteral(" \u220C ", new AsciiArt("  __/\n /_/ \n \\/_ \n /").setCenter(1), new AsciiArt(" \u220C "), "\\not\\ni", new Literal(true, "\u220C").renderMathMLNode(RenderOptions.DEFAULT));
    public static final RenderableExpression AND = new SpecialLiteral(true, "\u2227", "&", "\u2227", "\\land");
    public static final RenderableExpression OR = new SpecialLiteral(true, "\u2228", " || ", "\u2228", "\\lor");
    public static final RenderableExpression NEGATE = new Literal(true, "-");
    public static final RenderableExpression NOT = new SpecialLiteral(true, "\u00AC", "!", "\u00AC", "\\lnot");
    public static final RenderableExpression FACTORIAL = new Literal(true, "!");
    public static final RenderableExpression PERCENT = new SpecialLiteral(true, "%", "%", "%", "\\%");
    public static final RenderableExpression DEGREE = new SpecialLiteral(true, "°", "°", "°", "^\\circ");

    public static final RenderableExpression LEFT_ARROW = new SpecialLiteral(true, "<-", "<-", "\u2190", "\\leftarrow");
    public static final RenderableExpression D_LEFT_ARROW = new SpecialLiteral(true, "<==", "<==", "\u21D0", "\\Leftarrow");
    public static final RenderableExpression RIGHT_ARROW = new SpecialLiteral(true, "->", "->", "\u2192", "\\rightarrow");
    public static final RenderableExpression D_RIGHT_ARROW = new SpecialLiteral(true, "=>", "=>", "\u21D2", "\\Rightarrow");
    public static final RenderableExpression LEFT_RIGHT_ARROW = new SpecialLiteral(true, "<->", "<->", "\u2194", "\\leftrightarrow");
    public static final RenderableExpression D_LEFT_RIGHT_ARROW = new SpecialLiteral(true, "<=>", "<=>", "\u21D4", "\\Leftrightarrow");

    final String inline;
    final AsciiArt ascii;
    final AsciiArt unicode;
    final String latex;
    final Node mathML;

    SpecialLiteral(boolean operator, String inline, String ascii, String unicode, String latex) {
        this(operator, inline, ascii, unicode, latex, unicode);
    }

    SpecialLiteral(boolean operator, String inline, String ascii, String unicode, String latex, String mathML) {
        this(inline, new AsciiArt(ascii), new AsciiArt(unicode), latex, new Literal(operator, mathML).renderMathMLNode(RenderOptions.DEFAULT));
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
    public int precedence() {
        return Precedence.MAX;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return inline;
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return options.charset.orFallback(unicode, ascii);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return latex;
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return mathML.clone();
    }
}
