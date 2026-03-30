package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;

import java.util.List;

public interface MonAnDAO {
    List<MonAn> findAll();
    MonAn findById(Long id);
    List<MonAn> findByTenMon(String tenMon);
    List<MonAn> findByLoaiMonAn(LoaiMonAn loaiMonAn);

    void save(MonAn entity);
    void update(MonAn entity);
    void delete(Long id);
}