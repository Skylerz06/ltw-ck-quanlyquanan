package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.HoaDon;

import java.time.LocalDateTime;
import java.util.List;

public interface HoaDonDAO {
    List<HoaDon> findAll();
    HoaDon findById(Long id);
    List<HoaDon> findByKhoangNgay(LocalDateTime from, LocalDateTime to);
    List<HoaDon> findByNhanVien(Long maNv);
    List<HoaDon> findByBan(Long maBan);

    void save(HoaDon entity);
    void update(HoaDon entity);
    void delete(Long id);
}
