package com.github.rccookie.math.rendering;

import java.math.BigDecimal;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.util.Console;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.XML;

import org.jetbrains.annotations.Nullable;

public interface Expression {

    Bracket DEFAULT_MATRIX_BRACKET = Bracket.SQUARE;



    String renderInline();

    AsciiArt renderAscii();

    AsciiArt renderUnicode();

    AsciiArt renderAscii(CharacterSet charset);

    String renderLatex();

    Node renderMathMLNode();

    default Node renderMathML(boolean inline) {
        Node math = new Node("math");
        math.attributes.put("displaystyle", !inline+"");
        math.attributes.put("display", inline ? "inline" : "block");
        math.children.add(renderMathMLNode());
        return math;
    }




    static Expression pi() {
        return SpecialLiteral.PI;
    }

    static Expression Pi() {
        return SpecialLiteral.CAP_PI;
    }

    static Expression inf() {
        return SpecialLiteral.INFINITY;
    }

    static Expression sum() {
        return SpecialLiteral.SUM;
    }

    static Expression prod() {
        return SpecialLiteral.PRODUCT;
    }

    static Expression lim() {
        return SpecialLiteral.LIMES;
    }

    static Expression naturals() {
        return SpecialLiteral.NATURAL_NUMS;
    }

    static Expression integers() {
        return SpecialLiteral.INT_NUMS;
    }

    static Expression rationals() {
        return SpecialLiteral.RATIONAL_NUMS;
    }

    static Expression reals() {
        return SpecialLiteral.REAL_NUMS;
    }

    static Expression complexes() {
        return SpecialLiteral.COMPLEX_NUMS;
    }

    static Expression plus() {
        return SpecialLiteral.PLUS;
    }

    static Expression minus() {
        return SpecialLiteral.MINUS;
    }

    static Expression mult() {
        return SpecialLiteral.MULTIPLY;
    }

    static Expression div() {
        return SpecialLiteral.DIVIDE;
    }

    static Expression cross() {
        return SpecialLiteral.CROSS;
    }

    static Expression eq() {
        return SpecialLiteral.EQUALS;
    }

    static Expression nEquals() {
        return SpecialLiteral.NOT_EQUALS;
    }

    static Expression approx() {
        return SpecialLiteral.APPROXIMATELY;
    }

    static Expression nApprox() {
        return SpecialLiteral.NOT_APPROXIMATELY;
    }

    static Expression less() {
        return SpecialLiteral.LESS;
    }

    static Expression nLess() {
        return SpecialLiteral.NOT_LESS;
    }

    static Expression greater() {
        return SpecialLiteral.GREATER;
    }

    static Expression nGreater() {
        return SpecialLiteral.NOT_GREATER;
    }

    static Expression leq() {
        return SpecialLiteral.LESS_OR_EQUAL;
    }

    static Expression nLeq() {
        return SpecialLiteral.NOT_LESS_OR_EQUAL;
    }

    static Expression geq() {
        return SpecialLiteral.GREATER_OR_EQUAL;
    }

    static Expression nGeq() {
        return SpecialLiteral.NOT_GREATER_OR_EQUAL;
    }

    static Expression def() {
        return SpecialLiteral.DEFINE;
    }

    static Expression defRev() {
        return SpecialLiteral.DEFINE_REVERSE;
    }

    static Expression in() {
        return SpecialLiteral.IN;
    }

    static Expression nIn() {
        return SpecialLiteral.NOT_IN;
    }

    static Expression contains() {
        return SpecialLiteral.CONTAINS;
    }

    static Expression nContains() {
        return SpecialLiteral.CONTAINS_NOT;
    }

    static Expression and() {
        return SpecialLiteral.AND;
    }

    static Expression or() {
        return SpecialLiteral.OR;
    }

    static Expression neg() {
        return SpecialLiteral.NEGATE;
    }

    static Expression not() {
        return SpecialLiteral.NOT;
    }

    static Expression factorial() {
        return SpecialLiteral.FACTORIAL;
    }

    static Expression percent() {
        return SpecialLiteral.PERCENT;
    }

    static Expression deg() {
        return SpecialLiteral.DEGREE;
    }

    static Expression arrow(Boolean right, boolean doubleLine) {
        if(right == null) return doubleLine ? SpecialLiteral.D_LEFT_RIGHT_ARROW : SpecialLiteral.LEFT_RIGHT_ARROW;
        if(right) return doubleLine ? SpecialLiteral.D_RIGHT_ARROW : SpecialLiteral.RIGHT_ARROW;
        return doubleLine ? SpecialLiteral.D_LEFT_ARROW : SpecialLiteral.LEFT_ARROW;
    }

    // ---------------------------------------------------

    static Expression num(long value) {
        return new Number("" + value);
    }

    static Expression num(double value, boolean forcePlain) {
        return new Number(forcePlain ? new BigDecimal(""+value).toPlainString() : (""+value));
    }

    static Expression num(String literal) {
        return new Number(literal);
    }

    static Expression name(String literal) {
        return new Literal(false, literal);
    }

    static Expression name(String inline, String ascii, String unicode, String latex, String mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline name may not contain newline characters");
        return new SpecialLiteral(false, inline, ascii, unicode, latex, mathML);
    }

    static Expression name(String inline, AsciiArt ascii, String unicode, String latex, String mathML) {
        return name(inline, ascii, new AsciiArt(unicode), latex, name(mathML).renderMathMLNode());
    }

    static Expression name(String inline, AsciiArt ascii, AsciiArt unicode, String latex, Node mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline name may not contain newline characters");
        return new SpecialLiteral(inline, ascii, unicode, latex, mathML);
    }

    static Expression symbol(String literal) {
        return new Literal(true, literal);
    }

    static Expression symbol(String inline, String ascii, String unicode, String latex, String mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline symbol may not contain newline characters");
        return new SpecialLiteral(true, inline, ascii, unicode, latex, mathML);
    }

    static Expression symbol(String inline, AsciiArt ascii, String unicode, String latex, String mathML) {
        return symbol(inline, ascii, new AsciiArt(unicode), latex, symbol(mathML).renderMathMLNode());
    }

    static Expression symbol(String inline, AsciiArt ascii, AsciiArt unicode, String latex, Node mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline symbol may not contain newline characters");
        return new SpecialLiteral(inline, ascii, unicode, latex, mathML);
    }

    // ---------------------------------------------------

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

    // ---------------------------------------------------

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

    static Expression augMatrix(Expression a, Expression b) {
        return augMatrix(DEFAULT_MATRIX_BRACKET, a, b);
    }

    static Expression augMatrix(Bracket bracketType, Expression a, Expression b) {
        return bracketType != null ? brackets(bracketType, mid(a,b)) : mid(a,b);
    }

    // ---------------------------------------------------

    static Expression mid(Expression a, Expression b) {
        if(a instanceof Grid && b instanceof Grid && ((Grid) a).elements.length == ((Grid) b).elements.length)
            return new AugmentedGrid((Grid) a, (Grid) b);
        return new Middle(a,b);
    }

    static Expression set(Expression elementPattern, Expression predicate) {
        return set(mid(elementPattern, predicate));
    }

    static Expression set(Expression inner) {
        return curly(inner);
    }

    static Expression list(Expression... values) {
        return customList(num(","), values);
    }

    static Expression customList(Expression delimiter, Expression... values) {
        return new List(delimiter, values);
    }

    // ---------------------------------------------------

    static Expression call(String fName, Expression... params) {
        return call(num(fName), params);
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

    // ---------------------------------------------------

    static Expression plus(Expression a, Expression b) {
        return infix(plus(), a, b);
    }

    static Expression minus(Expression a, Expression b) {
        return infix(minus(), a, b);
    }

    static Expression mult(Expression a, Expression b) {
        return infix(mult(), a, b);
    }

    static Expression div(Expression a, Expression b) {
        return infix(div(), a, b);
    }

    static Expression cross(Expression a, Expression b) {
        return infix(cross(), a, b);
    }

    static Expression eq(Expression a, Expression b) {
        return infix(eq(), a, b);
    }

    static Expression nEquals(Expression a, Expression b) {
        return infix(nEquals(), a, b);
    }

    static Expression approx(Expression a, Expression b) {
        return infix(approx(), a, b);
    }

    static Expression nApprox(Expression a, Expression b) {
        return infix(nApprox(), a, b);
    }

    static Expression less(Expression a, Expression b) {
        return infix(less(), a, b);
    }

    static Expression nLess(Expression a, Expression b) {
        return infix(nLess(), a, b);
    }

    static Expression leq(Expression a, Expression b) {
        return infix(leq(), a, b);
    }

    static Expression nLeq(Expression a, Expression b) {
        return infix(nLeq(), a, b);
    }

    static Expression greater(Expression a, Expression b) {
        return infix(greater(), a, b);
    }

    static Expression nGreater(Expression a, Expression b) {
        return infix(nGreater(), a, b);
    }

    static Expression geq(Expression a, Expression b) {
        return infix(geq(), a, b);
    }

    static Expression nGeq(Expression a, Expression b) {
        return infix(nGeq(), a, b);
    }

    static Expression def(Expression a, Expression b) {
        return infix(def(), a, b);
    }

    static Expression defRev(Expression a, Expression b) {
        return infix(defRev(), a, b);
    }

    static Expression in(Expression a, Expression b) {
        return infix(in(), a, b);
    }

    static Expression nIn(Expression a, Expression b) {
        return infix(nIn(), a, b);
    }

    static Expression contains(Expression a, Expression b) {
        return infix(contains(), a, b);
    }

    static Expression nContains(Expression a, Expression b) {
        return infix(nContains(), a, b);
    }

    static Expression and(Expression a, Expression b) {
        return infix(and(), a, b);
    }

    static Expression or(Expression a, Expression b) {
        return infix(or(), a, b);
    }

    static Expression infix(Expression symbol, Expression a, Expression b) {
        return new SimpleInfixOperation(a, b, symbol);
    }

    static Expression infix(String symbol, Expression a, Expression b) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return infix(symbol(symbol), a, b);
    }

    static Expression neg(Expression value) {
        return prefix(neg(), value);
    }

    static Expression not(Expression value) {
        return prefix(not(), value);
    }

    static Expression prefix(Expression symbol, Expression value) {
        return new SimplePrefixOperation(value, symbol);
    }

    static Expression prefix(String symbol, Expression value) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return prefix(symbol(symbol), value);
    }

    static Expression factorial(Expression value) {
        return postfix(factorial(), value);
    }

    static Expression percent(Expression value) {
        return new SimplePostfixOperation(percent(), value);
    }

    static Expression deg(Expression value) {
        return new SimplePostfixOperation(deg(), value);
    }

    static Expression postfix(Expression symbol, Expression value) {
        return new SimplePostfixOperation(value, symbol);
    }

    static Expression postfix(String symbol, Expression value) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return postfix(symbol(symbol), value);
    }

    // ---------------------------------------------------

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
        return root(num(""), value);
    }

    static Expression cbrt(Expression value) {
        return root(num("3"), value);
    }

    static Expression root(Expression degree, Expression value) {
        return new Root(degree, value);
    }

    static Expression text(String text) {
        return new Text(text);
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
        return lim(num(var), target, value);
    }

    static Expression lim(Expression var, Expression target, Expression value) {
        return big(lim(), infix(arrow(true, false), var, target), null, value);
    }

    static Expression big(Expression symbol, @Nullable Expression subscript, @Nullable Expression superscript, Expression value) {
        return new BigSymbol(symbol, subscript, superscript, value);
    }

    static Expression integral(@Nullable Expression lowerBound, @Nullable Expression upperBound, Expression value, String indeterminant) {
        return integral(lowerBound, upperBound, value, num(indeterminant));
    }

    static Expression integral(@Nullable Expression lowerBound, @Nullable Expression upperBound, Expression value, Expression indeterminant) {
        return new Integral(lowerBound, upperBound, concat(value, concat(num(" d"), indeterminant)));
    }


    static void main(String[] args) {
        Expression[] es = {
                def(num("A"), matrix(2, 2, not(par(and(num(1), num(0)))), frac(pi(), num(2)), call("exp", num(2), num(3)), neg(factorial(num(4))))),
                root(num("123456"), augMatrix(grid(3,3, num(1), num(2), num(3), num(4), num(5), num(6), num(7), num(8), num(10)), column(num(10), num(20), num(30)))),
                set(eq(sum(eq(num("k"), num(0)), inf(), frac(num(1), pow(num("q"), num("k")))), frac(num(1), minus(num(1), num("k")))), nIn(num("q"), par(list(num(0), num(1))))),
                integral(num(2), num(5), pow(num("x"), num(2)), "x"),
                eq(lim("x", inf(), frac(num(1), num("x"))), num(0))
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
            Console.split("MathML");
            System.out.println(e.renderMathML(false).toHTML(XML.HTML & ~XML.FORMATTED));
        }
    }
}
