package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class SimplePostfixOperation implements Expression {

    final Expression value;
    final Expression symbol;

    SimplePostfixOperation(Expression value, Expression symbol) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "postfix("+symbol.renderInline().trim()+", "+value+")";
    }

    @Override
    public String renderInline() {
        return value.renderInline() + symbol.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return value.renderAscii().appendCenter(symbol.renderAscii());
    }

    @Override
    public AsciiArt renderUnicode() {
        return value.renderUnicode().appendCenter(symbol.renderUnicode());
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return value.renderAscii(charset).appendCenter(symbol.renderAscii(charset));
    }

    @Override
    public String renderLatex() {
        return value.renderLatex() + symbol.renderLatex();
    }
}
