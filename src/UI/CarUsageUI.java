package UI;

import DAO.CarDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.List;

public class CarUsageUI extends JPanel {

    private DefaultTableModel tableModel;
    private JTable usageTable;
    private JTextField searchField; // 검색 필드 추가
    private String sort = "DESC";
    private boolean isSelected = false;
    private String[] columnNames;
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
            updateTableData();
        });
        searchPanel.add(searchButton);

        // 버튼 패널 추가
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        // JCheckBox 생성
        JCheckBox checkBox1 = new JCheckBox("등록이용객");
        buttonPanel.add(checkBox1);
        checkBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    isSelected = true;
                    columnNames = new String[]{"차량번호", "이름", "경고누적수", "불이익단계", "총이용시간"};
                    tableModel = new DefaultTableModel(columnNames, 0);
                    usageTable.setModel(tableModel);
                    updateTableData();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    columnNames = new String[]{"차량번호", "경고누적수", "불이익단계", "총이용시간"};
                    isSelected = false;
                    tableModel = new DefaultTableModel(columnNames, 0);
                    usageTable.setModel(tableModel);
                    updateTableData();
                }
            }
        });

        JButton sortButton = createStyledButton("내림차순");
        sortButton.addActionListener(e -> {
            if (sortButton.getText().equals("오름차순")) {
                sortButton.setText("내림차순");
                sort = "DESC";
            } else {
                sortButton.setText("오름차순");
                sort = "ASC";
            }
            updateTableData();
        });
        buttonPanel.add(sortButton);

        // 헤더 패널에 검색 패널과 버튼 패널 추가
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 테이블 생성
        columnNames = new String[]{"차량번호", "경고누적수", "불이익단계", "총이용시간"};
        tableModel = new DefaultTableModel(columnNames, 0);
        updateTableData();

        usageTable = new JTable(tableModel);
        usageTable.setRowHeight(30);
        usageTable.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        usageTable.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        usageTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(usageTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tableScrollPane, BorderLayout.CENTER);
    }

    // 테이블 데이터를 갱신하는 메소드
    private void updateTableData() {
        tableModel.setRowCount(0);
        CarDAO carDAO = new CarDAO();
        if ( !searchField.getText().isEmpty() ) {
            try {
                List<Object[]> dataList = carDAO.getCarUsage(isSelected,searchField.getText(),sort); // 현재 sort 값을 기반으로 데이터를 가져옴
                for (Object[] row : dataList) {
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<Object[]> dataList = carDAO.getCarUsage(isSelected,sort); // 현재 sort 값을 기반으로 데이터를 가져옴
                for (Object[] row : dataList) {
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        return button;
    }
}
