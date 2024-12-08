package UI;

import DB.DB_Conn; // DB 연결을 위한 클래스
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagementUI extends JPanel {

    private JTable userTable; // 회원 정보를 표시할 JTable
    private DefaultTableModel tableModel; // 테이블 모델
    private JTextField searchField;
    private String[] columnNames = {"회원 ID", "이름", "연락처", "소속", "주소"};

    public void initialize() {
        searchField.setText("");
        loadUserData();
    }

    public ManagementUI(JPanel mainPanel) {
        setLayout(new BorderLayout());

        // 오른쪽 콘텐츠 패널
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // 헤더 패널 생성
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("회원 정보", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // 검색 및 필터 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(15);
        searchPanel.add(searchField);

        JButton searchButton = createStyledButton("검색");
        searchButton.addActionListener(e -> searchUserData(searchField.getText()));
        searchPanel.add(searchButton);

        JButton filterButton = createStyledButton("필터");
        filterButton.addActionListener(e -> applyFilter());
        searchPanel.add(filterButton);

        headerPanel.add(searchPanel, BorderLayout.EAST);

        // 데이터 테이블
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        styleTable(userTable);

        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 하단 회원 등록 버튼
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);

        JButton addUserButton = new JButton("회원 등록");
        styleButton(addUserButton);
        footerPanel.add(addUserButton);

        // 콘텐츠 패널 구성
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        // 콘텐츠 패널을 현재 패널에 추가
        add(contentPanel, BorderLayout.CENTER);

        // 회원 데이터 로드
        loadUserData();

        // 이벤트 리스너 추가
        addUserButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.show(mainPanel, "MemberRegistrationUI"); // MemberRegistrationUI로 전환
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);
    }

    private void loadUserData() {
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String query = "SELECT 회원ID, 이름, 연락처, 소속, 주소 FROM 등록이용객"; // 데이터 조회 쿼리

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // 테이블 초기화
            tableModel.setRowCount(0); // 기존 데이터 초기화

            // 결과 집합을 테이블 모델에 추가
            while (rs.next()) {
                String memberId = rs.getString("회원ID");
                String name = rs.getString("이름");
                String contact = rs.getString("연락처");
                String affiliation = rs.getString("소속");
                String address = rs.getString("주소");

                tableModel.addRow(new Object[]{memberId, name, contact, affiliation, address}); // 새로운 행 추가
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "회원 정보가 없습니다."); // 데이터가 없을 경우 메시지
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "회원 정보를 로드하는 데 실패했습니다."); // 오류 메시지
        } finally {
            dbConn.closeConnection(); // 데이터베이스 연결 종료
        }
    }

    private void searchUserData(String query) {
        DB_Conn dbConn = new DB_Conn(); // DB 연결 객체 생성
        dbConn.DB_Connect(); // 데이터베이스 연결

        String sqlQuery = "SELECT 회원ID, 이름, 연락처, 소속, 주소 FROM 등록이용객";

        // 입력된 검색어가 비어 있지 않으면 검색 쿼리를 추가
        if (!query.trim().isEmpty()) {
            sqlQuery += " WHERE 이름 LIKE ?";
        }

        try (Connection conn = dbConn.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            // 검색어가 있을 경우 이름 조건 추가
            if (!query.trim().isEmpty()) {
                pstmt.setString(1, "%" + query + "%"); // LIKE 연산자로 부분 일치를 찾기
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                // 테이블 초기화
                tableModel.setRowCount(0); // 기존 데이터 초기화

                // 결과 집합을 테이블 모델에 추가
                while (rs.next()) {
                    String memberId = rs.getString("회원ID");
                    String name = rs.getString("이름");
                    String contact = rs.getString("연락처");
                    String affiliation = rs.getString("소속");
                    String address = rs.getString("주소");

                    tableModel.addRow(new Object[]{memberId, name, contact, affiliation, address}); // 새로운 행 추가
                }

                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "검색 결과가 없습니다."); // 검색 결과가 없을 경우 메시지
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "회원 정보를 검색하는 데 실패했습니다."); // 오류 메시지
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
            userTable.setModel(newModel);
        }
    }
}
