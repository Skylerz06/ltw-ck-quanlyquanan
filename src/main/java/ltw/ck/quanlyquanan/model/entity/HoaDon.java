package ltw.ck.quanlyquanan.model.entity;

import jakarta.persistence.*;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hd")
    private Long maHd;

    @Column(name = "ngay_lap", nullable = false)
    private LocalDateTime ngayLap;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 100)
    private HoaDonStatus trangThai = HoaDonStatus.CREATED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_kh",
            foreignKey = @ForeignKey(name = "fk_hoa_don_khach_hang"))
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_nv", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hoa_don_nhan_vien"))
    private NhanVien nhanVien;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_ban", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hoa_don_ban"))
    private Ban ban;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietHD> lstChiTietHoaDon = new ArrayList<>();

    public HoaDon() {
    }

    @PrePersist
    public void prePersist() {
        if (this.ngayLap == null) {
            this.ngayLap = LocalDateTime.now();
        }
        if (this.trangThai == null) {
            this.trangThai = HoaDonStatus.CREATED;
        }
    }

    public Long getMaHd() {
        return maHd;
    }

    public void setMaHd(Long maHd) {
        this.maHd = maHd;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public HoaDonStatus getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(HoaDonStatus trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }

    public List<ChiTietHD> getLstChiTietHoaDon() {
        return lstChiTietHoaDon;
    }

    public void setLstChiTietHoaDon(List<ChiTietHD> lstChiTietHoaDon) {
        this.lstChiTietHoaDon = lstChiTietHoaDon;
    }
}
