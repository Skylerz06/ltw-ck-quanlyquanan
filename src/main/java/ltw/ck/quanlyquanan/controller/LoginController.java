package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dao.TaiKhoanDAO;
import ltw.ck.quanlyquanan.model.dao.impl.TaiKhoanDAOImpl;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.view.LoginFrame;
import ltw.ck.quanlyquanan.view.MainFrame;

import javax.swing.*;

public class LoginController {

    private final LoginFrame view;
    private final TaiKhoanDAO taiKhoanDAO;

    public LoginController(LoginFrame view) {
        this.view = view;
        this.taiKhoanDAO = new TaiKhoanDAOImpl();
        init();
    }

    private void init() {
        registerEvents();
    }

    private void registerEvents() {
        view.getBtnDangNhap().addActionListener(e -> dangNhap());
        view.getBtnThoat().addActionListener(e -> thoat());

        view.getTxtMatKhau().addActionListener(e -> dangNhap());
        view.getTxtTenDangNhap().addActionListener(e -> dangNhap());
    }

    private void dangNhap() {
        String tenDangNhap = view.getTenDangNhap();
        String matKhau = view.getMatKhau();

        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập tên đăng nhập.");
            view.getTxtTenDangNhap().requestFocus();
            return;
        }

        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập mật khẩu.");
            view.getTxtMatKhau().requestFocus();
            return;
        }

        try {
            TaiKhoan taiKhoan = taiKhoanDAO.checkLogin(tenDangNhap, matKhau);

            if (taiKhoan == null) {
                JOptionPane.showMessageDialog(view, "Sai tên đăng nhập hoặc mật khẩu.");
                view.getTxtMatKhau().setText("");
                view.getTxtMatKhau().requestFocus();
                return;
            }

            AppSession.setCurrentTaiKhoan(taiKhoan);

            String tenHienThi = taiKhoan.getNhanVien() != null
                    ? taiKhoan.getNhanVien().getHoTen()
                    : taiKhoan.getTenDangNhap();

            JOptionPane.showMessageDialog(view, "Đăng nhập thành công!");

            MainFrame mainView = new MainFrame();
            MainController mainController = new MainController(mainView);
            mainView.setWelcomeText("Xin chào, " + tenHienThi);
            mainController.showMainView();

            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Có lỗi khi đăng nhập: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void thoat() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn thoát?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            view.dispose();
        }
    }

    public void showLoginView() {
        view.setVisible(true);
    }
}
