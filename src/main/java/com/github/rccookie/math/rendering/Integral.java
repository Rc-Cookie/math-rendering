package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

import org.jetbrains.annotations.Nullable;

final class Integral implements Expression {

    @Nullable
    final Expression a;
    @Nullable
    final Expression b;
    final Expression value;

    Integral(@Nullable Expression a, @Nullable Expression b, Expression value) {
        this.a = a;
        this.b = b;
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return "int("+a+", "+b+", "+value+")";
    }

    @Override
    public String renderInline() {
        return new BigSymbol(Expression.value("\u222B"), a, b, value).renderInline();
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(Expression::renderAscii, 3, null, "/", "|", "/");
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderArt(Expression::renderUnicode, 1, "\u222B", "\u2320", "\u23AE", "\u2321");
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        if(charset.canDisplay("\u222B\u2320\u23AE\u2321"))
            return renderArt(e -> e.renderAscii(charset), 1, "\u222B", "\u2320", "\u23AE", "\u2321");
        return renderArt(e -> e.renderAscii(charset), 3, null, "/", "|", "/");
    }

    private AsciiArt renderArt(Function<Expression, AsciiArt> renderer, int minHeight, String inline, String top, String middle, String bottom) {
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
    public String renderLatex() {
        return new BigSymbol(Expression.value("\u222B"), a, b, value).renderLatex();
    }
}
