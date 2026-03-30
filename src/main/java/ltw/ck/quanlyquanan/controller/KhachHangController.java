package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dao.KhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.LoaiKhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.impl.KhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.LoaiKhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.LoaiKH;
import ltw.ck.quanlyquanan.view.KhachHangView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KhachHangController {

    private final KhachHangView view;
    private final KhachHangDAO khachHangDAO;
    private final LoaiKhachHangDAO loaiKhachHangDAO;

    private List<KhachHang> danhSachKhachHang = new ArrayList<>();
    private List<LoaiKH> danhSachLoaiKH = new ArrayList<>();

    public KhachHangController(KhachHangView view) {
        this(view, new KhachHangDAOImpl(), new LoaiKhachHangDAOImpl());
    }

    public KhachHangController(KhachHangView view, KhachHangDAO khachHangDAO, LoaiKhachHangDAO loaiKhachHangDAO) {
        this.view = view;
        this.khachHangDAO = khachHangDAO;
        this.loaiKhachHangDAO = loaiKhachHangDAO;
        init();
    }

    private void init() {
        registerEvents();
        capNhatTrangThaiNut(false);
        taiLoaiKhachHang();
        taiDanhSachKhachHang();
    }

    private void registerEvents() {
        view.getBtnTimKiem().addActionListener(e -> timKiemKhachHang());
        view.getBtnTaiLai().addActionListener(e -> taiDanhSachKhachHang());
        view.getBtnThem().addActionListener(e -> themKhachHang());
        view.getBtnCapNhat().addActionListener(e -> capNhatKhachHang());
        view.getBtnXoa().addActionListener(e -> xoaKhachHang());
        view.getBtnLamMoi().addActionListener(e -> lamMoiForm());
        view.getBtnDong().addActionListener(e -> view.dispose());

        view.getTxtTimKiem().addActionListener(e -> timKiemKhachHang());
        view.getTblKhachHang().getSelectionModel().addListSelectionListener(this::xuLyChonDong);
    }

    private void taiLoaiKhachHang() {
        try {
            danhSachLoaiKH = new ArrayList<>(loaiKhachHangDAO.findAll());
            DefaultComboBoxModel<LoaiKH> comboBoxModel = new DefaultComboBoxModel<>();
            for (LoaiKH loaiKH : danhSachLoaiKH) {
                comboBoxModel.addElement(loaiKH);
            }
            view.getCboLoaiKH().setModel(comboBoxModel);
            view.getCboLoaiKH().setSelectedItem(null);
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách loại khách hàng", ex);
        }
    }

    private void xuLyChonDong(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = view.getTblKhachHang().getSelectedRow();
        if (selectedRow < 0) {
            capNhatTrangThaiNut(false);
            return;
        }

        int modelRow = view.getTblKhachHang().convertRowIndexToModel(selectedRow);
        Long maKH = (Long) view.getTableModel().getValueAt(modelRow, 0);
        KhachHang khachHang = timKhachHangTrongDanhSach(maKH);
        if (khachHang == null) {
            return;
        }

        view.setFormData(
                String.valueOf(khachHang.getMaKh()),
                khachHang.getTenKh(),
                khachHang.getLoaiKhachHang()
        );
        capNhatTrangThaiNut(true);
    }

    private void taiDanhSachKhachHang() {
        try {
            danhSachKhachHang = new ArrayList<>(khachHangDAO.findAll());
            doDuLieuLenBang(danhSachKhachHang);
            lamMoiForm();
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách khách hàng", ex);
        }
    }

    private void timKiemKhachHang() {
        String tuKhoa = view.getTuKhoaTimKiem();

        if (tuKhoa.isEmpty()) {
            taiDanhSachKhachHang();
            return;
        }

        try {
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

            danhSachKhachHang = ketQua;
            doDuLieuLenBang(ketQua);
            lamMoiForm();

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy khách hàng phù hợp.");
            }
        } catch (Exception ex) {
            hienThiLoi("Không thể tìm kiếm khách hàng", ex);
        }
    }

    private void doDuLieuLenBang(List<KhachHang> dsKhachHang) {
        var tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        for (KhachHang khachHang : dsKhachHang) {
            String tenLoai = khachHang.getLoaiKhachHang() == null
                    ? ""
                    : khachHang.getLoaiKhachHang().getTenLoaiKh();

            tableModel.addRow(new Object[]{
                    khachHang.getMaKh(),
                    khachHang.getTenKh(),
                    tenLoai
            });
        }
    }

    private void themKhachHang() {
        try {
            FormData formData = layVaKiemTraDuLieu(false, null);

            KhachHang khachHang = new KhachHang();
            khachHang.setTenKh(formData.tenKH());
            khachHang.setLoaiKhachHang(formData.loaiKH());

            khachHangDAO.save(khachHang);
            taiDanhSachKhachHang();
            chonKhachHangTheoId(khachHang.getMaKh());
            JOptionPane.showMessageDialog(view, "Thêm khách hàng thành công.");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể thêm khách hàng", ex);
        }
    }

    private void capNhatKhachHang() {
        Long maKH = layMaKhachHangDangChon();
        if (maKH == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng cần cập nhật.");
            return;
        }

        try {
            KhachHang khachHang = khachHangDAO.findById(maKH);
            if (khachHang == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy khách hàng cần cập nhật.");
                taiDanhSachKhachHang();
                return;
            }

            FormData formData = layVaKiemTraDuLieu(true, khachHang);

            khachHang.setTenKh(formData.tenKH());
            khachHang.setLoaiKhachHang(formData.loaiKH());

            khachHangDAO.update(khachHang);
            taiDanhSachKhachHang();
            chonKhachHangTheoId(maKH);
            JOptionPane.showMessageDialog(view, "Cập nhật khách hàng thành công.");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể cập nhật khách hàng", ex);
        }
    }

    private void xoaKhachHang() {
        Long maKH = layMaKhachHangDangChon();
        if (maKH == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa khách hàng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            khachHangDAO.delete(maKH);
            taiDanhSachKhachHang();
            JOptionPane.showMessageDialog(view, "Xóa khách hàng thành công.");
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa khách hàng. Khách hàng này có thể đang được tham chiếu bởi dữ liệu khác", ex);
        }
    }

    private void lamMoiForm() {
        view.clearForm();
        capNhatTrangThaiNut(false);
    }

    private FormData layVaKiemTraDuLieu(boolean isUpdate, KhachHang khachHangHienTai) {
        String tenKH = view.getTenKhachHang();
        LoaiKH loaiKH = view.getLoaiKhachHangDangChon();

        if (tenKH.isEmpty()) {
            throw new ValidationException("Vui lòng nhập tên khách hàng.");
        }

        if (loaiKH == null) {
            throw new ValidationException("Vui lòng chọn loại khách hàng.");
        }

        List<KhachHang> khachHangCungTen = khachHangDAO.findByTenKh(tenKH);
        for (KhachHang item : khachHangCungTen) {
            boolean isSameKhachHang = isUpdate
                    && khachHangHienTai != null
                    && item.getMaKh().equals(khachHangHienTai.getMaKh());

            if (!isSameKhachHang && item.getTenKh().equalsIgnoreCase(tenKH)) {
                throw new ValidationException("Tên khách hàng đã tồn tại.");
            }
        }

        return new FormData(tenKH, loaiKH);
    }

    private Long layMaKhachHangDangChon() {
        String maKhachHang = view.getMaKhachHang();
        if (maKhachHang.isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(maKhachHang);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void chonKhachHangTheoId(Long maKH) {
        if (maKH == null) {
            return;
        }

        for (int modelRow = 0; modelRow < view.getTableModel().getRowCount(); modelRow++) {
            Long value = (Long) view.getTableModel().getValueAt(modelRow, 0);
            if (maKH.equals(value)) {
                int viewRow = view.getTblKhachHang().convertRowIndexToView(modelRow);
                view.getTblKhachHang().setRowSelectionInterval(viewRow, viewRow);
                view.getTblKhachHang().scrollRectToVisible(view.getTblKhachHang().getCellRect(viewRow, 0, true));
                return;
            }
        }
    }

    private KhachHang timKhachHangTrongDanhSach(Long maKH) {
        for (KhachHang khachHang : danhSachKhachHang) {
            if (khachHang.getMaKh().equals(maKH)) {
                return khachHang;
            }
        }
        return null;
    }

    private void capNhatTrangThaiNut(boolean dangChonKhachHang) {
        view.getBtnThem().setEnabled(!dangChonKhachHang);
        view.getBtnCapNhat().setEnabled(dangChonKhachHang);
        view.getBtnXoa().setEnabled(dangChonKhachHang);
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

    public void showKhachHangView() {
        view.setVisible(true);
    }

    private record FormData(String tenKH, LoaiKH loaiKH) {
    }

    private static class ValidationException extends RuntimeException {
        private ValidationException(String message) {
            super(message);
        }
    }
}