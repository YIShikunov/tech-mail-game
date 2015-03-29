package frontend.AccountService;

import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private static Map<String, UserProfile> users = new HashMap<>();
    private static Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile gameProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, gameProfile);
        return true;
    }

    public static void addSessions(String sessionId, UserProfile gameProfile) {
        sessions.put(sessionId, gameProfile);
    }
    public static void delSessions(String sessionId) {
        sessions.remove(sessionId);
    }

    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    public static UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public static Integer getCountUsers() {
        return users.size();
    }

    public static Integer getCountLogUsers() {
        return sessions.size();
    }

    public static boolean isAuthorised(String sessionId) { return sessions.containsKey(sessionId); };

    public static String getUsernameBySession(String sessionID) {
        UserProfile user = getSessions(sessionID);
        return user.getLogin();
    }
}
