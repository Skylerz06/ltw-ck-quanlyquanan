package ltw.ck.quanlyquanan.model.entity;
import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "khach_hang")

public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_kh")
    private Long maKh;

    @Column(name = "ten_kh", nullable = false, length = 100)
    private String tenKh;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_loai_kh", nullable = false,
        foreignKey = @ForeignKey(name = "fk_khach_hang_loai_khach_hang"))
    private LoaiKH loaiKhachHang;

    @OneToMany(mappedBy = "khachHang")
    private List<HoaDon> lstHoaDon = new ArrayList<>();


    public KhachHang() {
    }

    public KhachHang(Long maKh, String tenKh, LoaiKH loaiKhachHang) {
        this.maKh = maKh;
        this.tenKh = tenKh;
        this.loaiKhachHang = loaiKhachHang;
    }

    public Long getMaKh() {
        return maKh;
    }

    public void setMaKh(Long maKh) {
        this.maKh = maKh;
    }

    public String getTenKh() {
        return tenKh;
    }

    public void setTenKh(String tenKh) {
        this.tenKh = tenKh;
    }

    public LoaiKH getLoaiKhachHang() {
        return loaiKhachHang;
    }

    public void setLoaiKhachHang(LoaiKH loaiKhachHang) {
        this.loaiKhachHang = loaiKhachHang;
    }

    public List<HoaDon> getLstHoaDon() {
        return lstHoaDon;
    }

    public void setLstHoaDon(List<HoaDon> lstHoaDon) {
        this.lstHoaDon = lstHoaDon;
    }

    public String toString() { return tenKh;}
}
