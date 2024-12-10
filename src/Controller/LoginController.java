package Controller;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import DB.DB_Conn;
// 로그인 DB 연결
public class LoginController {
    public boolean authenticate(String adminId, String password) {
        String sql = "{ call admin_Login(?, ?, ?) }"; // 프로시저 호출
        DB_Conn dbConn = new DB_Conn();
        dbConn.DB_Connect(); // 데이터베이스 연결

        try (Connection conn = dbConn.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, adminId);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, java.sql.Types.VARCHAR); // 출력 파라미터 등록

            cstmt.execute(); // 프로시저 실행

            String isValid = cstmt.getString(3); // 유효성 결과 가져오기

            dbConn.closeConnection(); // 데이터베이스 연결 종료
            return "Y".equals(isValid); // 유효한 경우 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
            dbConn.closeConnection(); // 데이터베이스 연결 종료
            return false; // 예외 발생 시 false 반환
        }
    }
}
