package myapp.forms;

import myapp.models.User;

public class UserForm extends User {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}
