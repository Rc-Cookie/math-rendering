package com.github.rccookie.math.rendering;

import java.util.Arrays;
import java.util.function.IntFunction;

import com.github.rccookie.geometry.IRect;
import com.github.rccookie.primitive.int2;
import com.github.rccookie.util.Arguments;

import org.jetbrains.annotations.Contract;

import static com.github.rccookie.math.rendering.Utils.blank;

public final class AsciiArt {

    private final String[] lines;
    private final int center;

    public AsciiArt(String value) {
        lines = value.lines().toArray(String[]::new);
        center = lines.length / 2;
        pad();
    }

    public AsciiArt(int height, IntFunction<String> lineGenerator) {
        lines = new String[height];
        center = height / 2;
        for(int i=0; i<lines.length; i++)
            lines[i] = Arguments.checkNull(lineGenerator.apply(i), "lineGenerator.apply("+i+")");
        pad();
    }

    public AsciiArt(String[] lines) {
        this.lines = Arguments.checkNull(lines, "lines").clone();
        center = lines.length / 2;
        pad();
    }

    private AsciiArt(String[] lines, int center) {
        this.lines = lines;
        this.center = center;
    }

    private void pad() {
        if(lines.length == 0) return;
        int w = this.lines[0].length();
        for(int i=1; i<this.lines.length; i++)
            if(this.lines[i].length() > w) w = this.lines[i].length();
        for(int i=0; i<this.lines.length; i++)
            if(this.lines[i].length() != w)
                this.lines[i] = this.lines[i] + blank(w - this.lines[i].length());
    }

    public int2 size() {
        return new int2(width(), height());
    }

    public int width() {
        return lines.length == 0 ? 0 : lines[0].length();
    }

    public int height() {
        return lines.length;
    }

    public int center() {
        return center;
    }

    public String getLine(int index) {
        return lines[index];
    }

    @Override
    public String toString() {
        return String.join("\n", lines);
    }

    public AsciiArt setCenter(int center) {
        return new AsciiArt(lines, Arguments.checkRange(center, 0, lines.length));
    }

    public AsciiArt recalculateCenter() {
        return setCenter(lines.length / 2);
    }

    @Contract(pure = true)
    public AsciiArt appendTop(AsciiArt a) {
        return appendTop(a, true);
    }

    @Contract(pure = true)
    public AsciiArt appendTop(AsciiArt a, boolean keepCenter) {
        String[] lines = new String[Math.max(this.lines.length, a.lines.length)];
        for(int i=0; i<lines.length; i++) {
            String line = i < this.lines.length ? this.lines[i] : blank(width());
            lines[i] = line + (i < a.lines.length ? a.lines[i] : blank(a.width()));
        }
        return new AsciiArt(lines, keepCenter ? center : a.center);
    }

    @Contract(pure = true)
    public AsciiArt appendBottom(AsciiArt a) {
        return appendBottom(a, true);
    }

    @Contract(pure = true)
    public AsciiArt appendBottom(AsciiArt a, boolean keepCenter) {
        String[] lines = new String[Math.max(this.lines.length, a.lines.length)];
        for(int i=0; i<lines.length; i++) {
            String line = i < this.lines.length ? this.lines[this.lines.length - i - 1] : blank(width());
            lines[lines.length - i - 1] = line + (i < a.lines.length ? a.lines[a.lines.length - i - 1] : blank(a.width()));
        }
        return new AsciiArt(lines, keepCenter ? Math.max(0, a.lines.length-lines.length) + center : Math.max(0, lines.length-a.lines.length) + a.center);
    }

    @Contract(pure = true)
    public AsciiArt appendCenter(AsciiArt a) {
        return draw(a, new int2(width(), center - a.center));
    }

    @Contract(pure = true)
    public AsciiArt append(AsciiArt a, RenderableExpression.OperatorAlignment alignment, boolean keepCenter) {
        switch(Arguments.checkNull(alignment, "alignment")) {
            case TOP: return appendTop(a, keepCenter);
            case CENTER: return appendCenter(a);
            case BOTTOM: return appendBottom(a, keepCenter);
            default: throw new AssertionError();
        }
    }

    @Contract(pure = true)
    public AsciiArt draw(AsciiArt a, int2 position) {
        return draw(a, position, true);
    }

    @Contract(pure = true)
    public AsciiArt draw(AsciiArt a, int2 position, boolean keepCenter) {
        int2 min = int2.min(int2.zero, position);
        IRect selfArea = new IRect(min.negated(), min.negated().add(size()));
        IRect aArea = new IRect(position.subed(min), position.subed(min).add(a.size()));

        int w = Math.max(selfArea.max.x, aArea.max.x);
        String[] lines = new String[Math.max(selfArea.max.y, aArea.max.y)];

        for(int i=0; i<lines.length; i++) {
            if(i >= selfArea.min.y && i < selfArea.max.y) {
                String line;
                if(i >= aArea.min.y && i < aArea.max.y) {
                    if(selfArea.min.x == 0) {
                        if(aArea.min.x < selfArea.max.x)
                            line = this.lines[i-selfArea.min.y].substring(0, aArea.min.x);
                        else line = this.lines[i-selfArea.min.y] + blank(aArea.min.x - selfArea.max.x);
                        line += a.lines[i-aArea.min.y];
                        if(w != aArea.max.x)
                            line += this.lines[i-selfArea.min.y].substring(line.length());
                    }
                    else {
                        line = a.lines[i-aArea.min.y];
                        if(w != aArea.max.x) {
                            if(selfArea.min.x < aArea.max.x)
                                line += this.lines[i - selfArea.min.y].substring(aArea.max.x - selfArea.min.x);
                            else line += blank(selfArea.min.x - aArea.max.x) + this.lines[i - selfArea.min.y];
                        }
                    }
                }
                else line = blank(selfArea.min.x) + this.lines[i - selfArea.min.y] + blank(w - selfArea.max.x);
                lines[i] = line;
            }
            else if(i >= aArea.min.y && i < aArea.max.y)
                lines[i] = blank(aArea.min.x) + a.lines[i - aArea.min.y] + blank(w - aArea.max.x);
            else lines[i] = blank(w);
        }

        return new AsciiArt(lines, keepCenter ? Math.max(0, -position.y) + center : Math.max(0, position.y) + a.center);
    }

    public static AsciiArt empty(int2 size) {
        String line = blank(size.x);
        String[] lines = new String[size.y];
        Arrays.fill(lines, line);
        return new AsciiArt(lines, size.y / 2);
    }
}
