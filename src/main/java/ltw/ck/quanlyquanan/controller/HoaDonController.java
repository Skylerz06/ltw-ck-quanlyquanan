package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dto.ChiTietHDDto;
import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;
import ltw.ck.quanlyquanan.session.AppSession;
import ltw.ck.quanlyquanan.services.HoaDonService;
import ltw.ck.quanlyquanan.services.HoaDonService.DanhMucData;
import ltw.ck.quanlyquanan.services.HoaDonService.FormData;
import ltw.ck.quanlyquanan.services.HoaDonService.HistoryRow;
import ltw.ck.quanlyquanan.services.HoaDonService.ItemData;
import ltw.ck.quanlyquanan.services.impl.HoaDonServiceImpl;
import ltw.ck.quanlyquanan.view.HoaDonPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class HoaDonController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.##");

    private final HoaDonPanel view;
    private final HoaDonService hoaDonService;

    private List<HoaDon> danhSachHoaDon = new ArrayList<>();
    private List<ItemData> chiTietTam = new ArrayList<>();
    private List<MonAn> tatCaMonAn = new ArrayList<>();

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
        view.getCboLocLoaiMonAn().addActionListener(e -> apDungBoLocMonAn());
        view.getTblHoaDon().getSelectionModel().addListSelectionListener(this::xuLyChonHoaDonLichSu);
        view.getTblChiTietForm().getSelectionModel().addListSelectionListener(this::xuLyChonChiTietForm);
    }

    private void taiDuLieuDanhMuc() {
        try {
            DanhMucData danhMucData = hoaDonService.loadDanhMuc();
            taiKhachHang(danhMucData.khachHangs());
            taiNhanVien(danhMucData.nhanViens());
            taiBan(danhMucData.bans());
            taiMonAn(danhMucData.monAns());
        } catch (Exception ex) {
            hienThiLoi("Không thể tải dữ liệu danh mục", ex);
        }
    }

    private void taiKhachHang(List<KhachHang> khachHangs) {
        DefaultComboBoxModel<KhachHang> model = new DefaultComboBoxModel<>();
        for (KhachHang khachHang : khachHangs) {
            model.addElement(khachHang);
        }
        view.getCboKhachHang().setModel(model);
        view.getCboKhachHang().setSelectedItem(null);
        view.setTenKhachHangNhap("");
    }

    private void taiNhanVien(List<NhanVien> nhanViens) {
        nhanVienDangNhap = AppSession.getCurrentNhanVien();

        DefaultComboBoxModel<NhanVien> model = new DefaultComboBoxModel<>();
        if (nhanVienDangNhap != null) {
            model.addElement(nhanVienDangNhap);
        } else {
            for (NhanVien nhanVien : nhanViens) {
                model.addElement(nhanVien);
            }
        }

        view.getCboNhanVien().setModel(model);
        view.getCboNhanVien().setEnabled(false);
        if (model.getSize() > 0) {
            view.getCboNhanVien().setSelectedIndex(0);
        }
    }

    private void taiBan(List<Ban> bans) {
        DefaultComboBoxModel<Ban> model = new DefaultComboBoxModel<>();
        for (Ban ban : bans) {
            model.addElement(ban);
        }
        view.getCboBan().setModel(model);
        view.getCboBan().setSelectedItem(null);
    }

    private void taiMonAn(List<MonAn> monAns) {
        tatCaMonAn = new ArrayList<>(monAns);
        taiLoaiMonAnLoc(monAns);
        apDungBoLocMonAn();
    }

    private void taiLoaiMonAnLoc(List<MonAn> monAns) {
        Map<Long, LoaiMonAn> loaiTheoId = new LinkedHashMap<>();
        for (MonAn monAn : monAns) {
            if (monAn.getLoaiMonAn() != null) {
                loaiTheoId.putIfAbsent(monAn.getLoaiMonAn().getMaLoai(), monAn.getLoaiMonAn());
            }
        }

        List<LoaiMonAn> danhSachLoaiMonAn = new ArrayList<>(loaiTheoId.values());
        danhSachLoaiMonAn.sort(Comparator.comparing(LoaiMonAn::getTenLoai, String.CASE_INSENSITIVE_ORDER));

        DefaultComboBoxModel<LoaiMonAn> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (LoaiMonAn loaiMonAn : danhSachLoaiMonAn) {
            model.addElement(loaiMonAn);
        }
        view.getCboLocLoaiMonAn().setModel(model);
        view.getCboLocLoaiMonAn().setRenderer(new NullableObjectRenderer("Tất cả loại"));
        view.getCboLocLoaiMonAn().setSelectedItem(null);
    }

    private void apDungBoLocMonAn() {
        MonAn monDangChon = view.getMonAnDangChon();
        LoaiMonAn loaiDangChon = view.getLoaiMonAnLocDangChon();

        DefaultComboBoxModel<MonAn> model = new DefaultComboBoxModel<>();
        for (MonAn monAn : tatCaMonAn) {
            boolean khopLoai = loaiDangChon == null
                    || (monAn.getLoaiMonAn() != null
                    && loaiDangChon.getMaLoai().equals(monAn.getLoaiMonAn().getMaLoai()));

            if (khopLoai) {
                model.addElement(monAn);
            }
        }

        view.getCboMonAn().setModel(model);

        if (monDangChon != null) {
            selectComboItemById(view.getCboMonAn(), monDangChon.getMaMon(), MonAn::getMaMon);
        } else {
            view.getCboMonAn().setSelectedItem(null);
        }
    }

    private void taiDanhSachHoaDon() {
        try {
            danhSachHoaDon = new ArrayList<>(hoaDonService.findAll());
            doDuLieuHoaDonLenBang(danhSachHoaDon);
            xoaChiTietLichSu();
            hoaDonDangChon = null;
            capNhatTrangThaiNutLichSu();
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
            capNhatTrangThaiNutLichSu();

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

        for (HistoryRow row : hoaDonService.toHistoryRows(dsHoaDon)) {
            tableModel.addRow(new Object[]{
                    row.maHd(),
                    formatDateTime(row.ngayLap()),
                    row.trangThai() == null ? "" : row.trangThai().name(),
                    row.tenKhachHang(),
                    row.tenNhanVien(),
                    row.tenBan(),
                    row.tongSoLuong(),
                    formatMoney(row.tongTien())
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
            capNhatTrangThaiNutLichSu();
            return;
        }

        int modelRow = view.getTblHoaDon().convertRowIndexToModel(selectedRow);
        Long maHd = (Long) view.getHoaDonTableModel().getValueAt(modelRow, 0);
        hoaDonDangChon = timHoaDonTrongDanhSach(maHd);

        if (hoaDonDangChon == null) {
            xoaChiTietLichSu();
            capNhatTrangThaiNutLichSu();
            return;
        }

        doDuLieuChiTietLichSuLenBang(hoaDonDangChon);
        capNhatTrangThaiNutLichSu();
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
        ItemData item = timChiTietTamTheoMaMon(maMon);
        if (item == null) {
            capNhatTrangThaiNutMon(false);
            return;
        }

        if (!comboContainsMonAn(item.maMon())) {
            view.clearBoLocMonAn();
            apDungBoLocMonAn();
        }

        selectComboItemById(view.getCboMonAn(), item.maMon(), MonAn::getMaMon);
        view.setSoLuong(String.valueOf(item.soLuong()));
        capNhatTrangThaiNutMon(true);
    }

    private void doDuLieuChiTietFormLenBang() {
        DefaultTableModel tableModel = view.getChiTietFormTableModel();
        tableModel.setRowCount(0);

        for (ChiTietHDDto item : hoaDonService.toChiTietHDDtos(chiTietTam)) {
            tableModel.addRow(new Object[]{
                    item.getMaMon(),
                    item.getTenMon(),
                    formatMoney(item.getDonGia()),
                    item.getSoLuong(),
                    formatMoney(item.getThanhTien())
            });
        }

        view.getLblTongTienForm().setText("Tổng tiền: " + formatMoney(hoaDonService.tinhTongTien(chiTietTam)) + " đ");
    }

    private void doDuLieuChiTietLichSuLenBang(HoaDon hoaDon) {
        DefaultTableModel tableModel = view.getChiTietLichSuTableModel();
        tableModel.setRowCount(0);

        for (ChiTietHDDto item : hoaDonService.toChiTietHDDtos(hoaDon)) {
            tableModel.addRow(new Object[]{
                    item.getMaMon(),
                    item.getTenMon(),
                    formatMoney(item.getDonGia()),
                    item.getSoLuong(),
                    formatMoney(item.getThanhTien())
            });
        }

        view.getLblTongTienLichSu().setText("Tổng tiền: " + formatMoney(hoaDonService.tinhTongTienHoaDon(hoaDon)) + " đ");
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
        } catch (IllegalArgumentException ex) {
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
        } catch (IllegalArgumentException ex) {
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
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void themHoaDon() {
        try {
            NhanVien nhanVien = nhanVienDangNhap != null ? nhanVienDangNhap : view.getNhanVienDangChon();
            KhachHang khachHang = hoaDonService.resolveKhachHang(
                    view.getKhachHangDangChon(),
                    view.getTenKhachHangNhap()
            );

            HoaDon hoaDon = hoaDonService.create(
                    khachHang,
                    nhanVien,
                    view.getBanDangChon(),
                    view.getTrangThaiDangChon(),
                    chiTietTam
            );

            HoaDon hoaDonDaLuu = hoaDonService.findById(hoaDon.getMaHd());
            taiDuLieuDanhMuc();
            taiDanhSachHoaDon();
            apHoaDonLenForm(hoaDonDaLuu);

            view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
            capNhatTrangThaiNutForm(true);
            capNhatTrangThaiNutMon(false);
            view.clearMonAnForm();

            JOptionPane.showMessageDialog(view, "Lập hóa đơn thành công.");
        } catch (IllegalArgumentException ex) {
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
            KhachHang khachHang = hoaDonService.resolveKhachHang(
                    view.getKhachHangDangChon(),
                    view.getTenKhachHangNhap()
            );

            HoaDon hoaDonDaCapNhat = hoaDonService.update(
                    maHd,
                    khachHang,
                    nhanVien,
                    view.getBanDangChon(),
                    view.getTrangThaiDangChon(),
                    chiTietTam
            );

            taiDuLieuDanhMuc();
            taiDanhSachHoaDon();
            apHoaDonLenForm(hoaDonDaCapNhat);

            view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
            capNhatTrangThaiNutForm(true);
            capNhatTrangThaiNutMon(false);
            view.clearMonAnForm();

            JOptionPane.showMessageDialog(view, "Cập nhật hóa đơn thành công.");
        } catch (IllegalArgumentException ex) {
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

        try {
            hoaDonService.validateEditable(hoaDonDangChon);
            apHoaDonLenForm(hoaDonDangChon);
            view.clearMonAnForm();
            capNhatTrangThaiNutForm(true);
            capNhatTrangThaiNutMon(false);
            view.showCard(HoaDonPanel.CARD_LAP_HOA_DON);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        }
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
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa hóa đơn", ex);
        }
    }

    private void apHoaDonLenForm(HoaDon hoaDon) {
        hoaDonDangChon = hoaDon;

        FormData formData = hoaDonService.toFormData(hoaDon);
        chiTietTam = new ArrayList<>(formData.items());
        doDuLieuChiTietFormLenBang();

        view.setMaHoaDon(String.valueOf(formData.maHd()));
        view.setNgayLap(formatDateTime(formData.ngayLap()));
        view.setTrangThai(formData.trangThai());
        view.setTrangThaiEditable(true);

        selectComboItemById(view.getCboKhachHang(), formData.maKhachHang(), KhachHang::getMaKh);
        if (hoaDon.getKhachHang() == null) {
            view.setTenKhachHangNhap("");
        }
        selectComboItemById(view.getCboBan(), formData.maBan(), Ban::getMaBan);

        if (nhanVienDangNhap != null) {
            view.getCboNhanVien().setSelectedIndex(0);
        }
    }

    private void lamMoiForm() {
        chiTietTam = new ArrayList<>();
        hoaDonDangChon = null;
        view.clearHoaDonForm();
        apDungBoLocMonAn();
        doDuLieuChiTietFormLenBang();
        view.setNgayLap(formatDateTime(LocalDateTime.now()));
        view.setTrangThai(HoaDonStatus.CREATED);
        view.setTenKhachHangNhap("");

        if (nhanVienDangNhap != null && view.getCboNhanVien().getItemCount() > 0) {
            view.getCboNhanVien().setSelectedIndex(0);
        }

        capNhatTrangThaiNutForm(false);
        capNhatTrangThaiNutMon(false);
        capNhatTrangThaiNutLichSu();
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

    private boolean comboContainsMonAn(Long maMon) {
        ComboBoxModel<MonAn> model = view.getCboMonAn().getModel();
        for (int i = 0; i < model.getSize(); i++) {
            MonAn item = model.getElementAt(i);
            if (item != null && maMon.equals(item.getMaMon())) {
                return true;
            }
        }
        return false;
    }

    private <T, ID> void selectComboItemById(JComboBox<T> comboBox, ID id, Function<T, ID> idExtractor) {
        if (id == null) {
            comboBox.setSelectedItem(null);
            if (comboBox.isEditable()) {
                comboBox.getEditor().setItem("");
            }
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
        if (comboBox.isEditable()) {
            comboBox.getEditor().setItem("");
        }
    }

    private HoaDon timHoaDonTrongDanhSach(Long maHd) {
        for (HoaDon hoaDon : danhSachHoaDon) {
            if (hoaDon.getMaHd().equals(maHd)) {
                return hoaDon;
            }
        }
        return null;
    }

    private ItemData timChiTietTamTheoMaMon(Long maMon) {
        for (ItemData item : chiTietTam) {
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

    private void capNhatTrangThaiNutLichSu() {
        boolean editable = hoaDonService.isEditable(hoaDonDangChon);
        view.getBtnChinhSua().setEnabled(editable);
        view.getBtnXoaHoaDon().setEnabled(editable);
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
