package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class Subscript implements RenderableExpression {

    final RenderableExpression a, b;

    Subscript(RenderableExpression a, RenderableExpression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "sub("+a+", "+b+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        String a = this.a.renderInline(options), b = this.b.renderInline(options);
        if(!Utils.isSubscript(a)) {
            String subscript = Utils.toSubscript(b);
            if(subscript != null) return a + subscript;
        }
        return a+"_"+Utils.encapsulate(b);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt a = this.a.renderAsciiArt(options), b = this.b.renderAsciiArt(options);
        if(b.height() == 1 && !Utils.isSubscript(a.toString())) {
            String subscript = Utils.toSubscript(b.getLine(0));
            if(subscript != null && options.charset.canDisplay(subscript))
                return a.appendBottom(new AsciiArt(subscript));
        }
        return renderArt(a,b, this.a instanceof Subscript || this.a instanceof SuperSubscript, null);
    }

    static AsciiArt renderArt(AsciiArt a, AsciiArt b, boolean aIsSub, int2 aSize) {
        if(aSize == null) aSize = a.size();
        int2 pos = new int2(aSize.x, a.height() - (b.height()+1)/2);
        pos.y = Math.max(pos.y, aIsSub ? aSize.x - b.height() + 1 : a.height() - aSize.y + 1);
        return a.draw(b, pos);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return "{"+a.renderLatex(options)+"}_{"+b.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node sub = new Node("msub");
        sub.children.add(a.renderMathMLNode(options));
        sub.children.add(b.renderMathMLNode(options));
        return sub;
    }
}
