package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.entity.TaiKhoan;
import ltw.ck.quanlyquanan.services.NhanVienService;
import ltw.ck.quanlyquanan.services.ServiceException;
import ltw.ck.quanlyquanan.services.impl.NhanVienServiceImpl;
import ltw.ck.quanlyquanan.view.NhanVienPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;

public class NhanVienController {

    private final NhanVienPanel view;
    private final NhanVienService nhanVienService;
    private List<NhanVien> danhSachNhanVien = new ArrayList<>();

    public NhanVienController(NhanVienPanel view) {
        this(view, new NhanVienServiceImpl());
    }

    public NhanVienController(NhanVienPanel view, NhanVienService nhanVienService) {
        this.view = view;
        this.nhanVienService = nhanVienService;
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

    private void taiDanhSachNhanVien() {
        try {
            danhSachNhanVien = new ArrayList<>(nhanVienService.findAll());
            doDuLieuLenBang(danhSachNhanVien);
            lamMoiForm();
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách nhân viên", ex);
        }
    }

    private void timKiemNhanVien() {
        try {
            danhSachNhanVien = new ArrayList<>(nhanVienService.search(view.getTuKhoaTimKiem()));
            doDuLieuLenBang(danhSachNhanVien);
            lamMoiForm();

            if (danhSachNhanVien.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên phù hợp.");
            }
        } catch (Exception ex) {
            hienThiLoi("Không thể tìm kiếm nhân viên", ex);
        }
    }

    private void themNhanVien() {
        try {
            NhanVien nhanVien = nhanVienService.create(
                    view.getHoTen(),
                    view.getSdt(),
                    view.getDiaChi(),
                    view.getTenDangNhap(),
                    view.getMatKhau()
            );

            taiDanhSachNhanVien();
            chonNhanVienTheoId(nhanVien.getMaNV());
            JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công.");
        } catch (ServiceException ex) {
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
            nhanVienService.update(
                    maNV,
                    view.getHoTen(),
                    view.getSdt(),
                    view.getDiaChi(),
                    view.getTenDangNhap(),
                    view.getMatKhau()
            );

            taiDanhSachNhanVien();
            chonNhanVienTheoId(maNV);
            JOptionPane.showMessageDialog(view, "Cập nhật nhân viên thành công.");
        } catch (ServiceException ex) {
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
            nhanVienService.delete(maNV);
            taiDanhSachNhanVien();
            JOptionPane.showMessageDialog(view, "Xóa nhân viên thành công.");
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa nhân viên. Nhân viên này có thể đang được tham chiếu bởi dữ liệu khác", ex);
        }
    }

    private void xuLyChonDong(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) return;

        int selectedRow = view.getTblNhanVien().getSelectedRow();
        if (selectedRow < 0) {
            capNhatTrangThaiNut(false);
            return;
        }

        int modelRow = view.getTblNhanVien().convertRowIndexToModel(selectedRow);
        Long maNV = (Long) view.getTableModel().getValueAt(modelRow, 0);
        NhanVien nhanVien = timNhanVienTrongDanhSach(maNV);
        if (nhanVien == null) return;

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

    private void lamMoiForm() {
        view.clearForm();
        capNhatTrangThaiNut(false);
    }

    private Long layMaNhanVienDangChon() {
        try {
            return view.getMaNhanVien().isEmpty() ? null : Long.parseLong(view.getMaNhanVien());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void chonNhanVienTheoId(Long maNV) {
        if (maNV == null) return;

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
            if (nhanVien.getMaNV().equals(maNV)) return nhanVien;
        }
        return null;
    }

    private void capNhatTrangThaiNut(boolean dangChonNhanVien) {
        view.getBtnThem().setEnabled(!dangChonNhanVien);
        view.getBtnCapNhat().setEnabled(dangChonNhanVien);
        view.getBtnXoa().setEnabled(dangChonNhanVien);
    }

    private void hienThiLoi(String message, Exception ex) {
        JOptionPane.showMessageDialog(
                view,
                message + ": " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }
}