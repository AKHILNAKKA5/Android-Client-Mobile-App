
<%@ page import="java.sql.*" %>
<%
    String ssn = request.getParameter("ssn");
    String employeeName = "";
    
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
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // Output employee name
    out.print(employeeName);
%>
