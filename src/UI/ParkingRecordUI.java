package UI;

import DB.DB_Conn; // DB 연결을 위한 클래스
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingRecordUI extends JPanel {

    private DefaultTableModel tableModel;
    private JTable parkingTable;

    public ParkingRecordUI(JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 콘텐츠 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // 헤더 패널 생성
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("차량 입/출차 기록", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);

        // 검색 버튼 추가
        JButton searchButton = createStyledButton("검색");
        searchButton.addActionListener(e -> searchParkingRecords(searchField.getText()));
        searchPanel.add(searchButton);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // 테이블 생성
        String[] columnNames = {"회원ID", "차량번호", "주차장 번호", "주차장 공간 번호", "입차시간", "출차시간"};
        tableModel = new DefaultTableModel(columnNames, 0);
        parkingTable = new JTable(tableModel);
        parkingTable.setRowHeight(30);
        parkingTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        parkingTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parkingTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(parkingTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        // 콘텐츠 패널을 현재 패널에 추가
        add(contentPanel, BorderLayout.CENTER);

        // 데이터 로드
        loadParkingRecords();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void loadParkingRecords() {
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String query = "SELECT 차량.회원ID, 차량.차량번호, 주차.주차장ID, 주차.공간번호, " +
                "주차.입차일시, 주차.출차일시 " +
                "FROM 주차 " +
                "JOIN 차량 ON 주차.차량번호 = 차량.차량번호"; // 데이터 조회 쿼리

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // 테이블 초기화
            tableModel.setRowCount(0); // 기존 데이터 초기화

            // 결과 집합을 테이블 모델에 추가
            while (rs.next()) {
                String memberId = rs.getString("회원ID");
                String carNumber = rs.getString("차량번호");
                String parkingId = rs.getString("주차장ID");
                String spaceNumber = rs.getString("공간번호");
                String entryTime = rs.getString("입차일시");
                String exitTime = rs.getString("출차일시");

                tableModel.addRow(new Object[]{memberId, carNumber, parkingId, spaceNumber, entryTime, exitTime}); // 새로운 행 추가
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "입차 기록이 없습니다."); // 데이터가 없을 경우 메시지
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "입차 기록을 로드하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }

    private void searchParkingRecords(String query) {
        if (query.trim().isEmpty()) {
            loadParkingRecords(); // 검색어가 비어 있으면 모든 기록 로드
            return;
        }

        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String sqlQuery = "SELECT 차량.회원ID, 차량.차량번호, 주차.주차장ID, 주차.공간번호, " +
                "주차.입차일시, 주차.출차일시 " +
                "FROM 주차 " +
                "JOIN 차량 ON 주차.차량번호 = 차량.차량번호 " +
                "WHERE 주차.차량번호 = ?"; // 검색 쿼리 수정

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, query); // 차량 번호 조건

            try (ResultSet rs = pstmt.executeQuery()) {
                // 테이블 초기화
                tableModel.setRowCount(0); // 기존 데이터 초기화

                // 결과 집합을 테이블 모델에 추가
                while (rs.next()) {
                    String memberId = rs.getString("회원ID");
                    String carNumber = rs.getString("차량번호");
                    String parkingId = rs.getString("주차장ID");
                    String spaceNumber = rs.getString("공간번호");
                    String entryTime = rs.getString("입차일시");
                    String exitTime = rs.getString("출차일시");

                    tableModel.addRow(new Object[]{memberId, carNumber, parkingId, spaceNumber, entryTime, exitTime}); // 새로운 행 추가
                }

                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "검색 결과가 없습니다."); // 검색 결과가 없을 경우 메시지
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "입차 기록을 검색하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }
}
