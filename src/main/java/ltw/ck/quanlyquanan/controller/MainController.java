package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.view.HoaDonView;
import ltw.ck.quanlyquanan.view.KhachHangView;
import ltw.ck.quanlyquanan.view.LoginView;
import ltw.ck.quanlyquanan.view.MainView;
import ltw.ck.quanlyquanan.view.MonAnView;
import ltw.ck.quanlyquanan.view.NhanVienView;
import ltw.ck.quanlyquanan.view.ThongKeView;

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
        view.getBtnMonAn().addActionListener(e -> moManHinhMonAn());
        view.getBtnKhachHang().addActionListener(e -> moManHinhKhachHang());
        view.getBtnNhanVien().addActionListener(e -> moManHinhNhanVien());
        view.getBtnHoaDon().addActionListener(e -> moManHinhHoaDon());
        view.getBtnThongKe().addActionListener(e -> moManHinhThongKe());

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
            AppSession.clear();
            view.dispose();

            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView);
            loginController.showLoginView();
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

    private void moManHinhNhanVien() {
        NhanVienView nhanVienView = new NhanVienView();
        NhanVienController nhanVienController = new NhanVienController(nhanVienView);
        nhanVienController.showNhanVienView();
    }

    private void moManHinhMonAn() {
        MonAnView monAnView = new MonAnView();
        MonAnController monAnController = new MonAnController(monAnView);
        monAnController.showMonAnView();
    }

    private void moManHinhKhachHang() {
        KhachHangView khachHangView = new KhachHangView();
        KhachHangController khachHangController = new KhachHangController(khachHangView);
        khachHangController.showKhachHangView();
    }

    private void moManHinhHoaDon() {
        HoaDonView hoaDonView = new HoaDonView();
        HoaDonController hoaDonController = new HoaDonController(hoaDonView);
        hoaDonController.showHoaDonView();
    }

    private void moManHinhThongKe() {
        ThongKeView thongKeView = new ThongKeView();
        ThongKeController thongKeController = new ThongKeController(thongKeView);
        thongKeController.showThongKeView();
    }

    public void showMainView() {
        view.setVisible(true);
    }
}
