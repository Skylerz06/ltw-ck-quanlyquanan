package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.dto.ChiTietHDDto;
import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonService {
    DanhMucData loadDanhMuc();

    List<HoaDon> findAll();
    List<HoaDon> search(String tuKhoa);

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

    KhachHang resolveKhachHang(KhachHang khachHangDangChon, String tenKhachHangNhap);

    FormData toFormData(HoaDon hoaDon);
    List<HistoryRow> toHistoryRows(List<HoaDon> hoaDons);
    List<ItemData> toItemDataList(HoaDon hoaDon);
    List<ChiTietHDDto> toChiTietHDDtos(HoaDon hoaDon);
    List<ChiTietHDDto> toChiTietHDDtos(List<ItemData> items);

    List<ItemData> addItem(List<ItemData> currentItems, MonAn monAn, String soLuongText);
    List<ItemData> updateItem(List<ItemData> currentItems, Long maMonCu, MonAn monAnMoi, String soLuongText);
    List<ItemData> removeItem(List<ItemData> currentItems, Long maMon);

    int tinhTongSoLuong(List<ItemData> items);
    double tinhTongTien(List<ItemData> items);
    double tinhTongTienHoaDon(HoaDon hoaDon);

    boolean isEditable(HoaDon hoaDon);
    void validateEditable(HoaDon hoaDon);

    record DanhMucData(
            List<KhachHang> khachHangs,
            List<NhanVien> nhanViens,
            List<Ban> bans,
            List<MonAn> monAns
    ) {
    }

    record FormData(
            Long maHd,
            LocalDateTime ngayLap,
            HoaDonStatus trangThai,
            Long maKhachHang,
            Long maBan,
            List<ItemData> items
    ) {
    }

    record HistoryRow(
            Long maHd,
            LocalDateTime ngayLap,
            HoaDonStatus trangThai,
            String tenKhachHang,
            String tenNhanVien,
            String tenBan,
            int tongSoLuong,
            double tongTien,
            boolean editable
    ) {
    }

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
