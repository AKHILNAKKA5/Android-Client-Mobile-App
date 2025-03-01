<%@ page import="java.sql.*" %>
<%
    String ssn = request.getParameter("ssn");
    
    // Connect to the database and retrieve transaction details
    try {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        Connection conn = DriverManager.getConnection(url, "anakka", "glegrign");
        PreparedStatement pstmt = conn.prepareStatement("SELECT DISTINCT award_id FROM GRANTED WHERE ssn =?");
        pstmt.setString(1, ssn);
        ResultSet rs = pstmt.executeQuery();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Award ID</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>


<table>
    <tr>
        <th>Award Id</th>
    </tr>
    <% while (rs.next()) { %>
    <tr>
        <td><%= rs.getInt("award_id") %></td>
    </tr>
    <% } %>
</table>

<%
        // Close resources
        rs.close();
        pstmt.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>

</body>
</html>
