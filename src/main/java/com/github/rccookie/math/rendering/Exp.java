package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Exp implements RenderableExpression {

    final RenderableExpression value;

    Exp(RenderableExpression value) {
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String renderInline(RenderOptions options) {
        return asCall().renderInline(options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return asPower().renderAscii(options);
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        return asPower().renderUnicode(options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        return asPower().renderAscii(options, charset);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return asPower().renderLatex(options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return asPower().renderMathMLNode(options);
    }

    private RenderableExpression asCall() {
        return RenderableExpression.call("exp", value);
    }

    private RenderableExpression asPower() {
        return RenderableExpression.pow(RenderableExpression.name("e"), value);
    }
}
