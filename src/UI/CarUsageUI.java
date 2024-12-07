package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarUsageUI extends JPanel {

    private DefaultTableModel tableModel;
    private JTable usageTable;
    private JTextField searchField; // 검색 필드 추가

    public CarUsageUI(JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 헤더 패널 생성
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("차량 이용 시간 검색", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // 검색 패널 추가
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        // 검색 필드 추가
        searchField = new JTextField(10);
        searchPanel.add(searchField);

        // 검색 버튼 추가
        JButton searchButton = createStyledButton("검색");
        searchButton.addActionListener(e -> {
            // 검색 버튼 클릭 시 실행할 코드
            String searchText = searchField.getText().trim();
            System.out.println("검색 기준: " + searchText);
            // 여기에 검색 로직 추가
        });
        searchPanel.add(searchButton);

        // 버튼 패널 추가
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        // 오름차순 버튼
        JButton ascButton = createStyledButton("오름차순");
        ascButton.addActionListener(e -> {
            // 오름차순 버튼 클릭 시 실행할 코드
            System.out.println("오름차순 선택됨");
            // 여기에 오름차순 정렬 로직 추가
        });
        buttonPanel.add(ascButton);

        // 내림차순 버튼
        JButton descButton = createStyledButton("내림차순");
        descButton.addActionListener(e -> {
            // 내림차순 버튼 클릭 시 실행할 코드
            System.out.println("내림차순 선택됨");
            // 여기에 내림차순 정렬 로직 추가
        });
        buttonPanel.add(descButton);

        // 헤더 패널에 검색 패널과 버튼 패널 추가
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 테이블 생성
        String[] columnNames = {"차량번호", "경고누적수", "불이익단계", "총이용시간"};
        tableModel = new DefaultTableModel(columnNames, 0);
        usageTable = new JTable(tableModel);
        usageTable.setRowHeight(30);
        usageTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        usageTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        usageTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(usageTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        return button;
    }
}
