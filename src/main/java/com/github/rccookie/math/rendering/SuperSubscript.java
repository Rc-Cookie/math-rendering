package com.github.rccookie.math.rendering;

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
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt main = this.main.renderAsciiArt(options), sup = this.sup.renderAsciiArt(options), sub = this.sub.renderAsciiArt(options);

        if(sub.height() == 1 && !Utils.isSubscript(sub.toString())) {
            String subscript = Utils.toSubscript(sub.toString());
            if(subscript != null && options.charset.canDisplay(subscript))
                return Superscript.renderArt(main.appendBottom(new AsciiArt(subscript)), sup, this.main instanceof Superscript || this.main instanceof SuperSubscript, main.size());
        }

        return Subscript.renderArt(
                Superscript.renderArt(main, sup, this.main instanceof Superscript || this.main instanceof SuperSubscript, null),
                sub,
                this.main instanceof Subscript || this.main instanceof SuperSubscript,
                main.size()
        );
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
