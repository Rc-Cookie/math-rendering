package com.github.rccookie.math.rendering;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;

public final class CharacterSet {

    public static final CharacterSet ASCII = new CharacterSet(StandardCharsets.US_ASCII);
    public static final CharacterSet UNICODE = new CharacterSet(StandardCharsets.UTF_8);
    public static final CharacterSet ISO_8859_1 = new CharacterSet(StandardCharsets.ISO_8859_1);
    private static final Set<Integer> MOD_UNICODE_EXCLUDE = Set.of((int) '\u2254', (int) '\u2255', (int) '\u21D4', (int) '\u2194', (int) '\u21D2', (int) '\u2192', (int) '\u21D0', (int) '\u2190');
    public static final CharacterSet MOD_UNICODE = new CharacterSet(UNICODE, c -> !MOD_UNICODE_EXCLUDE.contains(c));

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

    public CharacterSet(CharacterSet base, IntPredicate filter) {
        this(c -> base.canDisplay(c) && filter.test(c));
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



    public static CharacterSet intersection(CharacterSet a, CharacterSet b) {
        return new CharacterSet(c -> a.canDisplay(c) && b.canDisplay(c));
    }
}
