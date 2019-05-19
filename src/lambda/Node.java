package lambda;
import java.util.*;

public class Node{
    public NodeType type=null;

    public String qcontent;
    public char contentOfAtom;
    public String contentOfApplication;
    public char parmOfAbstraction;
    public String bodyOfAbstraction;

    public Node leftChild=null;
    public Node rightChild=null;

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

    public NodeType typeOfString(String s){
        if(s.length()==1)    //a
            return NodeType.ATOM;
        if(s.charAt(0)=='\\')   // \x.x y
            return NodeType.ABSTRACTION;
        return NodeType.APPLICATION;  // s d || (\x.x y) k
    }

    public boolean isValid(String s) {
        char[] stack = new char[s.length() + 1];
        int top = 1;
        for(char c : s.toCharArray())
            if(c == '(')
                stack[top++] = c;
            else if(c == ')' && stack[--top] != '(')
                return false;
        return top == 1;
    }
    public String offLR(String s){
        if(s.charAt(0)=='('&&s.charAt(s.length()-1)==')'){
            String after=s.substring(1, s.length()-1);
            return isValid(after)?after:s;
        }
        else
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
            if(!bodyOfAbstraction.contains("."))
                return;
            if(bodyOfAbstraction.charAt(0)!='('){
                this.leftChild=new Node(bodyOfAbstraction.substring(0,1));
                this.rightChild=new Node(bodyOfAbstraction.substring(2,bodyOfAbstraction.length()));
            }
            else{
                int leftcount=0,rightcount=0;
                int i;
                for(i=bodyOfAbstraction.length()-1;i>=0;i--){
                    if(bodyOfAbstraction.charAt(i)=='(')
                        leftcount++;
                    else if(bodyOfAbstraction.charAt(i)==')')
                        rightcount++;
                    if(leftcount==rightcount&&bodyOfAbstraction.charAt(i)==' ')
                        break;
                }
                this.leftChild=new Node(bodyOfAbstraction.substring(0,i));
                this.rightChild=new Node(bodyOfAbstraction.substring(i+1,bodyOfAbstraction.length()));
            }
        }
        if(this.type==NodeType.APPLICATION){
            if(!contentOfApplication.contains("."))
                return;
            int leftcount=0,rightcount=0;
            int i;
            for(i=contentOfApplication.length()-1;i>=0;i--){
                if(contentOfApplication.charAt(i)=='(')
                    leftcount++;
                else if(contentOfApplication.charAt(i)==')')
                    rightcount++;
                if(leftcount==rightcount&&contentOfApplication.charAt(i)==' ')
                    break;
            }
                this.leftChild=new Node(contentOfApplication.substring(0,i));
                this.rightChild=new Node(contentOfApplication.substring(i+1,contentOfApplication.length()));   
        }
        this.leftChild.extend();
        this.rightChild.extend();
    }

    public String toString(){
        if(this.type==NodeType.ATOM)
            return String.valueOf(contentOfAtom);
        if(this.type==NodeType.APPLICATION)
            return contentOfApplication;
        if(this.type==NodeType.ABSTRACTION)
            return "\\"+parmOfAbstraction+"."+bodyOfAbstraction;
        return null;
    }
}
