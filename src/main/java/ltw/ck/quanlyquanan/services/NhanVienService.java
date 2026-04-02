package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.entity.NhanVien;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> findAll();
    List<NhanVien> search(String tuKhoa);

    NhanVien create(String hoTen, String sdt, String diaChi,
                    String tenDangNhap, String matKhau);

    NhanVien update(Long maNV, String hoTen, String sdt, String diaChi,
                    String tenDangNhap, String matKhau);

    void delete(Long maNV);

    NhanVien findById(Long maNV);
}