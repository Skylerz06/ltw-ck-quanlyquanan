package ltw.ck.quanlyquanan.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nhan_vien")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nv")
    private Long maNV;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "dia_chi", length = 255)
    private String diaChi;

    @Column(name = "sdt", nullable = false, unique = true, length = 15)
    private String sdt;

    @OneToOne(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private TaiKhoan taiKhoan;

    public NhanVien() {
    }

    public NhanVien(String hoTen, String diaChi, String sdt) {
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public Long getMaNV() {
        return maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @Override
    public String toString() {
        return hoTen;
    }
}