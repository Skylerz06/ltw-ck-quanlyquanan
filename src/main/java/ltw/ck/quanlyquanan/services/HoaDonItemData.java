package ltw.ck.quanlyquanan.services;

public record HoaDonItemData(
        Long maMon,
        String tenMon,
        Double donGia,
        int soLuong
) {
    public double thanhTien() {
        return (donGia == null ? 0 : donGia) * soLuong;
    }
}