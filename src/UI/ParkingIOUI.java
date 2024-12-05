package UI;

import javax.swing.*;
import java.awt.*;

public class ParkingIOUI extends JPanel {

    private JPanel titlePanel; // 타이틀 패널을 멤버 변수로 선언
    private JButton entryButton; // 입차 버튼을 멤버 변수로 선언

    public ParkingIOUI() {
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

        // 버튼 위치 설정 (간격을 늘림)
        entryButton.setBounds(80, 40, 150, 30); // y 좌표를 30에서 40으로 변경하여 간격 증가
        exitButton.setBounds(250, 40, 150, 30); // 동일하게 설정

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
        entryPanel.add(new JTextField()).setBounds(120, 70, 300, 25);

        JLabel parkingIdLabel = new JLabel("주차장ID:");
        parkingIdLabel.setBounds(20, 110, 100, 25);
        entryPanel.add(parkingIdLabel);
        entryPanel.add(new JTextField()).setBounds(120, 110, 300, 25);

        JLabel spaceNumberLabel = new JLabel("공간번호:");
        spaceNumberLabel.setBounds(20, 150, 100, 25);
        entryPanel.add(spaceNumberLabel);
        entryPanel.add(new JTextField()).setBounds(120, 150, 300, 25);

        // "기입" 버튼 추가 (위치 고정)
        JButton submitButton = new JButton("기입");
        submitButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(180, 200, 100, 30); // 위치 고정
        entryPanel.add(submitButton);

        // 기존 패널 제거 후 새 패널 추가
        removeAll();
        add(titlePanel); // titlePanel 추가
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
        exitPanel.add(new JTextField()).setBounds(120, 70, 300, 25);

        // "기입" 버튼 추가 (위치 고정)
        JButton submitButton = new JButton("기입");
        submitButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBounds(180, 200, 100, 30); // 입차 패널과 동일한 위치
        exitPanel.add(submitButton);

        // 기존 패널 제거 후 새 패널 추가
        removeAll();
        add(titlePanel); // titlePanel 추가
        add(exitPanel);
        revalidate();
        repaint();
    }
}