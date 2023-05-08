# Math rendering

A small library for rendering expressions into different formats. Currently supported are:

 - Inline unicode string
 - Ascii art
 - Unicode ascii art
 - Latex source code

Additionally, options to render to these formats is planned:

 - MathML
 - Rendering as an image

The library does not support parsing math expressions from LaTeX or similar, it is mainly intended for displaying math which is already given in "program format".

### Example output

`def(value("A"), matrix(2, 2, not(par(and(value(1), value(0)))), frac(pi(),value(2)), call("exp", value(2), value(3)), neg(factorial(value(4)))))`

Unicode ascii art:
```
     ⎡          π ⎤
     ⎢ ¬(1∧0)   - ⎥
A := ⎢          2 ⎥
     ⎣exp(2,3) -4!⎦
```

`set(eq(sum(eq(value("k"), value(0)), inf(), frac(value(1), pow(value("q"), value("k")))), frac(value(1), minus(value(1), value("k")))), nIn(value("q"), par(list(value(0), value(1)))))`

Ascii art:
```
/ oo            |             \
| __   1     1  |    __/      |
< \  ---- = --- |   /_/       >
| /_   k    1-k | q \/_ (0,1) |
\ k=0 q         |   /         /
```
