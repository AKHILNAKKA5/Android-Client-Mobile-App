/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**
 *
 * @author akhil
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class TestServlet extends HttpServlet {

public void doGet(HttpServletRequest request, HttpServletResponse response){
    
    try{
        String user=request.getParameter("user");
        String pass=request.getParameter("pass");
        
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu","anakka","glegrign");
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery("Select * from login where username='"+user+"' AND password='"+pass+"'");
        PrintWriter out=response.getWriter();
        String result="";
        while(rs.next()){
            result+=rs.getObject(1);
            
        }
        if(result.equals("")){
            out.print("No");
        }
        else{
            out.print(result);
        }
    }
    catch(Exception e){}
    }
}