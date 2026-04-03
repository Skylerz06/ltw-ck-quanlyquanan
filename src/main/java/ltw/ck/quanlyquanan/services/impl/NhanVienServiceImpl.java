package ltw.ck.quanlyquanan.services.impl;

import ltw.ck.quanlyquanan.model.dao.NhanVienDAO;
import ltw.ck.quanlyquanan.model.dao.TaiKhoanDAO;
import ltw.ck.quanlyquanan.model.dao.impl.NhanVienDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.TaiKhoanDAOImpl;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.services.NhanVienService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienDAO nhanVienDAO;
    private final TaiKhoanDAO taiKhoanDAO;

    public NhanVienServiceImpl() {
        this(new NhanVienDAOImpl(), new TaiKhoanDAOImpl());
    }

    public NhanVienServiceImpl(NhanVienDAO nhanVienDAO, TaiKhoanDAO taiKhoanDAO) {
        this.nhanVienDAO = nhanVienDAO;
        this.taiKhoanDAO = taiKhoanDAO;
    }

    @Override
    public List<NhanVien> findAll() {
        return new ArrayList<>(nhanVienDAO.findAll());
    }

    @Override
    public List<NhanVien> search(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return findAll();
        }

        String normalizedKeyword = tuKhoa.toLowerCase(Locale.ROOT);
        List<NhanVien> ketQua = new ArrayList<>();

        for (NhanVien nhanVien : nhanVienDAO.findAll()) {
            String hoTen = safeLower(nhanVien.getHoTen());
            String sdt = safeLower(nhanVien.getSdt());
            String tenDangNhap = nhanVien.getTaiKhoan() == null
                    ? ""
                    : safeLower(nhanVien.getTaiKhoan().getTenDangNhap());

            if (hoTen.contains(normalizedKeyword)
                    || sdt.contains(normalizedKeyword)
                    || tenDangNhap.contains(normalizedKeyword)) {
                ketQua.add(nhanVien);
            }
        }

        return ketQua;
    }

    @Override
    public NhanVien create(String hoTen, String sdt, String diaChi,
                           String tenDangNhap, String matKhau) {
        validate(false, null, hoTen, sdt, tenDangNhap, matKhau);

        NhanVien nhanVien = new NhanVien();
        nhanVien.setHoTen(hoTen.trim());
        nhanVien.setSdt(sdt.trim());
        nhanVien.setDiaChi(diaChi == null ? "" : diaChi.trim());

        if (!isBlank(tenDangNhap) && !isBlank(matKhau)) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap(tenDangNhap.trim());
            taiKhoan.setMatKhau(matKhau);
            taiKhoan.setNhanVien(nhanVien);
            nhanVien.setTaiKhoan(taiKhoan);
        }

        nhanVienDAO.save(nhanVien);
        return nhanVien;
    }

    @Override
    public NhanVien update(Long maNV, String hoTen, String sdt, String diaChi,
                           String tenDangNhap, String matKhau) {
        NhanVien nhanVien = nhanVienDAO.findById(maNV);
        if (nhanVien == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên cần cập nhật.");
        }

        validate(true, nhanVien, hoTen, sdt, tenDangNhap, matKhau);

        TaiKhoan taiKhoanHienTai = nhanVien.getTaiKhoan();

        nhanVien.setHoTen(hoTen.trim());
        nhanVien.setSdt(sdt.trim());
        nhanVien.setDiaChi(diaChi == null ? "" : diaChi.trim());

        if (isBlank(tenDangNhap) && isBlank(matKhau)) {
            nhanVien.setTaiKhoan(null);
        } else if (taiKhoanHienTai == null) {
            TaiKhoan taiKhoanMoi = new TaiKhoan();
            taiKhoanMoi.setTenDangNhap(tenDangNhap.trim());
            taiKhoanMoi.setMatKhau(matKhau);
            taiKhoanMoi.setNhanVien(nhanVien);
            nhanVien.setTaiKhoan(taiKhoanMoi);
        } else {
            taiKhoanHienTai.setTenDangNhap(tenDangNhap.trim());
            taiKhoanHienTai.setMatKhau(matKhau);
            taiKhoanHienTai.setNhanVien(nhanVien);
            nhanVien.setTaiKhoan(taiKhoanHienTai);
        }

        nhanVienDAO.update(nhanVien);
        return nhanVien;
    }

    @Override
    public void delete(Long maNV) {
        nhanVienDAO.delete(maNV);
    }

    @Override
    public NhanVien findById(Long maNV) {
        return nhanVienDAO.findById(maNV);
    }

    private void validate(boolean isUpdate, NhanVien nhanVienHienTai,
                          String hoTen, String sdt, String tenDangNhap, String matKhau) {
        if (isBlank(hoTen)) {
            throw new IllegalArgumentException("Vui lòng nhập họ tên nhân viên.");
        }

        if (isBlank(sdt)) {
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại.");
        }

        if (!sdt.trim().matches("\\d{9,15}")) {
            throw new IllegalArgumentException("Số điện thoại chỉ được chứa 9 đến 15 chữ số.");
        }

        NhanVien nhanVienTheoSdt = nhanVienDAO.findBySdt(sdt.trim());
        if (nhanVienTheoSdt != null) {
            boolean isSameNhanVien = isUpdate
                    && nhanVienHienTai != null
                    && nhanVienTheoSdt.getMaNV().equals(nhanVienHienTai.getMaNV());
            if (!isSameNhanVien) {
                throw new IllegalArgumentException("Số điện thoại đã tồn tại.");
            }
        }

        boolean coNhapTenDangNhap = !isBlank(tenDangNhap);
        boolean coNhapMatKhau = !isBlank(matKhau);

        if (!isUpdate && (!coNhapTenDangNhap || !coNhapMatKhau)) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }

        if (isUpdate && (coNhapTenDangNhap != coNhapMatKhau)) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }

        if (!coNhapTenDangNhap) {
            return;
        }

        TaiKhoan taiKhoanTheoTenDangNhap = taiKhoanDAO.findByTenDangNhap(tenDangNhap.trim());
        if (taiKhoanTheoTenDangNhap != null) {
            boolean isSameTaiKhoan = isUpdate
                    && nhanVienHienTai != null
                    && nhanVienHienTai.getTaiKhoan() != null
                    && taiKhoanTheoTenDangNhap.getMaTK().equals(nhanVienHienTai.getTaiKhoan().getMaTK());

            if (!isSameTaiKhoan) {
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
            }
        }
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
