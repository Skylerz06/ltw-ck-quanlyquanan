package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.KhachHang;

import java.util.List;

public interface KhachHangDAO {
    List<KhachHang> findAll();
    KhachHang findById(Long id);
    List<KhachHang> findByTenKh(String tenKh);

    void save(KhachHang entity);
    void update(KhachHang entity);
    void delete(Long id);
}
