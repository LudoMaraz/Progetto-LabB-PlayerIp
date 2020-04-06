public class PlayerInfo {
    String nome;
    String cognome;
    String nickname;
    String indirizzo_email;
    String password;
    long codiceAuth;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIndirizzo_email() {
        return indirizzo_email;
    }

    public void setIndirizzo_email(String indirizzo_email) {
        this.indirizzo_email = indirizzo_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCodiceAuth() {
        return codiceAuth;
    }

    public void setCodiceAuth(long codiceAuth) {
        this.codiceAuth = codiceAuth;
    }
}
