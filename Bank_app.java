 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bank_app;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.Connection; 
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Lenovo
 */
public class Bank_app {

    Scanner s;
    public static void main(String[] args) {
        
        Bank_app  ba = new Bank_app();
        
        ba.startBankApp(); 
        
      }
    
    void startBankApp()
    {
        System.out.println("\n================");
        System.out.println("1.Add Account");
        System.out.println("2.Fund Transfer");
        System.out.println("3.Mini Statement");
        System.out.println("4.Exit");
        System.out.println("\n Select any one option");
        System.out.println("========================");
        
        getUserInput();
    }   

    void getUserInput()
    {
         s = new Scanner(System.in);
        int useroption=s.nextInt();
        
        System.out.println("===========================");
        
        if(useroption==1)
		{
			addAccount();
		}
		else if (useroption==2)
		{
			fundTransfer();
		}
		else if(useroption==3)
		{
			miniStatement();
		}
		else
		{
			System.out.println("Bank App CLosed");
			System.exit(0);
		}
        
       } 
       
      void addAccount()
      {
          System.out.println("Enter  Id : ");
          int id =s.nextInt();
          
          System.out.println("Enter Name : ");
          String name =s.next();
          
          System.out.println("Enter Email : ");
          String email =s.next();
          
          System.out.println("Enter Phone_no : ");
          String Phn_no =s.next();
          
          System.out.println("Enter Accnt_no : ");
          int Accnt_no =s.nextInt();
          
          Connection con= null;
          try
          {
              con=db1connection.getConnect();
              
              con.setAutoCommit(false);
              
            
             
             PreparedStatement ps1 = con.prepareStatement("insert into users values (?,?,?,?,?)");
             
             ps1.setInt(1,id);
             ps1.setString(2,name);
             ps1.setString(3,email);
             ps1.setString(4,Phn_no);
             ps1.setInt(5,Accnt_no);
             
             int rowCount1 =ps1.executeUpdate();
             
             PreparedStatement ps2 = con.prepareStatement("insert into total_amount (id, accnt_no, balance) values (?,?,?)");
             ps2.setInt(1,id);
             ps2.setInt(2,Accnt_no);
             ps2.setInt(3,50000);
             
             int rowCount2 =ps2.executeUpdate();
             
             if(rowCount1 > 0 && rowCount2 >0)
             {
                 con.commit();
                 System.out.println("Account has been created successfully");
             }
             else
             {
                 con.rollback();
                 System.out.println("Account creation failed due to some error");
             }    
          } 
          catch(SQLException e)
          {
              try
              {
                  con.rollback();
              }
              catch(SQLException ee)
              {
                  System.out.println(ee);
              } 
              System.out.println(e);  
          } 
          
          startBankApp();
      } 
      void fundTransfer()
      {
          System.out.println("Enter From Account No: ");
          int from_accnt =s.nextInt(); 
          
          System.out.println("Enter to Account No: ");
          int to_accnt =s.nextInt(); 
          
          System.out.println("Enter Amount: ");
          int deposit_amt =s.nextInt(); 
          
          int from_bal=0,to_bal=0;
          
          Connection con =null;
          try
          {
              con =db1connection.getConnect();
              PreparedStatement ps3 = con.prepareStatement("select balance from total_amount where accnt_no=?");
              ps3.setInt(1,from_accnt);
              
              ResultSet rs =ps3.executeQuery();
              
              while(rs.next())
              {
                  from_bal=rs.getInt(1);
              }
              
               
              PreparedStatement ps4 = con.prepareStatement("select balance from total_amount where accnt_no=?");
              ps4.setInt(1,to_accnt);
              
              ResultSet rs1 =ps4.executeQuery();
              
              while(rs1.next())
              {
                  to_bal=rs1.getInt(1);
              }
              int new_from_bal= from_bal-deposit_amt;
              int new_to_bal= to_bal+deposit_amt;
              
              PreparedStatement ps5 = con.prepareStatement("update total_amount set balance=? where accnt_no=?");
              ps5.setInt(1,new_from_bal);
              ps5.setInt(2,from_accnt);
              
              int rowCount1=ps5.executeUpdate();
              
              PreparedStatement ps6 = con.prepareStatement("update total_amount set balance=? where accnt_no=?");
              ps6.setInt(1,new_to_bal);
              ps6.setInt(2,to_accnt);
              
              int rowCount2=ps6.executeUpdate();
              
              //--------------get the system current date & time--------------
              
              Date d = new Date();
              
              SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
              String date1=sdf1.format(d);
              
              SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
              String time1=sdf2.format(d);
              
              //---------------------------------------------
              
              PreparedStatement ps7 = con.prepareStatement("insert into mini_statement values(?,?,?,?,?)");
              
               ps7.setInt(1,from_accnt);
               ps7.setInt(2,deposit_amt);
               ps7.setString(3,"Debit");
               ps7.setString(4,date1);
               ps7.setString(5,time1);
               
               int rowCount3=ps7.executeUpdate();
               
                PreparedStatement ps8 = con.prepareStatement("insert into mini_statement values(?,?,?,?,?)");
              
               ps8.setInt(1,to_accnt);
               ps8.setInt(2,deposit_amt);
               ps8.setString(3,"Credit");
               ps8.setString(4,date1);
               ps8.setString(5,time1);
               
               int rowCount4=ps8.executeUpdate();
               
               if(rowCount1>0 && rowCount2>0 && rowCount3>0 && rowCount4>0)
               {
                   con.commit();
                   System.out.println("Amount deposited Successfully");
               } 
               else
               {
                   con.rollback();
                   System.out.println("Transaction failed");
               }   
          } 
          catch(SQLException e)
          {
              try
              {
                  con.rollback();
              }
              catch(SQLException ee)
              {
                  System.out.println(ee);
              }   
               System.out.println(e);       
                      
          }
          startBankApp();
      }
      void miniStatement()
      {
          StringBuffer bf = new StringBuffer();
          
          System.out.println("Enter accnt_no");
          int accnt_no=s.nextInt();
          
          bf.append("Below is the transaction details for '"+accnt_no+"'account no\n\n");
          
//          Connection con =null; 
          try(Connection con = db1connection.getConnect();PreparedStatement ps = con. prepareStatement("select * from mini_statement where account_no=?")){
          ps.setInt(1,accnt_no);
          
          try(ResultSet rs =ps.executeQuery()){
          while(rs.next())
          {
              System.out.println(rs.getInt(2));
              bf.append(rs.getInt(2));
              System.out.println("\t"+rs.getString(3));
              bf.append("\t"+rs.getString(3));
              System.out.println("\t"+rs.getString(4));
              bf.append("\t"+rs.getString(4));
              System.out.println("\t"+rs.getString(5));
              bf.append("\t"+rs.getString(5));
              
              System.out.println();
              bf.append("\n");
            }
            generateMinistatementFile(bf,accnt_no);
           System.out.println("Mini-statement generated successfully.");
          }
} 
          
         
          catch(SQLException e)
          {
              System.out.println(e);
          } 
          
          
             
      }   
       public void generateMinistatementFile(StringBuffer details,int accno)
          {
              String st = details.toString();
             try
             {
                  try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Lenovo\\Desktop\\cc.txt")) {
                      fos.write(st.getBytes());
                  }
               
             }
             catch(IOException e)
             {
                 System.out.println(e);
             }   
          }     
           
           
    
}
