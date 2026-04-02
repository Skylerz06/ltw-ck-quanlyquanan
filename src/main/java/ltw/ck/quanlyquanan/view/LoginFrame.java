package ltw.ck.quanlyquanan.view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField txtTenDangNhap = new JTextField(20);
    private final JPasswordField txtMatKhau = new JPasswordField(20);

    private final JButton btnDangNhap = new JButton("Đăng nhập");
    private final JButton btnThoat = new JButton("Thoát");

    public LoginFrame() {
        setTitle("Đăng nhập hệ thống");
        setSize(420, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đăng nhập"));
        formPanel.add(new JLabel("Tên đăng nhập:"));
        formPanel.add(txtTenDangNhap);
        formPanel.add(new JLabel("Mật khẩu:"));
        formPanel.add(txtMatKhau);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(btnDangNhap);
        buttonPanel.add(btnThoat);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public JTextField getTxtTenDangNhap() {
        return txtTenDangNhap;
    }

    public JPasswordField getTxtMatKhau() {
        return txtMatKhau;
    }

    public JButton getBtnDangNhap() {
        return btnDangNhap;
    }

    public JButton getBtnThoat() {
        return btnThoat;
    }

    public String getTenDangNhap() {
        return txtTenDangNhap.getText().trim();
    }

    public String getMatKhau() {
        return new String(txtMatKhau.getPassword());
    }

    public void clearForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        txtTenDangNhap.requestFocus();
    }
}