package ltw.ck.quanlyquanan;

import ltw.ck.quanlyquanan.controller.LoginController;
import ltw.ck.quanlyquanan.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView);
            loginController.showLoginView();
        });
    }
}