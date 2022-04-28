/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Rumah
 */
public class testi {
 
    
    public void tried() {
        DBConnection koneksi = new DBConnection();
        
    try {
            Connection con = koneksi.open();
//            String query = "SELECT DISTINCT type FROM categories";
            String query = "SELECT name FROM categories WHERE type = Income";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String type = rs.getString("name");
                System.out.println(type);
           //     cbCategory.addItem(type);
            }
        } catch (Exception e) {
        } }
}
