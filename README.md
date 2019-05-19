# Lambda Interpreter

## What is it?
```
It's an interpreter to simplify lambda expressions.
```

## How does it works?
```
The type of each lambda expression is ATOM, APPLICATION or ABSTRACTION. By analysing
the composition of input, the interpreter generates and entends an AST. Then, it 
applies computing rules to simplize the AST and gets the result.
```

## Version Information
```
v1.0 (May 13, 2019) (Initial commit):This is a preliminary procedure (you can run through the main 
method of Interpreter.java) and contains many flaws. Next I am going to Optimize these codes.

v2.0 (May 19, 2019):Changes below are made to fix some bugs and support expressions with adjacent 
parameters (like "\x.\y...", actually v1.0 cant' deal with them).
    1.I rewrited the "offLR" method. (This method is used to remove the outermost parentheses of a 
      string.
    2.I changed the order of separating APPLICATION. In this version, it's divided from back to front.
    3.I optimized the logic of adding parentheses when simplifying.
    4.I added detection of special expressions，which will grow during simplification.
```
