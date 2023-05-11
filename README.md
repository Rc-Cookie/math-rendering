# Math rendering

A small library for rendering expressions into different formats. Currently supported are:

 - Inline unicode string
 - Ascii art
 - Unicode ascii art
 - Latex source code
 - MathML

Additionally, options to render to these formats is planned:

 - Rendering as an image

The library does not support parsing math expressions from LaTeX or similar, it is mainly intended for displaying math which is already given in "program format".

### Example output

`def(name("A"), matrix(2, 2, not(par(and(num(1), num(0)))), frac(pi(),num(2)), call("exp", num(2), num(3)), neg(factorial(num(4)))))`

Unicode ascii art:
```
     ⎡          π ⎤
     ⎢ ¬(1∧0)   - ⎥
A := ⎢          2 ⎥
     ⎣exp(2,3) -4!⎦
```

`set(eq(sum(eq(name("k"), num(0)), inf(), frac(num(1), pow(name("q"), num("k")))), frac(num(1), minus(num(1), name("k")))), nIn(name("q"), par(list(num(0), num(1)))))`

Ascii art:
```
/ oo            |             \
| __   1     1  |    __/      |
< \  ---- = --- |   /_/       >
| /_   k    1-k | q \/_ (0,1) |
\ k=0 q         |   /         /
```
