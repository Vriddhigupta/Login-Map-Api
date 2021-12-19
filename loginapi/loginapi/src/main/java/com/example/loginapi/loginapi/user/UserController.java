package com.example.loginapi.loginapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @PostMapping("/login")
    public ResponseEntity<HashMap<String,Object>> login(@RequestBody login_user user){
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        HashMap<String,Object> h = new HashMap<String,Object>();
        try {
            Class.forName("org.postgresql.Driver");

            c = ConnectionModel.getConnection();
            c.setAutoCommit(false);

            st = c.prepareStatement("Select id,email, password from user_info where email=? and password=?");
            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword());
            rs = st.executeQuery();


            if ( rs.next() ) {
                int id = rs.getInt("id");
                h.put("found", true);
                h.put("id", id);
            }
            else {
                h.put("found", false);
            }
            rs.close();
            st.close();
            c.close();
            return new ResponseEntity<>(h, HttpStatus.OK);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            return new ResponseEntity<>(h,HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<HashMap<String,Object>> signup(@RequestBody User user){
        Connection c = null;
        PreparedStatement st = null;
        HashMap<String,Object> h1 = new HashMap<String,Object>();

        try {
            System.out.println(user.getEmail()+user.getPassword());
            Class.forName("org.postgresql.Driver");

            c = ConnectionModel.getConnection();
            System.out.println(c);
            System.out.println(user.getEmail()+user.getPassword()+user.getId());

            st = c.prepareStatement("insert into user_info(email,password,id) values(?,?,?)");
            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword());
            st.setInt(3,user.getId());
            st.executeUpdate();
            h1.put("id",user.getId());
            st.close();
            c.close();
            return new ResponseEntity<>(h1,HttpStatus.CREATED);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            HashMap<String,Object> h = new HashMap<String,Object>();
            h.put("error", e.getClass().getName()+": "+ e.getMessage());
            return new ResponseEntity<>(h,HttpStatus.BAD_REQUEST);
        }
    }
}