package com.github.rccookie.math.rendering;

import com.github.rccookie.math.Precedence;

final class Negate extends SimplePrefixOperation {
    Negate(RenderableExpression value) {
        super(value, SpecialLiteral.NEGATE, Precedence.NEGATE);
    }
}
