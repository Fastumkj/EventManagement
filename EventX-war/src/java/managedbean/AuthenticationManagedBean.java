package managedbean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    private String username = null;
    private String password = null;
    private int userId = -1;

    public AuthenticationManagedBean() {
    }

    public String login() {
        if (username.equals("user1") && password.equals("password")) {
            userId = 10;
            return "/auth/createEvent.xhtml?faces-redirect=true";
        } else {
            username = null;
            password = null;
            userId = -1;
            return "/login.xhtml?faces-redirect=true";
        }
    }

    public String logout() {
        username = null;
        password = null;
        userId = -1;
        return "/login.xhtml?faces-redirect=true";

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
