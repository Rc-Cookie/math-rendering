package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

final class Middle implements RenderableExpression {

    private final RenderableExpression a, b;

    Middle(RenderableExpression a, RenderableExpression b) {
        this.a = Arguments.checkNull(a, "a");
        this.b = Arguments.checkNull(b, "b");
    }

    @Override
    public String toString() {
        return "mid("+a+", "+b+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return a.renderInline(options) + " | " + b.renderInline(options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        String bar = options.charset.orFallback("|", "\u2502");
        AsciiArt a = this.a.renderAsciiArt(options), b = this.b.renderAsciiArt(options);

        int height = Math.max(a.center(), b.center()), depth = Math.max(a.height()-a.center(), b.height()-b.center());
        int totalHeight = height + depth;
        AsciiArt barArt = new AsciiArt(totalHeight, i -> i==totalHeight-1 ? " | " : " "+bar+" ").setCenter(height);
        return a.appendCenter(barArt).appendCenter(b);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return "\\left."+a.renderLatex(options)+"\\;\\middle|\\;"+b.renderLatex(options)+"\\right.";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node o = new Node("mo");
        o.children.add(new Text("|"));
        o.attributes.put("separator", "true");
        o.attributes.put("fence", "true");
        o.attributes.put("stretchy", "true");
        return Utils.join(a.renderMathMLNode(options), o, b.renderMathMLNode(options));
    }
}
