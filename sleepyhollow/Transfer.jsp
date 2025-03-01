<%@ page import="java.sql.*,java.io.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Transfer Page</title>
</head>
<body>

    <%
        // Get parameters such as from_ssn,to_ssn and amount to transfer
        String from_ssn = request.getParameter("ssn1");
        String to_ssn = request.getParameter("ssn2");
        double amount = Double.parseDouble(request.getParameter("amount"));

        // database connection to gmu database
        Class.forName("oracle.jdbc.OracleDriver");
        String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        Connection conn = DriverManager.getConnection(url, "anakka", "glegrign");

        // sql stmnt
        String sql = "INSERT INTO Transfer (from_ssn, to_ssn, transfer_date, amount) VALUES (?, ?, SYSDATE, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, from_ssn);
        pstmt.setString(2, to_ssn);
        pstmt.setDouble(3, amount);

        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        
        if (rowsAffected > 0) {
    %>
            <p>Transfer completed successfully.</p>
    <%
        } else {
    %>
            <p>Transfer failed. Please check the input parameters.</p>
    <%
        }
    %>
</body>
</html>
