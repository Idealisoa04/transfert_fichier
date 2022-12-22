package server;
import java.io.*;
import java.net.*;
import java.util.Vector;
import function.*;

public class Server{
    int port;
    int clport;
    int clport1;
    int clport2;

    ServerSocket server;

    Server1 svr1;
    Server1 svr2;
    Server1 svr3;

    public void setSvr1(Server1 svr1) {
        this.svr1 = svr1;
        this.svr1.setPortP(this.getPort());
        this.svr1.setPrincip("localhost");
    }
    public void setSvr2(Server1 svr2) {
        this.svr2 = svr2;
        this.svr2.setPortP(this.getPort());
        this.svr2.setPrincip("localhost");
    }
    public void setSvr3(Server1 svr3) {
        this.svr3 = svr3;
        this.svr3.setPortP(this.getPort());
        this.svr3.setPrincip("localhost");
    }
    public Server1 getSvr1() {
        return svr1;
    }
    public Server1 getSvr2() {
        return svr2;
    }
    public Server1 getSvr3() {
        return svr3;
    }
    public Server() {
        try {
            ServerSocket server = new ServerSocket( this.getPort() ); 
            this.setServer(server);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }


    public Server(int port, int clport,int clport1,int clport2) {
        this.port = port;
        this.clport = clport;
        this.clport1 = clport1;
        this.clport2 = clport2;
        try {
            ServerSocket server = new ServerSocket( this.getPort() ); 
            this.setServer(server);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public String get() throws Exception{
        String rep = "";
        try {
                Socket client = this.getServer().accept();           
                InputStream is = client.getInputStream(); 
                ObjectInputStream msg = new ObjectInputStream(is);  
                Object obj1  = msg.readObject();
                rep = String.valueOf(obj1);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
        return rep;
    }

    public String sendClient() throws Exception{
        String rep = "";
        try {
                String from1 = this.get();
                String from2 = this.get();
                String from3 = this.get();
                rep = from1+"==="+from2+"==="+from3;

        } catch (Exception e) {
            //TODO: handle exception
        }
        return rep;
    }

    public void getSend(String content1, String content2, String content3) throws Exception{     /// distribue en 3 parties et les renvoient au 3 server 
        try {

            Socket clientSvr = new Socket("localhost", this.getClport());
            OutputStream os = clientSvr.getOutputStream();                          
            ObjectOutputStream message = new ObjectOutputStream(os);
            message.writeObject(content1);
            this.getSvr1().get();

            Socket clientSvr1 = new Socket("localhost", this.getClport1());
            OutputStream os1 = clientSvr1.getOutputStream();                         
            ObjectOutputStream message1 = new ObjectOutputStream(os1);
            message1.writeObject(content2);  
            this.getSvr2().get();

            Socket clientSvr2 = new Socket("localhost", this.getClport2());
            OutputStream os2 = clientSvr2.getOutputStream();                         
            ObjectOutputStream message2 = new ObjectOutputStream(os2);
            message2.writeObject(content3);   
            this.getSvr3().get();

            os.close();
            os1.close();
            os2.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }
    public void sendSearch(String txt) throws Exception{       
        Socket clientSvr = new Socket("localhost", this.getClport());
        OutputStream os = clientSvr.getOutputStream();                          
        ObjectOutputStream message = new ObjectOutputStream(os);
        message.writeObject(txt);

        Socket clientSvr1 = new Socket("localhost", this.getClport1());
        OutputStream os1 = clientSvr1.getOutputStream();                         
        ObjectOutputStream message1 = new ObjectOutputStream(os1);
        message1.writeObject(txt);

        Socket clientSvr2 = new Socket("localhost", this.getClport2());
        OutputStream os2 = clientSvr2.getOutputStream();                         
        ObjectOutputStream message2 = new ObjectOutputStream(os2);
        message2.writeObject(txt);
        
        this.getSvr1().searchinFile();
        this.getSvr2().searchinFile();
        this.getSvr3().searchinFile();

        os.close();
        os1.close();
        os2.close();
    }
    public boolean verify() throws Exception{                      ///verifie l action que le client veut effectue et agit selon cette action 
        try {
            Socket client = this.getServer().accept();           
            InputStream is = client.getInputStream(); 
            ObjectInputStream msg = new ObjectInputStream(is);  
            Object obj1  = msg.readObject();
            String action = String.valueOf(obj1);


            if (action.equalsIgnoreCase("upload")) {
                InputStream is3 = client.getInputStream(); 
                ObjectInputStream msg3 = new ObjectInputStream(is3);  
                Object obj3  = msg3.readObject();
                String content1 = String.valueOf(obj3);
    
                InputStream is1 = client.getInputStream(); 
                ObjectInputStream msg1 = new ObjectInputStream(is1);  
                Object obj  = msg1.readObject();
                String content2 = String.valueOf(obj);
    
                InputStream is2 = client.getInputStream(); 
                ObjectInputStream msg2 = new ObjectInputStream(is2);  
                Object obj2  = msg2.readObject();
                String content3 = String.valueOf(obj2);
                this.getSend(content1,content2,content3);
   
                is3.close();
                is1.close();
                is2.close();

            }
            if (String.valueOf(obj1).equalsIgnoreCase("download")) {
                InputStream is4 = client.getInputStream(); 
                ObjectInputStream msg4 = new ObjectInputStream(is4);  
                Object obj4  = msg4.readObject();
                String txt = String.valueOf(obj4);

                this.sendSearch(txt);

                OutputStream os1 = client.getOutputStream();
                ObjectOutputStream message1 = new ObjectOutputStream(os1);
                message1.writeObject("download");

                OutputStream os = client.getOutputStream();
                ObjectOutputStream message = new ObjectOutputStream(os);
                message.writeObject(sendClient());

                OutputStream os2 = client.getOutputStream();
                ObjectOutputStream message2 = new ObjectOutputStream(os2);
                message2.writeObject(txt);
                
                os1.close();
                os.close();
                os2.close();

                is4.close();

            }
            is.close();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return true;
    }
    public String lister() throws Exception{
        Functions fct =  new Functions();
        Vector vect = fct.readerLine("Texte.txt");
        String rep1 = "";
        for (int i = 0; i < vect.size(); i++) {
            String[] rep = String.valueOf(vect.elementAt(i)).split("=>");
            rep1 = rep[0];
            for (int j = 1; j < rep.length; j++) {
                rep1 = "===" + rep[j];
            }
            }
            return rep1;
    }
    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }


    public int getClport() {
        return clport;
    }


    public void setClport(int clport) {
        this.clport = clport;
    }


    public int getClport1() {
        return clport1;
    }


    public void setClport1(int clport1) {
        this.clport1 = clport1;
    }


    public int getClport2() {
        return clport2;
    }


    public void setClport2(int clport2) {
        this.clport2 = clport2;
    }


    public ServerSocket getServer() {
        return server;
    }


    public void setServer(ServerSocket server) {
        this.server = server;
    }
}