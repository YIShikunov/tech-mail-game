package base;

import frontend.AccountService.UserProfile;

public interface AccountService
{
    boolean addUser(String name, UserProfile user);
    void addSessions(String sessionId, UserProfile gameProfile);
    void delSessions(String sessionId);
    UserProfile getUser(String username);
    UserProfile getSessions(String session);
    Integer getCountUsers();
    Integer getCountLogUsers();
    boolean isAuthorised(String sessionId);
    String getUsernameBySession(String sessionID);



}
