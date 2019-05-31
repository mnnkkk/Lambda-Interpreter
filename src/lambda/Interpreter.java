package lambda;

public class Interpreter {

    public static String transSourceIA(String source){
        char ch='A';
        char[] charList=source.toCharArray();
        char[] outPutList=charList.clone();
        for(int i=1;i<charList.length;i++){
            if(charList[i-1]=='\\'){
                outPutList[i]=ch;
                ch++;
                if(ch=='Z')
                    ch='a';
                if(ch=='z')
                    ch='0';
                if(ch=='9')
                    ch='A';
            }
        }
        for(int i=charList.length-1;i>=1;i--){
            if(((charList[i]>='a'&&charList[i]<='z')||(charList[i]>='A'&&charList[i]<='Z')||(charList[i]>='0'&&charList[i]<='9'))&&charList[i-1]!='\\'){
                int left=0,right=0;   
                for(int j=i-1;j>=0;j--){
                    if(charList[j]=='(')
                        left++;
                    if(charList[j]==')')
                        right++;
                    if(left>right)
                        left=right;
                    if(right<=left){
                        if(charList[j]==charList[i]&&j!=0&&charList[j-1]=='\\'){
                            outPutList[i]=outPutList[j];
                            break;
                        }
                    }
                }
            }
        }
        StringBuffer b = new StringBuffer(); 
        for(int i=0;i<outPutList.length;i++)
            b.append(outPutList[i]);
        return b.toString();
    }

    public static boolean isValid(String s) {
        char[] stack = new char[s.length() + 1];
        int top = 1;
        for(char c : s.toCharArray())
            if(c == '(')
                stack[top++] = c;
            else if(c == ')' && stack[--top] != '(')
                return false;
        return top == 1;
    }
    public static String offLR(String s){
        if(s.charAt(0)=='('&&s.charAt(s.length()-1)==')'){
            String after=s.substring(1, s.length()-1);
            return isValid(after)?after:s;
        }
        else
            return s;
    }

    public static String replaceStr(String source,char before,String after){
        StringBuffer b = new StringBuffer();
        for(int i=0;i<source.length();i++){
            if(source.charAt(i)=='\\'){
                b.append('\\');
                i++;
                b.append(source.charAt(i));
                continue;
            }
            if(source.charAt(i)==before)
                b.append(after);
            else
                b.append(source.charAt(i));
        }
        return b.toString();
    }

    public static String transInput(String s){
        StringBuffer b = new StringBuffer(); 
        for(int i=0;i<s.length()-1;i++){
            b.append(s.charAt(i));
            if((s.charAt(i)==')'&&s.charAt(i+1)!=')'&&s.charAt(i+1)!=' ')
                ||(s.charAt(i)!=' '&&s.charAt(i)!='('&&s.charAt(i)!=')'&&s.charAt(i)!='.'&&s.charAt(i+1)=='('))
                b.append(' ');
        }
        b.append(s.charAt(s.length()-1));
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
        if(current != null&&current.leftChild!=null&&current.rightChild!=null){

            postOrderSimplize(current.leftChild);
            postOrderSimplize(current.rightChild);
            
            if(current.type==NodeType.ABSTRACTION){
                String lastBody=current.bodyOfAbstraction;
                if(current.leftChild.type==NodeType.ABSTRACTION&&Node.flag==0){
                    //
                    if(current.rightChild.qcontent.length()!=1&&current.rightChild.qcontent.charAt(0)=='\\')
                        current.rightChild.qcontent="("+current.rightChild.qcontent+")";
                    //
                    current.bodyOfAbstraction=replaceStr(current.leftChild.bodyOfAbstraction,current.leftChild.parmOfAbstraction,current.rightChild.qcontent);
                    current.qcontent="\\"+current.parmOfAbstraction+"."+current.bodyOfAbstraction;
                    Node.flag=1;
                }
                else{
                    if(current.leftChild.qcontent.length()!=3||current.leftChild.qcontent.charAt(0)!='\\'){
                        if(current.leftChild.qcontent.length()!=1)
                            current.leftChild.qcontent="("+current.leftChild.qcontent+")";
                        if(current.rightChild.qcontent.length()!=1)
                            current.rightChild.qcontent=" ("+current.rightChild.qcontent+")";
                        if(current.rightChild.qcontent.length()==1)
                        current.rightChild.qcontent=" "+current.rightChild.qcontent;
                    }
                    current.bodyOfAbstraction=current.leftChild.qcontent+current.rightChild.qcontent;    
                    current.qcontent="\\"+current.parmOfAbstraction+".("+current.bodyOfAbstraction+")";
                }
            }
            else if(current.type==NodeType.APPLICATION){
                if(current.leftChild.type==NodeType.ABSTRACTION&&Node.flag==0){
                    if(current.rightChild.qcontent.length()!=1&&(current.rightChild.qcontent.charAt(0)=='\\'||current.rightChild.qcontent.charAt(0)!='('))
                        current.rightChild.qcontent="("+current.rightChild.qcontent+")";
                    current.contentOfApplication=replaceStr(current.leftChild.bodyOfAbstraction,current.leftChild.parmOfAbstraction,current.rightChild.qcontent);
                    current.qcontent=current.contentOfApplication;
                    Node.flag=1;
                }
                else{
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
        do{
            Node.flag=0;
            current=transInput(current);
            source=offLR(source);
            current=transSourceIA(current);
            before=current;
            Parser parser = new Parser(current);
            Node head = parser.parse();
            cutTwoParaAtom(head);
            postOrderSimplize(head);
            current=head.toString();
        }while(!before.equals(current));
        return current;
    }    
}
