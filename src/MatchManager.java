import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.Instant;

public class MatchManager {

    protected static void createMatch(PrintWriter writer, BufferedReader reader, BufferedReader br, JsonObject playerInfo) {
        JsonObject infoPartita = new JsonObject();
        try {
            System.out.println("Inserisci il nome della partita");
            infoPartita.addProperty("nome", br.readLine());
            System.out.println("Inserisci numero massimo di giocatori");
            int num_player = 0;
            do {
                System.out.println("Il numero minimo di partecipanti è 2 e il numero massimo è 6");
                num_player = Integer.parseInt(br.readLine());
            } while (num_player >= 2 && num_player <= 6);
            infoPartita.addProperty("num_player", br.readLine());
            Instant instant = Instant.now();
            long timeStampSeconds = instant.getEpochSecond();
            System.out.println(playerInfo);
            infoPartita.addProperty("id_partita", timeStampSeconds + playerInfo.get("id_number").getAsString());
            infoPartita.addProperty("player1", playerInfo.get("nickname").getAsString());
            writer.println(infoPartita);
            writer.flush();

            if (reader.readLine().equalsIgnoreCase("ok_match_create")) {
                System.out.println("Il tuo id partita è :" + infoPartita.get("id_partita").getAsString());
                System.out.println("Condividilo con i tuoi amici per giocare insieme");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static void visualizzaListaMatch(BufferedReader reader){
        JsonArray listaPartite;
        try{
            listaPartite = new Gson().fromJson(reader.readLine(), JsonArray.class);
            System.out.println(listaPartite.toString());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    protected static void partecipaMatch(BufferedReader br, PrintWriter writer, BufferedReader reader, JsonObject playerInfo){
        JsonObject infoPartita = new JsonObject();
        try{
            System.out.println("Richiedi id_partita");
            infoPartita.addProperty("id_partita", br.readLine());
            infoPartita.addProperty("id_player", playerInfo.get("nickname").getAsString());
            writer.println(infoPartita);
            writer.flush();
            if(reader.readLine().equalsIgnoreCase("ok_partecipazione_accettata")){
                System.out.println("Sei iscritto alla partita");
            } else {
                System.out.println("La partita che hai richiesto non c'è");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected static void leaveMatch(BufferedReader br, PrintWriter writer, BufferedReader reader, JsonObject playerInfo){
        JsonObject infoPartita = new JsonObject();
        try{
            System.out.println("Richiedi id_partita");
            infoPartita.addProperty("id_partita", br.readLine());
            infoPartita.addProperty("id_player", playerInfo.get("nickname").getAsString());
            writer.println(infoPartita);
            writer.flush();
            if(reader.readLine().equalsIgnoreCase("ok_match_left")){
                System.out.println("Hai abbandonato la partita");
            } else {
                System.out.println("Errore! Non hai abbandonato la partita");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
