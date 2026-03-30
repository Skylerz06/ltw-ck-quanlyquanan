package ltw.ck.quanlyquanan.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private final JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ QUÁN ĂN", SwingConstants.CENTER);
    private final JLabel lblWelcome = new JLabel("Xin chào!", SwingConstants.CENTER);

    private final JButton btnMonAn = new JButton("Quản lý món ăn");
    private final JButton btnKhachHang = new JButton("Quản lý khách hàng");
    private final JButton btnNhanVien = new JButton("Quản lý nhân viên");
    private final JButton btnHoaDon = new JButton("Lập hóa đơn");
    private final JButton btnThongKe = new JButton("Thống kê");
    private final JButton btnDangXuat = new JButton("Đăng xuất");
    private final JButton btnThoat = new JButton("Thoát");

    public MainView() {
        setTitle("Quản lý quán ăn - Main");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        headerPanel.add(lblTitle);
        headerPanel.add(lblWelcome);

        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Chức năng chính"));
        menuPanel.add(btnMonAn);
        menuPanel.add(btnKhachHang);
        menuPanel.add(btnNhanVien);
        menuPanel.add(btnHoaDon);
        menuPanel.add(btnThongKe);
        menuPanel.add(btnDangXuat);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        centerWrapper.add(menuPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footerPanel.add(btnThoat);

        add(headerPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public JLabel getLblWelcome() {
        return lblWelcome;
    }

    public JButton getBtnMonAn() {
        return btnMonAn;
    }

    public JButton getBtnKhachHang() {
        return btnKhachHang;
    }

    public JButton getBtnNhanVien() {
        return btnNhanVien;
    }

    public JButton getBtnHoaDon() {
        return btnHoaDon;
    }

    public JButton getBtnThongKe() {
        return btnThongKe;
    }

    public JButton getBtnDangXuat() {
        return btnDangXuat;
    }

    public JButton getBtnThoat() {
        return btnThoat;
    }

    public void setWelcomeText(String message) {
        lblWelcome.setText(message);
    }
}