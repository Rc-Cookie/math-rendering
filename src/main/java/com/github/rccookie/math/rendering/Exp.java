package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class Exp implements RenderableExpression {

    final RenderableExpression value;

    Exp(RenderableExpression value) {
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public int precedence() {
        return Math.min(Precedence.FUNCTION_CALL, Precedence.POWER);
    }

    @Override
    public String renderInline(RenderOptions options) {
        return asCall().render(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return asPower().render(ASCII_ART, options);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return asPower().render(LATEX, options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return asPower().render(MATH_ML_NODE, options);
    }

    @Override
    public <T> T render(RenderMode<T> mode, RenderOptions options) {
        return mode.render(this, options);
    }

    private RenderableExpression asCall() {
        return RenderableExpression.call("exp", value);
    }

    private RenderableExpression asPower() {
        return RenderableExpression.pow(RenderableExpression.name("e"), value);
    }
}
