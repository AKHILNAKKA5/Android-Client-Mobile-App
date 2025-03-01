<%@ page import="java.sql.*, java.text.SimpleDateFormat" %>
<%
    String txnid = request.getParameter("txnid"); // Retrieve txnid parameter from the URL
    
    // Connect to the database and retrieve transaction details
    try {
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        Connection conn = DriverManager.getConnection(url, "anakka", "glegrign");
        PreparedStatement pstmt = conn.prepareStatement("SELECT T.t_date AS \"Transaction Date\", T.amount AS \"Transaction Amount\", P.prod_name AS \"Product Name\", P.p_price AS \"Product Price\", TP.quantity AS \"Quantity\" FROM Txns_Prods TP JOIN Products P ON TP.prod_id = P.prod_id JOIN Transactions T ON TP.trans_id = T.trans_id WHERE TP.trans_id = ?");
        pstmt.setString(1, txnid);
        ResultSet rs = pstmt.executeQuery();
        
        // Print Transaction Date and Transaction Amount
        if (rs.next()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
            String formattedDate = dateFormat.format(rs.getTimestamp("Transaction Date"));
%>

<p>Transaction Date: <%= formattedDate %>, Transaction Amount: <%= rs.getInt("Transaction Amount") %></p>

<table>
    <tr>
        <th>Product Name</th>
        <th>Product Price</th>
        <th>Quantity</th>
    </tr>
    <% do { %>
    <tr>
        <td><%= rs.getString("Product Name") %></td>
        <td><%= rs.getInt("Product Price") %></td>
        <td><%= rs.getInt("Quantity") %></td>
    </tr>
    <% } while (rs.next()); %>
</table>

<%
        }
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
