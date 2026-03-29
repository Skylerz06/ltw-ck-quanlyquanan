package ltw.ck.quanlyquanan.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loai_mon_an")
public class LoaiMonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLoai;

    @Column(nullable = false, unique = true, length = 100)
    private String tenLoai;

    @OneToMany(mappedBy = "loaiMonAn", cascade = CascadeType.ALL)
    private List<MonAn> monAns = new ArrayList<>();

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

    public List<MonAn> getMonAns() {
        return monAns;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public void addMonAn(MonAn monAn) {
        monAns.add(monAn);
        monAn.setLoaiMonAn(this);
    }

    public void removeMonAn(MonAn monAn) {
        monAns.remove(monAn);
        monAn.setLoaiMonAn(null);
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}