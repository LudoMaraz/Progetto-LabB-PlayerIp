import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class PlayerIp {
    private static String password = "Test"; //
    private static String username = "Test";

    public static void main(String[] args) {
        boolean isAuth = false;
        JsonObject playerInfo = new JsonObject();
        // verifico se deve registrarsi o eseguo login


        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(hostname, port)) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            do {
                try {
                    System.out.println("Sei già registrato?");
                    if (br.readLine().equalsIgnoreCase("Si")) {
                        writer.println("login");
                        writer.flush();
                        playerInfo = login(writer, reader, br);
                        isAuth = playerInfo.size() > 0 && !playerInfo.isJsonNull();
                    } else {
                        writer.println("registrazione");
                        writer.flush();
                        registrazione_player(writer, reader, br);
                    }
                    if (isAuth) {
                        System.out.println("AUTORIZZATO");
                        System.out.println("Vuoi organizzare una partita?");
                        if (br.readLine().equalsIgnoreCase("Si")) {
                            writer.println("create_match");
                            writer.flush();
                            createMatch(writer, reader, br, playerInfo);
                        }
                    }
                    if(isAuth){
                        System.out.println("AUTORIZZATO");
                        System.out.println("Vuoi visualizzare la lista delle partite?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            writer.println("visualizza_lista_match");
                            writer.flush();
                            visualizzaListaMatch(reader);
                        }
                    }
                    if(isAuth){
                        System.out.println("AUTORIZZATO");
                        System.out.println("Vuoi richiedere la partecipazione a una partita?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            writer.println("partecipa_match");
                            writer.flush();
                            System.out.println("Richiedi id_partita");
                            if(br.readLine().equalsIgnoreCase("id_partita")) {
                                writer.println("id_partita");
                                writer.flush();
                                partecipaMatch(br);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Errore autenticazione");
                }
            } while (reader.readLine().equalsIgnoreCase("bye"));

            socket.close();

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private static JsonObject login(PrintWriter writer, BufferedReader reader, BufferedReader br) {
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

    private static void registrazione_player(PrintWriter writer, BufferedReader reader, BufferedReader br) {
        try {
            JsonObject playerInfo = new JsonObject();

            System.out.println("Enter Nome:");
            playerInfo.addProperty("nome", br.readLine());
            System.out.print("Enter Cognome:");
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

    private static void createMatch(PrintWriter writer, BufferedReader reader, BufferedReader br, JsonObject playerInfo) {
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

    private static void visualizzaListaMatch(BufferedReader reader){
        JsonObject listaPartite = new JsonObject();
        try{
            listaPartite = new Gson().fromJson(reader.readLine(), JsonObject.class);
            System.out.println(listaPartite);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void partecipaMatch(BufferedReader br){
        JsonObject infoPartita = new JsonObject();
        try{
            System.out.println("Inserisci id_partita");
            infoPartita.addProperty("id_partita", br.readLine());
            if(infoPartita.get("isOpenGame").getAsBoolean() == true){
                System.out.println("Sei iscritto alla partita");
            } else {
                System.out.println("La partita che hai richiesto non c'è");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}


