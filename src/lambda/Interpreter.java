package lambda;

public class Interpreter {
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
                    current.bodyOfAbstraction=current.leftChild.bodyOfAbstraction.replaceAll(String.valueOf(current.leftChild.parmOfAbstraction),current.rightChild.qcontent);
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                }
                else if(current.leftChild!=null
                        &&current.rightChild!=null){
                    if(current.leftChild.qcontent.length()!=1)
                        current.leftChild.qcontent="("+current.leftChild.qcontent+")";
                    if(current.rightChild.qcontent.length()!=1)
                        current.rightChild.qcontent=" ("+current.rightChild.qcontent+")";
                    current.bodyOfAbstraction=current.leftChild.qcontent+current.rightChild.qcontent;
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                }

            }
            
            else if(current.type==NodeType.APPLICATION){
                if(current.leftChild!=null
                        &&current.rightChild!=null
                        &&current.leftChild.type==NodeType.ABSTRACTION){
                    if(current.rightChild.qcontent.contains("."))
                        current.rightChild.qcontent='\\'+current.rightChild.qcontent;
                    if(current.rightChild.qcontent.length()!=1&&current.rightChild.qcontent.charAt(0)=='\\')
                        current.rightChild.qcontent='('+current.rightChild.qcontent+')';
                    current.contentOfApplication=current.leftChild.bodyOfAbstraction.replaceAll(String.valueOf(current.leftChild.parmOfAbstraction),current.rightChild.qcontent);
                    current.qcontent=current.contentOfApplication;
                }
                else if(current.leftChild!=null
                        &&current.rightChild!=null){
                    if(current.leftChild.qcontent.length()!=1&&current.leftChild.qcontent.charAt(0)=='\\')
                        current.leftChild.qcontent="("+current.leftChild.qcontent+")";
                    if(current.rightChild.qcontent.length()!=1&&current.rightChild.qcontent.charAt(0)=='\\')
                        current.rightChild.qcontent=" ("+current.rightChild.qcontent+")";
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
            before=current;
            Parser parser = new Parser(current);
            Node head = parser.parse();
            cutTwoParaAtom(head);
            postOrderSimplize(head);
            current=head.toString();
            if(current.length()>before.length())
                break;
        }
        return current;
    }

    public static void main(String[] args) {
        /*
        *input law(codes to ignore this will be made if time permiting)
        *") (" instead of ")("
        */

        //Here are examples
        //String source = "(\\x.(\\y.y x) z) v";  //success
        //String source = "(\\x.x (x y)) m";  //success
        //String source = "(\\x.x x) (\\x.x x)";  //success
        //String source = "(\\x.x x y) (\\x.x x y)";  //ok (this will be longer and longer)
        //String source="(\\x.\\y.x y) m n";  //success
        String source="(\\x.\\y.\\z.x z(y z)) (\\x.x) m n";  //success

        System.out.println(toInterpreter(source));
    }
}
