package frontend.AccountService;

import base.AccountService;

import java.util.HashMap;
import java.util.Map;

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
        addUser("123", new UserProfile("123", "123", "123@123"));
        addUser("234", new UserProfile("234", "234", "123@123"));
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
}
