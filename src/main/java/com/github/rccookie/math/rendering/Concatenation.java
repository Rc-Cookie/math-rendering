package com.github.rccookie.math.rendering;

import java.util.Set;

import com.github.rccookie.util.Arguments;

final class Concatenation implements Expression {

    final Expression a;
    final Expression b;
    final boolean maybeSpace;

    Concatenation(Expression a, Expression b, boolean maybeSpace) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        this.maybeSpace = maybeSpace;
    }

    @Override
    public String toString() {
        return (maybeSpace ? "implicit" : "concat") + "("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        return a.renderInline() + (renderSpace() ? " " : "") + b.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        if(renderSpace())
            return a.renderAscii().appendTop(new AsciiArt(" ")).appendCenter(b.renderAscii(), false);
        return a.renderAscii().appendCenter(b.renderAscii(), false);
    }

    @Override
    public AsciiArt renderUnicode() {
        if(renderSpace())
            return a.renderUnicode().appendTop(new AsciiArt(" ")).appendCenter(b.renderUnicode(), false);
        return a.renderUnicode().appendCenter(b.renderUnicode(), false);
    }

    @Override
    public String renderLatex() {
        if(renderSpace())
            return a.renderLatex() + " \\; " + b.renderLatex();
        return a.renderLatex() + " " + b.renderLatex(); // Math mode, spaces are ignored
    }

    private static final Set<Class<? extends Expression>> NO_SPACE_TYPES = Set.of(
            Brackets.class,
            BracketLiteral.class,
            Fraction.class,
            Grid.class
    );

    private boolean renderSpace() {
        if(!maybeSpace) return false;
        if(NO_SPACE_TYPES.contains(a.getClass()) || NO_SPACE_TYPES.contains(b.getClass()))
            return false;
        if(!(a instanceof Literal && b instanceof Literal)) return true;

        String a = ((Literal) this.a).value, b = ((Literal) this.b).value;
        if(a.isEmpty() || b.isEmpty()) return false;
        if(a.contains("\n") || b.contains("\n")) return true;

        char aEnd = a.charAt(a.length()-1), bStart = b.charAt(0);
        return aEnd >= '0' && aEnd <= '9' && bStart >= '0' && bStart <= '9';
    }
}
