package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.entity.MonAn;

import java.util.List;

public interface MonAnService {
    List<MonAn> findAll();
    List<MonAn> search(String tuKhoa);
    List<LoaiMonAn> findAllLoaiMonAn();

    MonAn findById(Long maMon);

    MonAn create(String tenMon, String donGiaText, LoaiMonAn loaiMonAn);
    MonAn update(Long maMon, String tenMon, String donGiaText, LoaiMonAn loaiMonAn);
    void delete(Long maMon);
}