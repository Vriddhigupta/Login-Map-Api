package com.example.loginapi.loginapi.bank;

import com.example.loginapi.loginapi.user.ConnectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bank/mumbai")
public class BankController {
    @GetMapping("")
    public ResponseEntity<List<Bank>> getAll(){
        List<Bank> temp = new ArrayList<Bank>();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");

            c = ConnectionModel.getConnection();
            c.setAutoCommit(false);
            stmt = c.createStatement();
            rs = stmt.executeQuery( "SELECT bank,ifsc,branch,address,city1,city2,state,stdcode,phone,longitude,latitude,"
                    + "ST_AsGeoJSON(location) from bank_table;" );

            while ( rs.next() ) {
                String bank = rs.getString("bank");
                String  ifsc = rs.getString("ifsc");
                String branch = rs.getString("branch");
                String address = rs.getString("address");
                String city1 = rs.getString("city1");
                String city2 = rs.getString("city2");
                String state = rs.getString("state");
                String phone = rs.getString("phone");
                int stdcode = rs.getInt("stdcode");
                double longitude = rs.getDouble("longitude");
                double latitude = rs.getDouble("latitude");
                String location = rs.getString("st_asgeojson");
                temp.add(new Bank(bank,ifsc,branch,address,city1,city2,state,stdcode,phone,longitude,latitude,location));
            }
            rs.close();
            stmt.close();
            c.close();
            return new ResponseEntity<>(temp, HttpStatus.OK);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/nearest")
    public ResponseEntity<List<Bank>> getAllNearest(@RequestBody NearBody nearBody){
        List<Bank> temp = new ArrayList<Bank>();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");

            c = ConnectionModel.getConnection();
            c.setAutoCommit(false);


            stmt = c.createStatement();
            rs = stmt.executeQuery( "SELECT bank,ifsc,branch,address,city1,city2,state,stdcode,phone,longitude,latitude,"
                    + "ST_AsGeoJSON(location),ST_Distance(location::geography,ST_GeomFromText('POINT("+nearBody.getLongitude()+" "+nearBody.getLatitude()+")')::geography) as dt " +
                    "from bank_table " +
                    "where ST_Distance(location::geography,ST_GeomFromText('POINT("+nearBody.getLongitude()+" "+nearBody.getLatitude()+")')::geography)<="+nearBody.getDistance()+";");

            while ( rs.next() ) {
                String bank = rs.getString("bank");
                String  ifsc = rs.getString("ifsc");
                String branch = rs.getString("branch");
                String address = rs.getString("address");
                String city1 = rs.getString("city1");
                String city2 = rs.getString("city2");
                String state = rs.getString("state");
                String phone = rs.getString("phone");
                int stdcode = rs.getInt("stdcode");
                double longitude = rs.getDouble("longitude");
                double latitude = rs.getDouble("latitude");
                double dt = rs.getDouble("dt");
                String location = rs.getString("st_asgeojson");

                temp.add(new Bank(bank,ifsc,branch,address,city1,city2,state,stdcode,phone,longitude,latitude,location,dt));
            }
            rs.close();
            stmt.close();
            c.close();
            return new ResponseEntity<>(temp, HttpStatus.OK);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
