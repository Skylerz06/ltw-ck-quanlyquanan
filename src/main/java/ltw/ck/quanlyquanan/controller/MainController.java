package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.view.BaseSubView;
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
    private BaseSubView currentSubForm;

    public MainController(MainView view) {
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
        new NhanVienController(nhanVienView);
        hienThiSubForm(nhanVienView);
    }

    private void moManHinhMonAn() {
        MonAnView monAnView = new MonAnView();
        new MonAnController(monAnView);
        hienThiSubForm(monAnView);
    }

    private void moManHinhKhachHang() {
        KhachHangView khachHangView = new KhachHangView();
        new KhachHangController(khachHangView);
        hienThiSubForm(khachHangView);
    }

    private void moManHinhHoaDon() {
        HoaDonView hoaDonView = new HoaDonView();
        new HoaDonController(hoaDonView);
        hienThiSubForm(hoaDonView);
    }

    private void moManHinhThongKe() {
        ThongKeView thongKeView = new ThongKeView();
        new ThongKeController(thongKeView);
        hienThiSubForm(thongKeView);
    }

    private void hienThiSubForm(BaseSubView subForm) {
        currentSubForm = subForm;
        currentSubForm.setCloseHandler(this::dongSubForm);
        view.showSubView(subForm);
    }

    private void dongSubForm() {
        currentSubForm = null;
        moManHinhHoaDon();
    }

    public void showMainView() {
        view.setVisible(true);
    }
}
