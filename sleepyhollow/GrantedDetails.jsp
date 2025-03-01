<%@ page import="java.sql.*, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <title>Granted Details</title>
</head>
<body>

    <%
        // Get parameters award_id and ssn
        String awardIdParam = request.getParameter("awardid");
        String ssn = request.getParameter("ssn");

        // Parse award_id parameter to int
        int award_id = Integer.parseInt(awardIdParam);

        // Connect to the database
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
            conn = DriverManager.getConnection(url, "anakka", "glegrign");

            // Prepare and execute SQL query
            pstmt = conn.prepareStatement("SELECT G.award_date AS \"Award Date\", AC.center_name AS \"Award Center Name\" FROM GRANTED G JOIN Award_Centers AC ON G.center_id = AC.center_id WHERE G.award_id = ? AND G.ssn = ?");
            pstmt.setInt(1, award_id);
            pstmt.setString(2, ssn);
            rs = pstmt.executeQuery();

            // Output results in a table
    %>
            <table border="1">
                <tr>
                    <th>Award Date</th>
                    <th>Award Center Name</th>
                </tr>
    <%
                while (rs.next()) {
                    // Format the date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
                    String formattedDate = dateFormat.format(rs.getTimestamp("Award Date"));
    %>
                    <tr>
                        <td><%= formattedDate %></td>
                        <td><%= rs.getString("Award Center Name") %></td>
                    </tr>
    <%
                }
    %>
            </table>
    <%
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    %>
</body>
</html>
