package com.github.rccookie.math.rendering;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import com.github.rccookie.math.Precedence;
import com.github.rccookie.util.Arguments;
import com.github.rccookie.xml.Node;

import org.jetbrains.annotations.Nullable;

public interface RenderableExpression {

    Bracket DEFAULT_MATRIX_BRACKET = Bracket.SQUARE;



    int precedence();

    String renderInline(RenderOptions options);

    AsciiArt renderAsciiArt(RenderOptions options);

    String renderLatex(RenderOptions options);

    Node renderMathMLNode(RenderOptions options);

    @SuppressWarnings("SpellCheckingInspection")
    default Node renderMathML(RenderOptions options, boolean inline) {
        Node math = new Node("math");
        math.attributes.put("displaystyle", !inline+"");
        math.attributes.put("display", inline ? "inline" : "block");
        math.children.add(renderMathMLNode(options));
        return math;
    }

    default <T> T render(RenderMode<T> mode, RenderOptions options) {
        if(options.autoParenthesis && options.outsidePrecedence > precedence())
            return par(this).render(mode, options);
        return mode.render(this, options);
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

    static RenderableExpression mod() {
        return SpecialLiteral.MODULO;
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

    static RenderableExpression abs(RenderableExpression inner) {
        return brackets(Bracket.ABS, inner);
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

    static RenderableExpression matrixBrackets(RenderableExpression inner) {
        return new MatrixBrackets(inner);
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
        return matrixBrackets(grid(m, n, elementsRowByRow));
    }

    static RenderableExpression matrix(Bracket bracketType, int m, int n, RenderableExpression... elementsRowByRow) {
        RenderableExpression grid = grid(m, n, elementsRowByRow);
        return bracketType != null ? brackets(bracketType, grid) : grid;
    }

    static RenderableExpression matrix(RenderableExpression[]... rows) {
        return matrixBrackets(grid(rows));
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

    static RenderableExpression tuple(RenderableExpression... values) {
        return par(list(values));
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
        return b instanceof Negate ? minus(a, ((Negate) b).value) : infix(plus(), a, b, Precedence.PLUS, true);
    }

    static RenderableExpression minus(RenderableExpression a, RenderableExpression b) {
        return infix(minus(), a, b, Precedence.PLUS, false);
    }

    static RenderableExpression mult(RenderableExpression a, RenderableExpression b) {
        return infix(mult(), a, b, Precedence.MULTIPLY, true);
    }

    static RenderableExpression div(RenderableExpression a, RenderableExpression b) {
        return infix(div(), a, b, Precedence.DIVIDE, false);
    }

    static RenderableExpression cross(RenderableExpression a, RenderableExpression b) {
        return infix(cross(), a, b, Precedence.MULTIPLY, true);
    }

    static RenderableExpression mod(RenderableExpression a, RenderableExpression b) {
        return infix(mod(), a, b, Precedence.MODULO, false);
    }

    static RenderableExpression eq(RenderableExpression a, RenderableExpression b) {
        return infix(eq(), a, b, Precedence.EQUALS, true);
    }

    static RenderableExpression nEquals(RenderableExpression a, RenderableExpression b) {
        return infix(nEquals(), a, b, Precedence.EQUALS, true);
    }

    static RenderableExpression approx(RenderableExpression a, RenderableExpression b) {
        return infix(approx(), a, b, Precedence.EQUALS, true);
    }

    static RenderableExpression nApprox(RenderableExpression a, RenderableExpression b) {
        return infix(nApprox(), a, b, Precedence.EQUALS, true);
    }

    static RenderableExpression less(RenderableExpression a, RenderableExpression b) {
        return infix(less(), a, b, Precedence.LESS, true);
    }

    static RenderableExpression nLess(RenderableExpression a, RenderableExpression b) {
        return infix(nLess(), a, b, Precedence.LESS, true);
    }

    static RenderableExpression leq(RenderableExpression a, RenderableExpression b) {
        return infix(leq(), a, b, Precedence.LESS_OR_EQUAL, true);
    }

    static RenderableExpression nLeq(RenderableExpression a, RenderableExpression b) {
        return infix(nLeq(), a, b, Precedence.LESS_OR_EQUAL, true);
    }

    static RenderableExpression greater(RenderableExpression a, RenderableExpression b) {
        return infix(greater(), a, b, Precedence.GREATER, true);
    }

    static RenderableExpression nGreater(RenderableExpression a, RenderableExpression b) {
        return infix(nGreater(), a, b, Precedence.GREATER, true);
    }

    static RenderableExpression geq(RenderableExpression a, RenderableExpression b) {
        return infix(geq(), a, b, Precedence.GREATER_OR_EQUAL, true);
    }

    static RenderableExpression nGeq(RenderableExpression a, RenderableExpression b) {
        return infix(nGeq(), a, b, Precedence.GREATER_OR_EQUAL, true);
    }

    static RenderableExpression def(RenderableExpression a, RenderableExpression b) {
        return infix(def(), a, b, Precedence.DEFINE, true);
    }

    static RenderableExpression defRev(RenderableExpression a, RenderableExpression b) {
        return infix(defRev(), a, b, Precedence.DEFINE, true);
    }

    static RenderableExpression in(RenderableExpression a, RenderableExpression b) {
        return infix(in(), a, b, Precedence.IN, false);
    }

    static RenderableExpression nIn(RenderableExpression a, RenderableExpression b) {
        return infix(nIn(), a, b, Precedence.IN, false);
    }

    static RenderableExpression contains(RenderableExpression a, RenderableExpression b) {
        return infix(contains(), a, b, Precedence.IN, false);
    }

    static RenderableExpression nContains(RenderableExpression a, RenderableExpression b) {
        return infix(nContains(), a, b, Precedence.IN, false);
    }

    static RenderableExpression and(RenderableExpression a, RenderableExpression b) {
        return infix(and(), a, b, Precedence.AND, true);
    }

    static RenderableExpression or(RenderableExpression a, RenderableExpression b) {
        return infix(or(), a, b, Precedence.OR, true);
    }

    static RenderableExpression infix(RenderableExpression symbol, RenderableExpression a, RenderableExpression b, int precedence, boolean associative) {
        return new SimpleInfixOperation(a, b, symbol, precedence, associative);
    }

    static RenderableExpression infix(String symbol, RenderableExpression a, RenderableExpression b, int precedence, boolean associative) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return infix(symbol(symbol), a, b, precedence, associative);
    }

    static RenderableExpression neg(RenderableExpression value) {
        return new Negate(value);
    }

    static RenderableExpression not(RenderableExpression value) {
        return prefix(not(), value, Precedence.NOT);
    }

    static RenderableExpression prefix(RenderableExpression symbol, RenderableExpression value, int precedence) {
        return new SimplePrefixOperation(value, symbol, precedence);
    }

    static RenderableExpression prefix(String symbol, RenderableExpression value, int precedence) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return prefix(symbol(symbol), value, precedence);
    }

    static RenderableExpression factorial(RenderableExpression value) {
        return postfix(factorial(), value, Precedence.FACTORIAL);
    }

    static RenderableExpression percent(RenderableExpression value) {
        return new SimplePostfixOperation(percent(), value, Precedence.PERCENT);
    }

    static RenderableExpression deg(RenderableExpression value) {
        return new SimplePostfixOperation(deg(), value, Precedence.DEGREE);
    }

    static RenderableExpression postfix(RenderableExpression symbol, RenderableExpression value, int precedence) {
        return new SimplePostfixOperation(value, symbol, precedence);
    }

    static RenderableExpression postfix(String symbol, RenderableExpression value, int precedence) {
        if(Arguments.checkNull(symbol, "symbol").contains("\n"))
            throw new IllegalArgumentException("Symbol may not contain newline characters");
        return postfix(symbol(symbol), value, precedence);
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

    static RenderableExpression exp(RenderableExpression value) {
        return new Exp(value);
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
        return big(lim(), infix(arrow(true, false), var, target, Precedence.LAMBDA, false), null, value);
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


    /**
     * Options to adjust the rendering result. The same option may have a different impact in different rendering modes,
     * and some options may be ignored by some modes completely.
     */
    final class RenderOptions {

        /**
         * Default rendering options.
         */
        public static final RenderOptions DEFAULT = new RenderOptions(40, DecimalMode.SMART, true, 4, Bracket.SQUARE, CharacterSet.UNICODE, true, Precedence.MIN, SpaceMode.AUTO);

        /**
         * Maximum output precision of floating point numbers. Does not work if the number has
         * been created by string.
         */
        public final int precision;
        /**
         * Determines how non-integer numbers are displayed. Does not work if the number
         * has been created by string
         */
        public final DecimalMode decimalMode;
        /**
         * Whether to allow to display big or small numbers in scientific notation. Does not
         * work if the number has been created by string.
         */
        public final boolean scientific;
        /**
         * If both numerator and denominator have at most this many characters and don't need parenthesis,
         * fractions will be displayed inline instead of as fraction. Ascii art rendering only.
         */
        public final int smallFractionsSizeLimit;
        /**
         * The default bracket style to use for matrices and vectors, if not specified explicitly.
         */
        public final Bracket matrixBrackets;
        /**
         * The allowed output charset. If unicode characters are unsupported, they will be displayed with
         * ascii art instead. Ascii art and inline mode only.
         */
        public final CharacterSet charset;
        /**
         * Whether to automatically add necessary parenthesis to keep the semantic meaning of the input,
         * for example a*(b+c) instead of a*b+c, if the input was <code>mult(a,plus(b,c))</code>.
         */
        public final boolean autoParenthesis;
        /**
         * The precedence of the operator that the rendered expression is an operand of.
         */
        public final int outsidePrecedence;
        /**
         * Determines how many and how big spaces to use.
         */
        public final SpaceMode spaceMode;

        /**
         * Creates a new render options object.
         */
        public RenderOptions(int precision, DecimalMode decimalMode, boolean scientific, int smallFractionsSizeLimit, Bracket matrixBrackets, CharacterSet charset, boolean autoParenthesis, int outsidePrecedence, SpaceMode spaceMode) {
            this.precision = Arguments.checkRange(precision, 1, null);
            this.decimalMode = Arguments.checkNull(decimalMode, "decimalMode");
            this.scientific = scientific;
            this.smallFractionsSizeLimit = Arguments.checkRange(smallFractionsSizeLimit, 0, null);
            this.matrixBrackets = Arguments.checkNull(matrixBrackets, "matrixBrackets");
            this.charset = Arguments.checkNull(charset, "charset");
            this.autoParenthesis = autoParenthesis;
            this.outsidePrecedence = outsidePrecedence;
            this.spaceMode = Arguments.checkNull(spaceMode, "spaceMode");
        }

        @Override
        public String toString() {
            return "RenderOptions{" +
                    "precision=" + precision +
                    ", decimalMode=" + decimalMode +
                    ", scientific=" + scientific +
                    ", smallFractionsSizeLimit=" + smallFractionsSizeLimit +
                    ", matrixBrackets=" + matrixBrackets +
                    ", charset=" + charset +
                    ", autoParenthesis=" + autoParenthesis +
                    ", outsidePrecedence=" + outsidePrecedence +
                    ", spaceMode=" + spaceMode +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            RenderOptions that = (RenderOptions) o;
            return precision == that.precision
                    && scientific == that.scientific
                    && decimalMode == that.decimalMode
                    && smallFractionsSizeLimit == that.smallFractionsSizeLimit
                    && matrixBrackets == that.matrixBrackets
                    && autoParenthesis == that.autoParenthesis
                    && outsidePrecedence == that.outsidePrecedence
                    && spaceMode == that.spaceMode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setPrecision(int precision) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setDecimalMode(DecimalMode decimalMode) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setScientific(boolean scientific) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setSmallFractionsSizeLimit(int smallFractionsSizeLimit) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setMatrixBrackets(Bracket matrixBrackets) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setCharset(CharacterSet charset) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setAutoParenthesis(boolean autoParenthesis) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setOutsidePrecedence(int outsidePrecedence) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        public RenderOptions setSpaceMode(SpaceMode spaceMode) {
            return new RenderOptions(precision, decimalMode, scientific, smallFractionsSizeLimit, matrixBrackets, charset, autoParenthesis, outsidePrecedence, spaceMode);
        }

        /**
         * Different output modes for non-integer numbers.
         */
        public enum DecimalMode {
            /**
             * Force rendering as floating point number, even if that can only approximate it.
             */
            FORCE_DECIMAL,
            /**
             * Force rendering as fraction. This will never lose precision, but the fraction may
             * become very big.
             */
            FORCE_FRACTION,
            /**
             * Render as floating point number, if the value can be represented in a finite number of
             * decimal places. Otherwise, render as fraction.
             */
            DECIMAL_IF_POSSIBLE,
            /**
             * Choose automatically an appropriate rendering mode, based on whether the number can be
             * displayed as decimal, the size of the fraction and the precision of the input number.
             */
            SMART
        }

        /**
         * Different modes for adding spaces.
         */
        public enum SpaceMode {
            /**
             * Omit spaces where possible.
             */
            COMPACT,
            /**
             * Omit spaces if the operands are visually small, add spaces if the operands are visually bigger,
             * and generally where it "looks better".
             */
            AUTO,
            /**
             * Always pad operators with spaces.
             */
            FORCE
        }
    }


//    static void main(String[] args) {
//        RenderableExpression a = name("a"), b = name("b");
////        RenderableExpression e = frac(plus(a,b), minus(a,b));
////        RenderableExpression e = tuple(implicit(num(2),a),b,num(2));
//        RenderableExpression e = num(123456.789);
//        Console.log(e.render(RenderMode.ASCII_ART, RenderOptions.DEFAULT.setSpaceMode(RenderOptions.SpaceMode.AUTO) ));
//    }
}
