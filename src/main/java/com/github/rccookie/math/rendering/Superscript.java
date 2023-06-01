package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class Superscript implements RenderableExpression {

    final RenderableExpression a, b;

    Superscript(RenderableExpression a, RenderableExpression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "sup("+a+", "+b+")";
    }

    @Override
    public int precedence() {
        return Precedence.SUPERSCRIPT;
    }

    @Override
    public String renderInline(RenderOptions options) {
        String a = this.a.render(INLINE, options.setOutsidePrecedence(precedence()+1)), b = this.b.render(INLINE, options.setOutsidePrecedence(Precedence.MIN));
        if(!Utils.isSuperscript(a)) {
            String superscript = Utils.toSuperscript(b);
            if(superscript != null && options.charset.canDisplay(superscript)) return a + superscript;
        }
        return a+"^"+this.b.render(INLINE, options.setOutsidePrecedence(Precedence.MAX));
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt a = this.a.render(ASCII_ART, options), b = this.b.render(ASCII_ART, options);
        if(b.height() == 1 && !Utils.isSuperscript(a.toString())) {
            String superscript = Utils.toSuperscript(b.getLine(0));
            if(superscript != null && options.charset.canDisplay(superscript))
                return a.appendTop(new AsciiArt(superscript));
        }
        return renderArt(a, b, this.a instanceof Superscript || this.a instanceof SuperSubscript, null);
    }

    static AsciiArt renderArt(AsciiArt a, AsciiArt b, boolean aIsSup, int2 aSize) {
        if(aSize == null) aSize = a.size();
        int2 pos = new int2(aSize.x, -b.height()/2);
        pos.y = Math.min(pos.y, aIsSup ? -1 : aSize.y - b.height() - 1);
        return a.draw(b, pos);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return "{"+a.render(LATEX, options.setOutsidePrecedence(precedence()+1))+"}^{"+b.render(LATEX, options.setOutsidePrecedence(Precedence.MIN))+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node sup = new Node("msup");
        sup.children.add(a.render(MATH_ML_NODE, options.setOutsidePrecedence(precedence() + 1)));
        sup.children.add(b.render(MATH_ML_NODE, options.setOutsidePrecedence(Precedence.MIN)));
        return sup;
    }
}
