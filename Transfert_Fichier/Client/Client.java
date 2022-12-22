package server;
import java.io.*;
import java.net.*;
import java.util.Vector;

import function.*;
import affichage.*;
public class Client {
    int port;
    String id;
    Socket client;
    Fenetre fen;

    public void setFen(Fenetre fen) {
        this.fen = fen;
    }
    public Fenetre getFen() {
        return fen;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setClient(Socket client) {
        this.client = client;
    }
    public Socket getClient() {
        return client;
    }

    public Client(int port,String id) throws Exception{
        this.port = port;
        this.id = id;
        Socket client = new Socket(this.getId(), this.getPort());   
        this.setClient(client);
    }
    public Client() {
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getPort() {
        return port;
    }
    public boolean verify() throws Exception{
        try {
            InputStream is = client.getInputStream(); 
            ObjectInputStream msg = new ObjectInputStream(is);  
            Object obj1  = msg.readObject();
            String fst = String.valueOf(obj1);
            
            InputStream is1 = client.getInputStream(); 
            ObjectInputStream msg1 = new ObjectInputStream(is1);  
            Object obj  = msg1.readObject();
            String scd = String.valueOf(obj);

            if (fst.equalsIgnoreCase("download")) {
                InputStream is2 = client.getInputStream(); 
                ObjectInputStream msg2 = new ObjectInputStream(is2);  
                Object obj2  = msg2.readObject();
                String thrd = String.valueOf(obj2);

                System.out.println(thrd);
                this.get(thrd,scd);

                is2.close();
            }
            is.close();
            is1.close();

        } catch (Exception e) {
            //TODO: handle exception
        }
        return true;
    }
    public  void send(String txt,String action) throws Exception{
        try {                                
            OutputStream output = this.getClient().getOutputStream();                                 
            Object msg = action;                                                            
            ObjectOutputStream objoutput = new ObjectOutputStream(output);                  
            objoutput.writeObject(msg);   
            
            this.getSend(txt);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public  void sendD(String txt,String action) throws Exception{
        try {                                
            OutputStream output = this.getClient().getOutputStream();                                 
            Object msg = action;                                                            
            ObjectOutputStream objoutput = new ObjectOutputStream(output);                  
            objoutput.writeObject(msg);   
            
            OutputStream output1 = this.getClient().getOutputStream();                                 
            Object msg1 = txt;                                                            
            ObjectOutputStream objoutput1 = new ObjectOutputStream(output1);                  
            objoutput1.writeObject(msg1);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void get(String file,String obj1) throws Exception{
        try {
            
            String txt = String.valueOf(obj1);

            Functions fct = new Functions();
            
            String home = System.getProperty("user.home");
            String directory = home+"/Downloads/"+file;
            File fichier = new File(directory);
            fichier.createNewFile();
            
            String[] vect = txt.split("===");
            String all = vect[0];
            for(int i=1 ; i<vect.length ; i++){
                all = all+vect[i];
            }
            String[] vect1 = all.split("&&&");
            for (int j = 0; j < vect1.length; j++) {
                String[] vect2 = vect1[j].split("//");
                fct.writeLine(directory, vect2[0]);
                for (int k = 1; k < vect2.length; k++) {
                    fct.writeLine(directory, vect2[k]);
                }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
    public void getSend(String txt) throws Exception{     /// distribue en 3 parties et les renvoient au 3 server 
        try {
            Functions fct = new Functions();

            int rand = (int)(Math.random()*100);
            fct.writeLine("Texte.txt","Texte"+rand+".txt =>"+txt);
            String texte = "Texte"+rand+".txt";

            Vector vect = fct.readerLine(txt);

            OutputStream os = this.getClient().getOutputStream();                          
            ObjectOutputStream message = new ObjectOutputStream(os);
            Object obj  = String.valueOf(vect.elementAt(0));
            for (int i = 1; i < (int) vect.size()/3; i++) {
                 obj  = obj +"//"+String.valueOf(vect.elementAt(i)); 
            }
            message.writeObject(obj+"=>"+texte);

            OutputStream os1 = this.getClient().getOutputStream();                         
            ObjectOutputStream message1 = new ObjectOutputStream(os1);
            Object obj2  = String.valueOf(vect.elementAt((int) vect.size()/3));
            for (int i = ((int) vect.size()/3)+1 ; i < (int) vect.size()*2/3; i++) {
                obj2 = obj2 +"//" +String.valueOf(vect.elementAt(i));
            }
            message1.writeObject(obj2+"=>"+texte);  

            OutputStream os2 = this.getClient().getOutputStream();                         
            ObjectOutputStream message2 = new ObjectOutputStream(os2);
            Object obj3  = String.valueOf(vect.elementAt((int) vect.size()*2/3));
            for (int i = ((int) vect.size()*2/3)+1; i <  vect.size(); i++) {
                obj3 = obj3 +"//"+String.valueOf(vect.elementAt(i));
            }
            message2.writeObject(obj3+"=>"+texte);   

            os.close();
            os1.close();
            os2.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

}
