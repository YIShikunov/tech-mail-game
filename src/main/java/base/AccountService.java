package base;

import frontend.AccountService.UserDataSet;

import java.sql.SQLException;

public interface AccountService
{
    public boolean addUser(String username, String email, String password);
    public UserDataSet getUser(long id) throws SQLException;
    public void addSession(String sessionID, Long userID);
    public void deleteSession(String sessionID);
    public UserDataSet getUserByName(String username) throws SQLException;
    public UserDataSet getUserBySession(String sessionID) throws SQLException;
    public Integer getCountUsers();
    public Integer getCountLoggedInUsers();
    public boolean isAuthorised(String sessionID);
    public String getUsernameBySession(String sessionID) throws SQLException;
    public void updateUser(UserDataSet user) throws SQLException;
    public boolean deleteUser(String username);
}