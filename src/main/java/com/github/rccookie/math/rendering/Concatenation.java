package com.github.rccookie.math.rendering;

import java.util.Set;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Concatenation implements RenderableExpression {

    final RenderableExpression a;
    final RenderableExpression b;
    final boolean maybeSpace;

    Concatenation(RenderableExpression a, RenderableExpression b, boolean maybeSpace) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
        this.maybeSpace = maybeSpace;
    }

    @Override
    public String toString() {
        return (maybeSpace ? "implicit" : "concat") + "("+a+", "+b+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return a.renderInline(options) + (renderSpace() ? " " : "") + b.renderInline(options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        if(renderSpace())
            return a.renderAscii(options).appendTop(new AsciiArt(" ")).appendCenter(b.renderAscii(options));
        return a.renderAscii(options).appendCenter(b.renderAscii(options));
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        if(renderSpace())
            return a.renderUnicode(options).appendTop(new AsciiArt(" ")).appendCenter(b.renderUnicode(options));
        return a.renderUnicode(options).appendCenter(b.renderUnicode(options));
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        if(renderSpace())
            return a.renderAscii(options, charset).appendTop(new AsciiArt(" ")).appendCenter(b.renderAscii(options, charset));
        return a.renderAscii(options, charset).appendCenter(b.renderAscii(options, charset));
    }

    @Override
    public String renderLatex(RenderOptions options) {
        if(renderSpace())
            return a.renderLatex(options) + " \\; " + b.renderLatex(options);
        return a.renderLatex(options) + " " + b.renderLatex(options); // Math mode, spaces are ignored
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        if(renderSpace())
            return Utils.join(a.renderMathMLNode(options), new Node("mspace"), b.renderMathMLNode(options));
        return Utils.join(a.renderMathMLNode(options), b.renderMathMLNode(options));
    }

    private static final Set<Class<? extends RenderableExpression>> NO_SPACE_TYPES = Set.of(
            Brackets.class,
            BracketLiteral.class,
            Fraction.class,
            Grid.class
    );

    private boolean renderSpace() {
        if(!maybeSpace) return false;
        if(NO_SPACE_TYPES.contains(a.getClass()) || NO_SPACE_TYPES.contains(b.getClass()))
            return false;
        if(!(a instanceof NumberLiteral && b instanceof NumberLiteral)) return true;

        String a = ((NumberLiteral) this.a).value, b = ((NumberLiteral) this.b).value;
        if(a.isEmpty() || b.isEmpty()) return false;
        if(a.contains("\n") || b.contains("\n")) return true;

        char aEnd = a.charAt(a.length()-1), bStart = b.charAt(0);
        return aEnd >= '0' && aEnd <= '9' && bStart >= '0' && bStart <= '9';
    }
}
