package ltw.ck.quanlyquanan.model.dto;

public class MonAnStatsDto {

    private final String tenMon;
    private final long soLuongBan;

    public MonAnStatsDto(String tenMon, long soLuongBan) {
        this.tenMon = tenMon;
        this.soLuongBan = soLuongBan;
    }

    public String getTenMon() {
        return tenMon;
    }

    public long getSoLuongBan() {
        return soLuongBan;
    }
}
