import org.json.simple.JSONObject;

public class Credentials {
    private String email;

    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public JSONObject toJSON() {
        JSONObject credentials = new JSONObject();
        credentials.put("email", this.email);
        credentials.put("password", this.password);
        return credentials;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
