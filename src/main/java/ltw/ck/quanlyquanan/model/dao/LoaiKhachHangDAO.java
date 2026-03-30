package ltw.ck.quanlyquanan.model.dao;

import ltw.ck.quanlyquanan.model.entity.LoaiKH;

import java.util.List;

public interface LoaiKhachHangDAO {
    List<LoaiKH> findAll();
    LoaiKH findById(Long id);
    LoaiKH findByTenLoaiKh(String tenLoaiKh);

    void save(LoaiKH entity);
    void update(LoaiKH entity);
    void delete(Long id);
}
