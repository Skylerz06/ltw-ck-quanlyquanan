package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dao.NhanVienDAO;
import ltw.ck.quanlyquanan.model.dao.TaiKhoanDAO;
import ltw.ck.quanlyquanan.model.dao.impl.NhanVienDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.TaiKhoanDAOImpl;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.view.NhanVienPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NhanVienController {

    private final NhanVienPanel view;
    private final NhanVienDAO nhanVienDAO;
    private final TaiKhoanDAO taiKhoanDAO;
    private List<NhanVien> danhSachNhanVien = new ArrayList<>();

    public NhanVienController(NhanVienPanel view) {
        this(view, new NhanVienDAOImpl(), new TaiKhoanDAOImpl());
    }

    public NhanVienController(NhanVienPanel view, NhanVienDAO nhanVienDAO, TaiKhoanDAO taiKhoanDAO) {
        this.view = view;
        this.nhanVienDAO = nhanVienDAO;
        this.taiKhoanDAO = taiKhoanDAO;
        init();
    }

    private void init() {
        registerEvents();
        capNhatTrangThaiNut(false);
        taiDanhSachNhanVien();
    }

    private void registerEvents() {
        view.getBtnTimKiem().addActionListener(e -> timKiemNhanVien());
        view.getBtnTaiLai().addActionListener(e -> taiDanhSachNhanVien());
        view.getBtnThem().addActionListener(e -> themNhanVien());
        view.getBtnCapNhat().addActionListener(e -> capNhatNhanVien());
        view.getBtnXoa().addActionListener(e -> xoaNhanVien());
        view.getBtnLamMoi().addActionListener(e -> lamMoiForm());

        view.getTxtTimKiem().addActionListener(e -> timKiemNhanVien());
        view.getTblNhanVien().getSelectionModel().addListSelectionListener(this::xuLyChonDong);
    }

    private void xuLyChonDong(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = view.getTblNhanVien().getSelectedRow();
        if (selectedRow < 0) {
            capNhatTrangThaiNut(false);
            return;
        }

        int modelRow = view.getTblNhanVien().convertRowIndexToModel(selectedRow);
        Long maNV = (Long) view.getTableModel().getValueAt(modelRow, 0);
        NhanVien nhanVien = timNhanVienTrongDanhSach(maNV);
        if (nhanVien == null) {
            return;
        }

        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        view.setFormData(
                String.valueOf(nhanVien.getMaNV()),
                nhanVien.getHoTen(),
                nhanVien.getSdt(),
                nhanVien.getDiaChi() == null ? "" : nhanVien.getDiaChi(),
                taiKhoan == null ? "" : taiKhoan.getTenDangNhap(),
                taiKhoan == null ? "" : taiKhoan.getMatKhau()
        );
        capNhatTrangThaiNut(true);
    }

    private void taiDanhSachNhanVien() {
        try {
            danhSachNhanVien = new ArrayList<>(nhanVienDAO.findAll());
            doDuLieuLenBang(danhSachNhanVien);
            lamMoiForm();
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách nhân viên", ex);
        }
    }

    private void timKiemNhanVien() {
        String tuKhoa = view.getTuKhoaTimKiem();

        if (tuKhoa.isEmpty()) {
            taiDanhSachNhanVien();
            return;
        }

        try {
            List<NhanVien> tatCaNhanVien = nhanVienDAO.findAll();
            String normalizedKeyword = tuKhoa.toLowerCase(Locale.ROOT);
            List<NhanVien> ketQua = new ArrayList<>();

            for (NhanVien nhanVien : tatCaNhanVien) {
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

            danhSachNhanVien = ketQua;
            doDuLieuLenBang(ketQua);
            lamMoiForm();

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên phù hợp.");
            }
        } catch (Exception ex) {
            hienThiLoi("Không thể tìm kiếm nhân viên", ex);
        }
    }

    private void doDuLieuLenBang(List<NhanVien> dsNhanVien) {
        var tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        for (NhanVien nhanVien : dsNhanVien) {
            String tenDangNhap = nhanVien.getTaiKhoan() == null
                    ? ""
                    : nhanVien.getTaiKhoan().getTenDangNhap();

            tableModel.addRow(new Object[]{
                    nhanVien.getMaNV(),
                    nhanVien.getHoTen(),
                    nhanVien.getSdt(),
                    nhanVien.getDiaChi() == null ? "" : nhanVien.getDiaChi(),
                    tenDangNhap
            });
        }
    }

    private void themNhanVien() {
        try {
            FormData formData = layVaKiemTraDuLieu(false, null, null);

            NhanVien nhanVien = new NhanVien();
            nhanVien.setHoTen(formData.hoTen());
            nhanVien.setSdt(formData.sdt());
            nhanVien.setDiaChi(formData.diaChi());

            if (formData.tenDangNhap() != null) {
                TaiKhoan taiKhoan = new TaiKhoan();
                taiKhoan.setTenDangNhap(formData.tenDangNhap());
                taiKhoan.setMatKhau(formData.matKhau());
                taiKhoan.setNhanVien(nhanVien);
                nhanVien.setTaiKhoan(taiKhoan);
            }

            nhanVienDAO.save(nhanVien);
            taiDanhSachNhanVien();
            chonNhanVienTheoId(nhanVien.getMaNV());
            JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công.");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể thêm nhân viên", ex);
        }
    }

    private void capNhatNhanVien() {
        Long maNV = layMaNhanVienDangChon();
        if (maNV == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên cần cập nhật.");
            return;
        }

        try {
            NhanVien nhanVien = nhanVienDAO.findById(maNV);
            if (nhanVien == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên cần cập nhật.");
                taiDanhSachNhanVien();
                return;
            }

            TaiKhoan taiKhoanHienTai = nhanVien.getTaiKhoan();
            FormData formData = layVaKiemTraDuLieu(true, nhanVien, taiKhoanHienTai);

            nhanVien.setHoTen(formData.hoTen());
            nhanVien.setSdt(formData.sdt());
            nhanVien.setDiaChi(formData.diaChi());

            if (formData.tenDangNhap() == null) {
                nhanVien.setTaiKhoan(null);
            } else if (taiKhoanHienTai == null) {
                TaiKhoan taiKhoanMoi = new TaiKhoan();
                taiKhoanMoi.setTenDangNhap(formData.tenDangNhap());
                taiKhoanMoi.setMatKhau(formData.matKhau());
                taiKhoanMoi.setNhanVien(nhanVien);
                nhanVien.setTaiKhoan(taiKhoanMoi);
            } else {
                taiKhoanHienTai.setTenDangNhap(formData.tenDangNhap());
                taiKhoanHienTai.setMatKhau(formData.matKhau());
                taiKhoanHienTai.setNhanVien(nhanVien);
                nhanVien.setTaiKhoan(taiKhoanHienTai);
            }

            nhanVienDAO.update(nhanVien);
            taiDanhSachNhanVien();
            chonNhanVienTheoId(maNV);
            JOptionPane.showMessageDialog(view, "Cập nhật nhân viên thành công.");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể cập nhật nhân viên", ex);
        }
    }

    private void xoaNhanVien() {
        Long maNV = layMaNhanVienDangChon();
        if (maNV == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn nhân viên cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa nhân viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            nhanVienDAO.delete(maNV);
            taiDanhSachNhanVien();
            JOptionPane.showMessageDialog(view, "Xóa nhân viên thành công.");
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa nhân viên. Nhân viên này có thể đang được tham chiếu bởi dữ liệu khác", ex);
        }
    }

    private void lamMoiForm() {
        view.clearForm();
        capNhatTrangThaiNut(false);
    }

    private FormData layVaKiemTraDuLieu(boolean isUpdate, NhanVien nhanVienHienTai, TaiKhoan taiKhoanHienTai) {
        String hoTen = view.getHoTen();
        String sdt = view.getSdt();
        String diaChi = view.getDiaChi();
        String tenDangNhap = view.getTenDangNhap();
        String matKhau = view.getMatKhau();

        if (hoTen.isEmpty()) {
            throw new ValidationException("Vui lòng nhập họ tên nhân viên.");
        }

        if (sdt.isEmpty()) {
            throw new ValidationException("Vui lòng nhập số điện thoại.");
        }

        if (!sdt.matches("\\d{9,15}")) {
            throw new ValidationException("Số điện thoại chỉ được chứa 9 đến 15 chữ số.");
        }

        NhanVien nhanVienTheoSdt = nhanVienDAO.findBySdt(sdt);
        if (nhanVienTheoSdt != null) {
            boolean isSameNhanVien = isUpdate && nhanVienHienTai != null
                    && nhanVienTheoSdt.getMaNV().equals(nhanVienHienTai.getMaNV());
            if (!isSameNhanVien) {
                throw new ValidationException("Số điện thoại đã tồn tại.");
            }
        }

        boolean coNhapTenDangNhap = !tenDangNhap.isEmpty();
        boolean coNhapMatKhau = !matKhau.isEmpty();

        if (coNhapTenDangNhap != coNhapMatKhau) {
            throw new ValidationException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }

        if (taiKhoanHienTai != null && !coNhapTenDangNhap) {
            throw new ValidationException("Nhân viên này đang có tài khoản. Vui lòng nhập đầy đủ để cập nhật.");
        }

        if (!coNhapTenDangNhap) {
            return new FormData(hoTen, sdt, diaChi, null, null);
        }

        TaiKhoan taiKhoanTheoTenDangNhap = taiKhoanDAO.findByTenDangNhap(tenDangNhap);
        if (taiKhoanTheoTenDangNhap != null) {
            boolean isSameTaiKhoan = isUpdate && taiKhoanHienTai != null
                    && taiKhoanTheoTenDangNhap.getMaTK().equals(taiKhoanHienTai.getMaTK());
            if (!isSameTaiKhoan) {
                throw new ValidationException("Tên đăng nhập đã tồn tại.");
            }
        }

        return new FormData(hoTen, sdt, diaChi, tenDangNhap, matKhau);
    }

    private Long layMaNhanVienDangChon() {
        String maNhanVien = view.getMaNhanVien();
        if (maNhanVien.isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(maNhanVien);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void chonNhanVienTheoId(Long maNV) {
        if (maNV == null) {
            return;
        }

        for (int modelRow = 0; modelRow < view.getTableModel().getRowCount(); modelRow++) {
            Long value = (Long) view.getTableModel().getValueAt(modelRow, 0);
            if (maNV.equals(value)) {
                int viewRow = view.getTblNhanVien().convertRowIndexToView(modelRow);
                view.getTblNhanVien().setRowSelectionInterval(viewRow, viewRow);
                view.getTblNhanVien().scrollRectToVisible(view.getTblNhanVien().getCellRect(viewRow, 0, true));
                return;
            }
        }
    }

    private NhanVien timNhanVienTrongDanhSach(Long maNV) {
        for (NhanVien nhanVien : danhSachNhanVien) {
            if (nhanVien.getMaNV().equals(maNV)) {
                return nhanVien;
            }
        }
        return null;
    }

    private void capNhatTrangThaiNut(boolean dangChonNhanVien) {
        view.getBtnThem().setEnabled(!dangChonNhanVien);
        view.getBtnCapNhat().setEnabled(dangChonNhanVien);
        view.getBtnXoa().setEnabled(dangChonNhanVien);
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private void hienThiLoi(String message, Exception ex) {
        JOptionPane.showMessageDialog(
                view,
                message + ": " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void showNhanVienView() {
        view.setVisible(true);
    }

    private record FormData(String hoTen, String sdt, String diaChi, String tenDangNhap, String matKhau) {
    }

    private static class ValidationException extends RuntimeException {
        private ValidationException(String message) {
            super(message);
        }
    }
}