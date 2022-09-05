package noteme.com.noteme;

import android.support.v7.app.AppCompatActivity;

public class User extends AppCompatActivity{

    String name;
    String email;

    public User() {}
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
