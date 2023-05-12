package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class MatrixBrackets implements RenderableExpression {

    private final RenderableExpression inner;

    MatrixBrackets(RenderableExpression inner) {
        this.inner = Arguments.checkNull(inner, "inner");
    }

    @Override
    public String renderInline(RenderOptions options) {
        return new Brackets(options.matrixBrackets, inner).renderInline(options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return new Brackets(options.matrixBrackets, inner).renderAsciiArt(options);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return new Brackets(options.matrixBrackets, inner).renderLatex(options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return new Brackets(options.matrixBrackets, inner).renderMathMLNode(options);
    }
}
