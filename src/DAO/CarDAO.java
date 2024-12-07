package DAO;

import DB.DB_Conn;
import DTO.CarDTO;
import DTO.MemberDTO;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    /** (트랜잭션) 차량 주차 입력 명령어 차량번호, 공간번호, 주차장 ID*/
    public String insertCarParking(String carNumber, String spaceNumber, String parkingSpaceId) throws SQLException {
        String resultMessage = "";
        String checkCarQuery = "SELECT COUNT(*) FROM 차량 WHERE 차량번호 = ?";
        String insertCarQuery = "INSERT INTO 차량 (차량번호) VALUES (?)";
        String checkSpaceQuery =
                "SELECT COUNT(*) FROM 주차 " +
                        "WHERE 공간번호 = ? AND 주차장ID = ? AND 출차일시 IS NULL";
        String insertParkingQuery =
                "INSERT INTO 주차 (차량번호, 공간번호, 주차장ID) " +
                        "VALUES (?, ?, ?)";
        DB_Conn dbConn = new DB_Conn();
        dbConn.DB_Connect();
        try (Connection conn = dbConn.getConnection()) {

            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); // 트랜잭션 격리 수준 설정

            // 차량번호 확인 및 삽입
            try (PreparedStatement checkCarStmt = conn.prepareStatement(checkCarQuery)) {
                checkCarStmt.setString(1, carNumber);
                try (ResultSet rs = checkCarStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        try (PreparedStatement insertCarStmt = conn.prepareStatement(insertCarQuery)) {
                            insertCarStmt.setString(1, carNumber);
                            insertCarStmt.executeUpdate();
                        }
                    }
                }
            }

            // 공간번호와 주차장 ID 사용 가능 여부 확인
            try (PreparedStatement checkSpaceStmt = conn.prepareStatement(checkSpaceQuery)) {
                checkSpaceStmt.setString(1, spaceNumber);
                checkSpaceStmt.setString(2, parkingSpaceId);
                try (ResultSet rs = checkSpaceStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        resultMessage = "해당 공간번호와 주차장 ID는 이미 사용 중입니다.";
                        conn.rollback();
                        return resultMessage;
                    }
                }
            }

            // 주차 데이터 삽입
            try (PreparedStatement insertParkingStmt = conn.prepareStatement(insertParkingQuery)) {
                insertParkingStmt.setString(1, carNumber);
                insertParkingStmt.setString(2, spaceNumber);
                insertParkingStmt.setString(3, parkingSpaceId);
                insertParkingStmt.executeUpdate();
            }

            conn.commit(); // 커밋
            resultMessage = "차량 주차가 성공적으로 완료되었습니다.";
        } catch (SQLException e) {
            resultMessage = "주차 삽입 중 오류가 발생했습니다.";
            e.printStackTrace(); // 로깅 프레임워크 사용 권장
        }
        return resultMessage;
    }

    public String updateCarParking(String carNumber) {
        String resultMessage;
        // DB 연결 및 데이터 업데이트
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String query = "UPDATE 주차 SET 출차일시 = SYSDATE WHERE 차량번호 = ? AND 출차일시 IS NULL";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, carNumber);
            int rowsAffected = pstmt.executeUpdate(); // 데이터 업데이트 실행

            if (rowsAffected > 0) {
                resultMessage = "출차 정보가 성공적으로 업데이트되었습니다."; // 성공 메시지
            } else {
                resultMessage = "해당 차량번호의 출차 정보가 없습니다."; // 조건 미충족 메시지
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultMessage= "출차 정보 업데이트에 실패했습니다."; // 실패 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
        return resultMessage;
    }
    /** 총이용시간 검색 */
    public List<Object[]> getCarUsage(String selectSort) throws SQLException {
        return getCarUsage(false, selectSort); // 조건 없는 기본 쿼리 실행
    }
    public List<Object[]> getCarUsage(boolean registedMember, String selectSort) throws SQLException {
        List<Object[]> resultList = new ArrayList<>();
        String member = "";
        String memberJoin ="";
        if (registedMember) {
            member = "    등록이용객.이름, ";
            memberJoin = "JOIN " +
                    "    등록이용객 ON 차량.회원ID = 등록이용객.회원ID " ;
        }

        String query =
                "SELECT " +
                        "    주차.차량번호, " +
                        member +
                        "    주차.경고누적수, "+
                        "    주차.불이익단계, "+
                        "    SUM(NVL(주차.출차일시, SYSDATE) - 주차.입차일시) AS 총이용시간 " +
                        "FROM " +
                        "    주차 " +
                        "JOIN " +
                        "    차량 ON 주차.차량번호 = 차량.차량번호 " +
                        memberJoin +
                        "GROUP BY " +
                        member +
                        "    주차.경고누적수, "+
                        "    주차.불이익단계, "+
                        "    주차.차량번호 " +
                        "ORDER BY " +
                        "    총이용시간 " + selectSort; //DESC, ASC

        DB_Conn dbConn = new DB_Conn();
        try (Connection conn = dbConn.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CarDTO car = new CarDTO();
                car.setCarNumber(rs.getString("차량번호"));
                car.setWarningCount(rs.getInt("경고누적수"));
                car.setPenaltyLevel(rs.getInt("불이익단계"));
                MemberDTO memberDTO = new MemberDTO();

                // 총 이용 시간 등 추가 데이터를 member나 car에 필요시 추가 가능
                double totalUsageTime = rs.getDouble("총이용시간");
                if (registedMember) {
                    memberDTO.setName(rs.getString("이름"));
                    resultList.add(new Object[]{car, member, totalUsageTime});
                } else {
                    resultList.add(new Object[]{car, totalUsageTime});
                }
            }
        } catch (SQLException e) {
            throw new SQLException("데이터 조회 중 오류 발생: " + e.getMessage(), e);
        }
        dbConn.closeConnection(); // 데이터베이스 연결 종료
        return resultList;
    }
}
