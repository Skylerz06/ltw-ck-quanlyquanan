package ltw.ck.quanlyquanan.model.entity;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "ban",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_ban_ten_ban", columnNames = "ten_ban")
    })

public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ban")
    private Long maBan;

    @Column(name = "ten_ban", nullable = false, length = 100)
    private String tenBan;

    @OneToMany(mappedBy = "ban")
    private List<HoaDon> lstHoaDon = new ArrayList<>();


    public Ban() {
    }

    public Ban(Long maBan, String tenBan) {
        this.maBan = maBan;
        this.tenBan = tenBan;
    }

    public Long getMaBan() {
        return maBan;
    }

    public Long setMaBan(Long maBan) {
        return this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public List<HoaDon> getLstHoaDon() {
        return lstHoaDon;
    }

    public void setLstHoaDon(List<HoaDon> lstHoaDon) {
        this.lstHoaDon = lstHoaDon;
    }

    public String toString() { return tenBan;}
}
