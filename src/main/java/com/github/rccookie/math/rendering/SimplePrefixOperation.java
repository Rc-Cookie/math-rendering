package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SimplePrefixOperation implements Expression {

    final Expression value;
    final Expression symbol;

    SimplePrefixOperation(Expression value, Expression symbol) {
        this.value = Arguments.checkNull(value, "value");
        this.symbol = Arguments.checkNull(symbol, "symbol");
    }

    @Override
    public String toString() {
        return "prefix("+symbol.renderInline().trim()+", "+value+")";
    }

    @Override
    public String renderInline() {
        return symbol.renderInline() + value.renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return symbol.renderAscii().appendCenter(value.renderAscii());
    }

    @Override
    public AsciiArt renderUnicode() {
        return symbol.renderUnicode().appendCenter(value.renderUnicode());
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return symbol.renderAscii(charset).appendCenter(value.renderAscii(charset));
    }

    @Override
    public String renderLatex() {
        return symbol.renderLatex()+"{"+value.renderLatex()+"}";
    }

    @Override
    public Node renderMathMLNode() {
        return Utils.join(symbol.renderMathMLNode(), value.renderMathMLNode());
    }
}
