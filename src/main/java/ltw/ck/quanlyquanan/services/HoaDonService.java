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
                  List<ItemData> items);

    HoaDon update(Long maHd,
                  KhachHang khachHang,
                  NhanVien nhanVien,
                  Ban ban,
                  HoaDonStatus trangThai,
                  List<ItemData> items);

    List<ItemData> toItemDataList(HoaDon hoaDon);

    List<ItemData> addItem(List<ItemData> currentItems, MonAn monAn, String soLuongText);
    List<ItemData> updateItem(List<ItemData> currentItems, Long maMonCu, MonAn monAnMoi, String soLuongText);
    List<ItemData> removeItem(List<ItemData> currentItems, Long maMon);

    int tinhTongSoLuong(List<ItemData> items);
    double tinhTongTien(List<ItemData> items);
    double tinhTongTienHoaDon(HoaDon hoaDon);

    record ItemData(
            Long maMon,
            String tenMon,
            Double donGia,
            int soLuong
    ) {
        public double thanhTien() {
            return (donGia == null ? 0 : donGia) * soLuong;
        }
    }
}
