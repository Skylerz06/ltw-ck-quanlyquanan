package ltw.ck.quanlyquanan.services.impl;

import ltw.ck.quanlyquanan.model.dao.TaiKhoanDAO;
import ltw.ck.quanlyquanan.model.dao.impl.TaiKhoanDAOImpl;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.services.LoginService;

public class LoginServiceImpl implements LoginService {

    private final TaiKhoanDAO taiKhoanDAO;

    public LoginServiceImpl() {
        this(new TaiKhoanDAOImpl());
    }

    public LoginServiceImpl(TaiKhoanDAO taiKhoanDAO) {
        this.taiKhoanDAO = taiKhoanDAO;
    }

    @Override
    public TaiKhoan login(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập.");
        }

        if (matKhau == null || matKhau.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu.");
        }

        TaiKhoan taiKhoan = taiKhoanDAO.checkLogin(tenDangNhap.trim(), matKhau);
        if (taiKhoan == null) {
            throw new IllegalArgumentException("Sai tên đăng nhập hoặc mật khẩu.");
        }

        return taiKhoan;
    }
}
