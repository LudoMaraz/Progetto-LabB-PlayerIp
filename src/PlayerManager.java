import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.Instant;

public class PlayerManager {

    protected static JsonObject login(PrintWriter writer, BufferedReader reader, BufferedReader br) {
        JsonObject response = null;
        Gson gson = new Gson();
        try {
            JsonObject credenziali = new JsonObject();
            System.out.println("Enter NickName:");
            credenziali.addProperty("username", br.readLine());
            System.out.println("Enter password:");
            credenziali.addProperty("password", br.readLine());

            writer.println(credenziali);
            writer.flush();
            String comando = reader.readLine();
            if (comando.equalsIgnoreCase("autorizzato")) {
                System.out.println("Perfetto");
                response = gson.fromJson(reader.readLine(), JsonObject.class);
            } else if (comando.equalsIgnoreCase("codice_auth")) {
                System.out.println("Inserisci codice");
                writer.println(br.readLine());
                writer.flush();
                if (reader.readLine().equalsIgnoreCase("autorizzato")) {
                    System.out.println("Perfetto");
                    response = gson.fromJson(reader.readLine(), JsonObject.class);
                } else {
                    System.out.println("Errore inserimento codice");
                }
            } else {
                System.out.println("Credenziali errate");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore inserimento credenziali");
        }

        return response;
    }

    protected static void registrazione_player(PrintWriter writer, BufferedReader reader, BufferedReader br) {
        try {
            JsonObject playerInfo = new JsonObject();

            System.out.println("Enter Nome:");
            playerInfo.addProperty("nome", br.readLine());
            System.out.println("Enter Cognome:");
            playerInfo.addProperty("cognome", br.readLine());
            System.out.println("Enter NickName:");
            playerInfo.addProperty("nickname", br.readLine());
            System.out.print("Enter Email:");
            playerInfo.addProperty("email", br.readLine());
            System.out.print("Enter Password:");
            playerInfo.addProperty("password", br.readLine());
            Instant instant = Instant.now();
            long timeStampSeconds = instant.getEpochSecond();

            playerInfo.addProperty("codice_auth", timeStampSeconds);
            writer.println(playerInfo);
            writer.flush();
            // invio mail
            if (reader.readLine().equalsIgnoreCase("registrato")) {
                System.out.println("COMPLETA LA REGISTRAZIONE ESEGUENDO IL LOGIN E INSERENDO IL CODICE CHE TI e' STATO INVIATO ALLA TUA EMAIL");
            } else {
                System.out.println("Errore nella registrazione");
            }
        } catch (Exception e) {
            System.out.println("Errore inserimento credenziali");
        }
    }


    protected static void modifyProfile(BufferedReader br, PrintWriter writer, BufferedReader reader, JsonObject playerInfo){
        try{
            String n = "";
            System.out.println("Cosa vuoi modificare?");
            System.out.println("Nome\nCognome\nNickname\nPassword");
            //PrintWriter wr = new PrintWriter(System.out, true);
            n = br.readLine();
            switch (n){
                case "nome":
                    System.out.println("Inserisci il nuovo nome: ");
                    playerInfo.addProperty("nome", br.readLine());
                    writer.println(playerInfo);
                    writer.flush();
                    break;
                case "cognome":
                    System.out.println("Inserisci il nuovo cognome: ");
                    playerInfo.addProperty("cognome", br.readLine());
                    writer.println(playerInfo);
                    writer.flush();
                    break;
                case "nickname":
                    System.out.println("Inserisci il nuovo nickname: ");
                    playerInfo.addProperty("nickname", br.readLine());
                    writer.println(playerInfo);
                    writer.flush();
                    break;

                case "password":
                    System.out.println("Inserisci la vecchia psw: ");
                    if (playerInfo.get("password").getAsString().equals(br.readLine().trim())){
                        System.out.println("Inserisci la nuova password: ");
                        playerInfo.addProperty("password", br.readLine());
                        writer.println(playerInfo);
                        writer.flush();
                    } else {
                        System.out.println("ERRORE! La password inserita non corrisponde");
                    }
                    break;
            }
            if(reader.readLine().equalsIgnoreCase("ok_dati_modificati")){
                System.out.println("I dati sono stati modificati correttamente");
            } else {
                System.err.println("ERRORE: i dati non sono stati modificati correttamente");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected static void resetPsw(BufferedReader br, PrintWriter writer, BufferedReader reader, JsonObject playerInfo) {

        try {
            System.out.println("Inserisci l'email associata al tuo profilo: ");

            if (playerInfo.get("email").getAsString().equals(br.readLine().trim())){

                writer.println("reset_psw");
                writer.flush();
            }

            if (reader.readLine().equalsIgnoreCase("ok_reset_psw_effettuato")) {
                System.out.println("COMPLETA IL RESET ESEGUENDO IL LOGIN E INSERENDO LA PASSWORD CHE TI É STATA INVIATO ALLA TUA EMAIL");
            } else {
                System.out.println("Errore nel reset della password");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
