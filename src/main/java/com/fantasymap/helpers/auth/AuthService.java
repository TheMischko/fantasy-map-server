package com.fantasymap.helpers.auth;

import com.fantasymap.modelClasses.UserDB;

public interface AuthService {
    String authenticate(String username, int user_id);
    boolean isValid(String token);
}
