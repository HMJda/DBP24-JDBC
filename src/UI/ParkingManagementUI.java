package UI;

import DB.DB_Conn; // DB 연결을 위한 클래스
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingManagementUI extends JPanel {

    private DefaultTableModel tableModel;
    private JTable inspectionTable;
    private String[] columnNames = {"주차장 ID", "관리자ID", "점검일시"};

    public ParkingManagementUI(JPanel mainPanel) {
        setLayout(new BorderLayout());

        // 콘텐츠 영역
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // 헤더
        JPanel headerPanel = createHeaderPanel(mainPanel);
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // 테이블 생성
        tableModel = new DefaultTableModel(columnNames, 0);
        inspectionTable = new JTable(tableModel);
        inspectionTable.setRowHeight(30);
        inspectionTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        inspectionTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        inspectionTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(inspectionTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // 회원 데이터 로드
        loadInspectionData();
    }

    private JPanel createHeaderPanel(JPanel mainPanel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("주차장 관리 점검 기록", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);

        JButton searchButton = createStyledButton("검색");
        searchButton.addActionListener(e -> searchInspectionData(searchField.getText()));
        searchPanel.add(searchButton);

        headerPanel.add(searchPanel, BorderLayout.EAST);
        return headerPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void loadInspectionData() {
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String query = "SELECT 관리.주차장ID, 관리.관리자ID, 관리.점검일시 " +
                "FROM 관리"; // 데이터 조회 쿼리

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // 테이블 초기화
            tableModel.setRowCount(0); // 기존 데이터 초기화

            // 결과 집합을 테이블 모델에 추가
            while (rs.next()) {
                String parkingId = rs.getString("주차장ID");
                String managerId = rs.getString("관리자ID");
                String inspectionDate = rs.getString("점검일시");

                tableModel.addRow(new Object[]{parkingId, managerId, inspectionDate}); // 새로운 행 추가
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "주차장 점검 정보가 없습니다."); // 데이터가 없을 경우 메시지
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주차장 점검 정보를 로드하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }

    private void searchInspectionData(String query) {
        if (query.trim().isEmpty()) {
            // 검색어가 비어 있을 경우 모든 데이터를 로드
            loadInspectionData();
            return;
        }

        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String sqlQuery = "SELECT 관리.주차장ID, 관리.관리자ID, 관리.점검일시 " +
                "FROM 관리 " +
                "WHERE 관리.주차장ID = ? OR 관리.관리자ID = ?"; // 검색 쿼리

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, query); // 주차장 ID 조건
            pstmt.setString(2, query); // 관리자 ID 조건

            try (ResultSet rs = pstmt.executeQuery()) {
                // 테이블 초기화
                tableModel.setRowCount(0); // 기존 데이터 초기화

                // 결과 집합을 테이블 모델에 추가
                while (rs.next()) {
                    String parkingId = rs.getString("주차장ID");
                    String managerId = rs.getString("관리자ID");
                    String inspectionDate = rs.getString("점검일시");

                    tableModel.addRow(new Object[]{parkingId, managerId, inspectionDate}); // 새로운 행 추가
                }

                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "검색 결과가 없습니다."); // 검색 결과가 없을 경우 메시지
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주차장 점검 정보를 검색하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }
}
