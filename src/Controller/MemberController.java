package Controller;

import DTO.MemberDTO;
import DB.DB_Conn;
import java.sql.*;

public class MemberController {
    public String registerMember(MemberDTO member) throws SQLException {
        String resultMessage;

        // DB 연결 객체 가져오기
        DB_Conn dbConn = new DB_Conn();
        dbConn.DB_Connect(); // DB 연결 초기화
        Connection connection = dbConn.getConnection(); // DB 연결 가져오기

        String sql = "{CALL RegisterMember(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getName());
            stmt.setDate(3, member.getBirthDate() != null ? new java.sql.Date(member.getBirthDate().getTime()) : null);
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getAffiliation());
            stmt.setString(6, member.getAffiliationNumber());
            stmt.setString(7, member.getAddress());
            stmt.setString(8, member.getUsageType());
            stmt.registerOutParameter(9, Types.VARCHAR);

            stmt.execute();

            resultMessage = stmt.getString(9);
        }
        dbConn.closeConnection(); // DB 연결 종료
        return resultMessage;
    }
//    public Vector<Vector> allMember() throws SQLException {
//        String resultMessage;
//
//        // DB 연결 객체 가져오기
//        DB_Conn dbConn = new DB_Conn();
//        dbConn.DB_Connect(); // DB 연결 초기화
//        Connection connection = dbConn.getConnection(); // DB 연결 가져오기
//
//        String sql = "select * from 등록이용객";
//        try (PreparedStatement pstmt = connection.prepareCall(sql)) {
//            Vector<Vector> data = new Vector<>();
//        } finally {
//            dbConn.closeConnection(); // DB 연결 종료
//        }
//        return resultMessage;
//    }
}
