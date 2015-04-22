package frontend.AccountService;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class UserDataSetDAO {
    private SessionFactory sessionFactory;

    public UserDataSetDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addUser(UserDataSet user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    public UserDataSet getUser(long id) {
        Session session = sessionFactory.openSession();
        UserDataSet result = (UserDataSet) session.get(UserDataSet.class, id);
        session.close();
        return result;
    }

    public UserDataSet getUser(String username) {
        Session session = sessionFactory.openSession();
        UserDataSet result = (UserDataSet) session.createCriteria( UserDataSet.class ).add(
                Restrictions.eq("username", username) ).uniqueResult();
        session.close();
        return result;
    }

    public UserDataSet getAuthorisedUser(String username, String password) {
        Session session = sessionFactory.openSession();
        UserDataSet result = (UserDataSet) session.createCriteria(UserDataSet.class).add(
                Restrictions.eq("username", username) ).add(
                Restrictions.eq("password", password)).uniqueResult();
        session.close();
        return result;
    }

    // read all

    // delete user

    public Integer countUsers() {
        return 0;
        //TODO: implement
    }
}
