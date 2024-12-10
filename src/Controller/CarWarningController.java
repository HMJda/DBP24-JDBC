package Controller;

import DB.DB_Conn;
import DTO.CarWarningDTO;
import java.sql.*;

public class CarWarningController {
    public String registerWarning(CarWarningDTO carWarning)throws SQLException {
        String resultMessage;
        // DB 연결 객체 가져오기
        DB_Conn dbConn = new DB_Conn();
        dbConn.DB_Connect(); // DB 연결 초기화

        String insertQuery = "INSERT INTO 차량경고 (경고ID, 관리자ID , 차량번호, 경고일시, 경고사유) VALUES (?, ?, ?, ? ,? )";

        try (Connection conn = dbConn.getConnection(); // DB 연결
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, carWarning.getWarningId());
            pstmt.setString(2, carWarning.getAdminId());
            pstmt.setString(3, carWarning.getCarNumber());
            pstmt.setDate(4, carWarning.getWarningTimestamp() != null ? new java.sql.Date(carWarning.getWarningTimestamp().getTime()) : null);
            pstmt.setString(5, carWarning.getWarningReason());
            pstmt.executeUpdate();

            resultMessage= "경고가 등록되었습니다.";

        } catch (SQLException ex) {
            ex.printStackTrace();
            resultMessage = "경고 등록에 실패했습니다.";
            return resultMessage;
        }
        dbConn.closeConnection(); // 데이터베이스 연결 종료
        return resultMessage;
    }
}