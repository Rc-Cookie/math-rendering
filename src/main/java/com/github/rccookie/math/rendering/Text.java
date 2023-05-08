package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class Text implements Expression {

    private final String text;

    Text(String text) {
        this.text = Arguments.checkNull(text, "text");
    }

    @Override
    public String toString() {
        return "text("+text+")";
    }

    @Override
    public String renderInline() {
        return text.replace('\n', ' ');
    }

    @Override
    public AsciiArt renderAscii() {
        return new AsciiArt(text);
    }

    @Override
    public AsciiArt renderUnicode() {
        return new AsciiArt(text);
    }

    @Override
    public AsciiArt renderAscii(CharacterSet charset) {
        return new AsciiArt(text);
    }

    @Override
    public String renderLatex() {
        return "\\text{"+ text +"}";
    }
}
