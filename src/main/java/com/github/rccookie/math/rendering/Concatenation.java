package com.github.rccookie.math.rendering;

import java.util.Set;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

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
    public int precedence() {
        return maybeSpace ? Precedence.IMPLICIT : Math.min(a.precedence(), b.precedence());
    }

    @Override
    public String toString() {
        return (maybeSpace ? "implicit" : "concat") + "("+a+", "+b+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        return a.render(INLINE, options) + (renderSpace(options) ? " " : "") + b.render(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        if(renderSpace(options))
            return a.render(ASCII_ART, options).appendTop(new AsciiArt(" ")).appendCenter(b.render(ASCII_ART, options));
        return a.render(ASCII_ART, options).appendCenter(b.render(ASCII_ART, options));
    }

    @Override
    public String renderLatex(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        if(renderSpace(options))
            return a.render(LATEX, options) + " \\; " + b.render(LATEX, options);
        return a.render(LATEX, options) + " " + b.render(LATEX, options); // Math mode, spaces are ignored
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        if(renderSpace(options))
            return Utils.join(a.render(MATH_ML_NODE, options), new Node("mspace"), b.render(MATH_ML_NODE, options));
        return Utils.join(a.render(MATH_ML_NODE, options), b.render(MATH_ML_NODE, options));
    }

    private static final Set<Class<? extends RenderableExpression>> NO_SPACE_TYPES = Set.of(
            Brackets.class,
            BracketLiteral.class,
            Fraction.class,
            Grid.class
    );

    private boolean renderSpace(RenderOptions options) {
        if(!maybeSpace) return false;
        if(options.spaceMode == RenderOptions.SpaceMode.FORCE) return true;
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
