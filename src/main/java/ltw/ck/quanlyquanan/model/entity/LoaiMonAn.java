package ltw.ck.quanlyquanan.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loai_mon_an")
public class LoaiMonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_loai")
    private Long maLoai;

    @Column(name = "ten_loai", nullable = false, unique = true, length = 100)
    private String tenLoai;

    @OneToMany(mappedBy = "loaiMonAn", cascade = CascadeType.ALL)
    private List<MonAn> lstMonAn = new ArrayList<>();

    public LoaiMonAn() {
    }

    public LoaiMonAn(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public Long getMaLoai() {
        return maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public List<MonAn> getLstMonAn() {
        return lstMonAn;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public void addMonAn(MonAn monAn) {
        lstMonAn.add(monAn);
        monAn.setLoaiMonAn(this);
    }

    public void removeMonAn(MonAn monAn) {
        lstMonAn.remove(monAn);
        monAn.setLoaiMonAn(null);
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}