package com.github.rccookie.math.rendering;

import java.util.function.Function;

import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import org.jetbrains.annotations.Nullable;

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
    public String renderInline(RenderOptions options) {
        String header;
        if(sup == null) {
            if(sub == null) header = symbol.renderInline(options);
            else header = new Subscript(symbol, sub).renderInline(options);
        }
        else if(sub == null)
            header = new Superscript(symbol, sup).renderInline(options);
        else header = new SuperSubscript(symbol, sup, sub).renderInline(options);

        return header + Utils.encapsulate(value.renderInline(options));
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return renderArt(renderableExpression -> renderableExpression.renderAscii(options));
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        return renderArt(renderableExpression -> renderableExpression.renderUnicode(options));
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        return renderArt(e -> e.renderAscii(options, charset));
    }

    private AsciiArt renderArt(Function<RenderableExpression, AsciiArt> renderer) {
        AsciiArt symbol = renderer.apply(this.symbol);
        AsciiArt art = symbol;
        if(this.sub != null) {
            AsciiArt sub = renderer.apply(this.sub);
            art = art.draw(sub, new int2((symbol.width() - sub.width()) / 2, art.height()));
        }
        if(this.sup != null) {
            AsciiArt sup = renderer.apply(this.sup);
            art = art.draw(sup, new int2((symbol.width() - sup.width() + 1) / 2, -sup.height()));
        }
        AsciiArt value = renderer.apply(this.value);
        return art.appendTop(new AsciiArt(" ")).appendCenter(value);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        String str = symbol.renderLatex(options);
        if(sub != null) str += "_{"+sub.renderLatex(options)+"}";
        if(sup != null) str += "^{"+sup.renderLatex(options)+"}";
        return str+"{"+value.renderLatex(options)+"}";
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node underOver = new Node("munderover");
        underOver.children.add(symbol.renderMathMLNode(options));
        underOver.children.add(Utils.orEmpty(sub, options));
        underOver.children.add(Utils.orEmpty(sup, options));
        return Utils.join(underOver, value.renderMathMLNode(options));
    }
}
