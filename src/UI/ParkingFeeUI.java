package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Controller.ParkingFeeController;

public class ParkingFeeUI extends JPanel {

    private JLabel resultLabel;

    public ParkingFeeUI(JPanel mainPanel) {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("주차요금계산");
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        titleLabel.setBounds(20, 10, 200, 30);
        add(titleLabel);

        JLabel inputLabel = new JLabel("차량번호 입력 : ");
        inputLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 17));
        inputLabel.setBounds(50, 80, 130, 30);
        add(inputLabel);

        JTextField inputField = new JTextField();
        inputField.setBounds(180, 80, 190, 30);
        inputField.setBackground(Color.WHITE);
        inputField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(inputField);

        JButton calculateButton = new JButton("요금 계산");
        calculateButton.setBounds(380, 80, 100, 30);
        calculateButton.setBackground(Color.BLACK);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        add(calculateButton);

        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        resultLabel.setBounds(50, 150, 400, 30);
        add(resultLabel);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String carNumber = inputField.getText().trim();
                if (!carNumber.isEmpty()) {
                    calculateFee(carNumber);
                } else {
                    resultLabel.setText("차량번호를 입력하세요.");
                }
            }
        });
    }

    private void calculateFee(String carNumber) {
        ParkingFeeController dao = new ParkingFeeController();
        String resultMessage = dao.calculateFeeWithMemberInfo(carNumber); // 변경된 DAO 메서드 호출

        if (resultMessage != null) {
            resultLabel.setText(resultMessage);
        } else {
            resultLabel.setText("요금을 계산할 수 없습니다.");
        }
    }
}
