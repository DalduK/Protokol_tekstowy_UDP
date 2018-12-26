package Serwerek;
import javax.management.StringValueExp;
import javax.management.ValueExp;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Protokol {
    String OperaC;
    String StatuS;
    String IdentY;
    String CzasrT;
    String Argum1;
    String Argum2;
    String Argum3;

    public Protokol(String Op, String St, String Id, String Cz, String pie, String dru, String trz) {
        OperaC = Op;
        StatuS = St;
        IdentY = Id;
        CzasrT = Cz;
        Argum1 = pie;
        Argum2 = dru;
        Argum3 = trz;
    }

    void checkOP(){
        Time();
        if (OperaC.contains("op1")){
            odejmowanie();
        }
        if (OperaC.contains("op2")){
            dzielenie();
        }
        if (OperaC.contains("op3")){
            dodawanie();
        }
        if (OperaC.contains("op4")){
            mnozenie();
        }
        else{

        }
    }

    void error(){
        Argum1 = "Niepoprawna funkcja\n";
    }


    void odejmowanie(){

        float Arg1 =Float.valueOf(Argum1);
        float Arg2 =Float.valueOf(Argum2);
        float Arg3 =Float.valueOf(Argum3);
        float Arg4 = Arg1 - Arg2 - Arg3;
        String foo3 = Float.toString(Arg4);
        Argum1 = foo3;

}
    void dzielenie(){
        float Arg1 =Float.valueOf(Argum1);
        float Arg2 =Float.valueOf(Argum2);
        float Arg3 =Float.valueOf(Argum3);
        if(Arg1 !=0 && Arg2 !=0 && Arg3 !=0){
        float Arg4 = Arg1 / Arg2 / Arg3;
            String foo3 = Float.toString(Arg4);
            clearArg();
            Argum1 = foo3;
        }
        if((Arg3 == 0)|| (Arg2 == 0)){
            clearArg();
            Argum1 = "Nie można dzielić przez zero\n";
        }

    }
    void dodawanie(){
        float Arg1 =Float.valueOf(Argum1);
        float Arg2 =Float.valueOf(Argum2);
        float Arg3 =Float.valueOf(Argum3);
        float Arg4 = Arg1 + Arg2 + Arg3;
        String foo3 = Float.toString(Arg4);
        clearArg();
        Argum1 = foo3;
    }
    void mnozenie(){
        float Arg1 =Float.valueOf(Argum1);
        float Arg2 =Float.valueOf(Argum2);
        float Arg3 =Float.valueOf(Argum3);
        float Arg4 = Arg1 * Arg2 * Arg3;
        String foo3 = Float.toString(Arg4);
        clearArg();
        Argum1 = foo3;
    }
    void clear(){
        OperaC = null;
        StatuS = null;
        IdentY = null;
        CzasrT = null;
        Argum1 = null;
        Argum2 = null;
        Argum3 = null;
    }
    void clearArg(){
        Argum1 = null;
        Argum2 = null;
        Argum3 = null;
    }
    void Time(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatDateTime = now.format(formatter);
        CzasrT = formatDateTime;
    }


    public String Paczuszka() {
        String Paczuszka = new String("OperaC>>" + OperaC + "^StatuS>>" + StatuS + "^IdentY>>" + IdentY + "^CzasxD>>" + CzasrT +
                "^Argum1>>" + Argum1 + "^Argum2>>" + Argum2 + "^Argum3>>" + Argum3 + "^");
        return Paczuszka;
    }
    public String Rozpauj(){
        String Rozpakowanie = new String( "Data :" + CzasrT + "\n"+ "Identyfikator :" + IdentY+ "\n" + "Status : " + StatuS +"\n" + "Odpowiedz "+ Argum1+ "\n");
        return Rozpakowanie;
    }
}

