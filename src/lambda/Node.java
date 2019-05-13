package lambda;

public class Node{
    public NodeType type=null;

    public String qcontent;
    public char contentOfAtom;
    public String contentOfApplication;
    public char parmOfAbstraction;
    public String bodyOfAbstraction;

    public Node leftChild=null;
    public Node rightChild=null;

    public NodeType typeOfString(String s){
        if(s.length()==1)    //a
            return NodeType.ATOM;
        if(s.charAt(0)=='\\')   //xxx xxx
            return NodeType.ABSTRACTION;
        return NodeType.APPLICATION;
    }

    public String offLR(String s){
        if(s.charAt(0)=='('
                &&s.charAt(s.length()-1)==')'
                &&!s.contains(") ("))
            s=s.substring(1, s.length()-1);
        return s;
    }

    public void extend(){
        if(this.type==NodeType.ATOM)
            return;
        if(this.type==NodeType.ABSTRACTION){
            if(typeOfString(this.bodyOfAbstraction)==NodeType.ATOM
                    ||typeOfString(this.bodyOfAbstraction)==NodeType.ABSTRACTION)
                return;
            //typeOfString(this.bodyOfAbstraction)==APPLICATION
            if(bodyOfAbstraction.charAt(0)!='('){
                this.leftChild=new Node(bodyOfAbstraction.substring(0,1));
                this.rightChild=new Node(bodyOfAbstraction.substring(2,bodyOfAbstraction.length()));
            }
            else{
                int leftcount=0,rightcount=0;
                int i;
                for(i=0;i<bodyOfAbstraction.length();i++){
                    if(bodyOfAbstraction.charAt(i)=='(')
                        leftcount++;
                    else if(bodyOfAbstraction.charAt(i)==')')
                        rightcount++;
                    if(leftcount==rightcount)
                        break;
                }
                this.leftChild=new Node(bodyOfAbstraction.substring(0,i+1));
                this.rightChild=new Node(bodyOfAbstraction.substring(i+2,bodyOfAbstraction.length()));
            }
            this.leftChild.extend();
            this.rightChild.extend();
        }
        if(this.type==NodeType.APPLICATION){
            if(false&&contentOfApplication.charAt(0)!='('){
                this.leftChild=new Node(contentOfApplication.substring(0,1));
                this.rightChild=new Node(contentOfApplication.substring(2,contentOfApplication.length()));
            }
            else{
                int leftcount=0,rightcount=0;
                int i;
                for(i=0;i<contentOfApplication.length();i++){
                    if(contentOfApplication.charAt(i)=='(')
                        leftcount++;
                    else if(contentOfApplication.charAt(i)==')')
                        rightcount++;
                    if(leftcount==rightcount)
                        break;
                }
                this.leftChild=new Node(contentOfApplication.substring(0,i+1));
                this.rightChild=new Node(contentOfApplication.substring(i+2,contentOfApplication.length()));
            }
            this.leftChild.extend();
            this.rightChild.extend();
        }
    }

    Node(String content){
        content=offLR(content);
        qcontent=content;
        this.type=typeOfString(content);
        if(type==NodeType.ATOM)
            contentOfAtom = content.charAt(0);
        if(type==NodeType.APPLICATION)
            contentOfApplication=content;
        if(type==NodeType.ABSTRACTION){
            parmOfAbstraction=content.charAt(1);
            bodyOfAbstraction=offLR(content.substring(3,content.length()));
        }
    }

    void printNode(){
        if(this.type==NodeType.ATOM)
            System.out.println(contentOfAtom);
        if(this.type==NodeType.APPLICATION)
            System.out.println(contentOfApplication);
        if(this.type==NodeType.ABSTRACTION)
            System.out.println("\\"+parmOfAbstraction+"."+bodyOfAbstraction);
    }
}
