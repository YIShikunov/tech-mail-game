package frontend.AccountService;

import base.AccountService;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import utils.SessionHelper;

import java.sql.SQLException;
import java.util.HashMap;

public class AccountServiceImpl implements AccountService {

    //// SINGLETON
    private static AccountServiceImpl instance;
    public static AccountServiceImpl getInstance() {
        if (instance == null) {
            instance = new AccountServiceImpl();
        }
        return instance;
    }
    //// SINGLETON

    SessionFactory sessionFactory;
    UserDataSetDAO userDataSetDAO;
    static HashMap<String, Long> activeSessions;

    private AccountServiceImpl() {
        sessionFactory = SessionHelper.createSessionFactory();
        userDataSetDAO = new UserDataSetDAO(sessionFactory);
        activeSessions = new HashMap<>();
    }

    public boolean addUser(String username, String email, String password) {
        UserDataSet user = new UserDataSet(username, email, password);
        try {
            userDataSetDAO.addUser(user);
            return true;
        } catch (SQLException e) {
            return false;
        } catch (ConstraintViolationException e) {
            return false;
        }
    }

    // can return null
    public UserDataSet getUser(long id) throws SQLException {
        return userDataSetDAO.getUser(id);
    }

    public void updateUser(UserDataSet user) throws SQLException {
        userDataSetDAO.updateUser(user);
    }

    public void addSession(String sessionID, Long userID) {
        activeSessions.put(sessionID, userID);
    }

    public void deleteSession(String sessionID) {
        activeSessions.remove(sessionID);
    }

    // can return null
    public UserDataSet getUserByName(String username) throws SQLException  {
        return userDataSetDAO.getUser(username);
    }

    // can return null
    public UserDataSet getUserBySession(String sessionID) throws SQLException {
        Long userID = activeSessions.get(sessionID);
        if (userID == null)
            return null;
        return userDataSetDAO.getUser(userID);
    }

    public Integer getCountUsers() {
        return userDataSetDAO.countUsers();
    }

    public Integer getCountLoggedInUsers() {
        return activeSessions.size();
    }

    public boolean isAuthorised(String sessionID) {
        return activeSessions.containsKey(sessionID);
    }

    public String getUsernameBySession(String sessionID) throws SQLException {
        UserDataSet user = getUserBySession(sessionID);
        return user == null ? null : user.getUsername();
    }

    public boolean deleteUser(String username) {
        try {
            UserDataSet user = getUserByName(username);
            if (user == null) {
                return false;
            }
            userDataSetDAO.deleteUser(user);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
