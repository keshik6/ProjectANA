package sutdcreations.projectana;

import android.app.Application;

import sutdcreations.classes.User;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class GlobalData extends Application {
    private User user;
    public void setUser(User u){
        user = u;
    }
    public User getUser(){
        return user;
    }
}
