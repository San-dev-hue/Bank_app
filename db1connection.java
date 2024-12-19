/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class db1connection {
    static Connection con;
    public static Connection getConnect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_app","root","Developer@39");
         
        } 
        catch(ClassNotFoundException | SQLException e)
        {
            System.out.println(e);
        }
        return con;
    }
    
}
    

