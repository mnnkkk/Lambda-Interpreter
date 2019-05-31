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
v3.0 (May 31, 2019):Great improvement.
    Normal changes:The logic of "extend","replaceStr","postOrderSimplize" methods has been optimized.
    
    Special changes: "transSourceIA" method is added to deal with lambda expressions by α-alternation，
                     which makes one expression's all lambda parametes different from each other.
                     For example, in previous versions, "(\f.\x.f (f x)) (\f.\x.f (f x))" is finally
                     converted into "\x.\x.(x (x x)) ((x (x x)) (x (x x)))". But in this version, the
                     expression is firstly converted into "(\A.\B.A (A B)) (\C.\D.C (C D))" by the
                     "transSourceIA" method, and the "transSourceIA" method works after each single
                     β-statute, thus the final result is "\A.\B.A (A (A (A B)))".
                     
    New file to run:Test.java is added, which also has many examples of lambda expressions, and you
                    can run the main method of it to get the result. 
                   
    Restrictions and solutions related to "transSourceIA" method: The method makes lambda parametes
                    into 'A'to 'Y','a' to 'y','0' to '8', totally 58 characters. So it may cant's 
                    deal with lambda expressions with more than 58 lambda parametes. To deal with 
                    the problem, you can replace char with other data structures with more variants.                       
```
