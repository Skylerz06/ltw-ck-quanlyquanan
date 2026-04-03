package ltw.ck.quanlyquanan.model.dto;

public class ChiTietHDDto {

    private final Long maMon;
    private final String tenMon;
    private final int soLuong;
    private final double donGia;
    private final double thanhTien;

    public ChiTietHDDto(Long maMon, String tenMon, int soLuong, double donGia) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    public Long getMaMon() {
        return maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }
}
