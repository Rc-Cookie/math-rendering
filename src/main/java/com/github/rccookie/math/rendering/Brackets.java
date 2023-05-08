package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

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
    public AsciiArt renderAscii(CharacterSet charset) {
        AsciiArt inner = this.inner.renderAscii(charset);
        AsciiArt left = BracketLiteral.renderBracketUnicode(type, true, inner.height());
        AsciiArt right;
        if(!charset.canDisplay(left.toString())) {
            left = BracketLiteral.renderBracketAscii(type, true, inner.height());
            right = BracketLiteral.renderBracketAscii(type, false, inner.height());
        }
        else if(!charset.canDisplay((right = BracketLiteral.renderBracketUnicode(type, false, inner.height())).toString()))
            right = BracketLiteral.renderBracketAscii(type, false, inner.height());
        return left.appendBottom(inner).appendBottom(right);
    }

    @Override
    public String renderLatex() {
        return "\\left" + BracketLiteral.LEFT_LATEX[type.ordinal()] + inner.renderLatex() + "\\right" + BracketLiteral.RIGHT_LATEX[type.ordinal()];
    }

    @Override
    public Node renderMathMLNode() {
        Node left = new Node("mo"), right = new Node("mo");
        left.children.add(new Text(BracketLiteral.LEFT_SYMBOLS_UNICODE[type.ordinal()]));
        right.children.add(new Text(BracketLiteral.RIGHT_SYMBOLS_UNICODE[type.ordinal()]));
        left.attributes.put("fence", "true");
        right.attributes.put("fence", "true");
        left.attributes.put("stretchy", "true");
        right.attributes.put("stretchy", "true");
        return Utils.join(left, inner.renderMathMLNode(), right);
    }
}
