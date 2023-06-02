package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Transposition implements RenderableExpression {

    private final RenderableExpression inner;

    Transposition(RenderableExpression inner) {
        this.inner = Arguments.checkNull(inner, "inner");
    }

    @Override
    public String toString() {
        return "transp("+inner+")";
    }

    @Override
    public int precedence() {
        return Precedence.TRANSPOSITION;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return getStyle(options).renderInline(options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return getStyle(options).renderAsciiArt(options);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return getStyle(options).renderLatex(options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return getStyle(options).renderMathMLNode(options);
    }

    @Override
    public <T> T render(RenderMode<T> mode, RenderOptions options) {
        return getStyle(options).render(mode, options);
    }

    private RenderableExpression getStyle(RenderOptions options) {
        switch(options.transpositionStyle) {
            case APOSTROPH: return new SimplePostfixOperation(inner, SpecialLiteral.TRANSPOSITION_APOSTROPH, precedence(), OperatorAlignment.TOP);
            case LOWER_T: return new Superscript(inner, new Text("t"));
            case UPPER_T: return new Superscript(inner, new Text("T"));
            default: throw new AssertionError();
        }
    }
}
