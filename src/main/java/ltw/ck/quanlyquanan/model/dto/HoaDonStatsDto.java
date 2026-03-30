package ltw.ck.quanlyquanan.model.dto;

public class HoaDonStatsDto {

    private final long totalHoaDon;
    private final double tongDoanhThu;
    private final long hoaDonHomNay;

    public HoaDonStatsDto(long totalHoaDon, double tongDoanhThu, long hoaDonHomNay) {
        this.totalHoaDon = totalHoaDon;
        this.tongDoanhThu = tongDoanhThu;
        this.hoaDonHomNay = hoaDonHomNay;
    }

    public long getTotalHoaDon() {
        return totalHoaDon;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public long getHoaDonHomNay() {
        return hoaDonHomNay;
    }
}
