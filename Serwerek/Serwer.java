package Serwerek;
        import java.io.*;
        import java.net.*;
        import java.util.Vector;

public class Serwer {
    final static int x = 50000;
    public static void main(String args[]) {
        System.getProperty("line.separator","");
        System.getProperty("\r\n","");
        Ids zajete = new Ids();
        Socket s = null;
        ServerSocket ss2 = null;
        System.out.println("Server Listening......");
        try {
            ss2 = new ServerSocket(x);//nowy socket
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
        while (true) {
            try {
                s = ss2.accept();
                System.out.println("connection Established");
                ServerThread st = new ServerThread(s,zajete);
                st.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }
    }
}

