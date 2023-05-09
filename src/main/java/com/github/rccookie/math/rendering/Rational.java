package com.github.rccookie.math.rendering;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.github.rccookie.xml.Node;

final class Rational implements RenderableExpression {

    final BigInteger n,d;

    Rational(long value) {
        this(value, 1);
    }

    Rational(long n, long d) {
        this(BigInteger.valueOf(n), BigInteger.valueOf(d));
    }

    Rational(double value) {
        this(new BigDecimal(""+value));
    }

    Rational(BigInteger n, BigInteger d) {
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
    }

    Rational(BigDecimal value) {
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
    }

    @Override
    public String renderInline(RenderOptions options) {
        return null;
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options) {
        return null;
    }

    @Override
    public AsciiArt renderUnicode(RenderOptions options) {
        return null;
    }

    @Override
    public AsciiArt renderAscii(RenderOptions options, CharacterSet charset) {
        return null;
    }

    @Override
    public String renderLatex(RenderOptions options) {
        return null;
    }

    @Override
    public Node renderMathMLNode(RenderOptions options) {
        return null;
    }
}
