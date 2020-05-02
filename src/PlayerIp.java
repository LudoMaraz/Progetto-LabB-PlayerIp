import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Random;

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
            MatchManager matchManager = new MatchManager();
            PlayerManager playerManager = new PlayerManager();
            do {
                try {
                    System.out.println("Sei già registrato?");
                    if (br.readLine().equalsIgnoreCase("Si")) {
                        writer.println("login");
                        writer.flush();
                        playerInfo = playerManager.login(writer, reader, br);
                        isAuth = playerInfo.size() > 0 && !playerInfo.isJsonNull();
                    } else {
                        writer.println("registrazione");
                        writer.flush();
                        playerManager.registrazione_player(writer, reader, br);
                    }
                    if (isAuth) {
                        System.out.println("AUTORIZZATO");
                        System.out.println("Vuoi organizzare una partita?");
                        if (br.readLine().equalsIgnoreCase("Si")) {
                            writer.println("create_match");
                            writer.flush();
                            matchManager.createMatch(writer, reader, br, playerInfo);
                        }
                        System.out.println("Vuoi visualizzare la lista delle partite?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            writer.println("visualizza_lista_match");
                            writer.flush();
                            matchManager.visualizzaListaMatch(reader);
                        }
                        System.out.println("Vuoi richiedere la partecipazione a una partita?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            writer.println("partecipa_match");
                            writer.flush();
                            matchManager.partecipaMatch(br, writer, reader, playerInfo);
                        }
                        System.out.println("Vuoi abbandonare la partita?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            writer.println("leave_match");
                            writer.flush();
                            matchManager.leaveMatch(br, writer, reader, playerInfo);
                        }
                        System.out.println("Vuoi modificare i dati del tuo profilo?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            writer.println("modify_data");
                            writer.flush();
                            playerManager.modifyProfile(br, writer, reader, playerInfo);
                        }
                        System.out.println("Vuoi resettare la tua password?");
                        if(br.readLine().equalsIgnoreCase("Si")){
                            playerManager.resetPsw(br, writer, reader, playerInfo);
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




}




