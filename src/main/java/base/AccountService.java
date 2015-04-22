package base;

import frontend.AccountService.UserDataSet;

public interface AccountService
{
    public boolean addUser(String username, String email, String password);
    public UserDataSet getUser(long id);
    public void addSession(String sessionID, Long userID);
    public void deleteSession(String sessionID);
    public UserDataSet getUserByName(String username);
    public UserDataSet getUserBySession(String sessionID);
    public Integer getCountUsers();
    public Integer getCountLoggedInUsers();
    public boolean isAuthorised(String sessionID);
    public String getUsernameBySession(String sessionID);
}