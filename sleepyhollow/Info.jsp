<%@ page import="java.sql.*" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.*" %>
<%
    String ssn = request.getParameter("ssn");
    String employeeName = "";
    int totalSales = 0; // Change totalSales to int to remove decimal part
    
    // Connect to the database and retrieve employee name
    try {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        Connection conn = DriverManager.getConnection(url, "anakka", "glegrign");
        PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM Employees WHERE ssn=?");
        pstmt.setString(1, ssn);
        ResultSet rs = pstmt.executeQuery();
        
        // If employee with given SSN is found, retrieve the name
        if (rs.next()) {
            employeeName = rs.getString("name");
        }
        
        // Close resources
        rs.close();
        pstmt.close();
        
        // Query to retrieve total sales for the employee
        String salesQuery = "SELECT total_sales FROM Emp_Sales WHERE ssn = ?";
        PreparedStatement salesStmt = conn.prepareStatement(salesQuery);
        salesStmt.setString(1, ssn);
        ResultSet salesRs = salesStmt.executeQuery();
        
        // If a row is found, retrieve the total sales value
        if (salesRs.next()) {
            totalSales = salesRs.getInt("total_sales"); // Change to getInt to remove decimal part
        }
        
        // Close resources
        salesRs.close();
        salesStmt.close();
        
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // Output employee name and total sales
    out.print(employeeName + ":" + totalSales);
%>
