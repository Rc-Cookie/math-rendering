package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class SpecialValue implements Expression {

    public static final Expression PI = new SpecialValue("\u03C0", "pi", "\u03C0", "\\pi");
    public static final Expression CAP_PI = new SpecialValue("\u03A0", "Pi", "\u03A0", "\\Pi");

    public static final Expression NATURAL_NUMS = new SpecialValue("\u2115", "N", "\u2115", "\\NN");
    public static final Expression RATIONAL_NUMS = new SpecialValue("\u211A", "Q", "\u211A", "\\QQ");
    public static final Expression REAL_NUMS = new SpecialValue("\u211D", "R", "\u211D", "\\RR");
    public static final Expression COMPLEX_NUMS = new SpecialValue("\u2102", "C", "\u2102", "\\CC");

    public static final Expression INFINITY = new SpecialValue("\u221E", "oo", "\u221E", "\\infty");

    public static final Expression SUM = new SpecialValue("\u03A3", "__\n\\\n/_", "__\n\u2572\n\u2571_", "\\sum");
    public static final Expression PRODUCT = new SpecialValue("\u03A0", "___\n| |\n| |", "___\n\u2502 \u2502\n\u2502 \u2502", "\\prod");
    public static final Expression LIMES = new SpecialValue("lim", "lim", "lim", "\\lim");

    public static final Expression LEFT_ARROW = new SpecialValue("<-", "<-", "\u2190", "\\leftarrow");
    public static final Expression D_LEFT_ARROW = new SpecialValue("<==", "<==", "\u21D0", "\\Leftarrow");
    public static final Expression RIGHT_ARROW = new SpecialValue("->", "->", "\u2192", "\\rightarrow");
    public static final Expression D_RIGHT_ARROW = new SpecialValue("=>", "=>", "\u21D2", "\\Rightarrow");
    public static final Expression LEFT_RIGHT_ARROW = new SpecialValue("<->", "<->", "\u2194", "\\leftrightarrow");
    public static final Expression D_LEFT_RIGHT_ARROW = new SpecialValue("<=>", "<=>", "\u21D4", "\\Leftrightarrow");

    final String inline;
    final String ascii;
    final String unicode;
    final String latex;

    SpecialValue(String inline, String ascii, String unicode, String latex) {
        this.inline = Arguments.checkNull(inline, "inline");
        this.ascii = Arguments.checkNull(ascii, "ascii");
        this.unicode = Arguments.checkNull(unicode, "unicode");
        this.latex = Arguments.checkNull(latex, "latex");
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
        return new AsciiArt(ascii);
    }

    @Override
    public AsciiArt renderUnicode() {
        return new AsciiArt(unicode);
    }

    @Override
    public String renderLatex() {
        return latex;
    }
}
