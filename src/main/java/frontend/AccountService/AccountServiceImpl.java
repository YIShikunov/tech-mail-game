package frontend.AccountService;

import base.AccountService.AccountService;
import org.hibernate.exception.ConstraintViolationException;
import utils.SessionHelper;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AccountServiceImpl implements AccountService {

    private final DBService DBService;
    private final HashMap<String, Long> activeSessions;

    public AccountServiceImpl() {
        DBService = new DBService(SessionHelper.createSessionFactory());
        activeSessions = new HashMap<>();
    }

    public boolean addUser(String username, String email, String password) {
        UserDataSet user = new UserDataSet(username, email, password);
        try {
            DBService.addUser(user);
            return true;
        } catch (SQLException e) {
            return false;
        } catch (ConstraintViolationException e) {
            return false;
        }
    }

    // can return null
    public UserDataSet getUser(long id) throws SQLException {
        return DBService.getUser(id);
    }

    public void updateUser(UserDataSet user) throws SQLException {
        DBService.updateUser(user);
    }

    public void addSession(String sessionID, Long userID) {
        activeSessions.put(sessionID, userID);
    }

    public void deleteSession(String sessionID) {
        activeSessions.remove(sessionID);
    }

    // can return null
    public UserDataSet getUserByName(String username) throws SQLException  {
        return DBService.getUser(username);
    }

    // can return null
    public UserDataSet getUserBySession(String sessionID) throws SQLException {
        Long userID = activeSessions.get(sessionID);
        if (userID == null)
            return null;
        return DBService.getUser(userID);
    }

    public Integer getCountUsers() {
        return DBService.countUsers();
    }

    public Integer getCountLoggedInUsers() {
        return activeSessions.size();
    }

    public boolean isAuthorised(String sessionID) {
        return activeSessions.containsKey(sessionID);
    }

    public String getUsernameBySession(String sessionID) throws SQLException{
        UserDataSet user = getUserBySession(sessionID);
        return user == null ? null : user.getUsername();
    }
    public boolean deleteUser(String username)
    {
        try
        {
            UserDataSet user = getUserByName(username);
            if (user == null)
            {
                return false;
            }
            DBService.deleteUser(user);
            return true;
        } catch (SQLException e)
        {
            throw new RuntimeException();
        }
    }

    class UsersScoreComparator implements Comparator<UserDataSet>
    {

        public int compare(UserDataSet o1, UserDataSet o2)
        {
            if (o1.getScore() < o2.getScore())
                return -1;
            else if (o1.getScore() > o2.getScore())
                return  1;
            else return 0;
        }
    }

    public ArrayList<JSONObject> getScoreBoard(){
        ArrayList<JSONObject> scores = new ArrayList<>();
        try
        {
            ArrayList<UserDataSet> users = new ArrayList<>(DBService.getAllUsers()) ;
            users.sort(new UsersScoreComparator());
            int t =  users.size() - 1;
            for (int i = t  ; i > -1 && i > t -5 ; i-- ) {
                JSONObject score = new JSONObject();
                score.put("name", users.get(i).getUsername());
                score.put("score", users.get(i).getScore());
                scores.add(score);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.toString());
            return scores;
        }
        return scores;
    }
}
