package frontend.AccountService;

import org.eclipse.jetty.server.Authentication;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="user")
public class UserDataSet implements Serializable {

    private long id;
    private String username;
    private String email;
    private String password;
    private int score;

    public UserDataSet() {}

    public UserDataSet(long id, String username, String email, String password) {
        this.setId(id);
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.setScore(0);
    }

    public UserDataSet(String username, String email, String password) {
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.setScore(0);
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    public long getId() {
        return this.id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="username", unique=true)
    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="email", unique = true)
    public String getEmail() {
        return this.email;
    }

    @Column(name="score")
    public int getScore() {return this.score;}

    public void setScore(int score) { this.score = score; }

    public void setRandomScore() { this.score = Double.valueOf(Math.random()*100).intValue(); }

}
