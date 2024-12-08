package UI;

import DB.DB_Conn; // DB 연결을 위한 클래스
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingStatusUI extends JPanel {

    public ParkingStatusUI(JPanel mainPanel) {
        setLayout(null); // 절대 위치 배치를 위해 null로 설정
        setBackground(Color.WHITE);

        // 헤더 패널 생성
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("주차장 현황", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.setBounds(20, 10, 300, 40); // 헤더 패널 위치 설정

        // 새로고침 버튼 생성
        JButton refreshButton = new JButton("새로고침");
        refreshButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        refreshButton.setBounds(650, 15, 100, 30); // 위치 설정
        refreshButton.setBackground(Color.BLACK);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshData()); // 새로고침 이벤트 추가

        // "모든 주차장" 텍스트 패널 생성
        JPanel allParkingPanel = new JPanel();
        allParkingPanel.setBackground(Color.WHITE);
        JLabel allParkingLabel = new JLabel("모든 주차장", JLabel.CENTER);
        allParkingLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        allParkingLabel.setForeground(Color.BLACK);
        allParkingPanel.add(allParkingLabel);
        allParkingPanel.setBounds(250, 70, 300, 40); // 위치 조정

        // 상태 표시 패널
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3열로 구성
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        statusPanel.setBounds(20, 100, 750, 400); // 위치 및 크기 조정

        // 주차장 상태 데이터 가져오기
        loadParkingStatus(statusPanel);

        // 콘텐츠 패널에 추가
        add(headerPanel);
        add(refreshButton); // 새로고침 버튼 추가
        add(allParkingPanel);
        add(statusPanel);
    }

    private void loadParkingStatus(JPanel statusPanel) {
        String query = "SELECT p.주차장위치, p.현재주차가능수, p.최대주차가능수 " +
                "FROM 동의대주차장 p";

        DB_Conn dbConn = new DB_Conn(); // DB_Conn 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            statusPanel.removeAll(); // 기존 내용 제거
            while (rs.next()) {
                String parkingLocation = rs.getString("주차장위치");
                int remainingSpaces = rs.getInt("현재주차가능수");
                int totalCapacity = rs.getInt("최대주차가능수");

                // 색상 결정
                Color buttonColor;
                if (remainingSpaces >= (2.0 / 3.0) * totalCapacity) {
                    buttonColor = Color.GREEN;
                } else if (remainingSpaces <= (1.0 / 4.0) * totalCapacity) {
                    buttonColor = Color.RED;
                } else {
                    buttonColor = Color.ORANGE;
                }

                // 주차장 버튼 생성
                JButton parkingButton = new JButton(parkingLocation + " [남은 자리: " + remainingSpaces + "]");
                parkingButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 13));
                parkingButton.setBackground(buttonColor);
                parkingButton.setForeground(Color.BLACK);
                parkingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                parkingButton.setPreferredSize(new Dimension(200, 30));

                // 버튼을 패널에 추가
                JPanel parkingRow = new JPanel();
                parkingRow.setLayout(new BoxLayout(parkingRow, BoxLayout.Y_AXIS)); // 세로로 배치
                parkingRow.setBackground(Color.WHITE);
                parkingRow.setAlignmentX(Component.CENTER_ALIGNMENT);
                parkingRow.add(parkingButton);

                statusPanel.add(parkingRow);
            }
            statusPanel.revalidate(); // 패널 갱신
            statusPanel.repaint(); // 다시 그리기

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주차장 정보를 불러오는 데 실패했습니다.");
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }

    private void refreshData() {
        DB_Conn dbConn = new DB_Conn(); // DB_Conn 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String updateQuery = "UPDATE 동의대주차장 SET 현재주차가능수 = 최대주차가능수 - " +
                "(SELECT COUNT(*) FROM 주차 WHERE 주차장ID = 동의대주차장.주차장ID AND 출차일시 IS NOT NULL)";

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.executeUpdate(); // 업데이트 실행
            JOptionPane.showMessageDialog(this, "주차장 상태가 업데이트되었습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주차장 상태 업데이트에 실패했습니다.");
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }

        // 새로고침 시 상태 패널의 내용을 다시 로드
        JPanel statusPanel = (JPanel) getComponent(3); // statusPanel 가져오기
        loadParkingStatus(statusPanel);
    }
}
