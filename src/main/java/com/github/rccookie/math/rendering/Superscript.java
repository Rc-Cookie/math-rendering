package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Superscript implements Expression {

    final Expression a, b;

    Superscript(Expression a, Expression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "sup("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        String a = this.a.renderInline(), b = this.b.renderInline();
        if(!Utils.isSuperscript(a)) {
            String superscript = Utils.toSuperscript(b);
            if(superscript != null) return a + superscript;
        }
        return a+"^"+Utils.encapsulate(b);
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(a.renderAscii(), b.renderAscii(), a instanceof Superscript || a instanceof SuperSubscript, null);
    }

    @Override
    public AsciiArt renderUnicode() {
        AsciiArt a = this.a.renderUnicode(), b = this.b.renderUnicode();
        if(b.height() == 1 && !Utils.isSuperscript(a.toString())) {
            String superscript = Utils.toSuperscript(b.getLine(0));
            if(superscript != null)
                return a.appendTop(new AsciiArt(superscript));
        }
        return renderArt(a, b, this.a instanceof Superscript || this.a instanceof SuperSubscript, null);
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        AsciiArt a = this.a.renderAscii(charset), b = this.b.renderAscii(charset);
        if(b.height() == 1 && !Utils.isSuperscript(a.toString())) {
            String superscript = Utils.toSuperscript(b.getLine(0));
            if(superscript != null && charset.canDisplay(superscript))
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
    public String renderLatex() {
        return "{"+a.renderLatex()+"}^{"+b.renderLatex()+"}";
    }

    @Override
    public Node renderMathMLNode() {
        Node sup = new Node("msup");
        sup.children.add(a.renderMathMLNode());
        sup.children.add(b.renderMathMLNode());
        return sup;
    }
}
