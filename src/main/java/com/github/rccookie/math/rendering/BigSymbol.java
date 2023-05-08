package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

import org.jetbrains.annotations.Nullable;

final class BigSymbol implements Expression {

    final Expression symbol;
    @Nullable
    final Expression sup;
    @Nullable
    final Expression sub;
    final Expression value;

    BigSymbol(Expression symbol, @Nullable Expression sub, @Nullable Expression sup, Expression value) {
        this.symbol = Arguments.checkNull(symbol, "symbol");
        this.sup = sup;
        this.sub = sub;
        this.value = Arguments.checkNull(value, "value");
    }


    @Override
    public String renderInline() {
        String header;
        if(sup == null) {
            if(sub == null) header = symbol.renderInline();
            else header = new Subscript(symbol, sub).renderInline();
        }
        else if(sub == null)
            header = new Superscript(symbol, sup).renderInline();
        else header = new SuperSubscript(symbol, sup, sub).renderInline();

        return header + Utils.encapsulate(value.renderInline());
    }

    @Override
    public AsciiArt renderAscii() {
        return renderArt(Expression::renderAscii);
    }

    @Override
    public AsciiArt renderUnicode() {
        return renderArt(Expression::renderUnicode);
    }

    private AsciiArt renderArt(Function<Expression, AsciiArt> renderer) {
        AsciiArt symbol = renderer.apply(this.symbol);
        AsciiArt art = symbol;
        if(this.sub != null) {
            AsciiArt sub = renderer.apply(this.sub);
            art = art.draw(sub, new int2((symbol.width() - sub.width() + 1) / 2, art.height()));
        }
        int yOff = 0;
        if(this.sup != null) {
            AsciiArt sup = renderer.apply(this.sup);
            yOff = sup.height();
            art = art.draw(sup, new int2((symbol.width() - sup.width() + 1) / 2, -sup.height()));
        }
        AsciiArt value = renderer.apply(this.value);
        return art.draw(value, new int2(art.width(), yOff + (symbol.height() - value.height() + 1) / 2));
    }

    @Override
    public String renderLatex() {
        String str = symbol.renderLatex();
        if(sub != null) str += "_{"+sub.renderLatex()+"}";
        if(sup != null) str += "^{"+sup.renderLatex()+"}";
        return str+"{"+value.renderLatex()+"}";
    }
}
