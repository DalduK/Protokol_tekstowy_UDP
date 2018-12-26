package Serwerek;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;


public class ServerThread extends Thread{
    String line="";
    private BufferedInputStream  is = null;
    private PrintWriter os=null;
    private Socket s=null;
    private Protokol po = null;
    public String ID = "";
    public Ids zaj;
    boolean i = true;

    public ServerThread(Socket s, Ids z){
        this.s=s;
        this.zaj=z;
    }
    public void run() {
        try{
            is= new BufferedInputStream(s.getInputStream());
            os=new PrintWriter(s.getOutputStream());
        }catch(IOException e){
            System.out.println("IOException w threadzie");
        }
        try {//odbieranie wiadomości
            byte[] contents = new byte[1024];
            int bytesRead = 0;
            bytesRead=is.read(contents);
            String literek = "";
            literek += new String(contents,0,bytesRead);
            line = literek;
            System.out.print(line);
            while(!literek.contains("QUIT")){//Sprawdzanie czy wiadomość klienta zawiera wiadomość QUIT, jeśli nie
                String[] parts = line.split("\\^");//dzieli za wiadomość za pomocą funkcji split i pakuje wiadomość do naszego Protokołu
                String part1 = parts[0];
                String new1 = part1.replace("OperaC>>","");
                String part2 = parts[1];
                String new2 = part2.replace("StatuS>>","");
                String part3 = parts[2];
                String new3 = part3.replace("IdentY>>", "");
                ID = new3;
                if (zaj.zajID.contains(ID)) {//podczas dzielenia sprawdzane jest też ID klienta, jeśli zajęte Połączenie zostaje zakończone
                    System.out.println("ID zajęte");
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatDateTime = now.format(formatter);
                    String nn = ("OperaC>>" + part1 + "^StatuS>>" + "BladID" + "^IdentY>>" + ID + "^CzasxD>>" + formatDateTime + "^OdpowI>>" + "0" + "^");
                    os.print(nn);
                    os.flush();
                    System.out.println("Zamykam polaczenie");
                    IOException e = new IOException("Błąd ID");
                    throw e;
                } else {
                    zaj.zajID.add(ID);//Id zostaje dodane do wektora znajdującego się w klasie Ids, klasa stworzona aby rozwiązać problemy wielu wątków serwera jak i dużej ilości Identyfikatorów
                    String part4 = parts[3];
                    String new4 = part4.replace("CzasxD>>", "");
                    String part5 = parts[4];
                    String new5 = part5.replace("Argum1>>", "");
                    String part6 = parts[5];
                    String new6 = part6.replace("Argum2>>", "");
                    String part7 = parts[6];
                    String new7 = part7.replace("Argum3>>", "");
                    po = new Protokol(new1, new2, new3, new4, new5, new6, new7);//wiadomość spakowana
                    po.checkOP();//funkcja sprawdzająca jaką operację ma wykonać
                    String nowy = ("OperaC>>" + po.OperaC + "^StatuS>>" + "Connected" + "^IdentY>>" + po.IdentY + "^CzasxD>>" + po.CzasrT + "^OdpowI>>" + po.Argum1 + "^");//pakowanie odpowiedzi do klienta
                    os.print(nowy);//wysyłanie wiadomości do klienta
                    os.flush();
                    System.out.println("Odpowiedz do klienta  :  " + nowy);
                    line = "";
                    literek = "";
                    bytesRead=is.read(contents);
                    literek += new String(contents,0,bytesRead);
                    line = literek;
                    is = new BufferedInputStream(s.getInputStream());
                    //koniec i oczekiwanie na kolejną wiadomość aby połączanie trwało do wywołania operacji "QUIT"
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            line=this.getName(); //reused String line for getting thread name
            System.out.println("IOException / Klient "+line+" zakonczony nagle");
        }
        catch(NullPointerException e){
            e.printStackTrace();
            line=this.getName(); //reused String line for getting thread name
            System.out.println("Klient "+line+" zamkniety");
        }
        finally{//zamykamy połączenie
            try{
                zaj.zajID.remove(ID);
                System.out.println("Zamykanie polaczenia...");
                if (is!=null){
                    is.close();
                    System.out.println("Strumien wejsciowy socketu zamkniety");
                }

                if(os!=null){
                    os.close();
                    System.out.println("Strumien wysjsciowy socketu zamkniety");
                }
                if (s!=null){
                    s.close();
                    System.out.println("Socket zamkniety");
                }

            }
            catch(IOException ie){
                ie.printStackTrace();
                System.out.println("Blad zamykania socketu");
            }
        }

    }
}



