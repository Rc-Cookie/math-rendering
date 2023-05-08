package com.github.rccookie.math.rendering;

import com.github.rccookie.util.Arguments;

final class Literal implements Expression {

    final String value;

    Literal(String value) {
        this.value = Arguments.checkNull(value, "value");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String renderInline() {
        return value.replace('\n', ' ');
    }

    @Override
    public AsciiArt renderAscii() {
        return new AsciiArt(value);
    }

    @Override
    public AsciiArt renderUnicode() {
        return new AsciiArt(value);
    }

    @Override
    public String renderLatex() {
        return value;
    }
}
