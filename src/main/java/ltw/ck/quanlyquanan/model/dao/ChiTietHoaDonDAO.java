package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.ChiTietHD;

import java.util.List;

public interface ChiTietHoaDonDAO {
    List<ChiTietHD> findByHoaDon(Long maHd);
    ChiTietHD findById(Long id);
    ChiTietHD findByHoaDonAndMonAn(Long maHd, Long maMon);

    void save(ChiTietHD entity);
    void update(ChiTietHD entity);
    void delete(Long id);
}
