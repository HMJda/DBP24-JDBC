package UI;

import DAO.LoginDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login {
    public static void show() {
        // UTF-8 인코딩 및 한글 폰트 설정
        UIManager.put("Label.font", new Font("Malgun Gothic", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Malgun Gothic", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Malgun Gothic", Font.PLAIN, 14));

        // GUI 생성 및 실행
        SwingUtilities.invokeLater(() -> new Login().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // 메인 프레임 생성
        JFrame frame = new JFrame("DEU 주차장 시스템");
        frame.setUndecorated(true); // 장식 제거
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 400);
        frame.setLayout(null);

        // 프레임 배경색을 흰색으로 설정
        frame.getContentPane().setBackground(Color.WHITE);

        // 제목 레이블 추가
        JLabel titleLabel = new JLabel("DEU 주차장 시스템", JLabel.CENTER);
        titleLabel.setBounds(0, 30, 350, 30);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 25));
        titleLabel.setForeground(Color.BLACK);
        frame.add(titleLabel);

        // 로그인 레이블 추가
        JLabel loginLabel = new JLabel("Login", JLabel.CENTER);
        loginLabel.setBounds(0, 110, 350, 25);
        loginLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        frame.add(loginLabel);

        // ID 레이블 추가
        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 140, 250, 20);
        idLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        frame.add(idLabel);

        // ID 입력 필드 (플레이스홀더 포함) 추가
        JTextField idField = new JTextField("ID");
        idField.setBounds(50, 160, 250, 40);
        idField.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        idField.setForeground(Color.GRAY);
        addPlaceholderBehavior(idField, "ID");
        frame.add(idField);

        // 비밀번호 레이블 추가
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 210, 250, 20);
        passwordLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        frame.add(passwordLabel);

        // 비밀번호 입력 필드 (플레이스홀더 포함) 추가
        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setBounds(50, 230, 250, 40);
        passwordField.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        passwordField.setForeground(Color.GRAY);
        addPlaceholderBehavior(passwordField, "Password");
        frame.add(passwordField);

        // 로그인 버튼 추가
        JButton signInButton = new JButton("로그인");
        signInButton.setBounds(50, 290, 250, 40);
        signInButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
        signInButton.setBackground(Color.BLACK);
        signInButton.setForeground(Color.WHITE);
        signInButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        signInButton.setFocusPainted(false);
        signInButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 로그인 버튼 클릭 시 동작, DB 연결
        signInButton.addActionListener(e -> {
            String userId = idField.getText().trim();
            String password = new String(passwordField.getPassword());
            LoginDAO loginDAO = new LoginDAO();
            if (loginDAO.authenticate(userId, password)) { // 인증 성공
                new MainUI(userId); // 관리 UI 실행, ID를 전달
                frame.dispose(); // 로그인 창 닫기
            } else {
                JOptionPane.showMessageDialog(frame, "ID 또는 비밀번호가 잘못되었습니다.");
            }
        });

        // 엔터 키로 로그인 버튼 클릭 기능 추가
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signInButton.doClick(); // 로그인 버튼 클릭 이벤트 발생
                }
            }
        };

        // 키 리스너 추가
        idField.addKeyListener(enterKeyAdapter);
        passwordField.addKeyListener(enterKeyAdapter);

        // 버튼 마우스 오버 효과 추가
        signInButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signInButton.setBackground(Color.DARK_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signInButton.setBackground(Color.BLACK);
            }
        });

        frame.add(signInButton);

        // 프레임 중앙에 표시
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); // 즉시 표시
    }

    // 텍스트 필드에 플레이스홀더 기능 추가
    private static void addPlaceholderBehavior(JTextField textField, String placeholder) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText(""); // 플레이스홀더 제거
                    textField.setForeground(Color.BLACK); // 텍스트 색상 변경
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY); // 플레이스홀더 색상 설정
                    textField.setText(placeholder); // 플레이스홀더 복원
                }
            }
        });
    }
}
