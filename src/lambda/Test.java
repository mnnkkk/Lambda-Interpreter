package lambda;

public class Test{

    static String app(String func, String x) {
        return "("+func+x+")";
    }
    static String app(String func, String x, String y) {
        return "(("+ func+x+")"+y+")";
    }
    static String app(String func, String cond, String x, String y) {
        return "(" + func + cond + x + y + ")";
    }
    static String ZERO = "(\\f.\\x.x)";
    static String SUCC = "(\\n.\\f.\\x.f (n f x))";
    static String ONE = app(SUCC, ZERO);
    static String TWO = app(SUCC, ONE);
    static String THREE = app(SUCC, TWO);
    static String FOUR = app(SUCC, THREE);
    static String FIVE = app(SUCC, FOUR);
    static String PLUS = "(\\m.\\n.((m " + SUCC + ") n))";
    static String POW = "(\\b.\\e.e b)";      
    static String PRED = "(\\n.\\f.\\x.n(\\g.\\h.h(g f))(\\u.x)(\\u.u))";
    static String SUB = "(\\m.\\n.n" + PRED + "m)";
    static String TRUE = "(\\x.\\y.x)";
    static String FALSE = "(\\x.\\y.y)";
    static String AND = "(\\p.\\q.p q p)";
    static String OR = "(\\p.\\q.p p q)";
    static String NOT = "(\\p.\\a.\\b.p b a)";
    static String IF = "(\\p.\\a.\\b.p a b)";
    static String ISZERO = "(\\n.n(\\x." + FALSE + ")" + TRUE + ")";
    static String LEQ = "(\\m.\\n." + ISZERO + "(" + SUB + "m n))";
    static String EQ = "(\\m.\\n." + AND + "(" + LEQ + "m n)(" + LEQ + "n m))";
    static String MAX = "(\\m.\\n." + IF + "(" + LEQ + " m n)n m)";
    static String MIN = "(\\m.\\n." + IF + "(" + LEQ + " m n)m n)";
    static String[] sources = {
        ZERO,//0 s
        ONE,//1 s
        TWO,//2 s
        THREE,//3 s
        app(PLUS, ZERO, ONE),//4 s
        app(PLUS, TWO, THREE),//5 s 
        app(POW, TWO, TWO),//6 s
        app(PRED, ONE),//7 s
        app(PRED, TWO),//8 s
        app(SUB, FOUR, TWO),//9 s
        app(AND, TRUE, TRUE),//10 s
        app(AND, TRUE, FALSE),//11 s       
        app(AND, FALSE, FALSE),//12 s
        app(OR, TRUE, TRUE),//13 s
        app(OR, TRUE, FALSE),//14 s
        app(OR, FALSE, FALSE),//15 s
        app(NOT, TRUE),//16 s
        app(NOT, FALSE),//17 s
        app(IF, TRUE, TRUE, FALSE),//18 s
        app(IF, FALSE, TRUE, FALSE),//19 s
        app(IF, app(OR, TRUE, FALSE), ONE, ZERO),//20 s
        app(IF, app(AND, TRUE, FALSE), FOUR, THREE),//21 s
        app(ISZERO, ZERO),//22 s
        app(ISZERO, ONE),//23 s
        //
        app(LEQ, THREE, TWO),//24 s   
        app(LEQ, TWO, THREE),//25 s   
        app(EQ, TWO, FOUR),//26 s
        app(EQ, FIVE, FIVE),//27 f
        app(MAX, ONE, TWO),//28 s    
        app(MAX, FOUR, TWO),//29 s   
        app(MIN, ONE, TWO),//30 s   
        app(MIN, FOUR, TWO),//31 s 
    };

    public static void main(String[] args) {
        for(int i=0;i<=31;i++){
            System.out.println(i);
            String source=sources[i];
            System.out.println(Interpreter.toInterpreter(source));     
        }
    }
}
