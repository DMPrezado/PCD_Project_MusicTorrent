package Logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Socket{
    private int nodePortA;
    private int nodePortB;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Connection(int nodePortA, int nodePortB){
        super();
        this.nodePortA=nodePortA;
        this.nodePortB=nodePortB;
        prepareConnection();
    }

    public void prepareConnection(){
        try {
            out = new ObjectOutputStream(getOutputStream());
            in = new ObjectInputStream(getInputStream());
        } catch (IOException e) {
            System.err.println("Erro ao conectar ao nó: " + e.getMessage());
        } 
    }

    public Socket               getSocket(){return this;}
    public ObjectOutputStream   getOut() {return out;}
    public ObjectInputStream    getIn() {return in;}
    public int                  getNodePortA() {return nodePortA;}
    public int                  getNodePortB() {return nodePortB;}

    public void out(Object object){
        try {
            out.writeObject(object);
        } catch (IOException e) {e.printStackTrace();}
    }



}
