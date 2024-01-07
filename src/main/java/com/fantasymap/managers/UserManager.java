package com.fantasymap.managers;

import com.fantasymap.helpers.Database;
import com.fantasymap.helpers.PasswordHasher;
import com.fantasymap.modelClasses.UserDB;
import com.fantasymap.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public List<String> getUserNames(){
        Database.Init();
        List<User> users = User.findAll();
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add((String) user.get("name"));
        }
        Database.Close();
        return userNames;
    }

    public boolean addUser(String name, String password){
        try {
            Database.Init();
            String hashedPassword = PasswordHasher.hashPassword(password);
            User user = new User();
            user.set("name", name);
            user.set("password", hashedPassword);
            user.saveIt();
            Database.Close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            Database.Close();
            return false;
        }
    }

    public UserDB findUser(String name) {
        Database.Init();
        User user = User.findFirst("name = ?", name);
        if (user == null){
            return null;
        }
        Database.Close();
        return this.createUserDB(user);
    }

    public UserDB findUser(int user_id) {
        Database.Init();
        User user = User.findFirst("user_id = ?", user_id);
        if (user == null){
            return null;
        }
        Database.Close();
        return this.createUserDB(user);
    }

    private UserDB createUserDB(User user){
        Database.Init();
        int id = (int)user.get("user_id");
        String name = (String)user.get("name");
        String password = (String)user.get("password");
        Database.Close();
        return new UserDB(id, name, password);
    }
}
