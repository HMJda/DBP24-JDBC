package UI;


import DB.DB_Conn; // DB 연결을 위한 클래스
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import DAO.CarDAO;

public class ParkingIOUI extends JPanel {

    private JPanel titlePanel; // 타이틀 패널을 멤버 변수로 선언
    private CarInUI carInUI;
    private CarOutUI carOutUI;
    private JButton entryButton; // 입차 버튼을 멤버 변수로 선언
    private JTextField carNumberField; // 차량번호 입력 필드
    private JTextField parkingIdField; // 주차장ID 입력 필드
    private JTextField spaceNumberField; // 공간번호 입력 필드
    private JTextField exitCarNumberField; // 출차 차량번호 입력 필드
    private JPanel mainPanel; // 메인 패널을 멤버 변수로 선언

    public void initialize() {
        carNumberField.setText("");
        parkingIdField.setText("");
        spaceNumberField.setText("");
    }

    public ParkingIOUI(JPanel mainPanel, CarInUI carInUI, CarOutUI carOutUI) {
        this.mainPanel = mainPanel; // 메인 패널을 받아오기
        this.carInUI = carInUI;
        this.carOutUI = carOutUI;
        setLayout(null); // null 레이아웃 사용
        setBounds(0, 0, 1000, 600); // 패널 크기 설정
        setBackground(Color.WHITE); // 배경색을 흰색으로 설정

        createTitlePanel(); // 타이틀 패널 생성
        add(titlePanel); // 타이틀 패널을 현재 패널에 추가
        showEntryPanel(); // 초기 상태로 입차 패널 표시
    }

    private void createTitlePanel() {
        titlePanel = new JPanel(); // 타이틀 패널 초기화
        titlePanel.setLayout(null);
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBounds(60, 10, 480, 80); // 위치와 크기 설정

        JLabel titleLabel = new JLabel("입/출차 기입", JLabel.LEFT);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        titleLabel.setBounds(0, 0, 480, 30);
        titlePanel.add(titleLabel);

        entryButton = new JButton("입차하기"); // 입차 버튼을 멤버 변수로 선언
        JButton exitButton = new JButton("출차하기");

        // 버튼 스타일 설정
        styleButton(entryButton);
        styleButton(exitButton);

        // 버튼 위치 설정
        entryButton.setBounds(80, 40, 150, 30); // 입차 버튼
        exitButton.setBounds(250, 40, 150, 30); // 출차 버튼

        titlePanel.add(entryButton);
        titlePanel.add(exitButton);

        // 버튼 클릭 이벤트 설정
        entryButton.addActionListener(e -> {
            entryButton.setBackground(Color.LIGHT_GRAY); // 밝은 회색으로 설정
            exitButton.setBackground(Color.BLACK); // 기본 색상으로 설정
            showEntryPanel(); // 입차 패널 표시
        });

        exitButton.addActionListener(e -> {
            exitButton.setBackground(Color.LIGHT_GRAY); // 밝은 회색으로 설정
            entryButton.setBackground(Color.BLACK); // 기본 색상으로 설정
            showExitPanel(); // 출차 패널 표시
        });
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
    }

    private void showEntryPanel() {
        // 입차 패널
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(null); // null 레이아웃 사용
        entryPanel.setBackground(Color.WHITE);
        entryPanel.setBounds(60, 70, 480, 400); // 위치와 크기 설정

        JLabel entryLabel = new JLabel("입차", JLabel.CENTER);
        entryLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        entryLabel.setBounds(0, 20, 480, 30);
        entryPanel.add(entryLabel);

        // 입차 정보 입력 필드
        JLabel carNumberLabel = new JLabel("차량번호:");
        carNumberLabel.setBounds(20, 70, 100, 25);
        entryPanel.add(carNumberLabel);
        carNumberField = new JTextField(); // 차량번호 입력 필드 초기화
        entryPanel.add(carNumberField).setBounds(120, 70, 300, 25);

        JLabel parkingIdLabel = new JLabel("주차장ID:");
        parkingIdLabel.setBounds(20, 110, 100, 25);
        entryPanel.add(parkingIdLabel);
        parkingIdField = new JTextField(); // 주차장ID 입력 필드 초기화
        entryPanel.add(parkingIdField).setBounds(120, 110, 300, 25);

        JLabel spaceNumberLabel = new JLabel("공간번호:");
        spaceNumberLabel.setBounds(20, 150, 100, 25);
        entryPanel.add(spaceNumberLabel);
        spaceNumberField = new JTextField(); // 공간번호 입력 필드 초기화
        entryPanel.add(spaceNumberField).setBounds(120, 150, 300, 25);

        // "기입" 버튼 추가 (위치 고정)
        JButton submitButton = new JButton("기입");
        submitButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(120, 200, 100, 30); // 위치 조정
        entryPanel.add(submitButton);

        // 입차현황 버튼 추가 (위치 조정)
        JButton statusButton = new JButton("입차현황");
        statusButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        statusButton.setBackground(Color.BLACK);
        statusButton.setForeground(Color.WHITE);
        statusButton.setBounds(230, 200, 120, 30); // 기입 버튼 옆에 위치
        entryPanel.add(statusButton);

        // 기입 버튼 클릭 이벤트 설정
        submitButton.addActionListener(e -> {
            if (!carNumberField.getText().isEmpty()
                    && !spaceNumberField.getText().isEmpty() && !parkingIdField.getText().isEmpty()) {
                try {
                    CarDAO carDAO = new CarDAO();
                    String message = carDAO.insertCarParking(
                            carNumberField.getText(),
                            spaceNumberField.getText(),
                            parkingIdField.getText()
                    );
                    initialize();
                    JOptionPane.showMessageDialog(this, message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "차량입차에 실패했습니다: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.");
            }


        });

        // 입차현황 버튼 클릭 이벤트 설정
        statusButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout(); // 메인 패널의 CardLayout 가져오기
            carInUI.initialize();
            cardLayout.show(mainPanel, "CarInUI"); // CarInUI로 전환
        });

        // 기존 패널 제거 후 새 패널 추가
        removeAll();
        add(titlePanel); // 타이틀 패널 추가
        add(entryPanel);
        revalidate();
        repaint();

        // 입차 버튼을 밝은 회색으로 설정
        entryButton.setBackground(Color.LIGHT_GRAY); // 입차 버튼을 밝은 회색으로 설정
    }

    private void showExitPanel() {
        // 출차 패널
        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(null); // null 레이아웃 사용
        exitPanel.setBackground(Color.WHITE);
        exitPanel.setBounds(60, 70, 480, 400); // 위치와 크기 설정

        JLabel exitLabel = new JLabel("출차", JLabel.CENTER);
        exitLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        exitLabel.setBounds(0, 20, 480, 30);
        exitPanel.add(exitLabel);

        // 출차 정보 입력 필드
        JLabel carNumberLabel = new JLabel("차량번호:");
        carNumberLabel.setBounds(20, 70, 100, 25);
        exitPanel.add(carNumberLabel);
        exitCarNumberField = new JTextField(); // 출차 차량번호 입력 필드
        exitPanel.add(exitCarNumberField).setBounds(120, 70, 300, 25);

        // "기입" 버튼 추가 (위치 고정)
        JButton submitButton = new JButton("기입");
        submitButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(120, 200, 100, 30);  // 위치 조정
        exitPanel.add(submitButton);

        // 출차현황 버튼 추가
        JButton statusButton = new JButton("출차현황");
        statusButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        statusButton.setBackground(Color.BLACK);
        statusButton.setForeground(Color.WHITE);
        statusButton.setBounds(230, 200, 120, 30); // 기입 버튼 옆에 위치
        exitPanel.add(statusButton);

        // 기입 버튼 클릭 이벤트 설정
        submitButton.addActionListener(e -> {
            if (!exitCarNumberField.getText().isEmpty()) {
                try{
                    CarDAO carDAO = new CarDAO();
                    String message = carDAO.updateCarParking(
                            exitCarNumberField.getText()
                    );
                    exitCarNumberField.setText("");
                    JOptionPane.showMessageDialog(this, message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "차량출차에 실패했습니다: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "차량번호를 입력하세요.");
            }
        });

        // 출차현황 버튼 클릭 이벤트 설정
        statusButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout(); // 메인 패널의 CardLayout 가져오기
            carOutUI.initialize();
            cardLayout.show(mainPanel, "CarOutUI"); // CarOutUI로 전환
        });

        // 기존 패널 제거 후 새 패널 추가
        removeAll();
        add(titlePanel); // 타이틀 패널 추가
        add(exitPanel); // 출차 패널 추가
        revalidate();
        repaint();
    }
}
