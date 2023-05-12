package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.Text;

final class BracketLiteral implements RenderableExpression {

    static final String[] LEFT_SYMBOLS_ASCII = { "(", "[", "{", "<", "[", "|_", "|" };
    static final String[] RIGHT_SYMBOLS_ASCII = { ")", "]", "}", ">", "]", "_|", "|" };
    static final String[] LEFT_SYMBOLS_UNICODE = { "(", "[", "{", "\u27E8", "\u2308", "\u230A", "\u2502" };
    static final String[] RIGHT_SYMBOLS_UNICODE = { ")", "]", "}", "\u27E9", "\u2309", "\u230B", "\u2502" };
    static final String[] LEFT_LATEX = { "(", "[", "\\{", "<", "\\lceil", "\\lfloor", "\\vert" };
    static final String[] RIGHT_LATEX = { ")", "]", "\\}", ">", "\\rceil", "\\rfloor", "\\vert" };

    final Bracket type;
    final boolean left;
    final RenderableExpression inner;

    BracketLiteral(Bracket type, boolean left, RenderableExpression inner) {
        this.type = Arguments.checkNull(type, "type");
        this.left = left;
        this.inner = Arguments.checkNull(inner, "inner");
    }

    @Override
    public String toString() {
        return (left ? "left" : "right")+"("+type+", "+inner+")";
    }

    @Override
    public String renderInline(RenderOptions options) {
        return (left ? LEFT_SYMBOLS_ASCII : RIGHT_SYMBOLS_ASCII)[type.ordinal()] + inner.renderInline(options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        AsciiArt inner = this.inner.renderAsciiArt(options);
        AsciiArt bracket = renderBracketUnicode(type, left, inner.height());
        if(!options.charset.canDisplay(inner))
            bracket = renderBracketAscii(type, left, inner.height());
        return bracket.appendTop(inner);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        if(left)
            return "\\left" + LEFT_LATEX[type.ordinal()] + inner.renderLatex(options) + "\\right.";
        return "\\left." + inner.renderLatex(options) + "\\right" + RIGHT_LATEX[type.ordinal()];
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        Node o = new Node("mo");
        o.children.add(new Text((left ? LEFT_SYMBOLS_UNICODE : RIGHT_SYMBOLS_UNICODE)[type.ordinal()]));
        o.attributes.put("fence", "true");
        o.attributes.put("stretchy", "true");
        return Utils.join(o, inner.renderMathMLNode(options));
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    static AsciiArt renderBracketUnicode(Bracket type, boolean left, int height) {
        if(height <= 1)
            return new AsciiArt((left ? LEFT_SYMBOLS_UNICODE : RIGHT_SYMBOLS_UNICODE)[type.ordinal()]);
        switch(type) {
            case ROUND:
                return new AsciiArt(height, i -> {
                    if(i == 0) return left ? "\u239B" : "\u239E";
                    if(i == height-1) return left ? "\u239D" : "\u23A0";
                    return left ? "\u239C" : "\u239F";
                });
            case SQUARE:
                return new AsciiArt(height, i -> {
                    if(i == 0) return left ? "\u23A1" : "\u23A4";
                    if(i == height-1) return left ? "\u23A3" : "\u23A6";
                    return left ? "\u23A2" : "\u23A5";
                });
            case CURLY:
                if(height == 2) return left ? new AsciiArt("\u23B0\n\u23B1") : new AsciiArt("\u23B1\n\u23B0");
                return new AsciiArt(height, i -> {
                    if(i == 0) return left ? "\u23A7" : "\u23AB";
                    if(i == height-1) return left ? "\u23A9" : "\u23AD";
                    if((height&1)==1 && i==height/2) return left ? "\u23A8" : "\u23AC";
                    if((height&1)==0 && i == height/2-1) return left ? "\u23AD" : "\u23A9";
                    if((height&1)==0 && i == height/2) return left ? "\u23AB" : "\u23A7";
                    return "\u23AA";
                });
            case ANGLE:
                return new AsciiArt(height, i -> {
                    if((height&1)==1 && i == height / 2)
                        return left ? "\u27E8" : (" ".repeat(height/2) + "\u27E9");
                    if(left)
                        return " ".repeat(height/2 - Math.min(i, height-i-1)) + (i<height/2 ? "/" : "\\");
                    else return " ".repeat(Math.min(i, height-i-1)) + (i < height/2 ? "\\" : "/");
                });
            case CEIL:
                return new AsciiArt(height, i -> {
                    if(i == 0) return left ? "\u23A1" : "\u23A4";
                    return left ? "\u23A2" : "\u23A5";
                });
            case FLOOR:
                return new AsciiArt(height, i -> {
                    if(i == height-1) return left ? "\u23A3" : "\u23A6";
                    return left ? "\u23A2" : "\u23A5";
                });
            case ABS:
                return new AsciiArt(height, i -> (left ? LEFT_SYMBOLS_UNICODE : RIGHT_SYMBOLS_UNICODE)[Bracket.ABS.ordinal()]);
            default: throw new NullPointerException();
        }
    }

    static AsciiArt renderBracketAscii(Bracket type, boolean left, int height) {
        if(height <= 1 && type != Bracket.CEIL)
            return new AsciiArt((left ? LEFT_SYMBOLS_ASCII : RIGHT_SYMBOLS_ASCII)[type.ordinal()]);
        switch(type) {
            case ROUND:
                return new AsciiArt(height, i -> {
                    if(i == 0) return left ? "/" : "\\";
                    if(i == height-1) return left ? "\\" : "/";
                    return "|";
                });
            case SQUARE:
                return new AsciiArt(height+1, i -> {
                    if(i == 0) return left ? " _" : "_";
                    if(i == height) return left ? "|_" : "_|";
                    return left ? "|" : " |";
                });
            case CURLY:
                if(height == 2) return left ? new AsciiArt("/\n\\") : new AsciiArt("\\\n/");
                return new AsciiArt(height, i -> {
                    if(i == 0) return left ? "/ " : " \\";
                    if(i == height-1) return left ? "\\" : " /";
                    if((height&1)==1 && i==height/2) return left ? "<" : " >";
                    if((height&1)==0 && i == height/2-1) return left ? "/" : " \\";
                    if((height&1)==0 && i == height/2) return left ? "\\" : " /";
                    return left ? "|" : " |";
                });
            case ANGLE:
                return new AsciiArt(height, i -> {
                    if((height&1)==1 && i == height / 2)
                        return left ? "<" : (" ".repeat(height/2) + ">");
                    if(left)
                        return " ".repeat(height/2 - Math.min(i, height-i-1)) + (i<height/2 ? "/" : "\\");
                    else return " ".repeat(Math.min(i, height-i-1)) + (i < height/2 ? "\\" : "/");
                });
            case CEIL:
                return new AsciiArt(height+1, i -> {
                    if(i == 0) return left ? " _" : "_";
                    return left ? "|" : " |";
                });
            case FLOOR:
                return new AsciiArt(height, i -> {
                    if(i == height-1) return left ? "|_" : "_|";
                    return left ? "|" : " |";
                });
            case ABS:
                return new AsciiArt(height, i -> "|");
            default: throw new NullPointerException();
        }
    }
}
