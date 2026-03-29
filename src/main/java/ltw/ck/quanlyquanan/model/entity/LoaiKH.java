package ltw.ck.quanlyquanan.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "loai_khach_hang",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_loai_khach_hang_ten", columnNames = "ten_loai_kh")
        }
)
public class LoaiKH {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_loai_kh")
    private Long maLoaiKh;

    @Column(name = "ten_loai_kh", nullable = false, length = 100)
    private String tenLoaiKh;

    @OneToMany(mappedBy = "loaiKhachHang")
    private List<KhachHang> lstKhachHang = new ArrayList<>();

    public LoaiKH() {
    }

    public LoaiKH(String tenLoaiKh) {
        this.tenLoaiKh = tenLoaiKh;
    }

    public Long getMaLoaiKh() {
        return maLoaiKh;
    }

    public void setMaLoaiKh(Long maLoaiKh) {
        this.maLoaiKh = maLoaiKh;
    }

    public String getTenLoaiKh() {
        return tenLoaiKh;
    }

    public void setTenLoaiKh(String tenLoaiKh) {
        this.tenLoaiKh = tenLoaiKh;
    }

    public List<KhachHang> getLstKhachHang() {
        return lstKhachHang;
    }

    public void setLstKhachHang(List<KhachHang> lstKhachHang) {
        this.lstKhachHang = lstKhachHang;
    }
}
