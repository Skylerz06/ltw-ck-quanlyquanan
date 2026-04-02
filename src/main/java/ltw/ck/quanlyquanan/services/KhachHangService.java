package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.LoaiKH;

import java.util.List;

public interface KhachHangService {
    List<KhachHang> findAll();
    List<KhachHang> search(String tuKhoa);
    List<LoaiKH> findAllLoaiKH();
    KhachHang findById(Long maKH);
    KhachHang create(String tenKH, LoaiKH loaiKH);
    KhachHang update(Long maKH, String tenKH, LoaiKH loaiKH);
    void delete(Long maKH);
}
