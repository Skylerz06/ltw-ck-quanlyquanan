package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.services.MonAnService;
import ltw.ck.quanlyquanan.services.ServiceException;
import ltw.ck.quanlyquanan.services.impl.MonAnServiceImpl;
import ltw.ck.quanlyquanan.view.MonAnPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;

public class MonAnController {

    private final MonAnPanel view;
    private final MonAnService monAnService;

    private List<MonAn> danhSachMonAn = new ArrayList<>();
    private List<LoaiMonAn> danhSachLoaiMonAn = new ArrayList<>();

    public MonAnController(MonAnPanel view) {
        this(view, new MonAnServiceImpl());
    }

    public MonAnController(MonAnPanel view, MonAnService monAnService) {
        this.view = view;
        this.monAnService = monAnService;
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

        view.getTxtTimKiem().addActionListener(e -> timKiemMonAn());
        view.getTblMonAn().getSelectionModel().addListSelectionListener(this::xuLyChonDong);
    }

    private void taiLoaiMonAn() {
        try {
            danhSachLoaiMonAn = new ArrayList<>(monAnService.findAllLoaiMonAn());
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
            danhSachMonAn = new ArrayList<>(monAnService.findAll());
            doDuLieuLenBang(danhSachMonAn);
            lamMoiForm();
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách món ăn", ex);
        }
    }

    private void timKiemMonAn() {
        try {
            danhSachMonAn = new ArrayList<>(monAnService.search(view.getTuKhoaTimKiem()));
            doDuLieuLenBang(danhSachMonAn);
            lamMoiForm();

            if (danhSachMonAn.isEmpty()) {
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
            MonAn monAn = monAnService.create(
                    view.getTenMonAn(),
                    view.getDonGia(),
                    view.getLoaiMonAnDangChon()
            );

            taiDanhSachMonAn();
            chonMonAnTheoId(monAn.getMaMon());
            JOptionPane.showMessageDialog(view, "Thêm món ăn thành công.");
        } catch (ServiceException ex) {
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
            monAnService.update(
                    maMon,
                    view.getTenMonAn(),
                    view.getDonGia(),
                    view.getLoaiMonAnDangChon()
            );

            taiDanhSachMonAn();
            chonMonAnTheoId(maMon);
            JOptionPane.showMessageDialog(view, "Cập nhật món ăn thành công.");
        } catch (ServiceException ex) {
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
            monAnService.delete(maMon);
            taiDanhSachMonAn();
            JOptionPane.showMessageDialog(view, "Xóa món ăn thành công.");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa món ăn. Món ăn này có thể đang được tham chiếu bởi dữ liệu khác", ex);
        }
    }

    private void lamMoiForm() {
        view.clearForm();
        capNhatTrangThaiNut(false);
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
}