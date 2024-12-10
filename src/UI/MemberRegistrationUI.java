package UI;

import DTO.MemberDTO;
import Controller.MemberController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MemberRegistrationUI extends JPanel {

    public MemberRegistrationUI(JPanel mainPanel) {
        setLayout(null); // Null 레이아웃으로 설정하여 setBounds 사용 가능하도록 함
        setPreferredSize(new Dimension(1000, 600)); // 패널 크기 설정

        // 배경 패널 생성
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, 1000, 600);
        backgroundPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정
        add(backgroundPanel); // 배경 패널 추가

        JLabel titleLabel = new JLabel("회원 등록하기");
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        titleLabel.setBounds(20, 20, 300, 40); // 위치 설정
        backgroundPanel.add(titleLabel);

        // 폼 패널 생성
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10)); // 4행 4열 그리드 레이아웃 사용
        formPanel.setBounds(20, 80, 750, 200); // 위치와 크기 설정
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE); // 폼 패널 배경색 흰색으로 설정

        String[] labels = {"회원 ID", "이름", "생년월일", "연락처", "소속", "소속번호", "주소", "이용형태"};
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Malgun Gothic", Font.BOLD, 16));

            JTextField field = new JTextField();
            fields[i] = field;

            // 생년월일 필드에 플레이스홀더 설정
            if (i == 2) {
                field.setText("yyyy-mm-dd");
                field.setForeground(Color.GRAY); // 회색 글자 설정

                // 클릭 시 플레이스홀더 텍스트 제거
                field.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        if (field.getText().equals("yyyy-mm-dd")) {
                            field.setText("");
                            field.setForeground(Color.BLACK); // 기본 글자색으로 변경
                        }
                    }

                    public void focusLost(java.awt.event.FocusEvent evt) {
                        if (field.getText().isEmpty()) {
                            field.setForeground(Color.GRAY);
                            field.setText("yyyy-mm-dd");
                        }
                    }
                });
            }

            formPanel.add(label);
            formPanel.add(field);
        }

        backgroundPanel.add(formPanel); // 폼 패널 추가

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null); // null 레이아웃 사용
        buttonPanel.setBounds(20, 400, 750, 50); // 위치와 크기 설정
        buttonPanel.setBackground(Color.WHITE);

        JButton registerButton = new JButton("회원 등록");
        registerButton.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBounds(280, 0, 150, 40); // 위치 설정
        buttonPanel.add(registerButton);

        backgroundPanel.add(buttonPanel); // 버튼 패널 추가

        // 회원 등록 버튼 동작
        registerButton.addActionListener(e -> {
            try {
                MemberDTO memberDTO = new MemberDTO();
                memberDTO.setMemberId(fields[0].getText());
                memberDTO.setName(fields[1].getText());

                String birthDateStr = fields[2].getText();
                if (!birthDateStr.isEmpty() && !birthDateStr.equals("yyyy-mm-dd")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    memberDTO.setBirthDate(sdf.parse(birthDateStr));
                }

                memberDTO.setPhone(fields[3].getText());
                memberDTO.setAffiliation(fields[4].getText());
                memberDTO.setAffiliationNumber(fields[5].getText());
                memberDTO.setAddress(fields[6].getText());
                memberDTO.setUsageType(fields[7].getText());

                MemberController memberController = new MemberController();
                String message = memberController.registerMember(memberDTO);

                JOptionPane.showMessageDialog(this, message);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "회원 등록에 실패했습니다: " + ex.getMessage());
            }
        });
    }
}
