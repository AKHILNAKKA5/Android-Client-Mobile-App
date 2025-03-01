<%@ page import="java.sql.*" %>
<%
    String ssn = request.getParameter("ssn");
    
    // Connect to the database and retrieve transaction details
    try {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        Connection conn = DriverManager.getConnection(url, "anakka", "glegrign");
        PreparedStatement pstmt = conn.prepareStatement("SELECT trans_id as Txn_ID, t_date as transaction_date, amount FROM Transactions WHERE ssn=?");
        pstmt.setString(1, ssn);
        ResultSet rs = pstmt.executeQuery();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Transactions</title>
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

<h2>Transactions</h2>
<table>
    <tr>
        <th>Txn_ID</th>
    </tr>
    <% while (rs.next()) { %>
    <tr>
        <td><%= rs.getString("Txn_ID") %></td>
        
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
