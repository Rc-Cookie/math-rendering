package com.github.rccookie.math.rendering;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;

public final class CharacterSet {

    private final IntPredicate filter;
    private final Map<Integer, Boolean> cache = new HashMap<>();

    public CharacterSet(IntPredicate filter) {
        this.filter = filter;
    }

    public CharacterSet(Charset charset) {
        this(new IntPredicate() {
            final CharsetEncoder encoder = charset.newEncoder();
            @Override
            public boolean test(int value) {
                return encoder.canEncode(new String(new int[] { value }, 0, 1));
            }
        });
    }

    public boolean canDisplay(int character) {
        return cache.computeIfAbsent(character, filter::test);
    }

    public boolean canDisplay(String str) {
        for(int i=0; i<str.length(); i++)
            if(!canDisplay(str.codePointAt(i)))
                return false;
        return true;
    }

    public boolean canDisplay(AsciiArt a) {
        return canDisplay(a.toString());
    }

    public char orFallback(char maybe, char fallback) {
        return canDisplay(maybe) ? maybe : fallback;
    }

    public int orFallback(int maybe, int fallback) {
        return canDisplay(maybe) ? maybe : fallback;
    }

    public String orFallback(String maybe, String fallback) {
        return canDisplay(maybe) ? maybe : fallback;
    }

    public AsciiArt orFallback(AsciiArt maybe, AsciiArt fallback) {
        return canDisplay(maybe) ? maybe : fallback;
    }
}
