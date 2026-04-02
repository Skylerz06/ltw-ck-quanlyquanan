package ltw.ck.quanlyquanan;

import ltw.ck.quanlyquanan.controller.LoginController;
import ltw.ck.quanlyquanan.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginView = new LoginFrame();
            LoginController loginController = new LoginController(loginView);
            loginController.showLoginView();
        });
    }
}