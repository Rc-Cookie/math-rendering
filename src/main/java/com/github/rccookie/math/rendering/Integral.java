package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

import org.jetbrains.annotations.Nullable;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class Integral implements RenderableExpression {

    @Nullable
    final RenderableExpression a;
    @Nullable
    final RenderableExpression b;
    final RenderableExpression value;

    Integral(@Nullable RenderableExpression a, @Nullable RenderableExpression b, RenderableExpression value) {
        this.a = a;
        this.b = b;
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return "int("+a+", "+b+", "+value+")";
    }

    @Override
    public int precedence() {
        return Precedence.ITERATION;
    }

    @Override
    public String renderInline(RenderOptions options) {
        return new BigSymbol(RenderableExpression.num("\u222B"), a, b, value).render(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt value = this.value.renderAsciiArt(options);
        AsciiArt symbol;
        if(options.charset.canDisplay("\u222B\u2320\u23AE\u2321"))
            symbol = createSymbol(value.height(), 1, "\u222B", "\u2320", "\u23AE", "\u2321");
        else symbol = createSymbol(value.height(), 3, null, "/", "|", "/");

        AsciiArt art = symbol;
        if(this.a != null) {
            AsciiArt a = this.a.renderAsciiArt(options);
            art = art.draw(a, new int2(-(a.width()-1) / 2, art.height()));
        }
        if(this.b != null) {
            AsciiArt b = this.b.renderAsciiArt(options);
            art = art.draw(b, new int2(-b.width()/2, -b.height()));
        }
        return art.appendCenter(value);
    }

    private static AsciiArt createSymbol(int height, int minHeight, String inline, String top, String middle, String bottom) {
        int h = Math.max(height, minHeight);
        if(h <= 1) return new AsciiArt(inline);
        return new AsciiArt(h, i -> (i == 0 ? top : i == (h-1) ? bottom : middle) + " ");
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return new BigSymbol(RenderableExpression.num("\u222B"), a, b, value).render(LATEX, options);
    }

    @Override
    public <T> T render(RenderMode<T> mode, RenderOptions options) {
        if(mode == INLINE || mode == LATEX)
            return mode.render(this, options);
        return RenderableExpression.super.render(mode, options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node symbol = new Node("mo");
        Node underOver = new Node("munderover");
        symbol.children.add(new Text("\u222B"));
        underOver.children.add(symbol);
        underOver.children.add(Utils.orEmpty(a, options.setOutsidePrecedence(Precedence.MIN)));
        underOver.children.add(Utils.orEmpty(b, options.setOutsidePrecedence(Precedence.MIN)));
        return Utils.join(underOver, value.render(MATH_ML_NODE, options.setOutsidePrecedence(precedence())));
    }
}
