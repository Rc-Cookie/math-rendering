package com.github.rccookie.math.rendering;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

final class Subscript implements Expression {

    final Expression a, b;

    Subscript(Expression a, Expression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "sub("+a+", "+b+")";
    }

    @Override
    public String renderInline() {
        String a = this.a.renderInline(), b = this.b.renderInline();
        if(!Utils.isSubscript(a)) {
            String subscript = Utils.toSubscript(b);
            if(subscript != null) return a + subscript;
        }
        return a+"_"+Utils.encapsulate(b);
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(a.renderAscii(), b.renderAscii(), a instanceof Subscript || a instanceof SuperSubscript, null);
    }

    @Override
    public AsciiArt renderUnicode() {
        AsciiArt a = this.a.renderUnicode(), b = this.b.renderUnicode();
        if(b.height() == 1 && !Utils.isSubscript(a.toString())) {
            String subscript = Utils.toSubscript(b.getLine(0));
            if(subscript != null)
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
    public String renderLatex() {
        return "{"+a.renderLatex()+"}_{"+b.renderLatex()+"}";
    }
}
