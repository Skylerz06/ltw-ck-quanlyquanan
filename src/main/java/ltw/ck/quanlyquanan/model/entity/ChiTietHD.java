package ltw.ck.quanlyquanan.model.entity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "chi_tiet_hd",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_chi_tiet_hd_hd_mon", columnNames = {"ma_hd", "ma_mon"})
        }
)
public class ChiTietHD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_cthd")
    private Long maCthd;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_hd", nullable = false,
            foreignKey = @ForeignKey(name = "fk_chi_tiet_hd_hoa_don"))
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_mon", nullable = false,
            foreignKey = @ForeignKey(name = "fk_chi_tiet_hd_mon_an"))
    private MonAn monAn;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong = 1;

    public ChiTietHD() {
    }

    public ChiTietHD(HoaDon hoaDon, MonAn monAn, Integer soLuong) {
        this.hoaDon = hoaDon;
        this.monAn = monAn;
        this.soLuong = soLuong;
    }

    public Long getMaCthd() {
        return maCthd;
    }

    public void setMaCthd(Long maCthd) {
        this.maCthd = maCthd;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }
}
