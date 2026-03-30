package ltw.ck.quanlyquanan.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mon_an")
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_mon")
    private Long maMon;

    @Column(name = "ten_mon", nullable = false, unique = true, length = 100)
    private String tenMon;

    @Column(name = "don_gia", nullable = false)
    private Double donGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_loai", nullable = false)
    private LoaiMonAn loaiMonAn;

    public MonAn() {
    }

    public MonAn(String tenMon, Double donGia, LoaiMonAn loaiMonAn) {
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.loaiMonAn = loaiMonAn;
    }

    public Long getMaMon() {
        return maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public Double getDonGia() {
        return donGia;
    }

    public LoaiMonAn getLoaiMonAn() {
        return loaiMonAn;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public void setLoaiMonAn(LoaiMonAn loaiMonAn) {
        this.loaiMonAn = loaiMonAn;
    }

    @Override
    public String toString() {
        return tenMon;
    }
}