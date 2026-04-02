package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.view.HoaDonPanel;
import ltw.ck.quanlyquanan.view.KhachHangPanel;
import ltw.ck.quanlyquanan.view.LoginFrame;
import ltw.ck.quanlyquanan.view.MainFrame;
import ltw.ck.quanlyquanan.view.MonAnPanel;
import ltw.ck.quanlyquanan.view.NhanVienPanel;
import ltw.ck.quanlyquanan.view.ThongKePanel;

import javax.swing.*;

public class MainController {

    private final MainFrame view;
    private JPanel currentSubForm;

    public MainController(MainFrame view) {
        this.view = view;
        init();
    }

    private void init() {
        registerEvents();
        moManHinhHoaDon();
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

            LoginFrame loginView = new LoginFrame();
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
        NhanVienPanel nhanVienView = new NhanVienPanel();
        new NhanVienController(nhanVienView);
        hienThiSubForm(nhanVienView);
    }

    private void moManHinhMonAn() {
        MonAnPanel monAnView = new MonAnPanel();
        new MonAnController(monAnView);
        hienThiSubForm(monAnView);
    }

    private void moManHinhKhachHang() {
        KhachHangPanel khachHangView = new KhachHangPanel();
        new KhachHangController(khachHangView);
        hienThiSubForm(khachHangView);
    }

    private void moManHinhHoaDon() {
        HoaDonPanel hoaDonView = new HoaDonPanel();
        new HoaDonController(hoaDonView);
        hienThiSubForm(hoaDonView);
    }

    private void moManHinhThongKe() {
        ThongKePanel thongKeView = new ThongKePanel();
        new ThongKeController(thongKeView);
        hienThiSubForm(thongKeView);
    }

    private void hienThiSubForm(JPanel subForm) {
        currentSubForm = subForm;
        view.showSubView(subForm);
    }

    public void showMainView() {
        view.setVisible(true);
    }
}
