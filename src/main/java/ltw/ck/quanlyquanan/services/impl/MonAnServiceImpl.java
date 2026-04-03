package ltw.ck.quanlyquanan.services.impl;

import ltw.ck.quanlyquanan.model.dao.LoaiMonAnDAO;
import ltw.ck.quanlyquanan.model.dao.MonAnDAO;
import ltw.ck.quanlyquanan.model.dao.impl.LoaiMonAnDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.MonAnDAOImpl;
import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.services.MonAnService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonAnServiceImpl implements MonAnService {

    private final MonAnDAO monAnDAO;
    private final LoaiMonAnDAO loaiMonAnDAO;

    public MonAnServiceImpl() {
        this(new MonAnDAOImpl(), new LoaiMonAnDAOImpl());
    }

    public MonAnServiceImpl(MonAnDAO monAnDAO, LoaiMonAnDAO loaiMonAnDAO) {
        this.monAnDAO = monAnDAO;
        this.loaiMonAnDAO = loaiMonAnDAO;
    }

    @Override
    public List<MonAn> findAll() {
        return new ArrayList<>(monAnDAO.findAll());
    }

    @Override
    public List<MonAn> search(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return findAll();
        }

        List<MonAn> tatCaMonAn = monAnDAO.findAll();
        String normalizedKeyword = tuKhoa.toLowerCase(Locale.ROOT);
        List<MonAn> ketQua = new ArrayList<>();

        for (MonAn monAn : tatCaMonAn) {
            String tenMon = safeLower(monAn.getTenMon());
            String donGia = monAn.getDonGia() == null ? "" : String.valueOf(monAn.getDonGia());
            String tenLoai = monAn.getLoaiMonAn() == null ? "" : safeLower(monAn.getLoaiMonAn().getTenLoai());

            if (tenMon.contains(normalizedKeyword)
                    || donGia.contains(normalizedKeyword)
                    || tenLoai.contains(normalizedKeyword)) {
                ketQua.add(monAn);
            }
        }

        return ketQua;
    }

    @Override
    public List<LoaiMonAn> findAllLoaiMonAn() {
        return new ArrayList<>(loaiMonAnDAO.findAll());
    }

    @Override
    public MonAn findById(Long maMon) {
        return monAnDAO.findById(maMon);
    }

    @Override
    public MonAn create(String tenMon, String donGiaText, LoaiMonAn loaiMonAn) {
        FormData formData = validate(false, null, tenMon, donGiaText, loaiMonAn);

        MonAn monAn = new MonAn();
        monAn.setTenMon(formData.tenMon());
        monAn.setDonGia(formData.donGia());
        monAn.setLoaiMonAn(formData.loaiMonAn());

        monAnDAO.save(monAn);
        return monAn;
    }

    @Override
    public MonAn update(Long maMon, String tenMon, String donGiaText, LoaiMonAn loaiMonAn) {
        if (maMon == null) {
            throw new IllegalArgumentException("Vui lòng chọn món ăn cần cập nhật.");
        }

        MonAn monAn = monAnDAO.findById(maMon);
        if (monAn == null) {
            throw new IllegalArgumentException("Không tìm thấy món ăn cần cập nhật.");
        }

        FormData formData = validate(true, monAn, tenMon, donGiaText, loaiMonAn);

        monAn.setTenMon(formData.tenMon());
        monAn.setDonGia(formData.donGia());
        monAn.setLoaiMonAn(formData.loaiMonAn());

        monAnDAO.update(monAn);
        return monAn;
    }

    @Override
    public void delete(Long maMon) {
        if (maMon == null) {
            throw new IllegalArgumentException("Vui lòng chọn món ăn cần xóa.");
        }
        monAnDAO.delete(maMon);
    }

    private FormData validate(boolean isUpdate, MonAn monAnHienTai,
                              String tenMon, String donGiaText, LoaiMonAn loaiMonAn) {
        if (tenMon == null || tenMon.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập tên món ăn.");
        }

        if (donGiaText == null || donGiaText.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập đơn giá.");
        }

        Double donGia;
        try {
            donGia = Double.parseDouble(donGiaText.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Đơn giá phải là số hợp lệ.");
        }

        if (donGia <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0.");
        }

        if (loaiMonAn == null) {
            throw new IllegalArgumentException("Vui lòng chọn loại món ăn.");
        }

        List<MonAn> monAnCungTen = monAnDAO.findByTenMon(tenMon.trim());
        for (MonAn item : monAnCungTen) {
            boolean isSameMonAn = isUpdate
                    && monAnHienTai != null
                    && item.getMaMon().equals(monAnHienTai.getMaMon());

            if (!isSameMonAn && item.getTenMon().equalsIgnoreCase(tenMon.trim())) {
                throw new IllegalArgumentException("Tên món ăn đã tồn tại.");
            }
        }

        return new FormData(tenMon.trim(), donGia, loaiMonAn);
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private record FormData(String tenMon, Double donGia, LoaiMonAn loaiMonAn) {
    }
}
