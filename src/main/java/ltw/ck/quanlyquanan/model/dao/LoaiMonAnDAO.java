package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;

import java.util.List;

public interface LoaiMonAnDAO {
    List<LoaiMonAn> findAll();
    LoaiMonAn findById(Long id);
    LoaiMonAn findByTenLoaiMon(String tenLoaiMon);

    void save(LoaiMonAn entity);
    void update(LoaiMonAn entity);
    void delete(Long id);
}