package com.matthewmanopoli;

import java.sql.*;

/**
 * Created by mmanopoli on 8/27/17.
 */
public class DB {

    public Connection conn = null;

    public DB(String pathToDB) {
        String url = "jdbc:sqlite:"+pathToDB;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    public void runSql2(String sql) throws SQLException {
//        try (
//                // prepare the statement
//                PreparedStatement pstmt = conn.prepareStatement(sql)
//            ) {
//            // execute the delete statement
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public ResultSet runSql(String sql) throws SQLException {
        Statement stmt  = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public boolean runSql2(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.execute(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }

}
