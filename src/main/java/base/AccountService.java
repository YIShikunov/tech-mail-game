package base;

import frontend.AccountService.UserProfile;

public interface AccountService
{
    boolean addUser(String name, UserProfile user);
    static void addSessions(String name, UserProfile gameProfile){}
    static void delSessions(String name, UserProfile gameProfile){}
    UserProfile getUser(String username);
    static UserProfile getSessions(String session){return null;}
    static Integer getCountUsers() { return 0; }
    static Integer getCountLogUsers() { return 0; }
    static boolean isAuthorised(String sessionId) {return false;}
    static String getUsernameBySession(String sessionID)  {return null;}



}
