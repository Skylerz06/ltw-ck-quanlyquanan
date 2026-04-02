package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;

import java.util.List;

public interface HoaDonService {
    List<HoaDon> findAll();
    List<HoaDon> search(String tuKhoa);

    List<KhachHang> findAllKhachHang();
    List<NhanVien> findAllNhanVien();
    List<Ban> findAllBan();
    List<MonAn> findAllMonAn();

    HoaDon findById(Long maHd);
    void delete(Long maHd);

    HoaDon create(KhachHang khachHang,
                  NhanVien nhanVien,
                  Ban ban,
                  HoaDonStatus trangThai,
                  List<HoaDonItemData> items);

    HoaDon update(Long maHd,
                  KhachHang khachHang,
                  NhanVien nhanVien,
                  Ban ban,
                  HoaDonStatus trangThai,
                  List<HoaDonItemData> items);

    List<HoaDonItemData> toItemDataList(HoaDon hoaDon);

    List<HoaDonItemData> addItem(List<HoaDonItemData> currentItems, MonAn monAn, String soLuongText);
    List<HoaDonItemData> updateItem(List<HoaDonItemData> currentItems, Long maMonCu, MonAn monAnMoi, String soLuongText);
    List<HoaDonItemData> removeItem(List<HoaDonItemData> currentItems, Long maMon);

    int tinhTongSoLuong(List<HoaDonItemData> items);
    double tinhTongTien(List<HoaDonItemData> items);
    double tinhTongTienHoaDon(HoaDon hoaDon);
}