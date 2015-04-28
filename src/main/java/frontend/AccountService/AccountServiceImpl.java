package frontend.AccountService;

import base.AccountService;
import org.json.simple.JSONObject;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class AccountServiceImpl implements AccountService {
    private static Map<String, UserProfile> users = new HashMap<>();
    private static Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile gameProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, gameProfile);
        return true;
    }

    public AccountServiceImpl()
    {
        //Debug
        addUser("cat", new UserProfile("cat", "cat", "cat@yandex.ru"));
        addUser("qwerty", new UserProfile("qwerty", "qwerty", "qwerty@google.com"));
        addUser("mail", new UserProfile("mail", "mail", "mail@my.com"));
        addUser("dobby", new UserProfile("dobby", "dobby", "dobby@my.com"));
        addUser("clob", new UserProfile("clob", "clob", "clob@google.com"));
        //Debug
    }

    public void addSessions(String sessionId, UserProfile gameProfile) {
        sessions.put(sessionId, gameProfile);
    }
    public void delSessions(String sessionId) {
        sessions.remove(sessionId);
    }

    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public Integer getCountUsers() {
        return users.size();
    }

    public Integer getCountLogUsers() {
        return sessions.size();
    }

    public boolean isAuthorised(String sessionId) { return sessions.containsKey(sessionId); }

    public String getUsernameBySession(String sessionID) {
        UserProfile user = getSessions(sessionID);
        return user.getLogin();
    }

    public List<JSONObject> getScoreBoard() {
        List<JSONObject> scores = new ArrayList<>();
        int k = 0;
        for (Object key : users.keySet()) {
            if ( k<5 ) k++; else break;
            JSONObject score = new JSONObject();
            score.put("name", key.toString());
            score.put("score", users.get(key).getScores());
            scores.add(score);
        }
        return scores;
    }

}
