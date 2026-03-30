package ltw.ck.quanlyquanan.model.dto;

public class ChiTietHDDto {

    private final String tenMon;
    private final int soLuong;
    private final double donGia;
    private final double thanhTien;

    public ChiTietHDDto(String tenMon, int soLuong, double donGia) {
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    public String getTenMon() { return tenMon; }
    public int getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }
    public double getThanhTien() { return thanhTien; }
}
