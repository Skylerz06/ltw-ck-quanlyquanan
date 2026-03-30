package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.view.MainView;

import javax.swing.*;

public class MainController {

    private final MainView view;

    public MainController(MainView view) {
        this.view = view;
        init();
    }

    private void init() {
        registerEvents();
    }

    private void registerEvents() {
        view.getBtnMonAn().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Mở màn hình Quản lý món ăn")
                //MonAnView monAnView = new MonAnView();
                //new MonAnController(monAnView);
                //monAnView.setVisible(true);
        );

        view.getBtnKhachHang().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Mở màn hình Quản lý khách hàng")
        );

        view.getBtnNhanVien().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Mở màn hình Quản lý nhân viên")
        );

        view.getBtnHoaDon().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Mở màn hình Lập hóa đơn")
        );

        view.getBtnThongKe().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Mở màn hình Thống kê")
        );

        view.getBtnDangXuat().addActionListener(e -> dangXuat());

        view.getBtnThoat().addActionListener(e -> thoatUngDung());
    }

    private void dangXuat() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(view, "Đăng xuất thành công");
            view.dispose();

            // TODO: Mở lại LoginView ở đây
            // new LoginController(new LoginView());
        }
    }

    private void thoatUngDung() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn thoát chương trình?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
        }
    }

    public void showMainView() {
        view.setVisible(true);
    }
}