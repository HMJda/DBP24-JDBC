package UI;

import javax.swing.*;
import java.awt.*;

public class VehicleWarningUI extends JPanel {

    public VehicleWarningUI(JPanel mainPanel) {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600)); // 패널 크기 설정
        setBackground(Color.WHITE); // 배경색을 흰색으로 설정

        JLabel titleLabel = new JLabel("차량 경고 등록");
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        titleLabel.setBounds(20, 20, 300, 40); // 위치 설정
        add(titleLabel);

        // 차량 번호 입력 필드
        JLabel vehicleIdLabel = new JLabel("차량 번호:");
        vehicleIdLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        vehicleIdLabel.setBounds(20, 80, 100, 30);
        add(vehicleIdLabel);

        JTextField vehicleIdField = new JTextField();
        vehicleIdField.setBounds(130, 80, 200, 30);
        add(vehicleIdField);

        // 경고 사유 입력 필드
        JLabel warningReasonLabel = new JLabel("경고 사유:");
        warningReasonLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        warningReasonLabel.setBounds(20, 130, 100, 30);
        add(warningReasonLabel);

        JTextField warningReasonField = new JTextField();
        warningReasonField.setBounds(130, 130, 200, 30);
        add(warningReasonField);

        // 경고 등록 버튼
        JButton registerButton = new JButton("경고 등록");
        registerButton.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        registerButton.setBackground(Color.BLACK); // 배경색 검은색
        registerButton.setForeground(Color.WHITE); // 글자색 흰색
        registerButton.setBounds(130, 180, 200, 40);
        add(registerButton);

        // 버튼 클릭 이벤트 처리
        registerButton.addActionListener(e -> {
            String vehicleId = vehicleIdField.getText();
            String warningReason = warningReasonField.getText();

            // 경고 등록 로직
            if (!vehicleId.isEmpty() && !warningReason.isEmpty()) {
                // 데이터베이스에 경고 등록
                // 예: warningDAO.addWarning(vehicleId, warningReason);
                // 트리거가 작동하여 경고 누적 수 증가 및 불이익 단계 변경

                JOptionPane.showMessageDialog(this, "경고가 등록되었습니다.");
                vehicleIdField.setText(""); // 입력 필드 초기화
                warningReasonField.setText(""); // 입력 필드 초기화
            } else {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.");
            }
        });
    }
}
