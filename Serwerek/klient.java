package Serwerek;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class klient {
    final static int x = 50000;
    public static void main(String args[]) throws IOException{
        System.getProperty("line.separator","");
        System.getProperty("\r\n","");
        InetAddress address=InetAddress.getLocalHost();
        Socket s1=null;
        String line="";
        BufferedInputStream is=null;
        PrintWriter os=null;
        Protokol po = null;
        String Id = "";
        String Op = "";
        String Dane = "";
        String Czas = "";
        boolean dziala = true;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        Czas = formatDateTime;
        try {
            s1 = new Socket("127.0.0.1" , x);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Brak polaczenia, nastapi wylaczenie programu");
            System.exit(0);
        }
        System.out.print("Wybierz ID (Ciag alfanumeryczny o dlugosci max 6 znakow)\n");
        System.out.print("IdentY>>");
        Scanner odczyt = new Scanner(System.in);
        Id = odczyt.nextLine();
        while((Id.length() == 0) || (Id.length() > 6)) {
            System.out.println("Prosze podac prawidlowe ID");
            odczyt = new Scanner(System.in);
            Id = odczyt.nextLine();
        }
        try {
            while (dziala) {
                System.out.print("\nWybierz operacje wpisujac: [op1](-),[op2](/),[op3](+), lub [op4](*) albo wpisz QUIT aby zakonczy\n");
                System.out.print("OperaC>>\n");
                odczyt = new Scanner(System.in);
                Op = odczyt.nextLine();
                System.out.print(Op);
                while (!(Op.equals("op1") || Op.equals("op2") || Op.equals("op3") || Op.equals("op4"))) {
                    if (Op.equals("QUIT")) {
                        po = new Protokol("qt", "QUIT", Id, Czas,"0" ,"0" ,"0" );
                        line = po.Paczuszka();
                        os = new PrintWriter(s1.getOutputStream(),true);
                        os.print(line);
                        IOException o = new IOException("Zamykanie klienta");
                        throw o;
                    }
                    System.out.println("\nNiepoprawna operacja wpisz poprawna ([op1](-),[op2](/),[op3](+), lub [op4](*) albo wpisz QUIT aby zakonczy)");
                    odczyt = new Scanner(System.in);
                    Op = odczyt.nextLine();
                }
                System.out.print("\nWypisz dane w takiej kolejnosci a,b,c (Z przecinkami)");
                System.out.print("ArgumX>>");
                odczyt = new Scanner(System.in);
                Dane = odczyt.nextLine();
                while(true) {
                    if (Dane.matches("(\\-?[0-9]+\\.?[0-9]*\\,?){3}")) {
                        break;
                    }else {
                        System.out.println("\nPodano niepoprawne argumenty, prosze podac dane w postaci (ccc.c,ccc.c,ccc.c c - cyfra)");
                        odczyt = new Scanner(System.in);
                        Dane = odczyt.nextLine();
                    }
                }
                String[] dane = Dane.split(",");
                po = new Protokol(Op, "Zapytaine", Id, Czas, dane[0], dane[1], dane[2]);
                line = po.Paczuszka();
                is= new BufferedInputStream(s1.getInputStream());
                os = new PrintWriter(s1.getOutputStream(),true);
                os.print(line);
                System.out.println("Adres klienta : " + address + "\nID : " + po.IdentY);
                String response = "";
                try {
                    while (response != null) {
                        os.flush();
                        try {
                            byte[] contents = new byte[1024];
                            int bytesRead = 0;
                            bytesRead=is.read(contents);
                            response += new String(contents,0,bytesRead);
                        }
                        catch (IOException e) {
                            System.out.println("Nieprawidlowe ID\n");
                            System.out.println("Brak polaczenia, zamykam");
                            dziala = false;
                            return;
                        }
                        String[] parts = response.split("\\^");
                        String part1 = parts[0];
                        String new1 = part1.replace("OperaC>>", "");
                        String part2 = parts[1];
                        String new2 = part2.replace("StatuS>>", "");
                        if(new2.contains("Blad"))
                        {
                            IOException e = new IOException();
                            dziala = false;
                            throw e;
                        }
                        String part3 = parts[2];
                        String new3 = part3.replace("IdentY>>", "");
                        String part4 = parts[3];
                        String new4 = part4.replace("CzasxD>>", "");
                        String part5 = parts[4];
                        String new5 = part5.replace("OdpowI>>", "");
                        po = new Protokol(new1, new2, new3, new4, new5, null, null);
                        String odp = po.Rozpauj();
                        System.out.println("Odpowiedz : \n" + odp);
                        response = null;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.print("IO Exception lub zle ID");
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Blad odczytu z socketu");
        }
        finally{
            is.close();os.close();s1.close();
            System.out.println("Polaczenie zamkniete");
        }
    }
}