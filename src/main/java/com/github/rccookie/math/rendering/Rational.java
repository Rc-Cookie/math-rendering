package com.github.rccookie.math.rendering;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.BiFunction;

import com.github.rccookie.xml.Node;

import org.jetbrains.annotations.Nullable;

final class Rational implements RenderableExpression {

    final BigInteger n,d;
    final boolean precise;

    Rational(BigInteger n, BigInteger d, boolean precise) {
        if(d.equals(BigInteger.ZERO))
            throw new ArithmeticException("Division by zero");
        if(n.equals(BigInteger.ZERO)) {
            this.n = BigInteger.ZERO;
            this.d = BigInteger.ONE;
        } else if(n.equals(d))
            this.n = this.d = BigInteger.ONE;
        else {
            BigInteger gcd = n.abs().gcd(d.abs()).multiply(d.compareTo(BigInteger.ZERO) > 0 ? BigInteger.ONE : BigInteger.ONE.negate());
            this.n = n.divide(gcd);
            this.d = d.divide(gcd);
        }
        this.precise = precise;
    }

    Rational(BigDecimal value, boolean precise) {
        BigInteger baseVal = value.unscaledValue();
        BigInteger n,d;
        if(value.scale() == 0) {
            n = baseVal;
            d = BigInteger.ONE;
        }
        else if(value.scale() < 0) {
            n = baseVal.multiply(BigInteger.TEN.pow(-value.scale()));
            d = BigInteger.ONE;
        }
        else {
            n = baseVal;
            d = BigInteger.TEN.pow(value.scale());
        }
//        if(exp > 0) n = n.multiply(BigInteger.TEN.pow(exp));
//        else if(exp < 0) d = d.multiply(BigInteger.TEN.pow(-exp));

        BigInteger gcd = n.abs().gcd(d.abs()).multiply(d.compareTo(BigInteger.ZERO) > 0 ? BigInteger.ONE : BigInteger.ONE.negate());
        this.n = n.divide(gcd);
        this.d = d.divide(gcd);
        this.precise = precise;
    }

    @Override
    public String toString() {
        return "num("+n+", "+d+", "+precise+")";
    }

    private BigDecimal toBigDecimal(RenderOptions options) {
        MathContext context = new MathContext(n.abs().divide(d).toString().length() + options.precision + 3, RoundingMode.HALF_UP);
        return new BigDecimal(n, context).divide(new BigDecimal(d, context), context).setScale(context.getPrecision(), context.getRoundingMode());
    }

    @Override
    public String renderInline(RenderOptions options) {
        return render(RenderableExpression::renderInline, options);
    }

    @Override
    public AsciiArt renderAsciiArt(RenderOptions options) {
        return render(RenderableExpression::renderAsciiArt, options);
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return render(RenderableExpression::renderLatex, options);
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return render(RenderableExpression::renderMathMLNode, options);
    }

    private <T> T render(BiFunction<RenderableExpression, RenderOptions, T> renderer, RenderOptions options) {
        if(options.scientific && scientificNeeded(options))
            return renderScientific(renderer, options);
        options = options.setScientific(false);
        switch(getDecimalMode(options)) {
            case FORCE_DECIMAL: return renderForceDecimal(renderer, options);
            case FORCE_FRACTION: return renderForceFraction(renderer, options);
            default: throw new AssertionError();
        }
    }

    private <T> T renderScientific(BiFunction<RenderableExpression, RenderOptions, T> renderer, RenderOptions options) {
        ScientificNotation s = calculateScientificNotation(options);
        RenderableExpression expr = new Superscript(new NumberLiteral("10"), new NumberLiteral(s.exponent+""));
        if(s.factor != null)
            expr = RenderableExpression.mult(s.factor, expr);
        if(s.negative)
            expr = RenderableExpression.neg(expr);
        return renderer.apply(expr, options.setScientific(false));
    }

    private <T> T renderForceDecimal(BiFunction<RenderableExpression, RenderOptions, T> renderer, RenderOptions options) {
        return renderer.apply(new NumberLiteral(toDecimalString(options)), options);
    }

    private <T> T renderForceFraction(BiFunction<RenderableExpression, RenderOptions, T> renderer, RenderOptions options) {
        if(d.equals(BigInteger.ONE))
            return renderer.apply(new NumberLiteral(n.toString()), options);
        return renderer.apply(new Fraction(new NumberLiteral(n.toString()), new NumberLiteral(d.toString())), options);
    }



    private RenderOptions.DecimalMode getDecimalMode(RenderOptions options) {
        switch(options.decimalMode) {
            case DECIMAL_IF_POSSIBLE: return getDecimalIfPossibleMode(options);
            case SMART: return getSmartDecimalMode(options);
            default: return options.decimalMode;
        }
    }

    private RenderOptions.DecimalMode getDecimalIfPossibleMode(RenderOptions options) {

        if(d.equals(BigInteger.ONE))
            return RenderOptions.DecimalMode.FORCE_FRACTION;
        if(n.signum() < 0)
            return new Rational(n.negate(), d, precise).getDecimalIfPossibleMode(options);

        BigInteger factor = Utils.getFactorToPowerOfTen(d);
        if(factor == null)
            return RenderOptions.DecimalMode.FORCE_FRACTION;

        if(n.compareTo(d) >= 0)
            return Utils.log(BigInteger.TEN, d.multiply(factor)) > options.precision ? RenderOptions.DecimalMode.FORCE_FRACTION : RenderOptions.DecimalMode.FORCE_DECIMAL;

        int len = 0;
        BigInteger nScaled = n.multiply(BigInteger.TEN);
        while(nScaled.compareTo(d) < 0) {
            nScaled = nScaled.multiply(BigInteger.TEN);
            if(++len > options.precision)
                return RenderOptions.DecimalMode.FORCE_FRACTION;
        }
        return len + n.multiply(factor).toString().length() > options.precision ? RenderOptions.DecimalMode.FORCE_FRACTION : RenderOptions.DecimalMode.FORCE_DECIMAL;
    }

    private RenderOptions.DecimalMode getSmartDecimalMode(RenderOptions options) {
        if(!precise || d.compareTo(BigInteger.valueOf(1000)) > 0)
            return RenderOptions.DecimalMode.FORCE_DECIMAL;
        return getDecimalIfPossibleMode(options);
    }

    private static final BigDecimal NO_SCIENTIFIC_RANGE_MIN = new BigDecimal("0.001");
    private static final BigDecimal NO_SCIENTIFIC_RANGE_MAX = new BigDecimal("10000");
    private static final BigInteger NO_SCIENTIFIC_INT_MAX = BigInteger.valueOf(10000000);

    private boolean scientificNeeded(RenderOptions options) {
        if(n.equals(BigInteger.ZERO)) return false;

        if(d.equals(BigInteger.ONE) && n.compareTo(NO_SCIENTIFIC_INT_MAX) < 0) return false;

        BigDecimal value = toBigDecimal(options.setPrecision(10));
        if(value.signum() < 0) value = value.negate();

        return value.compareTo(NO_SCIENTIFIC_RANGE_MIN) <= 0 || value.compareTo(NO_SCIENTIFIC_RANGE_MAX) > 0;
    }

    private ScientificNotation calculateScientificNotation(RenderOptions options) {
        if(n.signum() < 0)
            return new Rational(n.negate(), d, precise).calculateScientificNotation(options).negate();

        // 2/1000 -> 2000/1000 -> 2E-3
        BigInteger n = this.n, d = this.d;
        long e = 0;
        if(n.compareTo(d) < 0) {
            do {
                e--;
                n = n.multiply(BigInteger.TEN);
            } while(n.compareTo(d) < 0);
            if(n.compareTo(d) == 0) return new ScientificNotation(e);
            return new ScientificNotation(new Rational(n, d, precise), e);
        }
        else {
            while(n.compareTo(d) > 0) { // 1000/3 -> 1000/300 -> 10/3E2
                e++;
                d = d.multiply(BigInteger.TEN);
            }
            if(n.compareTo(d) == 0) return new ScientificNotation(e);
            e--;
            n = n.multiply(BigInteger.TEN);
            Rational factor = new Rational(n, d, precise);
            if(factor.n.equals(BigInteger.ONE) && factor.d.equals(BigInteger.ONE))
                return new ScientificNotation(e);
            return new ScientificNotation(factor, e);
        }
    }


    private String toDecimalString(RenderOptions options) {
        if(d.equals(BigInteger.ONE))
            return n.toString();

        if(n.signum() < 0)
            return "-" + new Rational(n.negate(), d, precise).toDecimalString(options);

        BigInteger factor = Utils.getFactorToPowerOfTen(d);
        if(factor != null) return preciseDecimalString(factor, options);

        String str = toBigDecimal(options).setScale(n.divide(d).abs().toString().length() + options.precision, RoundingMode.HALF_UP).toPlainString();
        int dot = str.indexOf('.');
        if(str.length() - dot - 1 <= options.precision) return str;
        return str.substring(0, dot + options.precision + 1);
    }

    private String preciseDecimalString(BigInteger factor, RenderOptions options) {
        if(this.n.compareTo(this.d) < 0) {
            StringBuilder str = new StringBuilder("0.");
            BigInteger n = this.n.multiply(BigInteger.TEN);
            while(n.compareTo(this.d) < 0) {
                str.append('0');
                n = n.multiply(BigInteger.TEN);
            }
            str.append(this.n.multiply(factor));
            if(str.length() <= options.precision + 2)
                return str.toString();
            return str.substring(0, options.precision) + 3;
        }
        // 14/10
        BigInteger n = this.n.multiply(factor), d = this.d.multiply(factor);
        String nStr = n.toString();

        int dot = Utils.log(BigInteger.TEN, d);
        int dotIndex = nStr.length() - dot;
        if(dot <= options.precision)
            return nStr.substring(0, dotIndex) + '.' + nStr.substring(dotIndex);
        return nStr.substring(0, dotIndex) + '.' + nStr.substring(dotIndex, dotIndex + options.precision);
    }

    private static class ScientificNotation {
        public final boolean negative;
        @Nullable
        public final Rational factor;
        public final long exponent;

        private ScientificNotation(long exponent) {
            this(null, exponent);
        }
        private ScientificNotation(@Nullable Rational factor, long exponent) {
            this(false, factor, exponent);
        }
        private ScientificNotation(boolean negative, @Nullable Rational factor, long exponent) {
            this.negative = negative;
            this.factor = factor;
            this.exponent = exponent;
        }

        public ScientificNotation negate() {
            return new ScientificNotation(!negative, factor, exponent);
        }
    }
}
