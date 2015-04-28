package frontend.AccountService;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Random;

public class UserProfile {
    private final String login;
    private final String password;
    private final String email;
    private Integer scores=0;

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        scores =  Double.valueOf(Math.random()*100).intValue();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Integer getScores() {
        return scores;
    }

}
