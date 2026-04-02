package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.entity.KhachHang;

import java.util.List;

public interface example {
    List<KhachHang> findAll();
    KhachHang findById(Long id);
}
