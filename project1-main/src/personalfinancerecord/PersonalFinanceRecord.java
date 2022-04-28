/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personalfinancerecord;

import Database.DBConnection;
import Database.testi;
import frame.LoginForm;
import frame.MainForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ASUS
 */
public class PersonalFinanceRecord {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainForm login = new MainForm();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
//        DBConnection login = new DBConnection();
//        System.out.println(login.open());
    
    DBConnection koneksi = new DBConnection();
        
//    try {
//            Connection con = koneksi.open();
////            String query = "SELECT DISTINCT type FROM categories";
//            String query = "SELECT name FROM categories WHERE type = 'Income'";
//            PreparedStatement ps = con.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()) {
//                String type = rs.getString("name");
//                System.out.println(type);
//           //     cbCategory.addItem(type);
//            }
//        } catch (Exception e) {
//        } 


    }
    
}
