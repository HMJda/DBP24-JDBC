package UI;

import DB.DB_Conn; // DB 연결을 위한 클래스
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;


public class CarOutUI extends JPanel {

    private JTable parkingTable; // 주차 테이블을 표시할 JTable
    private DefaultTableModel tableModel; // 테이블 모델
    private String[] columnNames = {"차량번호", "공간번호", "주차장ID", "입차일시" , "출차일시"};
    private String query = "SELECT 차량번호, 공간번호, 주차장ID, 입차일시, 출차일시 FROM 주차" +
            " WHERE 출차일시 IS NOT NULL ";
    private String queryGBOB = " GROUP BY " +
                                "    차량번호, "+
                                "    공간번호, "+
                                "    주차장ID, " +
                                "    입차일시, " +
                                "    출차일시 " +
                                "ORDER BY " +
                                "    출차일시 DESC" ;

    public CarOutUI(JPanel mainPanel) {
        setLayout(new BorderLayout()); // 레이아웃 설정
        setBackground(Color.WHITE); // 배경색 설정

        // 콘텐츠 영역
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // 헤더
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // 테이블 모델 생성
        tableModel = new DefaultTableModel(columnNames, 0);
        parkingTable = new JTable(tableModel); // JTable 생성
        parkingTable.setRowHeight(30);
        parkingTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        parkingTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        parkingTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(parkingTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // 뒤로가기 버튼 추가
        JButton backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> goBackToParkingIOUI(mainPanel));
        add(backButton, BorderLayout.SOUTH); // 아래쪽에 추가

        // 테이블에 데이터 로드
        loadParkingData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("입차 현황", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);

        JButton searchButton = createStyledButton("검색");
        searchButton.addActionListener(e -> searchParkingData(searchField.getText()));
        searchPanel.add(searchButton);

        JButton filterButton = createStyledButton("필터");
        filterButton.addActionListener(e -> applyFilter());
        searchPanel.add(filterButton);

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

    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void loadParkingData() {
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String sQuery = query;
        sQuery += queryGBOB;

        try (Connection conn = dbConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sQuery)) {

            // 테이블 초기화
            tableModel.setRowCount(0); // 기존 데이터 초기화

            // 결과 집합을 테이블 모델에 추가
            while (rs.next()) {
                String carNumber = rs.getString("차량번호");
                String spaceNumber = rs.getString("공간번호");
                String parkingId = rs.getString("주차장ID");
                String entryTime = rs.getString("입차일시");
                String exitTime = rs.getString("출차일시");

                tableModel.addRow(new Object[]{carNumber, spaceNumber, parkingId, entryTime, exitTime}); // 새로운 행 추가
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "주차 정보가 없습니다."); // 데이터가 없을 경우 메시지
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주차 정보를 로드하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }

    private void searchParkingData(String searchCarNumber) {
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String sqlQuery = query;
        // 입력된 검색어가 비어 있지 않으면 검색 쿼리를 추가
        if (!searchCarNumber.trim().isEmpty()) {
            sqlQuery += " AND 차량번호 LIKE ? ";
        }
        sqlQuery += queryGBOB;

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            // 검색어가 있을 경우 차량번호 조건 추가
            if (!searchCarNumber.trim().isEmpty()) {
                pstmt.setString(1, "%" + searchCarNumber + "%"); // LIKE 연산자로 부분 일치를 찾기
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                // 테이블 초기화
                tableModel.setRowCount(0); // 기존 데이터 초기화

                // 결과 집합을 테이블 모델에 추가
                while (rs.next()) {
                    String carNumber = rs.getString("차량번호");
                    String spaceNumber = rs.getString("공간번호");
                    String parkingId = rs.getString("주차장ID");
                    String entryTime = rs.getString("입차일시");
                    String outTime = rs.getString("입차일시");

                    tableModel.addRow(new Object[]{carNumber, spaceNumber, parkingId, entryTime, outTime}); // 새로운 행 추가
                }

                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "검색 결과가 없습니다."); // 검색 결과가 없을 경우 메시지
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "주차 정보를 검색하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }


    private void applyFilter() {
        JCheckBox[] checkBoxes = new JCheckBox[columnNames.length];
        JPanel filterPanel = new JPanel(new GridLayout(columnNames.length, 1));

        for (int i = 0; i < columnNames.length; i++) {
            checkBoxes[i] = new JCheckBox(columnNames[i], true);
            filterPanel.add(checkBoxes[i]);
        }

        int result = JOptionPane.showConfirmDialog(this, filterPanel, "필터 선택", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            ArrayList<Integer> selectedColumns = new ArrayList<>();
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    selectedColumns.add(i);
                }
            }

            DefaultTableModel newModel = new DefaultTableModel();
            for (int colIndex : selectedColumns) {
                newModel.addColumn(columnNames[colIndex]);
            }

            for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
                Object[] row = new Object[selectedColumns.size()];
                for (int i = 0; i < selectedColumns.size(); i++) {
                    row[i] = tableModel.getValueAt(rowIndex, selectedColumns.get(i));
                }
                newModel.addRow(row);
            }
            parkingTable.setModel(newModel);
        }
    }

    private void goBackToParkingIOUI(JPanel mainPanel) {
        // ParkingIOUI로 돌아가기
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "ParkingIOUI"); // 카드 레이아웃을 사용하여 전환
    }
}
