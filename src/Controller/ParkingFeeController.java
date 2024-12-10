package Controller;

import DB.DB_Conn;
import java.sql.*;

public class ParkingFeeController {

    // 차량 번호에 해당하는 회원 이름과 요금을 계산하여 반환
    public String calculateFeeWithMemberInfo(String carNumber) {
        DB_Conn dbConn = new DB_Conn();
        dbConn.DB_Connect();
        Connection con = dbConn.getConnection();

        String resultMessage = null;

        // 프로시저 호출을 위한 변수 선언
        CallableStatement cstmt = null;
        double totalFee = 0;
        String message = null;

        try {
            // 프로시저 호출
            String procedureCall = "{ call CalculateFee(?, ?, ?) }";
            cstmt = con.prepareCall(procedureCall);

            // 파라미터 설정
            cstmt.setString(1, carNumber);
            cstmt.registerOutParameter(2, Types.NUMERIC); // 요금 결과
            cstmt.registerOutParameter(3, Types.VARCHAR); // 메시지 결과

            // 프로시저 실행
            cstmt.execute();

            // 출력 파라미터에서 요금과 메시지 받아오기
            totalFee = cstmt.getDouble(2);
            message = cstmt.getString(3);

            // 결과 메시지 생성
            if (message == null) {
                // 회원 정보와 요금 계산 결과 메시지 생성
                String query = "SELECT c.차량번호, c.회원ID, m.이름 " +
                        "FROM 차량 c " +
                        "LEFT JOIN 등록이용객 m ON c.회원ID = m.회원ID " +
                        "WHERE c.차량번호 = ?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, carNumber);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String memberName = rs.getString("이름");
                    if (memberName != null) {
                        resultMessage = String.format("%s님 %.0f원", memberName, totalFee);
                    } else {
                        resultMessage = String.format("%s님 %.0f원", carNumber, totalFee);
                    }
                } else {
                    resultMessage = "해당 차량번호에 대한 주차 정보가 없습니다.";
                }
            } else {
                // 프로시저에서 반환된 메시지 처리
                resultMessage = message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resultMessage = "DB 처리 중 오류 발생: " + e.getMessage();
        } finally {
            dbConn.closeConnection();
        }

        return resultMessage;
    }
}
