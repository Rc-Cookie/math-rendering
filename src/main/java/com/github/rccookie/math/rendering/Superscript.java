package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

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
    public String renderInline(RenderOptions options) {
        String a = this.a.renderInline(options), b = this.b.renderInline(options);
        if(!Utils.isSuperscript(a)) {
            String superscript = Utils.toSuperscript(b);
            if(superscript != null) return a + superscript;
        }
        return a+"^"+Utils.encapsulate(b);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return renderArt(a.renderAscii(options), b.renderAscii(options), a instanceof Superscript || a instanceof SuperSubscript, null);
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        AsciiArt a = this.a.renderUnicode(options), b = this.b.renderUnicode(options);
        if(b.height() == 1 && !Utils.isSuperscript(a.toString())) {
            String superscript = Utils.toSuperscript(b.getLine(0));
            if(superscript != null)
                return a.appendTop(new AsciiArt(superscript));
        }
        return renderArt(a, b, this.a instanceof Superscript || this.a instanceof SuperSubscript, null);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        AsciiArt a = this.a.renderAscii(options, charset), b = this.b.renderAscii(options, charset);
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
    public String renderLatex(RenderOptions options) {
        return "{"+a.renderLatex(options)+"}^{"+b.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node sup = new Node("msup");
        sup.children.add(a.renderMathMLNode(options));
        sup.children.add(b.renderMathMLNode(options));
        return sup;
    }
}
