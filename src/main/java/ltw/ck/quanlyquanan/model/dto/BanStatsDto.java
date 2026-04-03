package ltw.ck.quanlyquanan.model.dto;

public class BanStatsDto {

    private final String tenBan;
    private final long soHoaDon;
    private final int tongSoLuong;
    private final double tongDoanhThu;

    public BanStatsDto(String tenBan, long soHoaDon, int tongSoLuong, double tongDoanhThu) {
        this.tenBan = tenBan;
        this.soHoaDon = soHoaDon;
        this.tongSoLuong = tongSoLuong;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getTenBan() {
        return tenBan;
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
