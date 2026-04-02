package ltw.ck.quanlyquanan.services.impl;

import ltw.ck.quanlyquanan.model.dao.KhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.LoaiKhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.impl.KhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.LoaiKhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.LoaiKH;
import ltw.ck.quanlyquanan.services.KhachHangService;
import ltw.ck.quanlyquanan.services.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangDAO khachHangDAO;
    private final LoaiKhachHangDAO loaiKhachHangDAO;

    public KhachHangServiceImpl() {
        this(new KhachHangDAOImpl(), new LoaiKhachHangDAOImpl());
    }

    public KhachHangServiceImpl(KhachHangDAO khachHangDAO, LoaiKhachHangDAO loaiKhachHangDAO) {
        this.khachHangDAO = khachHangDAO;
        this.loaiKhachHangDAO = loaiKhachHangDAO;
    }

    @Override
    public List<KhachHang> findAll() {
        return new ArrayList<>(khachHangDAO.findAll());
    }

    @Override
    public List<KhachHang> search(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return findAll();
        }

        List<KhachHang> tatCaKhachHang = khachHangDAO.findAll();
        String normalizedKeyword = tuKhoa.toLowerCase(Locale.ROOT);
        List<KhachHang> ketQua = new ArrayList<>();

        for (KhachHang khachHang : tatCaKhachHang) {
            String tenKH = safeLower(khachHang.getTenKh());
            String tenLoai = khachHang.getLoaiKhachHang() == null
                    ? ""
                    : safeLower(khachHang.getLoaiKhachHang().getTenLoaiKh());

            if (tenKH.contains(normalizedKeyword) || tenLoai.contains(normalizedKeyword)) {
                ketQua.add(khachHang);
            }
        }

        return ketQua;
    }

    @Override
    public List<LoaiKH> findAllLoaiKH() {
        return new ArrayList<>(loaiKhachHangDAO.findAll());
    }

    @Override
    public KhachHang findById(Long maKH) {
        return khachHangDAO.findById(maKH);
    }

    @Override
    public KhachHang create(String tenKH, LoaiKH loaiKH) {
        FormData formData = validate(false, null, tenKH, loaiKH);

        KhachHang khachHang = new KhachHang();
        khachHang.setTenKh(formData.tenKH());
        khachHang.setLoaiKhachHang(formData.loaiKH());

        khachHangDAO.save(khachHang);
        return khachHang;
    }

    @Override
    public KhachHang update(Long maKH, String tenKH, LoaiKH loaiKH) {
        if (maKH == null) {
            throw new ServiceException("Vui lòng chọn khách hàng cần cập nhật.");
        }

        KhachHang khachHang = khachHangDAO.findById(maKH);
        if (khachHang == null) {
            throw new ServiceException("Không tìm thấy khách hàng cần cập nhật.");
        }

        FormData formData = validate(true, khachHang, tenKH, loaiKH);

        khachHang.setTenKh(formData.tenKH());
        khachHang.setLoaiKhachHang(formData.loaiKH());

        khachHangDAO.update(khachHang);
        return khachHang;
    }

    @Override
    public void delete(Long maKH) {
        if (maKH == null) {
            throw new ServiceException("Vui lòng chọn khách hàng cần xóa.");
        }
        khachHangDAO.delete(maKH);
    }

    private FormData validate(boolean isUpdate, KhachHang khachHangHienTai,
                              String tenKH, LoaiKH loaiKH) {
        if (tenKH == null || tenKH.isBlank()) {
            throw new ServiceException("Vui lòng nhập tên khách hàng.");
        }

        if (loaiKH == null) {
            throw new ServiceException("Vui lòng chọn loại khách hàng.");
        }

        List<KhachHang> khachHangCungTen = khachHangDAO.findByTenKh(tenKH.trim());
        for (KhachHang item : khachHangCungTen) {
            boolean isSameKhachHang = isUpdate
                    && khachHangHienTai != null
                    && item.getMaKh().equals(khachHangHienTai.getMaKh());

            if (!isSameKhachHang && item.getTenKh().equalsIgnoreCase(tenKH.trim())) {
                throw new ServiceException("Tên khách hàng đã tồn tại.");
            }
        }

        return new FormData(tenKH.trim(), loaiKH);
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private record FormData(String tenKH, LoaiKH loaiKH) {
    }
}