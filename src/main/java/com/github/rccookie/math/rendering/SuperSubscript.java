package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

final class SuperSubscript implements RenderableExpression {

    final RenderableExpression main;
    final RenderableExpression sup;
    final RenderableExpression sub;

    SuperSubscript(RenderableExpression main, RenderableExpression sup, RenderableExpression sub) {
        this.main = Arguments.checkNull(main, "main");
        this.sup = Arguments.checkNull(sup, "sup");
        this.sub = Arguments.checkNull(sub, "sub");
    }

    @Override
    public String toString() {
        return "supSub("+main+", "+sup+", "+sub+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return new Superscript(new Subscript(main, sub), sup).renderInline(options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return renderArt(renderableExpression -> renderableExpression.renderAscii(options));
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        AsciiArt main = this.main.renderUnicode(options), sup = this.sup.renderUnicode(options), sub = this.sub.renderUnicode(options);

        if(sub.height() == 1 && !Utils.isSubscript(sub.toString())) {
            String subscript = Utils.toSubscript(sub.toString());
            if(subscript != null)
                return Superscript.renderArt(main.appendBottom(new AsciiArt(subscript)), sup, this.main instanceof Superscript || this.main instanceof SuperSubscript, main.size());
        }
        return renderArt(renderableExpression -> renderableExpression.renderUnicode(options));
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        AsciiArt main = this.main.renderAscii(options, charset), sup = this.sup.renderAscii(options, charset), sub = this.sub.renderAscii(options, charset);

        if(sub.height() == 1 && !Utils.isSubscript(sub.toString())) {
            String subscript = Utils.toSubscript(sub.toString());
            if(subscript != null && charset.canDisplay(subscript))
                return Superscript.renderArt(main.appendBottom(new AsciiArt(subscript)), sup, this.main instanceof Superscript || this.main instanceof SuperSubscript, main.size());
        }
        return renderArt(e -> e.renderAscii(options, charset));
    }

    private AsciiArt renderArt(Function<RenderableExpression, AsciiArt> renderer) {
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
    public String renderLatex(RenderOptions options) {
        return "{"+main.renderLatex(options)+"}^{"+sup.renderLatex(options)+"}_{"+sub.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node subSup = new Node("msubSup");
        subSup.children.add(main.renderMathMLNode(options));
        subSup.children.add(sub.renderMathMLNode(options));
        subSup.children.add(sup.renderMathMLNode(options));
        return subSup;
    }
}
