package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class AccountService {
    private static Map<String, UserProfile> users = new HashMap<>();
    private static Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userProfile);
        return true;
    }

    public static void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
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
}
