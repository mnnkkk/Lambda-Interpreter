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
            //
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
                        current.leftChild.qcontent=" ("+current.leftChild.qcontent+")";
                    if(current.rightChild.qcontent.length()!=1)
                        current.rightChild.qcontent=" ("+current.rightChild.qcontent+")";
                    current.bodyOfAbstraction=current.leftChild.qcontent+current.rightChild.qcontent;
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;

                    //current.bodyOfAbstraction="("+current.leftChild.qcontent+") ("+current.rightChild.qcontent+")";
                    //current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                }
                current.leftChild=null;
                current.rightChild=null;
            }
            //
            else if(current.type==NodeType.APPLICATION){
                if(current.leftChild!=null
                        &&current.rightChild!=null
                        &&current.leftChild.type==NodeType.ABSTRACTION){
                    if(current.rightChild.qcontent.contains("."))
                        current.rightChild.qcontent='\\'+current.rightChild.qcontent;
                    if(current.rightChild.qcontent.length()!=1)
                        current.rightChild.qcontent='('+current.rightChild.qcontent+')';
                    current.contentOfApplication=current.leftChild.bodyOfAbstraction.replaceAll(String.valueOf(current.leftChild.parmOfAbstraction),current.rightChild.qcontent);
                    current.qcontent=current.contentOfApplication;
                }
                else if(current.leftChild!=null
                        &&current.rightChild!=null){
                    if(current.leftChild.qcontent.length()!=1)
                        current.leftChild.qcontent=" ("+current.leftChild.qcontent+")";
                    if(current.rightChild.qcontent.length()!=1)
                        current.rightChild.qcontent=" ("+current.rightChild.qcontent+")";
                    current.contentOfApplication=current.leftChild.qcontent+" "+current.rightChild.qcontent;
                    current.qcontent=current.contentOfApplication;
                }
                current.leftChild=null;
                current.rightChild=null;
            }
        }
    }

    public static void main(String[] args) {

        //String source = "(\\x.(\\y.y x) z) v";  //ok
        String source = "(\\x.x (x y)) m";  //ok
        //String source = "(\\x.y) z";  //ok
        //String source = "(\\x.x x) (\\x.x x)";  //ok
        //String source = "(\\x.x x y) (\\x.x x y)";  //ok (add codes to cheak longer length?)
        //String source = "\\x.\\y.y x a b";  //ok
        Parser parser = new Parser(source);
        Node head = parser.parse();
        Node current=head;
        Interpreter.cutTwoParaAtom(current);
        current=head;
        Interpreter.postOrderSimplize(current);
        head.printNode();
    }
}
