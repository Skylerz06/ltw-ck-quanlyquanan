package ltw.ck.quanlyquanan.model.dto;

import java.time.LocalDateTime;

public class HoaDonRowDto {

    private final Long maHd;
    private final LocalDateTime ngayLap;
    private final String tenKhachHang;
    private final String tenNhanVien;
    private final String tenBan;
    private final int tongSoLuong;
    private final double tongTien;

    public HoaDonRowDto(Long maHd,
                        LocalDateTime ngayLap,
                        String tenKhachHang,
                        String tenNhanVien,
                        String tenBan,
                        int tongSoLuong,
                        double tongTien) {
        this.maHd = maHd;
        this.ngayLap = ngayLap;
        this.tenKhachHang = tenKhachHang;
        this.tenNhanVien = tenNhanVien;
        this.tenBan = tenBan;
        this.tongSoLuong = tongSoLuong;
        this.tongTien = tongTien;
    }

    public Long getMaHd() {
        return maHd;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public String getTenBan() {
        return tenBan;
    }

    public int getTongSoLuong() {
        return tongSoLuong;
    }

    public double getTongTien() {
        return tongTien;
    }
}
