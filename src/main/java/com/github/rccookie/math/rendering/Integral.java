package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

import org.jetbrains.annotations.Nullable;

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
    public String renderInline(RenderOptions options) {
        return new BigSymbol(RenderableExpression.num("\u222B"), a, b, value).renderInline(options);
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return renderArt(renderableExpression -> renderableExpression.renderAscii(options), 3, null, "/", "|", "/");
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        return renderArt(renderableExpression -> renderableExpression.renderUnicode(options), 1, "\u222B", "\u2320", "\u23AE", "\u2321");
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        if(charset.canDisplay("\u222B\u2320\u23AE\u2321"))
            return renderArt(e -> e.renderAscii(options, charset), 1, "\u222B", "\u2320", "\u23AE", "\u2321");
        return renderArt(e -> e.renderAscii(options, charset), 3, null, "/", "|", "/");
    }

    private AsciiArt renderArt(Function<RenderableExpression, AsciiArt> renderer, int minHeight, String inline, String top, String middle, String bottom) {
        AsciiArt value = renderer.apply(this.value);
        AsciiArt symbol = createSymbol(value.height(), minHeight, inline, top, middle, bottom);

        AsciiArt art = symbol;
        if(this.a != null) {
            AsciiArt a = renderer.apply(this.a);
            art = art.draw(a, new int2(-(a.width()-1) / 2, art.height()));
        }
        if(this.b != null) {
            AsciiArt b = renderer.apply(this.b);
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
        return new BigSymbol(RenderableExpression.num("\u222B"), a, b, value).renderLatex(options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node symbol = new Node("mo");
        Node underOver = new Node("munderover");
        symbol.children.add(new Text("\u222B"));
        underOver.children.add(symbol);
        underOver.children.add(Utils.orEmpty(a, options));
        underOver.children.add(Utils.orEmpty(b, options));
        return Utils.join(underOver, value.renderMathMLNode(options));
    }
}
