package lambda;

public class Parser {
    String source;

    public Parser(String source){
        this.source=source;
    }

    public Node parse(){
        Node Head=new Node(source);
        Head.extend();
        return Head;
    }
}