package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.services.LoginService;
import ltw.ck.quanlyquanan.services.ServiceException;
import ltw.ck.quanlyquanan.services.impl.LoginServiceImpl;
import ltw.ck.quanlyquanan.view.LoginFrame;
import ltw.ck.quanlyquanan.view.MainFrame;

import javax.swing.*;

public class LoginController {

    private final LoginFrame view;
    private final LoginService loginService;

    public LoginController(LoginFrame view) {
        this(view, new LoginServiceImpl());
    }

    public LoginController(LoginFrame view, LoginService loginService) {
        this.view = view;
        this.loginService = loginService;
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
        try {
            TaiKhoan taiKhoan = loginService.login(
                    view.getTenDangNhap(),
                    view.getMatKhau()
            );

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

        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage());

            if ("Vui lòng nhập tên đăng nhập.".equals(ex.getMessage())) {
                view.getTxtTenDangNhap().requestFocus();
            } else {
                view.getTxtMatKhau().setText("");
                view.getTxtMatKhau().requestFocus();
            }
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