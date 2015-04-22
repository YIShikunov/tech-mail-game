package frontend.AccountService;

import base.AccountService;
import org.hibernate.SessionFactory;
import utils.SessionHelper;

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
        activeSessions = new HashMap<String, Long>();
    }

    public boolean addUser(String username, String email, String password) {
        UserDataSet user = new UserDataSet(username, email, password);
        userDataSetDAO.addUser(user);
        return true; // TODO: make it actually cathc errors
    }

    public UserDataSet getUser(long id){
        return userDataSetDAO.getUser(id);
    }

    public void addSession(String sessionID, Long userID) {
        activeSessions.put(sessionID, userID);
    }

    public void deleteSession(String sessionID) {
        activeSessions.remove(sessionID);
    }

    public UserDataSet getUserByName(String username) {
        return userDataSetDAO.getUser(username);
    }

    public UserDataSet getUserBySession(String sessionID) {
        Long userID = activeSessions.get(sessionID);
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

    public String getUsernameBySession(String sessionID) {
        return getUserBySession(sessionID).getUsername();
    }
}
