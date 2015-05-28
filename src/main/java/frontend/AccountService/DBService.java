package frontend.AccountService;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class DBService {
    private final SessionFactory sessionFactory;

    public DBService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addUser(UserDataSet user) throws SQLException, ConstraintViolationException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }

    public void updateUser(UserDataSet user) throws SQLException, ConstraintViolationException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }

    // can return null
    public UserDataSet getUser(long id) throws SQLException  {
        Session session = null;
        UserDataSet result = null;
        try {
            session = sessionFactory.openSession();
            result = (UserDataSet) session.get(UserDataSet.class, id);
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return result;
    }

    // can return null
    public UserDataSet getUser(String username) throws SQLException {
        Session session = null;
        UserDataSet result = null;
        try {
            session = sessionFactory.openSession();
            result = (UserDataSet) session.createCriteria( UserDataSet.class ).add(
                    Restrictions.eq("username", username) ).uniqueResult();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return result;
    }

    /*// can return null
    public UserDataSet getAuthorisedUser(String username, String password) throws SQLException {
        Session session = null;
        UserDataSet result = null;
        try {
            session = sessionFactory.openSession();
            result = (UserDataSet) session.createCriteria(UserDataSet.class).add(
                    Restrictions.eq("username", username)).add(
                    Restrictions.eq("password", password)).uniqueResult();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return result;
    }*/

    public List<UserDataSet> getAllUsers() throws SQLException
    {
        Session session = null;
        List<UserDataSet> result = null;
        try {
            session = sessionFactory.openSession();
            result = session.createCriteria(UserDataSet.class).list();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
        return result;
    }

    public void deleteUser(UserDataSet user) throws SQLException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }

    public int countUsers() {
        // TODO: improve this stub
        try
        {
            return getAllUsers().size();
        } catch (SQLException e) {
            return 0;
        }
    }
}
