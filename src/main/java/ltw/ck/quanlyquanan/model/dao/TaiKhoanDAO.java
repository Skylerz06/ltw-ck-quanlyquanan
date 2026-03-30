package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.TaiKhoan;

import java.util.List;

public interface TaiKhoanDAO {
    List<TaiKhoan> findAll();
    TaiKhoan findById(Long id);
    TaiKhoan findByTenDangNhap(String tenDangNhap);
    TaiKhoan checkLogin(String tenDangNhap, String matKhau);

    void save(TaiKhoan entity);
    void update(TaiKhoan entity);
    void delete(Long id);
}