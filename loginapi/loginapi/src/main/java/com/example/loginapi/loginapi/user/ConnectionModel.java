package com.example.loginapi.loginapi.user;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionModel {

    static public Connection getConnection() throws Exception {
        Connection c = null;
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/logindb","postgres", "vrivjti@12345");
//        URI dbUri = new URI(System.getenv("DATABASE_URL"));
//
//        String username = dbUri.getUserInfo().split(":")[0];
//        String password = dbUri.getUserInfo().split(":")[1];
//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
//
//        c = DriverManager.getConnection(dbUrl, username, password);
        return c;

    }
}