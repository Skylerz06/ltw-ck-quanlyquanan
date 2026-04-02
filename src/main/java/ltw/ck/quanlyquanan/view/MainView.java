package ltw.ck.quanlyquanan.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private static final String CARD_HOME = "home";
    private static final String CARD_SUB_FORM = "subForm";

    private final JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ QUÁN ĂN", SwingConstants.CENTER);
    private final JLabel lblWelcome = new JLabel("Xin chào!", SwingConstants.CENTER);

    private final JButton btnMonAn = new JButton("Quản lý món ăn");
    private final JButton btnKhachHang = new JButton("Quản lý khách hàng");
    private final JButton btnNhanVien = new JButton("Quản lý nhân viên");
    private final JButton btnHoaDon = new JButton("Hóa đơn");
    private final JButton btnThongKe = new JButton("Thống kê");
    private final JButton btnDangXuat = new JButton("Đăng xuất");
    private final JButton btnThoat = new JButton("Thoát");

    private final JPanel workspaceContent = new JPanel(new CardLayout());
    private final JPanel homePanel = createHomePanel();
    private JPanel currentSubView;

    public MainView() {
        setTitle("Quản lý quán ăn - Main");
        setSize(1440, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        headerPanel.add(lblTitle);
        headerPanel.add(lblWelcome);

        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 0, 12));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        menuPanel.add(btnHoaDon);
        menuPanel.add(btnKhachHang);
        menuPanel.add(btnNhanVien);
        menuPanel.add(btnMonAn);
        menuPanel.add(btnThongKe);

        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Hệ thống"));
        actionPanel.add(btnDangXuat);
        actionPanel.add(btnThoat);

        JPanel navigationPanel = new JPanel(new BorderLayout(0, 15));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        navigationPanel.add(menuPanel, BorderLayout.CENTER);
        navigationPanel.add(actionPanel, BorderLayout.SOUTH);

        JPanel navigationWrapper = new JPanel(new BorderLayout());
        navigationWrapper.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 5));
        navigationWrapper.add(navigationPanel, BorderLayout.CENTER);

        workspaceContent.add(homePanel, CARD_HOME);

        JPanel workspacePanel = new JPanel(new BorderLayout());
        workspacePanel.setBorder(BorderFactory.createTitledBorder("Khu vực làm việc"));
        workspacePanel.add(workspaceContent, BorderLayout.CENTER);

        JPanel workspaceWrapper = new JPanel(new BorderLayout());
        workspaceWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 15, 15));
        workspaceWrapper.add(workspacePanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationWrapper, workspaceWrapper);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);

        add(headerPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        showHomeCard();
    }

    private JPanel createHomePanel() {
        JLabel lblHomeTitle = new JLabel("Chọn một chức năng ở bên trái để bắt đầu", SwingConstants.CENTER);
        lblHomeTitle.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel lblHomeSubTitle = new JLabel("Các màn hình nghiệp vụ sẽ hiển thị trực tiếp bên trong form chính.", SwingConstants.CENTER);
        lblHomeSubTitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.add(lblHomeTitle);
        panel.add(lblHomeSubTitle);
        return panel;
    }

    public void showSubView(JPanel subView) {
        if (currentSubView != null) {
            workspaceContent.remove(currentSubView);
        }
        currentSubView = subView;
        workspaceContent.add(subView, CARD_SUB_FORM);
        showCard(CARD_SUB_FORM);
    }

    public void clearSubView() {
        if (currentSubView != null) {
            workspaceContent.remove(currentSubView);
            currentSubView = null;
        }
        showHomeCard();
    }

    public void showHomeCard() {
        showCard(CARD_HOME);
    }

    private void showCard(String cardName) {
        CardLayout layout = (CardLayout) workspaceContent.getLayout();
        layout.show(workspaceContent, cardName);
        workspaceContent.revalidate();
        workspaceContent.repaint();
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
