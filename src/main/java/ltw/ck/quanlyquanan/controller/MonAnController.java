package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dao.LoaiMonAnDAO;
import ltw.ck.quanlyquanan.model.dao.MonAnDAO;
import ltw.ck.quanlyquanan.model.dao.impl.LoaiMonAnDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.MonAnDAOImpl;
import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.view.MonAnView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonAnController {

    private final MonAnView view;
    private final MonAnDAO monAnDAO;
    private final LoaiMonAnDAO loaiMonAnDAO;

    private List<MonAn> danhSachMonAn = new ArrayList<>();
    private List<LoaiMonAn> danhSachLoaiMonAn = new ArrayList<>();

    public MonAnController(MonAnView view) {
        this(view, new MonAnDAOImpl(), new LoaiMonAnDAOImpl());
    }

    public MonAnController(MonAnView view, MonAnDAO monAnDAO, LoaiMonAnDAO loaiMonAnDAO) {
        this.view = view;
        this.monAnDAO = monAnDAO;
        this.loaiMonAnDAO = loaiMonAnDAO;
        init();
    }

    private void init() {
        registerEvents();
        capNhatTrangThaiNut(false);
        taiLoaiMonAn();
        taiDanhSachMonAn();
    }

    private void registerEvents() {
        view.getBtnTimKiem().addActionListener(e -> timKiemMonAn());
        view.getBtnTaiLai().addActionListener(e -> taiDanhSachMonAn());
        view.getBtnThem().addActionListener(e -> themMonAn());
        view.getBtnCapNhat().addActionListener(e -> capNhatMonAn());
        view.getBtnXoa().addActionListener(e -> xoaMonAn());
        view.getBtnLamMoi().addActionListener(e -> lamMoiForm());
        view.getBtnDong().addActionListener(e -> view.dispose());

        view.getTxtTimKiem().addActionListener(e -> timKiemMonAn());
        view.getTblMonAn().getSelectionModel().addListSelectionListener(this::xuLyChonDong);
    }

    private void taiLoaiMonAn() {
        try {
            danhSachLoaiMonAn = new ArrayList<>(loaiMonAnDAO.findAll());
            DefaultComboBoxModel<LoaiMonAn> comboBoxModel = new DefaultComboBoxModel<>();
            for (LoaiMonAn loaiMonAn : danhSachLoaiMonAn) {
                comboBoxModel.addElement(loaiMonAn);
            }
            view.getCboLoaiMonAn().setModel(comboBoxModel);
            view.getCboLoaiMonAn().setSelectedItem(null);
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách loại món ăn", ex);
        }
    }

    private void xuLyChonDong(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = view.getTblMonAn().getSelectedRow();
        if (selectedRow < 0) {
            capNhatTrangThaiNut(false);
            return;
        }

        int modelRow = view.getTblMonAn().convertRowIndexToModel(selectedRow);
        Long maMon = (Long) view.getTableModel().getValueAt(modelRow, 0);
        MonAn monAn = timMonAnTrongDanhSach(maMon);
        if (monAn == null) {
            return;
        }

        view.setFormData(
                String.valueOf(monAn.getMaMon()),
                monAn.getTenMon(),
                String.valueOf(monAn.getDonGia()),
                monAn.getLoaiMonAn()
        );
        capNhatTrangThaiNut(true);
    }

    private void taiDanhSachMonAn() {
        try {
            danhSachMonAn = new ArrayList<>(monAnDAO.findAll());
            doDuLieuLenBang(danhSachMonAn);
            lamMoiForm();
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách món ăn", ex);
        }
    }

    private void timKiemMonAn() {
        String tuKhoa = view.getTuKhoaTimKiem();

        if (tuKhoa.isEmpty()) {
            taiDanhSachMonAn();
            return;
        }

        try {
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

            danhSachMonAn = ketQua;
            doDuLieuLenBang(ketQua);
            lamMoiForm();

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy món ăn phù hợp.");
            }
        } catch (Exception ex) {
            hienThiLoi("Không thể tìm kiếm món ăn", ex);
        }
    }

    private void doDuLieuLenBang(List<MonAn> dsMonAn) {
        var tableModel = view.getTableModel();
        tableModel.setRowCount(0);

        for (MonAn monAn : dsMonAn) {
            String tenLoai = monAn.getLoaiMonAn() == null ? "" : monAn.getLoaiMonAn().getTenLoai();

            tableModel.addRow(new Object[]{
                    monAn.getMaMon(),
                    monAn.getTenMon(),
                    monAn.getDonGia(),
                    tenLoai
            });
        }
    }

    private void themMonAn() {
        try {
            FormData formData = layVaKiemTraDuLieu(false, null);

            MonAn monAn = new MonAn();
            monAn.setTenMon(formData.tenMon());
            monAn.setDonGia(formData.donGia());
            monAn.setLoaiMonAn(formData.loaiMonAn());

            monAnDAO.save(monAn);
            taiDanhSachMonAn();
            chonMonAnTheoId(monAn.getMaMon());
            JOptionPane.showMessageDialog(view, "Thêm món ăn thành công.");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể thêm món ăn", ex);
        }
    }

    private void capNhatMonAn() {
        Long maMon = layMaMonAnDangChon();
        if (maMon == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn món ăn cần cập nhật.");
            return;
        }

        try {
            MonAn monAn = monAnDAO.findById(maMon);
            if (monAn == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy món ăn cần cập nhật.");
                taiDanhSachMonAn();
                return;
            }

            FormData formData = layVaKiemTraDuLieu(true, monAn);

            monAn.setTenMon(formData.tenMon());
            monAn.setDonGia(formData.donGia());
            monAn.setLoaiMonAn(formData.loaiMonAn());

            monAnDAO.update(monAn);
            taiDanhSachMonAn();
            chonMonAnTheoId(maMon);
            JOptionPane.showMessageDialog(view, "Cập nhật món ăn thành công.");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể cập nhật món ăn", ex);
        }
    }

    private void xoaMonAn() {
        Long maMon = layMaMonAnDangChon();
        if (maMon == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn món ăn cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa món ăn này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            monAnDAO.delete(maMon);
            taiDanhSachMonAn();
            JOptionPane.showMessageDialog(view, "Xóa món ăn thành công.");
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa món ăn. Món ăn này có thể đang được tham chiếu bởi dữ liệu khác", ex);
        }
    }

    private void lamMoiForm() {
        view.clearForm();
        capNhatTrangThaiNut(false);
    }

    private FormData layVaKiemTraDuLieu(boolean isUpdate, MonAn monAnHienTai) {
        String tenMon = view.getTenMonAn();
        String donGiaText = view.getDonGia();
        LoaiMonAn loaiMonAn = view.getLoaiMonAnDangChon();

        if (tenMon.isEmpty()) {
            throw new ValidationException("Vui lòng nhập tên món ăn.");
        }

        if (donGiaText.isEmpty()) {
            throw new ValidationException("Vui lòng nhập đơn giá.");
        }

        Double donGia;
        try {
            donGia = Double.parseDouble(donGiaText);
        } catch (NumberFormatException ex) {
            throw new ValidationException("Đơn giá phải là số hợp lệ.");
        }

        if (donGia <= 0) {
            throw new ValidationException("Đơn giá phải lớn hơn 0.");
        }

        if (loaiMonAn == null) {
            throw new ValidationException("Vui lòng chọn loại món ăn.");
        }

        List<MonAn> monAnCungTen = monAnDAO.findByTenMon(tenMon);
        for (MonAn item : monAnCungTen) {
            boolean isSameMonAn = isUpdate
                    && monAnHienTai != null
                    && item.getMaMon().equals(monAnHienTai.getMaMon());

            if (!isSameMonAn && item.getTenMon().equalsIgnoreCase(tenMon)) {
                throw new ValidationException("Tên món ăn đã tồn tại.");
            }
        }

        return new FormData(tenMon, donGia, loaiMonAn);
    }

    private Long layMaMonAnDangChon() {
        String maMon = view.getMaMonAn();
        if (maMon.isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(maMon);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void chonMonAnTheoId(Long maMon) {
        if (maMon == null) {
            return;
        }

        for (int modelRow = 0; modelRow < view.getTableModel().getRowCount(); modelRow++) {
            Long value = (Long) view.getTableModel().getValueAt(modelRow, 0);
            if (maMon.equals(value)) {
                int viewRow = view.getTblMonAn().convertRowIndexToView(modelRow);
                view.getTblMonAn().setRowSelectionInterval(viewRow, viewRow);
                view.getTblMonAn().scrollRectToVisible(view.getTblMonAn().getCellRect(viewRow, 0, true));
                return;
            }
        }
    }

    private MonAn timMonAnTrongDanhSach(Long maMon) {
        for (MonAn monAn : danhSachMonAn) {
            if (monAn.getMaMon().equals(maMon)) {
                return monAn;
            }
        }
        return null;
    }

    private void capNhatTrangThaiNut(boolean dangChonMonAn) {
        view.getBtnThem().setEnabled(!dangChonMonAn);
        view.getBtnCapNhat().setEnabled(dangChonMonAn);
        view.getBtnXoa().setEnabled(dangChonMonAn);
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

    public void showMonAnView() {
        view.setVisible(true);
    }

    private record FormData(String tenMon, Double donGia, LoaiMonAn loaiMonAn) {
    }

    private static class ValidationException extends RuntimeException {
        private ValidationException(String message) {
            super(message);
        }
    }
}
