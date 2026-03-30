package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.NhanVien;

import java.util.List;

public interface NhanVienDAO {
    List<NhanVien> findAll();
    NhanVien findById(Long id);
    List<NhanVien> findByHoTen(String hoTen);
    NhanVien findBySdt(String sdt);

    void save(NhanVien entity);
    void update(NhanVien entity);
    void delete(Long id);
}