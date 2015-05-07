package frontend.AccountService;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDataSetDAO {
    private SessionFactory sessionFactory;

    private int userCount; // I don't know how to actually count users in Hibernate
                            // TODO: find out how to do it properly

    public UserDataSetDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.userCount = 0;
    }

    public void addUser(UserDataSet user) throws SQLException, ConstraintViolationException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            this.userCount += 1;
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }

    // untested
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

    // can return null
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
    }

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

    // (!) unused, untested, untrusted
    public void deleteUser(UserDataSet user) throws SQLException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
            this.userCount -= 1;
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }

    public int countUsers() {
        // (!) Does not work as it should yet.
        return this.userCount;
    }
}
