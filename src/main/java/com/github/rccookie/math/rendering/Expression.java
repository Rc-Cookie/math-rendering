package com.github.rccookie.math.rendering;

import java.math.BigDecimal;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.util.Console;

import org.jetbrains.annotations.Nullable;

public interface Expression {

    Bracket DEFAULT_MATRIX_BRACKET = Bracket.SQUARE;



    String renderInline();

    AsciiArt renderAscii();

    AsciiArt renderUnicode();

    String renderLatex();

//    Node renderMathMLNode();
//    default Node renderMathML(boolean inline) {
//        Node math = new Node("math");
//        math.attributes.put("displaystyle", inline+"");
//        math.children.add(renderMathMLNode());
//        return math;
//    }




    static Expression value(long value) {
        return new Literal("" + value);
    }

    static Expression value(double value, boolean forcePlain) {
        return new Literal(forcePlain ? new BigDecimal(""+value).toPlainString() : (""+value));
    }

    static Expression value(String literal) {
        return new Literal(literal);
    }


    static Expression par(Expression inner) {
        return brackets(Bracket.ROUND, inner);
    }

    static Expression brackets(Expression inner) {
        return brackets(Bracket.SQUARE, inner);
    }

    static Expression curly(Expression inner) {
        return brackets(Bracket.CURLY, inner);
    }

    static Expression ceil(Expression inner) {
        return brackets(Bracket.CEIL, inner);
    }

    static Expression floor(Expression inner) {
        return brackets(Bracket.FLOOR, inner);
    }

    static Expression brackets(Bracket type, Expression inner) {
        return new Brackets(type, inner);
    }

    static Expression left(Bracket type, Expression inner) {
        return new BracketLiteral(type, true, inner);
    }

    static Expression right(Bracket type, Expression inner) {
        return new BracketLiteral(type, false, inner);
    }

    static Expression frac(Expression numerator, Expression denominator) {
        return new Fraction(numerator, denominator);
    }

    static Expression row(Expression... elements) {
        return grid(elements);
    }

    static Expression column(Expression... elements) {
        return grid(elements.length, 1, elements);
    }

    static Expression grid(int m, int n, Expression... elementsRowByRow) {
        if(m < 0 || m*n != Arguments.checkNull(elementsRowByRow, "elementsRowByRow").length)
            throw new IllegalArgumentException("Incorrect number of elements for "+m+"+x"+n+" grid");
        Expression[][] elements = new Expression[m][n];
        for(int i=0; i<m; i++)
            System.arraycopy(elementsRowByRow, i*n, elements[i], 0, n);
        return grid(elements);
    }

    static Expression grid(Expression[]... rows) {
        return new Grid(rows);
    }

    static Expression vec(Expression... elements) {
        return vec(DEFAULT_MATRIX_BRACKET, elements);
    }

    static Expression vec(Bracket bracketType, Expression... elements) {
        Expression[][] rows = new Expression[elements.length][1];
        for(int i=0; i<rows.length; i++)
            rows[i][0] = elements[i];
        return matrix(bracketType, rows);
    }

    static Expression rowVec(Expression... elements) {
        return rowVec(DEFAULT_MATRIX_BRACKET, elements);
    }

    static Expression rowVec(Bracket bracketType, Expression... elements) {
        return matrix(bracketType, elements);
    }

    static Expression matrix(int m, int n, Expression... elementsRowByRow) {
        return matrix(DEFAULT_MATRIX_BRACKET, m, n, elementsRowByRow);
    }

    static Expression matrix(Bracket bracketType, int m, int n, Expression... elementsRowByRow) {
        if(m < 0 || m*n != Arguments.checkNull(elementsRowByRow, "elementsRowByRow").length)
            throw new IllegalArgumentException("Incorrect number of elements for "+m+"+x"+n+" matrix");
        Expression[][] elements = new Expression[m][n];
        for(int i=0; i<m; i++)
            System.arraycopy(elementsRowByRow, i*n, elements[i], 0, n);
        return matrix(bracketType, elements);
    }

    static Expression matrix(Expression[]... rows) {
        return matrix(DEFAULT_MATRIX_BRACKET, rows);
    }

    static Expression matrix(Bracket bracketType, Expression[]... rows) {
        return bracketType != null ? brackets(bracketType, grid(rows)) : grid(rows);
    }

    static Expression mid(Expression a, Expression b) {
        if(a instanceof Grid && b instanceof Grid && ((Grid) a).elements.length == ((Grid) b).elements.length)
            return new AugmentedGrid((Grid) a, (Grid) b);
        return new Middle(a,b);
    }

    static Expression augMatrix(Expression a, Expression b) {
        return augMatrix(DEFAULT_MATRIX_BRACKET, a, b);
    }

    static Expression augMatrix(Bracket bracketType, Expression a, Expression b) {
        return bracketType != null ? brackets(bracketType, mid(a,b)) : mid(a,b);
    }

    static Expression set(Expression elementPattern, Expression predicate) {
        return set(mid(elementPattern, predicate));
    }

    static Expression set(Expression inner) {
        return curly(inner);
    }

    static Expression list(Expression... values) {
        return list(",", values);
    }

    static Expression list(String delimiter, Expression... values) {
        return new List(delimiter, values);
    }

    static Expression call(String fName, Expression... params) {
        return call(value(fName), params);
    }

    static Expression call(Expression f, Expression... params) {
        if(params.length != 1)
            return implicit(f, par(list(params)));
        return implicit(f, params[0] instanceof Brackets && ((Brackets) params[0]).type == Bracket.ROUND ? params[0] : par(params[0]));
    }

    static Expression implicit(Expression a, Expression b) {
        return new Concatenation(a,b,true);
    }

    static Expression concat(Expression a, Expression b) {
        return new Concatenation(a,b,false);
    }

    static Expression plus(Expression a, Expression b) {
        return infix("+", a, b);
    }

    static Expression minus(Expression a, Expression b) {
        return infix("-", a, b);
    }

    static Expression mult(Expression a, Expression b) {
        return infix(symbol("\u00B7", "*", "\u00B7", "\\cdot"), a, b);
    }

    static Expression div(Expression a, Expression b) {
        return infix("/", a, b);
    }

    static Expression cross(Expression a, Expression b) {
        return infix(symbol("\u2A2F", "x", "\u2A2F", "\\times"), a, b);
    }

    static Expression eq(Expression a, Expression b) {
        return infix("=", a, b);
    }

    static Expression nEquals(Expression a, Expression b) {
        return infix(symbol("!=", "!=", "\u2260", "\\not="), a, b);
    }

    static Expression approx(Expression a, Expression b) {
        return infix(symbol("\u2248", "~", "\u2248", "\\approx"), a, b);
    }

    static Expression nApprox(Expression a, Expression b) {
        return infix(symbol("\u2249", "!~", "\u2249", "\\not\\approx"), a, b);
    }

    static Expression less(Expression a, Expression b) {
        return infix("<", a, b);
    }

    static Expression nLess(Expression a, Expression b) {
        return infix(symbol("\u226E", "!<", "\u226E", "\\not<"), a, b);
    }

    static Expression leq(Expression a, Expression b) {
        return infix(symbol("<=", "<=", "\u2A7D", "\\leq"), a, b);
    }

    static Expression nLeq(Expression a, Expression b) {
        return infix(symbol("\u2270", "!<=", "\u2270", "\\not\\leq"), a, b);
    }

    static Expression greater(Expression a, Expression b) {
        return infix(">", a, b);
    }

    static Expression nGreater(Expression a, Expression b) {
        return infix(symbol("\u226F", "!>", "\u226F", "\\not>"), a, b);
    }

    static Expression geq(Expression a, Expression b) {
        return infix(symbol(">=", ">=", "\u2A7E", "\\geq"), a, b);
    }

    static Expression nGeq(Expression a, Expression b) {
        return infix(symbol("\u2271", "!<=", "\u2271", "\\not\\geq"), a, b);
    }

    static Expression def(Expression a, Expression b) {
        return infix(" := ", a, b);
    }

    static Expression defRev(Expression a, Expression b) {
        return infix(" =: ", a, b);
    }

    static Expression in(Expression a, Expression b) {
        return infix(symbol(" \u2208 ", new AsciiArt("  __ \n /__ \n \\__ ").setCenter(1), " \u2208 ", "\\in"), a, b);
    }

    static Expression nIn(Expression a, Expression b) {
        return infix(symbol(" \u2209 ", new AsciiArt("  __/\n /_/ \n \\/_ \n /").setCenter(1), " \u2209 ", "\\not\\in"), a, b);
    }

    static Expression contains(Expression a, Expression b) {
        return infix(symbol(" \u220B ", new AsciiArt(" __/\n _/\\ \n /_/\n/").setCenter(1), " \u220B ", "\\ni"), a, b);
    }

    static Expression nContains(Expression a, Expression b) {
        return infix(symbol(" \u220C ", new AsciiArt("  __/\n /_/ \n \\/_ \n /").setCenter(1), " \u220C ", "\\not\\ni"), a, b);
    }

    static Expression and(Expression a, Expression b) {
        return infix(symbol("\u2227", "&", "\u2227", "\\land"), a, b);
    }

    static Expression or(Expression a, Expression b) {
        return infix(symbol("\u2228", " || ", "\u2228", "\\lor"), a, b);
    }

    static Expression infix(Expression symbol, Expression a, Expression b) {
        return new SimpleInfixOperation(a, b, symbol);
    }

    static Expression infix(String symbol, Expression a, Expression b) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return infix(symbol(symbol, symbol, symbol, symbol), a, b);
    }

    static Expression neg(Expression value) {
        return prefix("-", value);
    }

    static Expression not(Expression value) {
        return prefix(symbol("\u00AC", "!", "\u00AC", "\\lnot"), value);
    }

    static Expression prefix(Expression symbol, Expression value) {
        return new SimplePrefixOperation(value, symbol);
    }

    static Expression prefix(String symbol, Expression value) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return prefix(symbol(symbol, symbol, symbol, symbol), value);
    }

    static Expression factorial(Expression value) {
        return postfix("!", value);
    }

    static Expression percent(Expression value) {
        return new SimplePostfixOperation(symbol("%", "%", "%", "\\%"), value);
    }

    static Expression deg(Expression value) {
        return new SimplePostfixOperation(symbol("°", "°", "°", "^\\circ"), value);
    }

    static Expression postfix(Expression symbol, Expression value) {
        return new SimplePostfixOperation(value, symbol);
    }

    static Expression postfix(String symbol, Expression value) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return postfix(symbol(symbol, symbol, symbol, symbol), value);
    }

    static Expression pi() {
        return SpecialValue.PI;
    }

    static Expression Pi() {
        return SpecialValue.CAP_PI;
    }

    static Expression inf() {
        return SpecialValue.INFINITY;
    }

    static Expression arrow(Boolean right, boolean doubleLine) {
        if(right == null) return doubleLine ? SpecialValue.D_LEFT_RIGHT_ARROW : SpecialValue.LEFT_RIGHT_ARROW;
        if(right) return doubleLine ? SpecialValue.D_RIGHT_ARROW : SpecialValue.RIGHT_ARROW;
        return doubleLine ? SpecialValue.D_LEFT_ARROW : SpecialValue.LEFT_ARROW;
    }

    static Expression symbol(String inline, String ascii, String unicode, String latex) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline symbol may not contain newline characters");
        return new SpecialValue(inline, ascii, unicode, latex);
    }

    static Expression symbol(String inline, AsciiArt ascii, String unicode, String latex) {
        return symbol(inline, ascii, new AsciiArt(unicode), latex);
    }

    static Expression symbol(String inline, String ascii, AsciiArt unicode, String latex) {
        return symbol(inline, new AsciiArt(ascii), unicode, latex);
    }

    static Expression symbol(String inline, AsciiArt ascii, AsciiArt unicode, String latex) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline symbol may not contain newline characters");
        return new SpecialValue(inline, ascii, unicode, latex);
    }

    static Expression pow(Expression a, Expression b) {
        return sup(a,b);
    }

    static Expression sup(Expression a, Expression b) {
        if(a instanceof Subscript)
            return new SuperSubscript(((Subscript) a).a, Arguments.checkNull(b, "b"), ((Subscript) a).b);
        return new Superscript(a,b);
    }

    static Expression sub(Expression a, Expression b) {
        if(a instanceof Superscript)
            return new SuperSubscript(((Superscript) a).a, ((Superscript) a).b, Arguments.checkNull(b, "b"));
        return new Subscript(a,b);
    }

    static Expression sqrt(Expression value) {
        return root(value(""), value);
    }

    static Expression cbrt(Expression value) {
        return root(value("3"), value);
    }

    static Expression root(Expression degree, Expression value) {
        return new Root(degree, value);
    }

    static Expression text(String text) {
        return new Text(text);
    }

    static Expression sum() {
        return SpecialValue.SUM;
    }

    static Expression prod() {
        return SpecialValue.PRODUCT;
    }

    static Expression lim() {
        return SpecialValue.LIMES;
    }

    static Expression sum(Expression subscript, Expression value) {
        return sum(subscript, null, value);
    }

    static Expression sum(Expression subscript, @Nullable Expression superscript, Expression value) {
        return big(sum(), subscript, superscript, value);
    }

    static Expression prod(Expression subscript, Expression value) {
        return prod(subscript, null, value);
    }

    static Expression prod(Expression subscript, @Nullable Expression superscript, Expression value) {
        return big(prod(), subscript, superscript, value);
    }

    static Expression lim(String var, Expression target, Expression value) {
        return lim(value(var), target, value);
    }

    static Expression lim(Expression var, Expression target, Expression value) {
        return big(lim(), infix(arrow(true, false), var, target), null, value);
    }

    static Expression big(Expression symbol, @Nullable Expression subscript, @Nullable Expression superscript, Expression value) {
        return new BigSymbol(symbol, subscript, superscript, value);
    }

    static Expression integral(@Nullable Expression lowerBound, @Nullable Expression upperBound, Expression value, String indeterminant) {
        return integral(lowerBound, upperBound, value, value(indeterminant));
    }

    static Expression integral(@Nullable Expression lowerBound, @Nullable Expression upperBound, Expression value, Expression indeterminant) {
        return new Integral(lowerBound, upperBound, concat(value, concat(value(" d"), indeterminant)));
    }


    static void main(String[] args) {
        Expression[] es = {
                def(value("A"), matrix(2, 2, not(par(and(value(1), value(0)))), frac(pi(),value(2)), call("exp", value(2), value(3)), neg(factorial(value(4))))),
                root(value("123456"), augMatrix(grid(3,3,value(1), value(2), value(3), value(4), value(5), value(6), value(7), value(8), value(10)), column(value(10), value(20), value(30)))),
                set(eq(sum(eq(value("k"), value(0)), inf(), frac(value(1), pow(value("q"), value("k")))), frac(value(1), minus(value(1), value("k")))), nIn(value("q"), par(list(value(0), value(1))))),
                integral(value(2), value(5), pow(value("x"), value(2)), "x"),
                eq(lim("x", inf(), frac(value(1), value("x"))), value(0))
        };
        for(Expression e : es) {
            Console.split();
            Console.log(e);
            Console.split("Inline");
            Console.log(e.renderInline());
            Console.split("Unicode");
            Console.log(e.renderUnicode());
            Console.split("Ascii");
            Console.log(e.renderAscii());
            Console.split("Latex");
            Console.log(e.renderLatex());
        }
    }
}
