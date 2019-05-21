package lambda;

public class Interpreter {

    public static String replaceStr(String source,char before,String after){
        StringBuffer b = new StringBuffer();
        for(int i=0;i<source.length();i++){
            if(source.charAt(i)==before)
                b.append(after);
            else
                b.append(source.charAt(i));
        }
        return b.toString();
    }

    public static void cutTwoParaAtom(Node current){
        if(current != null){
            if(current.leftChild!=null&&current.rightChild!=null&&current.leftChild.type==NodeType.ATOM &&current.rightChild.type==NodeType.ATOM){
                current.leftChild=null;
                current.rightChild=null;
            }
            cutTwoParaAtom(current.leftChild);
            cutTwoParaAtom(current.rightChild);
        }
    }

    public static void postOrderSimplize(Node current){
        if(current != null){
            postOrderSimplize(current.leftChild);
            postOrderSimplize(current.rightChild);
            
            if(current.type==NodeType.ABSTRACTION){
                if(current.leftChild!=null
                &&current.rightChild!=null
                &&current.leftChild.type==NodeType.ABSTRACTION){
                    current.bodyOfAbstraction=replaceStr(current.leftChild.bodyOfAbstraction, current.leftChild.parmOfAbstraction,current.rightChild.qcontent);
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                }
                else if(current.leftChild!=null
                &&current.rightChild!=null
                &&current.leftChild.qcontent.charAt(0)=='\\'
                &&current.leftChild.qcontent.length()>3){
                    current.bodyOfAbstraction=replaceStr(current.leftChild.qcontent.substring(3,current.leftChild.qcontent.length())
                                                        ,current.leftChild.qcontent.charAt(1)
                                                        ,current.rightChild.qcontent);
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                }
                else if(current.leftChild!=null
                &&current.rightChild!=null){
                    if(current.leftChild.qcontent.length()!=3||current.leftChild.qcontent.charAt(0)!='\\'){
                        if(current.leftChild.qcontent.length()!=1)
                            current.leftChild.qcontent="("+current.leftChild.qcontent+")";
                        if(current.rightChild.qcontent.length()!=1)
                            current.rightChild.qcontent=" ("+current.rightChild.qcontent+")";
                    }
                    current.bodyOfAbstraction="("+current.leftChild.qcontent+current.rightChild.qcontent+")";
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                }

            }
            
            else if(current.type==NodeType.APPLICATION){
                if(current.leftChild!=null
                &&current.rightChild!=null
                &&current.leftChild.type==NodeType.ABSTRACTION){

                    if(current.rightChild.qcontent.length()!=1&&current.rightChild.qcontent.charAt(0)=='\\')
                        current.rightChild.qcontent="("+current.rightChild.qcontent+")";
                    current.contentOfApplication=replaceStr(current.leftChild.bodyOfAbstraction,current.leftChild.parmOfAbstraction,current.rightChild.qcontent);
                    current.qcontent=current.contentOfApplication;
                }
                else if(current.leftChild!=null
                        &&current.rightChild!=null){
                    if(current.leftChild.qcontent.length()!=1&&current.leftChild.qcontent.charAt(0)=='\\')
                        current.leftChild.qcontent="("+current.leftChild.qcontent+")";
                    if(current.rightChild.qcontent.length()!=1)
                        current.rightChild.qcontent="("+current.rightChild.qcontent+")";
                    current.contentOfApplication=current.leftChild.qcontent+" "+current.rightChild.qcontent;
                    current.qcontent=current.contentOfApplication;
                }
            }
            current.leftChild=null;
            current.rightChild=null;
        }
    }

    public static String toInterpreter(String source){
        String before=".",current=source;
        while(!before.equals(current)){
            current=transInput(current);
            before=current;
            Parser parser = new Parser(current);
            Node head = parser.parse();
            cutTwoParaAtom(head);
            postOrderSimplize(head);
            current=head.toString();
            //if(current.length()>before.length())
                //break;
        }
        return current;
    }

    public static String transInput(String s){
        StringBuffer b = new StringBuffer(); 
        for(int i=0;i<s.length()-1;i++){
            b.append(s.charAt(i));
            if(s.charAt(i)==')'&&s.charAt(i+1)!=')'&&s.charAt(i+1)!=' ')
                b.append(' ');
        }
        b.append(s.charAt(s.length()-1));
        return b.toString();
    }
    public static void main(String[] args) {

        String ZERO="(\\f.\\x.x)";
        String SUCC="(\\n.\\f.\\x.f (n f x))";
        String TRUE="(\\x.\\y.x)";
        String FALSE="(\\x.\\y.y)";
        String AND="(\\p.\\q.p q p)";
        String OR="(\\p.\\q.p p q)";
        String NOT="(\\p.\\a.\\b.p b a)";
        //Here are examples
        //String source = "(\\x.(\\y.y x) z) v";  //succeed
        //String source = "(\\x.x (x y)) m";  //succeed
        //String source = "(\\x.x x) (\\x.x x)";  //succeed
        //String source = "(\\x.x x y) (\\x.x x y)";  //succeed
        //String source="(\\x.\\y.x y) m n";  //succeed
        //String source="(\\x.\\y.\\z.x z(y z)) (\\x.x) m n";  //succeed
        //String source="f ((\\f.\\x.f x) f x)";  //succeed
        //String source="\\x.f ((\\f.\\x.f x) f x)";  //succeed
        String source=NOT+FALSE;
        if(true){
            source=transInput(source);
            System.out.println(toInterpreter(source));
        }
        else{
            Parser parser = new Parser(source);
            Node head = parser.parse();
            cutTwoParaAtom(head);
            postOrderSimplize(head);
            //head=head.rightChild;

            source=head.toString();
            System.out.println(source);
        }
    }
}
