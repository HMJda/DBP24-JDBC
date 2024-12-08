package UI;

import javax.swing.*;
import java.awt.*;

public class MainUI {
    private String userId; // 로그인한 사용자 ID

    public MainUI(String userId) {
        this.userId = userId; // 로그인한 사용자 ID 저장
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("주차장 관리 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // 프레임 배경 색상을 흰색으로 설정
        frame.getContentPane().setBackground(Color.WHITE);

        // 메인 패널에 CardLayout 사용
        JPanel mainPanel = new JPanel(new CardLayout());
        mainPanel.setBackground(Color.WHITE); // 메인 패널 배경 색상도 흰색으로 설정

        // CarInUI 추가
        CarInUI carInUI = new CarInUI(mainPanel); // 입차 현황 UI
        mainPanel.add(carInUI, "CarInUI");

        // CarOutUI 추가
        CarOutUI carOutUI = new CarOutUI(mainPanel); // 입차 현황 UI
        mainPanel.add(carOutUI, "CarOutUI");

        // 각 UI 패널 생성 후 메인 패널에 추가
        ParkingIOUI parkingIOUI = new ParkingIOUI(mainPanel,carInUI,carOutUI); // 차량 입차/출차 UI
        mainPanel.add(parkingIOUI, "ParkingIOUI");

        ManagementUI managementUI = new ManagementUI(mainPanel); // 회원 정보 UI
        mainPanel.add(managementUI, "ManagementUI");

        ParkingFeeUI parkingFeeUI = new ParkingFeeUI(mainPanel); // 주차요금 계산 UI
        mainPanel.add(parkingFeeUI, "ParkingFeeUI");

        ParkingManagementUI parkingManagementUI = new ParkingManagementUI(mainPanel); // 주차장 관리 점검 기록 UI
        mainPanel.add(parkingManagementUI, "ParkingManagementUI");

        ParkingRecordUI parkingRecordUI = new ParkingRecordUI(mainPanel); // 차량 입출차 기록 UI
        mainPanel.add(parkingRecordUI, "ParkingRecordUI");

        ParkingStatusUI parkingStatusUI = new ParkingStatusUI(mainPanel); // 주차장 현황 UI
        mainPanel.add(parkingStatusUI, "ParkingStatusUI");

        // 회원 등록 UI 추가
        MemberRegistrationUI memberRegistrationUI = new MemberRegistrationUI(mainPanel); // 회원 등록 UI
        mainPanel.add(memberRegistrationUI, "MemberRegistrationUI");

        // 차량 경고 등록 UI 추가
        VehicleWarningUI vehicleWarningUI = new VehicleWarningUI(mainPanel, userId); // 차량 경고 등록 UI
        mainPanel.add(vehicleWarningUI, "VehicleWarningUI");

        // 차량 이용 시간 검색 UI 추가
        CarUsageUI carUsageUI = new CarUsageUI(mainPanel); // 차량 이용 시간 검색 UI
        mainPanel.add(carUsageUI, "CarUsageUI");

        // 왼쪽 메뉴 패널 생성
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        menuPanel.setBackground(Color.WHITE);

        // 메뉴 제목 추가
        JLabel menuTitle = new JLabel("메뉴");
        menuTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        menuPanel.add(menuTitle);

        // 메뉴 항목 정의
        String[] menuItems = {
                "차량 입차/출차",
                "회원 정보",
                "주차장 관리 점검 기록",
                "주차요금 계산",
                "차량 입출차 기록",
                "주차장 현황",
                "차량 경고 등록",
                "차량 이용 시간 검색" // 차량 이용 시간 검색 추가
        };

        // 각 메뉴 항목에 대해 버튼을 생성
        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButton.setFocusPainted(false);
            menuButton.setBackground(Color.WHITE);
            menuButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
            menuButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            menuButton.setPreferredSize(new Dimension(200, 50)); // 버튼 크기를 일정하게 설정

            // 메뉴 버튼 클릭 시 해당 UI를 카드 레이아웃에서 전환하여 보여주기
            menuButton.addActionListener(e -> {
                CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

                // 모든 메뉴 버튼의 배경색을 기본 색으로 설정
                for (Component comp : menuPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setBackground(Color.WHITE);
                    }
                }

                // 클릭된 버튼의 배경색을 밝은 회색으로 변경
                menuButton.setBackground(Color.LIGHT_GRAY);

                // 선택된 메뉴에 따라 다른 패널을 표시
                switch (item) {
                    case "차량 입차/출차":
                        cardLayout.show(mainPanel, "ParkingIOUI");
                        parkingIOUI.initialize();
                        break;
                    case "회원 정보":
                        cardLayout.show(mainPanel, "ManagementUI");
                        managementUI.initialize();
                        break;
                    case "주차장 관리 점검 기록":
                        cardLayout.show(mainPanel, "ParkingManagementUI");
                        parkingManagementUI.initialize();
                        break;
                    case "주차요금 계산":
                        cardLayout.show(mainPanel, "ParkingFeeUI");
                        break;
                    case "차량 입출차 기록":
                        cardLayout.show(mainPanel, "ParkingRecordUI");
                        parkingRecordUI.initialize();
                        break;
                    case "주차장 현황":
                        cardLayout.show(mainPanel, "ParkingStatusUI");
                        break;
                    case "차량 경고 등록":
                        cardLayout.show(mainPanel, "VehicleWarningUI");
                        break;
                    case "차량 이용 시간 검색":
                        cardLayout.show(mainPanel, "CarUsageUI"); // 차량 이용 시간 검색 UI 표시
                        carUsageUI.initialize();
                        break;
                }
            });

            // 메뉴 패널에 버튼을 추가
            menuPanel.add(menuButton);
        }

        // 처음 화면이 출력될 때 "차량 입차/출차" 버튼의 배경색을 밝은 회색으로 설정
        JButton firstButton = (JButton) menuPanel.getComponent(1); // 첫 번째 메뉴 버튼
        firstButton.setBackground(Color.LIGHT_GRAY);

        // 프레임에 메뉴 패널과 메인 패널 추가
        frame.add(menuPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        // 첫 화면으로 ParkingIOUI 설정
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "ParkingIOUI");
        parkingIOUI.initialize();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
