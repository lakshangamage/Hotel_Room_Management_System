/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intelligentz.appointmentz.controllers;

import com.mysql.jdbc.Connection;
import com.intelligentz.appointmentz.database.connectToDB;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ndine
 */
public class deleteRPI extends HttpServlet{  
    connectToDB con;
    
    @Override
    public void doPost(HttpServletRequest req,HttpServletResponse res)  throws ServletException,IOException  
    {  
        try {
            String serial = req.getParameter("serial");
            con = new connectToDB();
            if(con.connect()){
                Connection  connection = con.getConnection();
                Class.forName("com.mysql.jdbc.Driver");
                Statement stmt = connection.createStatement( ); 
                String SQL,SQL1;
                SQL1 = "delete from db_bro.rpi where serial=?";
                PreparedStatement preparedStmt = connection.prepareStatement(SQL1);
                    preparedStmt.setString (1, serial);
                      
                // execute the preparedstatement
                preparedStmt.execute();
                
                SQL = "select * from db_bro.rpi";
                ResultSet rs = stmt.executeQuery(SQL);
                
                if(rs.wasNull()){
                    displayMessage(res,"response is null");
                }
                boolean check = false;
                while ( rs.next( ) ) {
                    
                    String db_serial = rs.getString("serial");
                        
                    if((serial == null ? db_serial == null : serial.equals(db_serial))){
                        displayMessage(res,"Delete action failed!!!");
                        check=true;
                    } 
                }
                if(!check){
                    
                        try {
                            connection.close();
                        } catch (SQLException e) { 
                            displayMessage(res,"SQLException: "+e);
                        }
                        res.sendRedirect("./equipments");
                    //displayMessage(res,"SQL query Failed!");
                }
            }
            else{
                con.showErrormessage(res);
            }
            
            
            /*res.setContentType("text/html");//setting the content type
            PrintWriter pw=res.getWriter();//get the stream to write the data
            
            //writing html in the stream
            pw.println("<html><body>");
            pw.println("Welcome to servlet: "+username);
            pw.println("</body></html>");
            
            pw.close();//closing the stream
            */
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(authenticate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void displayMessage (HttpServletResponse res,String s) throws IOException{
        res.setContentType("text/html");//setting the content type
        PrintWriter pw=res.getWriter();//get the stream to write the data
        //writing html in the stream
        pw.println("<html><body>");
        pw.println("Info: "+s);
        pw.println("</body></html>");

        pw.close();//closing the stream
    }
}  
