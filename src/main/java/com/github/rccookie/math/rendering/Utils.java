package com.github.rccookie.math.rendering;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.rccookie.xml.Node;

import org.jetbrains.annotations.Nullable;

final class Utils {

    private Utils() { throw new UnsupportedOperationException(); }

    private static final String[] BLANK = new String[200];
    static {
        BLANK[0] = "";
        for(int i = 1; i<BLANK.length; i++) BLANK[i] = BLANK[i-1] + " ";
    }

    private static final Set<Character> VAR_CHARS;
    static {
        Set<Character> chars = new HashSet<>();
        for(char c='a'; c<='z'; c++) chars.add(c);
        for(char c='A'; c<='Z'; c++) chars.add(c);
        chars.addAll(Set.of('\u03C0', '\u03A0'));
        VAR_CHARS = Set.copyOf(chars);
    }
    private static final Map<Character,Character> SUPERSCRIPT = Map.ofEntries(
            Map.entry('0', '\u2070'),
            Map.entry('1', '\u00B9'),
            Map.entry('2', '\u00B2'),
            Map.entry('3', '\u00B3'),
            Map.entry('4', '\u2074'),
            Map.entry('5', '\u2075'),
            Map.entry('6', '\u2076'),
            Map.entry('7', '\u2077'),
            Map.entry('8', '\u2078'),
            Map.entry('9', '\u2079'),
            Map.entry('+', '\u207A'),
            Map.entry('-', '\u207B'),
            Map.entry('=', '\u207C'),
            Map.entry('(', '\u207D'),
            Map.entry(')', '\u207E')
    );
    private static final Map<Character,Character> SUBSCRIPT = Map.ofEntries(
            Map.entry('0', '\u2080'),
            Map.entry('1', '\u2081'),
            Map.entry('2', '\u2082'),
            Map.entry('3', '\u2083'),
            Map.entry('4', '\u2084'),
            Map.entry('5', '\u2085'),
            Map.entry('6', '\u2086'),
            Map.entry('7', '\u2087'),
            Map.entry('8', '\u2088'),
            Map.entry('9', '\u2089'),
            Map.entry('+', '\u208A'),
            Map.entry('-', '\u208B'),
            Map.entry('=', '\u208C'),
            Map.entry('(', '\u208D'),
            Map.entry(')', '\u208E')
    );

    public static String blank(int length) {
        if(length < BLANK.length) return BLANK[length];
        return " ".repeat(length);
    }

    public static boolean oneNumber(String s) {
        char c = s.charAt(0);
        if(c != '-' && (c < '0' || c > '9')) return false;
        for(int i=1; i<s.length(); i++)
            if((c = s.charAt(i)) < '0' || c > '9') return false;
        return true;
    }

    public static boolean oneVar(String s) {
        return oneVar(s, true);
    }

    public static boolean oneNumOrVar(String s) {
        return oneNumber(s) || oneVar(s);
    }

    public static String encapsulate(String s) {
        if(s.isEmpty()) return "()";
        return oneNumOrVar(s) ? s : "("+s+")";
    }

    private static boolean oneVar(String s, boolean allowMinus) {
        char c = s.charAt(0);
        if(allowMinus && c == '-') return s.length() != 1 && oneVar(s.substring(1), false);
        if(!VAR_CHARS.contains(c)) return false;
        for(int i=1; i<s.length(); i++)
            if(((c = s.charAt(i)) < '0' || c > '9') && !VAR_CHARS.contains(c)) return false;
        return true;
    }


    public static boolean isSuperscript(String s) {
        return !s.isEmpty() && SUPERSCRIPT.containsValue(s.charAt(s.length()-1));
    }

    public static String toSuperscript(String s) {
        char[] chars = s.toCharArray();
        for(int i=0; i<chars.length; i++) {
            Character c = SUPERSCRIPT.get(chars[i]);
            if(c == null) return null;
            chars[i] = c;
        }
        return new String(chars);
    }

    public static boolean isSubscript(String s) {
        return !s.isEmpty() && SUBSCRIPT.containsValue(s.charAt(s.length()-1));
    }

    public static String toSubscript(String s) {
        char[] chars = s.toCharArray();
        for(int i=0; i<chars.length; i++) {
            Character c = SUBSCRIPT.get(chars[i]);
            if(c == null) return null;
            chars[i] = c;
        }
        return new String(chars);
    }


    public static Node emptyNode() {
        return new Node("ms");
    }

    public static Node orEmpty(@Nullable RenderableExpression e, RenderableExpression.RenderOptions options) {
        return e != null ? e.renderMathMLNode(options) : emptyNode();
    }

    public static Node join(Node... nodes) {
        if(nodes.length == 0) return emptyNode();
        if(nodes.length == 1) return nodes[0];
        Node row = new Node("mrow");
        row.children.addAll(Arrays.asList(nodes));
        return row;
    }
}
