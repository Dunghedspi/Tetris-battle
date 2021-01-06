package server;

public class Room {
    private String username1;
    private String username2;
    private String token;
    private Connection client1;
    private Connection client2;
    private GamePlay p;
    private boolean isPublic;

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public GamePlay getP() {
        return p;
    }

    public void setP(GamePlay p) {
        this.p = p;
    }

    public Room (Connection client1){
        this.client1 = client1;
        this.client2 = null;
        this.isPublic = false;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Connection getClient1() {
        return client1;
    }

    public void setClient1(Connection client1) {
        this.client1 = client1;
    }

    public Connection getClient2() {
        return client2;
    }

    public void setClient2(Connection client2) {
        this.client2 = client2;
    }
}
