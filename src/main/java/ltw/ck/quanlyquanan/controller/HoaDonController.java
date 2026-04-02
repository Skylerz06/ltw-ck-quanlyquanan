package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;
import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.services.HoaDonItemData;
import ltw.ck.quanlyquanan.services.HoaDonService;
import ltw.ck.quanlyquanan.services.ServiceException;
import ltw.ck.quanlyquanan.services.impl.HoaDonServiceImpl;
import ltw.ck.quanlyquanan.view.HoaDonPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HoaDonController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.##");

    private final HoaDonPanel view;
    private final HoaDonService hoaDonService;

    private List<HoaDon> danhSachHoaDon = new ArrayList<>();
    private List<KhachHang> danhSachKhachHang = new ArrayList<>();
    private List<NhanVien> danhSachNhanVien = new ArrayList<>();
    private List<Ban> danhSachBan = new ArrayList<>();
    private List<MonAn> danhSachMonAn = new ArrayList<>();
    private List<HoaDonItemData> chiTietTam = new ArrayList<>();

    private HoaDon hoaDonDangChon;
    private NhanVien nhanVienDangNhap;

    public HoaDonController(HoaDonPanel view) {
        this(view, new HoaDonServiceImpl());
    }

    public HoaDonController(HoaDonPanel view, HoaDonService hoaDonService) {
        this.view = view;
        this.hoaDonService = hoaDonService;
        init();
    }

    private void init() {
        registerEvents();
        taiDuLieuDanhMuc();
        taiDanhSachHoaDon();
        lamMoiForm();
        view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
    }

    private void registerEvents() {
        view.getBtnTabLapHoaDon().addActionListener(e -> view.showCard(HoaDonPanel.CARD_LAP_HOA_DON));
        view.getBtnTabLichSu().addActionListener(e -> view.showCard(HoaDonPanel.CARD_LICH_SU));

        view.getBtnTimKiem().addActionListener(e -> timKiemHoaDon());
        view.getBtnTaiLai().addActionListener(e -> taiDanhSachHoaDon());
        view.getBtnChinhSua().addActionListener(e -> taiHoaDonVaoForm());
        view.getBtnXoaHoaDon().addActionListener(e -> xoaHoaDon());

        view.getBtnThemHoaDon().addActionListener(e -> themHoaDon());
        view.getBtnCapNhatHoaDon().addActionListener(e -> capNhatHoaDon());
        view.getBtnLamMoiForm().addActionListener(e -> lamMoiForm());

        view.getBtnThemMon().addActionListener(e -> themMonVaoHoaDon());
        view.getBtnCapNhatMon().addActionListener(e -> capNhatMonTrongHoaDon());
        view.getBtnXoaMon().addActionListener(e -> xoaMonKhoiHoaDon());

        view.getTxtTimKiem().addActionListener(e -> timKiemHoaDon());
        view.getTblHoaDon().getSelectionModel().addListSelectionListener(this::xuLyChonHoaDonLichSu);
        view.getTblChiTietForm().getSelectionModel().addListSelectionListener(this::xuLyChonChiTietForm);
    }

    private void taiDuLieuDanhMuc() {
        try {
            taiKhachHang();
            taiNhanVien();
            taiBan();
            taiMonAn();
        } catch (Exception ex) {
            hienThiLoi("Không thể tải dữ liệu danh mục", ex);
        }
    }

    private void taiKhachHang() {
        danhSachKhachHang = new ArrayList<>(hoaDonService.findAllKhachHang());
        DefaultComboBoxModel<KhachHang> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (KhachHang khachHang : danhSachKhachHang) {
            model.addElement(khachHang);
        }
        view.getCboKhachHang().setModel(model);
        view.getCboKhachHang().setSelectedItem(null);
        view.getCboKhachHang().setRenderer(new NullableObjectRenderer("Khách lẻ"));
    }

    private void taiNhanVien() {
        nhanVienDangNhap = AppSession.getCurrentNhanVien();
        danhSachNhanVien = new ArrayList<>(hoaDonService.findAllNhanVien());

        DefaultComboBoxModel<NhanVien> model = new DefaultComboBoxModel<>();
        if (nhanVienDangNhap != null) {
            model.addElement(nhanVienDangNhap);
        } else {
            for (NhanVien nhanVien : danhSachNhanVien) {
                model.addElement(nhanVien);
            }
        }

        view.getCboNhanVien().setModel(model);
        view.getCboNhanVien().setEnabled(false);
        if (model.getSize() > 0) {
            view.getCboNhanVien().setSelectedIndex(0);
        }
    }

    private void taiBan() {
        danhSachBan = new ArrayList<>(hoaDonService.findAllBan());
        DefaultComboBoxModel<Ban> model = new DefaultComboBoxModel<>();
        for (Ban ban : danhSachBan) {
            model.addElement(ban);
        }
        view.getCboBan().setModel(model);
        view.getCboBan().setSelectedItem(null);
    }

    private void taiMonAn() {
        danhSachMonAn = new ArrayList<>(hoaDonService.findAllMonAn());
        DefaultComboBoxModel<MonAn> model = new DefaultComboBoxModel<>();
        for (MonAn monAn : danhSachMonAn) {
            model.addElement(monAn);
        }
        view.getCboMonAn().setModel(model);
        view.getCboMonAn().setSelectedItem(null);
    }

    private void taiDanhSachHoaDon() {
        try {
            danhSachHoaDon = new ArrayList<>(hoaDonService.findAll());
            doDuLieuHoaDonLenBang(danhSachHoaDon);
            xoaChiTietLichSu();
            hoaDonDangChon = null;
            capNhatTrangThaiNutLichSu(false);
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách hóa đơn", ex);
        }
    }

    private void timKiemHoaDon() {
        try {
            danhSachHoaDon = new ArrayList<>(hoaDonService.search(view.getTuKhoaTimKiem()));
            doDuLieuHoaDonLenBang(danhSachHoaDon);
            xoaChiTietLichSu();
            hoaDonDangChon = null;
            capNhatTrangThaiNutLichSu(false);

            if (danhSachHoaDon.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy hóa đơn phù hợp.");
            }
        } catch (Exception ex) {
            hienThiLoi("Không thể tìm kiếm hóa đơn", ex);
        }
    }

    private void doDuLieuHoaDonLenBang(List<HoaDon> dsHoaDon) {
        DefaultTableModel tableModel = view.getHoaDonTableModel();
        tableModel.setRowCount(0);

        for (HoaDon hoaDon : dsHoaDon) {
            tableModel.addRow(new Object[]{
                    hoaDon.getMaHd(),
                    formatDateTime(hoaDon.getNgayLap()),
                    hoaDon.getTrangThai() == null ? "" : hoaDon.getTrangThai().name(),
                    hoaDon.getKhachHang() == null ? "Khách lẻ" : hoaDon.getKhachHang().getTenKh(),
                    hoaDon.getNhanVien() == null ? "" : hoaDon.getNhanVien().getHoTen(),
                    hoaDon.getBan() == null ? "" : hoaDon.getBan().getTenBan(),
                    tinhTongSoLuongEntity(hoaDon.getLstChiTietHoaDon()),
                    formatMoney(hoaDonService.tinhTongTienHoaDon(hoaDon))
            });
        }
    }

    private void xuLyChonHoaDonLichSu(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = view.getTblHoaDon().getSelectedRow();
        if (selectedRow < 0) {
            hoaDonDangChon = null;
            xoaChiTietLichSu();
            capNhatTrangThaiNutLichSu(false);
            return;
        }

        int modelRow = view.getTblHoaDon().convertRowIndexToModel(selectedRow);
        Long maHd = (Long) view.getHoaDonTableModel().getValueAt(modelRow, 0);
        hoaDonDangChon = timHoaDonTrongDanhSach(maHd);

        if (hoaDonDangChon == null) {
            xoaChiTietLichSu();
            capNhatTrangThaiNutLichSu(false);
            return;
        }

        doDuLieuChiTietLichSuLenBang(hoaDonService.toItemDataList(hoaDonDangChon));
        capNhatTrangThaiNutLichSu(true);
    }

    private void xuLyChonChiTietForm(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }

        int selectedRow = view.getTblChiTietForm().getSelectedRow();
        if (selectedRow < 0) {
            capNhatTrangThaiNutMon(false);
            return;
        }

        int modelRow = view.getTblChiTietForm().convertRowIndexToModel(selectedRow);
        Long maMon = (Long) view.getChiTietFormTableModel().getValueAt(modelRow, 0);
        HoaDonItemData item = timChiTietTamTheoMaMon(maMon);
        if (item == null) {
            capNhatTrangThaiNutMon(false);
            return;
        }

        selectComboItemById(view.getCboMonAn(), item.maMon(), MonAn::getMaMon);
        view.setSoLuong(String.valueOf(item.soLuong()));
        capNhatTrangThaiNutMon(true);
    }

    private void doDuLieuChiTietFormLenBang() {
        DefaultTableModel tableModel = view.getChiTietFormTableModel();
        tableModel.setRowCount(0);

        for (HoaDonItemData item : chiTietTam) {
            tableModel.addRow(new Object[]{
                    item.maMon(),
                    item.tenMon(),
                    formatMoney(item.donGia()),
                    item.soLuong(),
                    formatMoney(item.thanhTien())
            });
        }

        view.getLblTongTienForm().setText("Tổng tiền: " + formatMoney(hoaDonService.tinhTongTien(chiTietTam)) + " đ");
    }

    private void doDuLieuChiTietLichSuLenBang(List<HoaDonItemData> items) {
        DefaultTableModel tableModel = view.getChiTietLichSuTableModel();
        tableModel.setRowCount(0);

        for (HoaDonItemData item : items) {
            tableModel.addRow(new Object[]{
                    item.maMon(),
                    item.tenMon(),
                    formatMoney(item.donGia()),
                    item.soLuong(),
                    formatMoney(item.thanhTien())
            });
        }

        view.getLblTongTienLichSu().setText("Tổng tiền: " + formatMoney(hoaDonService.tinhTongTien(items)) + " đ");
    }

    private void xoaChiTietLichSu() {
        view.getChiTietLichSuTableModel().setRowCount(0);
        view.getLblTongTienLichSu().setText("Tổng tiền: 0 đ");
    }

    private void themMonVaoHoaDon() {
        try {
            chiTietTam = hoaDonService.addItem(
                    chiTietTam,
                    view.getMonAnDangChon(),
                    view.getSoLuong()
            );
            doDuLieuChiTietFormLenBang();
            view.clearMonAnForm();
            capNhatTrangThaiNutMon(false);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void capNhatMonTrongHoaDon() {
        int selectedRow = view.getTblChiTietForm().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn món cần cập nhật.");
            return;
        }

        try {
            int modelRow = view.getTblChiTietForm().convertRowIndexToModel(selectedRow);
            Long maMonCu = (Long) view.getChiTietFormTableModel().getValueAt(modelRow, 0);

            chiTietTam = hoaDonService.updateItem(
                    chiTietTam,
                    maMonCu,
                    view.getMonAnDangChon(),
                    view.getSoLuong()
            );

            MonAn monAnMoi = view.getMonAnDangChon();
            doDuLieuChiTietFormLenBang();
            if (monAnMoi != null) {
                chonChiTietTheoMaMon(monAnMoi.getMaMon());
            }
            capNhatTrangThaiNutMon(true);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void xoaMonKhoiHoaDon() {
        int selectedRow = view.getTblChiTietForm().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn món cần xóa.");
            return;
        }

        int modelRow = view.getTblChiTietForm().convertRowIndexToModel(selectedRow);
        Long maMon = (Long) view.getChiTietFormTableModel().getValueAt(modelRow, 0);

        try {
            chiTietTam = hoaDonService.removeItem(chiTietTam, maMon);
            doDuLieuChiTietFormLenBang();
            view.clearMonAnForm();
            capNhatTrangThaiNutMon(false);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void themHoaDon() {
        try {
            NhanVien nhanVien = nhanVienDangNhap != null ? nhanVienDangNhap : view.getNhanVienDangChon();

            HoaDon hoaDon = hoaDonService.create(
                    view.getKhachHangDangChon(),
                    nhanVien,
                    view.getBanDangChon(),
                    view.getTrangThaiDangChon(),
                    chiTietTam
            );

            HoaDon hoaDonDaLuu = hoaDonService.findById(hoaDon.getMaHd());
            if (hoaDonDaLuu == null) {
                JOptionPane.showMessageDialog(view, "Không thể tải lại hóa đơn vừa tạo.");
                return;
            }

            taiDanhSachHoaDon();
            apHoaDonLenForm(hoaDonDaLuu);

            view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
            capNhatTrangThaiNutForm(true);
            capNhatTrangThaiNutMon(false);
            view.clearMonAnForm();

            JOptionPane.showMessageDialog(view, "Lập hóa đơn thành công.");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể lập hóa đơn", ex);
        }
    }

    private void capNhatHoaDon() {
        Long maHd = layMaHoaDonDangChonTrenForm();
        if (maHd == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn hóa đơn từ lịch sử rồi bấm Chỉnh sửa trước khi cập nhật.");
            return;
        }

        try {
            NhanVien nhanVien = nhanVienDangNhap != null ? nhanVienDangNhap : view.getNhanVienDangChon();

            HoaDon hoaDonDaCapNhat = hoaDonService.update(
                    maHd,
                    view.getKhachHangDangChon(),
                    nhanVien,
                    view.getBanDangChon(),
                    view.getTrangThaiDangChon(),
                    chiTietTam
            );

            taiDanhSachHoaDon();
            if (hoaDonDaCapNhat != null) {
                apHoaDonLenForm(hoaDonDaCapNhat);
            }

            view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
            capNhatTrangThaiNutForm(true);
            capNhatTrangThaiNutMon(false);
            view.clearMonAnForm();

            JOptionPane.showMessageDialog(view, "Cập nhật hóa đơn thành công.");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể cập nhật hóa đơn", ex);
        }
    }

    private void taiHoaDonVaoForm() {
        if (hoaDonDangChon == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn hóa đơn trong lịch sử.");
            return;
        }

        apHoaDonLenForm(hoaDonDangChon);
        view.clearMonAnForm();
        capNhatTrangThaiNutForm(true);
        capNhatTrangThaiNutMon(false);
        view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
    }

    private void xoaHoaDon() {
        if (hoaDonDangChon == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn hóa đơn cần xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn xóa hóa đơn này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long maHd = hoaDonDangChon.getMaHd();
            hoaDonService.delete(maHd);
            taiDanhSachHoaDon();

            if (String.valueOf(maHd).equals(view.getMaHoaDon())) {
                lamMoiForm();
            }

            JOptionPane.showMessageDialog(view, "Xóa hóa đơn thành công.");
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa hóa đơn", ex);
        }
    }

    private void apHoaDonLenForm(HoaDon hoaDon) {
        hoaDonDangChon = hoaDon;
        chiTietTam = hoaDonService.toItemDataList(hoaDon);
        doDuLieuChiTietFormLenBang();

        view.setMaHoaDon(String.valueOf(hoaDon.getMaHd()));
        view.setNgayLap(formatDateTime(hoaDon.getNgayLap()));
        view.setTrangThai(hoaDon.getTrangThai());
        view.setTrangThaiEditable(true);

        selectComboItemById(
                view.getCboKhachHang(),
                hoaDon.getKhachHang() == null ? null : hoaDon.getKhachHang().getMaKh(),
                KhachHang::getMaKh
        );

        selectComboItemById(
                view.getCboBan(),
                hoaDon.getBan() == null ? null : hoaDon.getBan().getMaBan(),
                Ban::getMaBan
        );

        if (nhanVienDangNhap != null) {
            view.getCboNhanVien().setSelectedIndex(0);
        }
    }

    private void lamMoiForm() {
        chiTietTam = new ArrayList<>();
        view.clearHoaDonForm();
        doDuLieuChiTietFormLenBang();
        view.setNgayLap(formatDateTime(LocalDateTime.now()));
        view.setTrangThai(HoaDonStatus.CREATED);

        if (nhanVienDangNhap != null && view.getCboNhanVien().getItemCount() > 0) {
            view.getCboNhanVien().setSelectedIndex(0);
        }

        capNhatTrangThaiNutForm(false);
        capNhatTrangThaiNutMon(false);
    }

    private void chonChiTietTheoMaMon(Long maMon) {
        if (maMon == null) {
            return;
        }

        for (int modelRow = 0; modelRow < view.getChiTietFormTableModel().getRowCount(); modelRow++) {
            Long value = (Long) view.getChiTietFormTableModel().getValueAt(modelRow, 0);
            if (maMon.equals(value)) {
                int viewRow = view.getTblChiTietForm().convertRowIndexToView(modelRow);
                view.getTblChiTietForm().setRowSelectionInterval(viewRow, viewRow);
                view.getTblChiTietForm().scrollRectToVisible(view.getTblChiTietForm().getCellRect(viewRow, 0, true));
                return;
            }
        }
    }

    private <T, ID> void selectComboItemById(JComboBox<T> comboBox, ID id, Function<T, ID> idExtractor) {
        if (id == null) {
            comboBox.setSelectedItem(null);
            return;
        }

        ComboBoxModel<T> model = comboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            T item = model.getElementAt(i);
            if (item != null && id.equals(idExtractor.apply(item))) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }

        comboBox.setSelectedItem(null);
    }

    private HoaDon timHoaDonTrongDanhSach(Long maHd) {
        for (HoaDon hoaDon : danhSachHoaDon) {
            if (hoaDon.getMaHd().equals(maHd)) {
                return hoaDon;
            }
        }
        return null;
    }

    private HoaDonItemData timChiTietTamTheoMaMon(Long maMon) {
        for (HoaDonItemData item : chiTietTam) {
            if (item.maMon().equals(maMon)) {
                return item;
            }
        }
        return null;
    }

    private Long layMaHoaDonDangChonTrenForm() {
        String maHoaDon = view.getMaHoaDon();
        if (maHoaDon.isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(maHoaDon);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private int tinhTongSoLuongEntity(List<ChiTietHD> chiTietHDList) {
        int tong = 0;
        for (ChiTietHD chiTietHD : chiTietHDList) {
            tong += chiTietHD.getSoLuong() == null ? 0 : chiTietHD.getSoLuong();
        }
        return tong;
    }

    private void capNhatTrangThaiNutForm(boolean dangSuaHoaDon) {
        view.getBtnThemHoaDon().setEnabled(!dangSuaHoaDon);
        view.getBtnCapNhatHoaDon().setEnabled(dangSuaHoaDon);
        view.setTrangThaiEditable(dangSuaHoaDon);
    }

    private void capNhatTrangThaiNutMon(boolean dangChonMon) {
        view.getBtnThemMon().setEnabled(true);
        view.getBtnCapNhatMon().setEnabled(dangChonMon);
        view.getBtnXoaMon().setEnabled(dangChonMon);
    }

    private void capNhatTrangThaiNutLichSu(boolean dangChonHoaDon) {
        view.getBtnChinhSua().setEnabled(dangChonHoaDon);
        view.getBtnXoaHoaDon().setEnabled(dangChonHoaDon);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }

    private String formatMoney(double value) {
        return MONEY_FORMAT.format(value);
    }

    private void hienThiLoi(String message, Exception ex) {
        JOptionPane.showMessageDialog(view, message + ": " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showHoaDonView() {
        view.setVisible(true);
    }

    private static class NullableObjectRenderer extends DefaultListCellRenderer {
        private final String nullText;

        private NullableObjectRenderer(String nullText) {
            this.nullText = nullText;
        }

        @Override
        public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                               boolean isSelected, boolean cellHasFocus) {
            Object displayValue = value == null ? nullText : value;
            return super.getListCellRendererComponent(list, displayValue, index, isSelected, cellHasFocus);
        }
    }
}