package com.github.rccookie.math.rendering;

import com.github.rccookie.xml.Node;

public interface RenderMode<T> {

    RenderMode<String> INLINE = RenderableExpression::renderInline;
    RenderMode<AsciiArt> ASCII_ART = RenderableExpression::renderAsciiArt;
    RenderMode<String> LATEX = RenderableExpression::renderLatex;
    RenderMode<Node> MATH_ML_NODE = RenderableExpression::renderMathMLNode;
    RenderMode<Node> MATH_ML = (e,o) -> {
        Node math = new Node("math");
        math.attributes.put("displaystyle", "true");
        math.attributes.put("display", "block");
        math.children.add(e.render(MATH_ML_NODE, o));
        return math;
    };
    RenderMode<Node> MATH_ML_INLINE = (e,o) -> {
        Node math = new Node("math");
        math.attributes.put("displaystyle", "false");
        math.attributes.put("display", "inline");
        math.children.add(e.render(MATH_ML_NODE, o));
        return math;
    };

    T render(RenderableExpression e, RenderableExpression.RenderOptions options);
}
