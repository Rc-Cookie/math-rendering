package com.github.rccookie.math.rendering;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import com.github.rccookie.util.Arguments;
import com.github.rccookie.util.Console;
import com.github.rccookie.xml.Node;
import com.github.rccookie.xml.XML;

import org.jetbrains.annotations.Nullable;

public interface RenderableExpression {

    Bracket DEFAULT_MATRIX_BRACKET = Bracket.SQUARE;



    String renderInline(RenderOptions options);

    AsciiArt renderAscii(RenderOptions options);

    AsciiArt renderUnicode(RenderOptions options);

    AsciiArt renderAscii(RenderOptions options, CharacterSet charset);

    String renderLatex(RenderOptions options);

    Node renderMathMLNode(RenderOptions options);

    default Node renderMathML(RenderOptions options, boolean inline) {
        Node math = new Node("math");
        math.attributes.put("displaystyle", !inline+"");
        math.attributes.put("display", inline ? "inline" : "block");
        math.children.add(renderMathMLNode(options));
        return math;
    }




    static RenderableExpression pi() {
        return SpecialLiteral.PI;
    }

    static RenderableExpression Pi() {
        return SpecialLiteral.CAP_PI;
    }

    static RenderableExpression inf() {
        return SpecialLiteral.INFINITY;
    }

    static RenderableExpression sum() {
        return SpecialLiteral.SUM;
    }

    static RenderableExpression prod() {
        return SpecialLiteral.PRODUCT;
    }

    static RenderableExpression lim() {
        return SpecialLiteral.LIMES;
    }

    static RenderableExpression naturals() {
        return SpecialLiteral.NATURAL_NUMS;
    }

    static RenderableExpression integers() {
        return SpecialLiteral.INT_NUMS;
    }

    static RenderableExpression rationals() {
        return SpecialLiteral.RATIONAL_NUMS;
    }

    static RenderableExpression reals() {
        return SpecialLiteral.REAL_NUMS;
    }

    static RenderableExpression complexes() {
        return SpecialLiteral.COMPLEX_NUMS;
    }

    static RenderableExpression plus() {
        return SpecialLiteral.PLUS;
    }

    static RenderableExpression minus() {
        return SpecialLiteral.MINUS;
    }

    static RenderableExpression mult() {
        return SpecialLiteral.MULTIPLY;
    }

    static RenderableExpression div() {
        return SpecialLiteral.DIVIDE;
    }

    static RenderableExpression cross() {
        return SpecialLiteral.CROSS;
    }

    static RenderableExpression eq() {
        return SpecialLiteral.EQUALS;
    }

    static RenderableExpression nEquals() {
        return SpecialLiteral.NOT_EQUALS;
    }

    static RenderableExpression approx() {
        return SpecialLiteral.APPROXIMATELY;
    }

    static RenderableExpression nApprox() {
        return SpecialLiteral.NOT_APPROXIMATELY;
    }

    static RenderableExpression less() {
        return SpecialLiteral.LESS;
    }

    static RenderableExpression nLess() {
        return SpecialLiteral.NOT_LESS;
    }

    static RenderableExpression greater() {
        return SpecialLiteral.GREATER;
    }

    static RenderableExpression nGreater() {
        return SpecialLiteral.NOT_GREATER;
    }

    static RenderableExpression leq() {
        return SpecialLiteral.LESS_OR_EQUAL;
    }

    static RenderableExpression nLeq() {
        return SpecialLiteral.NOT_LESS_OR_EQUAL;
    }

    static RenderableExpression geq() {
        return SpecialLiteral.GREATER_OR_EQUAL;
    }

    static RenderableExpression nGeq() {
        return SpecialLiteral.NOT_GREATER_OR_EQUAL;
    }

    static RenderableExpression def() {
        return SpecialLiteral.DEFINE;
    }

    static RenderableExpression defRev() {
        return SpecialLiteral.DEFINE_REVERSE;
    }

    static RenderableExpression in() {
        return SpecialLiteral.IN;
    }

    static RenderableExpression nIn() {
        return SpecialLiteral.NOT_IN;
    }

    static RenderableExpression contains() {
        return SpecialLiteral.CONTAINS;
    }

    static RenderableExpression nContains() {
        return SpecialLiteral.CONTAINS_NOT;
    }

    static RenderableExpression and() {
        return SpecialLiteral.AND;
    }

    static RenderableExpression or() {
        return SpecialLiteral.OR;
    }

    static RenderableExpression neg() {
        return SpecialLiteral.NEGATE;
    }

    static RenderableExpression not() {
        return SpecialLiteral.NOT;
    }

    static RenderableExpression factorial() {
        return SpecialLiteral.FACTORIAL;
    }

    static RenderableExpression percent() {
        return SpecialLiteral.PERCENT;
    }

    static RenderableExpression deg() {
        return SpecialLiteral.DEGREE;
    }

    static RenderableExpression arrow(Boolean right, boolean doubleLine) {
        if(right == null) return doubleLine ? SpecialLiteral.D_LEFT_RIGHT_ARROW : SpecialLiteral.LEFT_RIGHT_ARROW;
        if(right) return doubleLine ? SpecialLiteral.D_RIGHT_ARROW : SpecialLiteral.RIGHT_ARROW;
        return doubleLine ? SpecialLiteral.D_LEFT_ARROW : SpecialLiteral.LEFT_ARROW;
    }

    // ---------------------------------------------------

    static RenderableExpression num(long value) {
        return num(value, 1);
    }

    static RenderableExpression num(long numerator, long denominator) {
        return num(numerator, denominator, true);
    }

    static RenderableExpression num(long numerator, long denominator, boolean precise) {
        return num(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator), precise);
    }

    static RenderableExpression num(BigInteger value) {
        return num(value, BigInteger.ONE);
    }

    static RenderableExpression num(BigInteger numerator, BigInteger denominator) {
        return num(numerator, denominator, true);
    }

    static RenderableExpression num(BigInteger numerator, BigInteger denominator, boolean precise) {
        return new Rational(numerator, denominator, precise);
    }

    static RenderableExpression num(double value) {
        return num(value, true);
    }

    static RenderableExpression num(double value, boolean precise) {
        return num(new BigDecimal(value+""), precise);
    }

    static RenderableExpression num(BigDecimal value) {
        return num(value, true);
    }

    static RenderableExpression num(BigDecimal value, boolean precise) {
        return new Rational(value, precise);
    }

    static RenderableExpression num(String literal) {
        return new NumberLiteral(literal);
    }

    // ---------------------------------------------------

    static RenderableExpression name(String literal) {
        return new Literal(false, literal);
    }

    static RenderableExpression name(String inline, String ascii, String unicode, String latex, String mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline name may not contain newline characters");
        return new SpecialLiteral(false, inline, ascii, unicode, latex, mathML);
    }

    static RenderableExpression name(String inline, AsciiArt ascii, String unicode, String latex, String mathML) {
        return name(inline, ascii, new AsciiArt(unicode), latex, name(mathML).renderMathMLNode(RenderOptions.DEFAULT));
    }

    static RenderableExpression name(String inline, AsciiArt ascii, AsciiArt unicode, String latex, Node mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline name may not contain newline characters");
        return new SpecialLiteral(inline, ascii, unicode, latex, mathML);
    }

    static RenderableExpression symbol(String literal) {
        return new Literal(true, literal);
    }

    static RenderableExpression symbol(String inline, String ascii, String unicode, String latex, String mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline symbol may not contain newline characters");
        return new SpecialLiteral(true, inline, ascii, unicode, latex, mathML);
    }

    static RenderableExpression symbol(String inline, AsciiArt ascii, String unicode, String latex, String mathML) {
        return symbol(inline, ascii, new AsciiArt(unicode), latex, symbol(mathML).renderMathMLNode(RenderOptions.DEFAULT));
    }

    static RenderableExpression symbol(String inline, AsciiArt ascii, AsciiArt unicode, String latex, Node mathML) {
        if(Arguments.checkNull(inline, "inline").contains("\n"))
            throw new IllegalArgumentException("Inline symbol may not contain newline characters");
        return new SpecialLiteral(inline, ascii, unicode, latex, mathML);
    }

    // ---------------------------------------------------

    static RenderableExpression par(RenderableExpression inner) {
        return brackets(Bracket.ROUND, inner);
    }

    static RenderableExpression brackets(RenderableExpression inner) {
        return brackets(Bracket.SQUARE, inner);
    }

    static RenderableExpression curly(RenderableExpression inner) {
        return brackets(Bracket.CURLY, inner);
    }

    static RenderableExpression ceil(RenderableExpression inner) {
        return brackets(Bracket.CEIL, inner);
    }

    static RenderableExpression floor(RenderableExpression inner) {
        return brackets(Bracket.FLOOR, inner);
    }

    static RenderableExpression brackets(Bracket type, RenderableExpression inner) {
        return new Brackets(type, inner);
    }

    static RenderableExpression left(Bracket type, RenderableExpression inner) {
        return new BracketLiteral(type, true, inner);
    }

    static RenderableExpression right(Bracket type, RenderableExpression inner) {
        return new BracketLiteral(type, false, inner);
    }

    // ---------------------------------------------------

    static RenderableExpression frac(RenderableExpression numerator, RenderableExpression denominator) {
        return new Fraction(numerator, denominator);
    }

    static RenderableExpression row(RenderableExpression... elements) {
        return grid(elements);
    }

    static RenderableExpression column(RenderableExpression... elements) {
        return grid(elements.length, 1, elements);
    }

    static RenderableExpression grid(int m, int n, RenderableExpression... elementsRowByRow) {
        if(m < 0 || m*n != Arguments.checkNull(elementsRowByRow, "elementsRowByRow").length)
            throw new IllegalArgumentException("Incorrect number of elements for "+m+"+x"+n+" grid");
        RenderableExpression[][] elements = new RenderableExpression[m][n];
        for(int i=0; i<m; i++)
            System.arraycopy(elementsRowByRow, i*n, elements[i], 0, n);
        return grid(elements);
    }

    static RenderableExpression grid(RenderableExpression[]... rows) {
        return new Grid(rows);
    }

    static RenderableExpression vec(RenderableExpression... elements) {
        return vec(DEFAULT_MATRIX_BRACKET, elements);
    }

    static RenderableExpression vec(Bracket bracketType, RenderableExpression... elements) {
        RenderableExpression[][] rows = new RenderableExpression[elements.length][1];
        for(int i=0; i<rows.length; i++)
            rows[i][0] = elements[i];
        return matrix(bracketType, rows);
    }

    static RenderableExpression rowVec(RenderableExpression... elements) {
        return rowVec(DEFAULT_MATRIX_BRACKET, elements);
    }

    static RenderableExpression rowVec(Bracket bracketType, RenderableExpression... elements) {
        return matrix(bracketType, elements);
    }

    static RenderableExpression matrix(int m, int n, RenderableExpression... elementsRowByRow) {
        return matrix(DEFAULT_MATRIX_BRACKET, m, n, elementsRowByRow);
    }

    static RenderableExpression matrix(Bracket bracketType, int m, int n, RenderableExpression... elementsRowByRow) {
        if(m < 0 || m*n != Arguments.checkNull(elementsRowByRow, "elementsRowByRow").length)
            throw new IllegalArgumentException("Incorrect number of elements for "+m+"+x"+n+" matrix");
        RenderableExpression[][] elements = new RenderableExpression[m][n];
        for(int i=0; i<m; i++)
            System.arraycopy(elementsRowByRow, i*n, elements[i], 0, n);
        return matrix(bracketType, elements);
    }

    static RenderableExpression matrix(RenderableExpression[]... rows) {
        return matrix(DEFAULT_MATRIX_BRACKET, rows);
    }

    static RenderableExpression matrix(Bracket bracketType, RenderableExpression[]... rows) {
        return bracketType != null ? brackets(bracketType, grid(rows)) : grid(rows);
    }

    static RenderableExpression augMatrix(RenderableExpression a, RenderableExpression b) {
        return augMatrix(DEFAULT_MATRIX_BRACKET, a, b);
    }

    static RenderableExpression augMatrix(Bracket bracketType, RenderableExpression a, RenderableExpression b) {
        return bracketType != null ? brackets(bracketType, mid(a,b)) : mid(a,b);
    }

    // ---------------------------------------------------

    static RenderableExpression mid(RenderableExpression a, RenderableExpression b) {
        if(a instanceof Grid && b instanceof Grid && ((Grid) a).elements.length == ((Grid) b).elements.length)
            return new AugmentedGrid((Grid) a, (Grid) b);
        return new Middle(a,b);
    }

    static RenderableExpression set(RenderableExpression elementPattern, RenderableExpression predicate) {
        return set(mid(elementPattern, predicate));
    }

    static RenderableExpression set(RenderableExpression inner) {
        return curly(inner);
    }

    static RenderableExpression list(RenderableExpression... values) {
        return customList(symbol(","), values);
    }

    static RenderableExpression customList(RenderableExpression delimiter, RenderableExpression... values) {
        return new List(delimiter, values);
    }

    // ---------------------------------------------------

    static RenderableExpression call(String fName, RenderableExpression... params) {
        return call(name(fName), params);
    }

    static RenderableExpression call(RenderableExpression f, RenderableExpression... params) {
        if(params.length != 1)
            return implicit(f, par(list(params)));
        return implicit(f, params[0] instanceof Brackets && ((Brackets) params[0]).type == Bracket.ROUND ? params[0] : par(params[0]));
    }

    static RenderableExpression implicit(RenderableExpression a, RenderableExpression b) {
        return new Concatenation(a,b,true);
    }

    static RenderableExpression concat(RenderableExpression a, RenderableExpression b) {
        return new Concatenation(a,b,false);
    }

    // ---------------------------------------------------

    static RenderableExpression plus(RenderableExpression a, RenderableExpression b) {
        return infix(plus(), a, b);
    }

    static RenderableExpression minus(RenderableExpression a, RenderableExpression b) {
        return infix(minus(), a, b);
    }

    static RenderableExpression mult(RenderableExpression a, RenderableExpression b) {
        return infix(mult(), a, b);
    }

    static RenderableExpression div(RenderableExpression a, RenderableExpression b) {
        return infix(div(), a, b);
    }

    static RenderableExpression cross(RenderableExpression a, RenderableExpression b) {
        return infix(cross(), a, b);
    }

    static RenderableExpression eq(RenderableExpression a, RenderableExpression b) {
        return infix(eq(), a, b);
    }

    static RenderableExpression nEquals(RenderableExpression a, RenderableExpression b) {
        return infix(nEquals(), a, b);
    }

    static RenderableExpression approx(RenderableExpression a, RenderableExpression b) {
        return infix(approx(), a, b);
    }

    static RenderableExpression nApprox(RenderableExpression a, RenderableExpression b) {
        return infix(nApprox(), a, b);
    }

    static RenderableExpression less(RenderableExpression a, RenderableExpression b) {
        return infix(less(), a, b);
    }

    static RenderableExpression nLess(RenderableExpression a, RenderableExpression b) {
        return infix(nLess(), a, b);
    }

    static RenderableExpression leq(RenderableExpression a, RenderableExpression b) {
        return infix(leq(), a, b);
    }

    static RenderableExpression nLeq(RenderableExpression a, RenderableExpression b) {
        return infix(nLeq(), a, b);
    }

    static RenderableExpression greater(RenderableExpression a, RenderableExpression b) {
        return infix(greater(), a, b);
    }

    static RenderableExpression nGreater(RenderableExpression a, RenderableExpression b) {
        return infix(nGreater(), a, b);
    }

    static RenderableExpression geq(RenderableExpression a, RenderableExpression b) {
        return infix(geq(), a, b);
    }

    static RenderableExpression nGeq(RenderableExpression a, RenderableExpression b) {
        return infix(nGeq(), a, b);
    }

    static RenderableExpression def(RenderableExpression a, RenderableExpression b) {
        return infix(def(), a, b);
    }

    static RenderableExpression defRev(RenderableExpression a, RenderableExpression b) {
        return infix(defRev(), a, b);
    }

    static RenderableExpression in(RenderableExpression a, RenderableExpression b) {
        return infix(in(), a, b);
    }

    static RenderableExpression nIn(RenderableExpression a, RenderableExpression b) {
        return infix(nIn(), a, b);
    }

    static RenderableExpression contains(RenderableExpression a, RenderableExpression b) {
        return infix(contains(), a, b);
    }

    static RenderableExpression nContains(RenderableExpression a, RenderableExpression b) {
        return infix(nContains(), a, b);
    }

    static RenderableExpression and(RenderableExpression a, RenderableExpression b) {
        return infix(and(), a, b);
    }

    static RenderableExpression or(RenderableExpression a, RenderableExpression b) {
        return infix(or(), a, b);
    }

    static RenderableExpression infix(RenderableExpression symbol, RenderableExpression a, RenderableExpression b) {
        return new SimpleInfixOperation(a, b, symbol);
    }

    static RenderableExpression infix(String symbol, RenderableExpression a, RenderableExpression b) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return infix(symbol(symbol), a, b);
    }

    static RenderableExpression neg(RenderableExpression value) {
        return prefix(neg(), value);
    }

    static RenderableExpression not(RenderableExpression value) {
        return prefix(not(), value);
    }

    static RenderableExpression prefix(RenderableExpression symbol, RenderableExpression value) {
        return new SimplePrefixOperation(value, symbol);
    }

    static RenderableExpression prefix(String symbol, RenderableExpression value) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return prefix(symbol(symbol), value);
    }

    static RenderableExpression factorial(RenderableExpression value) {
        return postfix(factorial(), value);
    }

    static RenderableExpression percent(RenderableExpression value) {
        return new SimplePostfixOperation(percent(), value);
    }

    static RenderableExpression deg(RenderableExpression value) {
        return new SimplePostfixOperation(deg(), value);
    }

    static RenderableExpression postfix(RenderableExpression symbol, RenderableExpression value) {
        return new SimplePostfixOperation(value, symbol);
    }

    static RenderableExpression postfix(String symbol, RenderableExpression value) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return postfix(symbol(symbol), value);
    }

    // ---------------------------------------------------

    static RenderableExpression pow(RenderableExpression a, RenderableExpression b) {
        return sup(a,b);
    }

    static RenderableExpression sup(RenderableExpression a, RenderableExpression b) {
        if(a instanceof Subscript)
            return new SuperSubscript(((Subscript) a).a, Arguments.checkNull(b, "b"), ((Subscript) a).b);
        return new Superscript(a,b);
    }

    static RenderableExpression sub(RenderableExpression a, RenderableExpression b) {
        if(a instanceof Superscript)
            return new SuperSubscript(((Superscript) a).a, ((Superscript) a).b, Arguments.checkNull(b, "b"));
        return new Subscript(a,b);
    }

    static RenderableExpression sqrt(RenderableExpression value) {
        return root(num(""), value);
    }

    static RenderableExpression cbrt(RenderableExpression value) {
        return root(num("3"), value);
    }

    static RenderableExpression root(RenderableExpression degree, RenderableExpression value) {
        return new Root(degree, value);
    }

    static RenderableExpression text(String text) {
        return new Text(text);
    }

    static RenderableExpression sum(RenderableExpression subscript, RenderableExpression value) {
        return sum(subscript, null, value);
    }

    static RenderableExpression sum(RenderableExpression subscript, @Nullable RenderableExpression superscript, RenderableExpression value) {
        return big(sum(), subscript, superscript, value);
    }

    static RenderableExpression prod(RenderableExpression subscript, RenderableExpression value) {
        return prod(subscript, null, value);
    }

    static RenderableExpression prod(RenderableExpression subscript, @Nullable RenderableExpression superscript, RenderableExpression value) {
        return big(prod(), subscript, superscript, value);
    }

    static RenderableExpression lim(String var, RenderableExpression target, RenderableExpression value) {
        return lim(name(var), target, value);
    }

    static RenderableExpression lim(RenderableExpression var, RenderableExpression target, RenderableExpression value) {
        return big(lim(), infix(arrow(true, false), var, target), null, value);
    }

    static RenderableExpression big(RenderableExpression symbol, @Nullable RenderableExpression subscript, @Nullable RenderableExpression superscript, RenderableExpression value) {
        return new BigSymbol(symbol, subscript, superscript, value);
    }

    static RenderableExpression integral(@Nullable RenderableExpression lowerBound, @Nullable RenderableExpression upperBound, RenderableExpression value, String indeterminant) {
        return integral(lowerBound, upperBound, value, name(indeterminant));
    }

    static RenderableExpression integral(@Nullable RenderableExpression lowerBound, @Nullable RenderableExpression upperBound, RenderableExpression value, RenderableExpression indeterminant) {
        return new Integral(lowerBound, upperBound, concat(value, concat(num(" d"), indeterminant)));
    }


    static void main(String[] args) {
        RenderableExpression[] es = {
                def(name("A"), matrix(2, 2, not(par(and(num(1), num(0)))), frac(pi(), num(2)), call("exp", num(2), num(3)), neg(factorial(num(4))))),
                root(num(123456), augMatrix(grid(3,3, num(1), num(2), num(3), num(4), num(5), num(6), num(7), num(8), num(10)), column(num(10), num(20), num(30)))),
                set(eq(sum(eq(name("k"), num(0)), inf(), frac(num(1), pow(name("q"), name("k")))), frac(num(1), minus(num(1), name("k")))), nIn(name("q"), par(list(num(0), num(1))))),
                integral(num(2), num(5), pow(name("x"), num(2)), "x"),
                eq(lim("x", inf(), frac(num(1), name("x"))), num(0)),
                plus(num(123456789.123456789), num(2,10)),
        };
        RenderOptions options = RenderOptions.DEFAULT;
        for(RenderableExpression e : es) {
            Console.split();
            Console.log(e);
            Console.split("Inline");
            Console.log(e.renderInline(options));
            Console.split("Unicode");
            Console.log(e.renderUnicode(options));
            Console.split("Ascii");
            Console.log(e.renderAscii(options));
            Console.split("Latex");
            Console.log(e.renderLatex(options));
            Console.split("MathML");
            System.out.println(e.renderMathML(options, false).toHTML(XML.HTML & ~XML.FORMATTED));
        }
    }



    final class RenderOptions {

        public static RenderOptions DEFAULT = new RenderOptions(50, DecimalMode.SMART, true);

        public final int precision;
        public final DecimalMode decimalMode;
        public final boolean scientific;

        public RenderOptions(int precision, DecimalMode decimalMode, boolean scientific) {
            this.precision = precision;
            this.decimalMode = Arguments.checkNull(decimalMode, "decimalMode");
            this.scientific = scientific;
        }

        @Override
        public String toString() {
            return "RenderOptions{" +
                    "precision=" + precision +
                    ", decimalMode=" + decimalMode +
                    ", scientific=" + scientific +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            RenderOptions that = (RenderOptions) o;
            return precision == that.precision && scientific == that.scientific && decimalMode == that.decimalMode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(precision, decimalMode, scientific);
        }

        public RenderOptions setPrecision(int precision) {
            return new RenderOptions(precision, decimalMode, scientific);
        }

        public RenderOptions setDecimalMode(DecimalMode decimalMode) {
            return new RenderOptions(precision, decimalMode, scientific);
        }

        public RenderOptions setScientific(boolean scientific) {
            return new RenderOptions(precision, decimalMode, scientific);
        }

        public enum DecimalMode {
            FORCE_DECIMAL,
            FORCE_FRACTION,
            DECIMAL_IF_POSSIBLE,
            SMART
        }
    }
}
