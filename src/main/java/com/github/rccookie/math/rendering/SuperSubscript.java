package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.util.Arguments;

final class SuperSubscript implements Expression {

    final Expression main;
    final Expression sup;
    final Expression sub;

    SuperSubscript(Expression main, Expression sup, Expression sub) {
        this.main = Arguments.checkNull(main, "main");
        this.sup = Arguments.checkNull(sup, "sup");
        this.sub = Arguments.checkNull(sub, "sub");
    }

    @Override
    public String toString() {
        return "supSub("+main+", "+sup+", "+sub+")";
    }

    @Override
    public String renderInline() {
        return new Superscript(new Subscript(main, sub), sup).renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(Expression::renderAscii);
    }

    @Override
    public AsciiArt renderUnicode() {
        AsciiArt main = this.main.renderUnicode(), sup = this.sup.renderUnicode(), sub = this.sub.renderUnicode();

        if(sub.height() == 1 && !Utils.isSubscript(sub.toString())) {
            String subscript = Utils.toSubscript(sub.toString());
            if(subscript != null)
                return Superscript.renderArt(main.appendBottom(new AsciiArt(subscript)), sup, this.main instanceof Superscript || this.main instanceof SuperSubscript, main.size());
        }
        return renderArt(Expression::renderUnicode);
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        AsciiArt main = this.main.renderAscii(charset), sup = this.sup.renderAscii(charset), sub = this.sub.renderAscii(charset);

        if(sub.height() == 1 && !Utils.isSubscript(sub.toString())) {
            String subscript = Utils.toSubscript(sub.toString());
            if(subscript != null && charset.canDisplay(subscript))
                return Superscript.renderArt(main.appendBottom(new AsciiArt(subscript)), sup, this.main instanceof Superscript || this.main instanceof SuperSubscript, main.size());
        }
        return renderArt(e -> e.renderAscii(charset));
    }

    private AsciiArt renderArt(Function<Expression, AsciiArt> renderer) {
        AsciiArt main = renderer.apply(this.main);
        return Subscript.renderArt(
                Superscript.renderArt(main, renderer.apply(sup), this.main instanceof Superscript || this.main instanceof SuperSubscript, null),
                renderer.apply(sub),
                this.main instanceof Subscript || this.main instanceof SuperSubscript,
                main.size()
        );
//        int2 supPos = new int2(main.width(), -sup.height()/2);
//        supPos.y = Math.min(supPos.y, main.height() - sup.height() - 1);
//        int2 subPos = new int2(main.width(), main.height() - (sub.height()+1)/2);
//        subPos.y = Math.max(subPos.y, 1);
//        return main.append(sub, subPos).append(sup, supPos);
    }

    @Override
    public String renderLatex() {
        return "{"+main.renderLatex()+"}^{"+sup.renderLatex()+"}_{"+sub.renderLatex()+"}";
    }
}
