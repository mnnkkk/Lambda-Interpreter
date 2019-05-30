# Lambda Interpreter

## What is it?
```
It's an interpreter to simplify lambda expressions.
```

## How does it works?
```
The type of each lambda expression is ATOM, APPLICATION or ABSTRACTION. By analysing the composition
of input, the interpreter generates and entends an AST. Then, it applies computing rules to simplize
the AST and gets the result.
```

## Version Information
```
v1.0 (May 13, 2019) (Initial commit):A procedure to deal with lambda expressions, you can run it 
through the main method of Interpreter.java.
```
```
v2.0 (May 19, 2019):Changes below are made to fix some bugs and support expressions with adjacent 
parameters (like "\x.\y...", actually v1.0 can't deal with them).
    1.I rewrited the "offLR" method. (This method is used to remove the outermost parentheses of a 
      string.
    2.I changed the order to separate APPLICATION. In this version, it's divided from back to front.
    3.I optimized the logic of adding parentheses when simplifying.
    4.I added detection to deal with special expressions，which will grow during simplification.
```
```
v2.1 (May 22, 2019):Changes have been made to divide ABSTRACTION. Also, some details are improved.
```
```
v2.2 (May 22, 2019):Changes below are made to fix some bugs.
    1.The logic of "replaceStr" method has been optimized.
    2.The way to add space during reduction has been improved.
    3. Some codes have been simplified.
```
```
v3.0 (May 30, 2019):A great improvement, can deal with at least 59 lambdas!
```
