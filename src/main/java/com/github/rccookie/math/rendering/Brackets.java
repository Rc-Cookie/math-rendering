package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class Brackets implements Expression {

    final Bracket type;
    final Expression inner;

    Brackets(Bracket type, Expression inner) {
        this.type = Arguments.checkNull(type, "type");
        this.inner = Arguments.checkNull(inner, "inner");
    }

    @Override
    public String toString() {
        return "brackets("+type+", "+inner+")";
    }

    @Override
    public String renderInline() {
        if(type == Bracket.CEIL)
            return "ceil("+inner.renderInline()+")";
        if(type == Bracket.FLOOR)
            return "floor("+inner.renderInline()+")";
        return BracketLiteral.LEFT_SYMBOLS_ASCII[type.ordinal()] + inner.renderInline() + BracketLiteral.RIGHT_SYMBOLS_ASCII[type.ordinal()];
    }

    @Override
    public AsciiArt renderAscii() {
        AsciiArt inner = this.inner.renderAscii();
        return BracketLiteral.renderBracketAscii(type, true, inner.height())
                .appendBottom(inner)
                .appendBottom(BracketLiteral.renderBracketAscii(type, false, inner.height()));
    }

    @Override
    public AsciiArt renderUnicode() {
        AsciiArt inner = this.inner.renderUnicode();
        return BracketLiteral.renderBracketUnicode(type, true, inner.height())
                .appendBottom(inner)
                .appendBottom(BracketLiteral.renderBracketUnicode(type, false, inner.height()));
    }

    @Override
    public String renderLatex() {
        return "\\left" + BracketLiteral.LEFT_LATEX[type.ordinal()] + inner.renderLatex() + "\\right" + BracketLiteral.RIGHT_LATEX[type.ordinal()];
    }
}
