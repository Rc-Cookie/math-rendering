package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

final class Brackets implements RenderableExpression {

    final Bracket type;
    final RenderableExpression inner;

    Brackets(Bracket type, RenderableExpression inner) {
        this.type = Arguments.checkNull(type, "type");
        this.inner = Arguments.checkNull(inner, "inner");
    }

    @Override
    public String toString() {
        return "brackets("+type+", "+inner+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        if(type == Bracket.CEIL)
            return "ceil("+inner.renderInline(options)+")";
        if(type == Bracket.FLOOR)
            return "floor("+inner.renderInline(options)+")";
        return BracketLiteral.LEFT_SYMBOLS_ASCII[type.ordinal()] + inner.renderInline(options) + BracketLiteral.RIGHT_SYMBOLS_ASCII[type.ordinal()];
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt inner = this.inner.renderAsciiArt(options);
        AsciiArt left = BracketLiteral.renderBracketUnicode(type, true, inner.height());
        AsciiArt right;
        if(!options.charset.canDisplay(left.toString())) {
            left = BracketLiteral.renderBracketAscii(type, true, inner.height());
            right = BracketLiteral.renderBracketAscii(type, false, inner.height());
        }
        else if(!options.charset.canDisplay((right = BracketLiteral.renderBracketUnicode(type, false, inner.height())).toString()))
            right = BracketLiteral.renderBracketAscii(type, false, inner.height());
        return left.appendBottom(inner).appendBottom(right);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return "\\left" + BracketLiteral.LEFT_LATEX[type.ordinal()] + inner.renderLatex(options) + "\\right" + BracketLiteral.RIGHT_LATEX[type.ordinal()];
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node left = new Node("mo"), right = new Node("mo");
        left.children.add(new Text(BracketLiteral.LEFT_SYMBOLS_UNICODE[type.ordinal()]));
        right.children.add(new Text(BracketLiteral.RIGHT_SYMBOLS_UNICODE[type.ordinal()]));
        left.attributes.put("fence", "true");
        right.attributes.put("fence", "true");
        left.attributes.put("stretchy", "true");
        right.attributes.put("stretchy", "true");
        return Utils.join(left, inner.renderMathMLNode(options), right);
    }
}
