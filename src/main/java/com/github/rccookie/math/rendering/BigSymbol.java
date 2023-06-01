package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import org.jetbrains.annotations.Nullable;

import static com.github.rccookie.math.rendering.RenderMode.*;

final class BigSymbol implements RenderableExpression {

    final RenderableExpression symbol;
    @Nullable
    final RenderableExpression sup;
    @Nullable
    final RenderableExpression sub;
    final RenderableExpression value;

    BigSymbol(RenderableExpression symbol, @Nullable RenderableExpression sub, @Nullable RenderableExpression sup, RenderableExpression value) {
        this.symbol = Arguments.checkNull(symbol, "symbol");
        this.sup = sup;
        this.sub = sub;
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return "iter("+symbol+", "+sup+", "+sub+", "+value+")";
    }

    @Override
    public int precedence() {
        return Precedence.ITERATION;
    }

    @Override
    public String renderInline(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        String header;
        if(sup == null) {
            if(sub == null) header = symbol.render(INLINE, options);
            else header = new Subscript(symbol, sub).render(INLINE, options);
        }
        else if(sub == null)
            header = new Superscript(symbol, sup).render(INLINE, options);
        else header = new SuperSubscript(symbol, sup, sub).render(INLINE, options);

        return header + value.render(INLINE, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        AsciiArt symbol = this.symbol.render(ASCII_ART, options);
        AsciiArt art = symbol;
        if(this.sub != null) {
            AsciiArt sub = this.sub.render(ASCII_ART, options.setOutsidePrecedence(Precedence.MIN));
            art = art.draw(sub, new int2((symbol.width() - sub.width()) / 2, art.height()));
        }
        if(this.sup != null) {
            AsciiArt sup = this.sup.render(ASCII_ART, options.setOutsidePrecedence(Precedence.MIN));
            art = art.draw(sup, new int2((symbol.width() - sup.width() + 1) / 2, -sup.height()));
        }
        return art.appendTop(new AsciiArt(" ")).appendCenter(this.value.render(ASCII_ART, options));
    }

    @Override
    public String renderLatex(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        String str = symbol.render(LATEX, options);
        if(sub != null) str += "_{"+sub.render(LATEX, options.setOutsidePrecedence(Precedence.MIN))+"}";
        if(sup != null) str += "^{"+sup.render(LATEX, options.setOutsidePrecedence(Precedence.MIN))+"}";
        return str+"{"+value.render(LATEX, options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        options = options.setOutsidePrecedence(precedence());
        Node underOver = new Node("munderover");
        underOver.children.add(symbol.render(MATH_ML_NODE, options));
        underOver.children.add(Utils.orEmpty(sub, options.setOutsidePrecedence(Precedence.MIN)));
        underOver.children.add(Utils.orEmpty(sup, options.setOutsidePrecedence(Precedence.MIN)));
        return Utils.join(underOver, value.render(MATH_ML_NODE, options));
    }
}
