package ltw.ck.quanlyquanan.model.dto;

public class KhachHangStatsDto {

    private final String tenKhachHang;
    private final long soHoaDon;
    private final int tongSoLuong;
    private final double tongDoanhThu;

    public KhachHangStatsDto(String tenKhachHang, long soHoaDon, int tongSoLuong, double tongDoanhThu) {
        this.tenKhachHang = tenKhachHang;
        this.soHoaDon = soHoaDon;
        this.tongSoLuong = tongSoLuong;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public long getSoHoaDon() {
        return soHoaDon;
    }

    public int getTongSoLuong() {
        return tongSoLuong;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }
}
