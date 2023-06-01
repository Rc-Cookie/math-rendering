package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

import static com.github.rccookie.math.rendering.RenderMode.*;

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
    public int precedence() {
        return Precedence.MID;
    }

    @Override
    public String renderInline(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence() + 1);
        String space = options.spaceMode == RenderOptions.SpaceMode.COMPACT ? "" : " ";
        return a.render(INLINE, options) + space + "|" + space + b.render(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence() + 1);

        String space = options.spaceMode == RenderOptions.SpaceMode.COMPACT ? "" : " ";
        String bar = space + options.charset.orFallback("|", "\u2502") + space;
        AsciiArt a = this.a.render(ASCII_ART, options), b = this.b.render(ASCII_ART, options);

        int height = Math.max(a.center(), b.center()), depth = Math.max(a.height()-a.center(), b.height()-b.center());
        int totalHeight = height + depth;
        AsciiArt barArt = new AsciiArt(totalHeight, i -> i==totalHeight-1 ? space+"|"+space : bar).setCenter(height);
        return a.appendCenter(barArt).appendCenter(b);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence() + 1);
        String space = options.spaceMode == RenderOptions.SpaceMode.COMPACT ? "" : "\\;";
        return "\\left."+a.render(LATEX, options)+space+"\\middle|"+space+b.render(LATEX, options)+"\\right.";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence() + 1);
        Node o = new Node("mo");
        o.children.add(new Text("|"));
        o.attributes.put("separator", "true");
        o.attributes.put("fence", "true");
        o.attributes.put("stretchy", "true");
        return Utils.join(a.render(MATH_ML_NODE, options), o, b.render(MATH_ML_NODE, options));
    }
}
