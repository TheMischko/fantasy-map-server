package com.fantasymap.modelClasses;

public class UserDB {
    public int user_id;
    public String name;
    public String password;

    public UserDB(int user_id, String name, String password) {
        this.user_id = user_id;
        this.name = name;
        this.password = password;
    }
}
