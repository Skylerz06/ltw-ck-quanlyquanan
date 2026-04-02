package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.entity.TaiKhoan;

public interface LoginService {
    TaiKhoan login(String tenDangNhap, String matKhau);
}