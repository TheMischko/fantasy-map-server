package com.fantasymap.helpers;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;

import javax.xml.crypto.Data;
import java.util.Objects;

public class Database {
    private static DB connection;
    private static String connectionString = "";
    private static String user = "";
    private static String password = "";

    public static void Init(){
        Database.setVariables();
        Database.connection = Base.open("org.postgresql.Driver", Database.connectionString, Database.user, Database.password);
        if (!Database.connection.hasConnection()){
            System.err.println("There is a problem with connection to the database.");
        }
    }

    private static void setVariables(){
        if(!Database.user.isEmpty() && !Database.password.isEmpty() && !Database.connectionString.isEmpty()){
            return;
        }
        String server = System.getenv("DB_SERVER");
        String dbName = System.getenv("DB_DATABASE");
        Database.user = System.getenv("DB_USER");
        Database.password = System.getenv("DB_PASSWORD");
        Database.connectionString = String.format("jdbc:postgresql://%s/%s", server, dbName);
    }

    public static void Close(){
        Database.connection.close();
    }
}
