package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dao.BanDAO;
import ltw.ck.quanlyquanan.model.dao.HoaDonDAO;
import ltw.ck.quanlyquanan.model.dao.KhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.MonAnDAO;
import ltw.ck.quanlyquanan.model.dao.NhanVienDAO;
import ltw.ck.quanlyquanan.model.dao.impl.BanDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.HoaDonDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.KhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.MonAnDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.NhanVienDAOImpl;
import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.services.AppSession;
import ltw.ck.quanlyquanan.view.HoaDonView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class HoaDonController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.##");

    private final HoaDonView view;
    private final HoaDonDAO hoaDonDAO;
    private final KhachHangDAO khachHangDAO;
    private final NhanVienDAO nhanVienDAO;
    private final BanDAO banDAO;
    private final MonAnDAO monAnDAO;

    private List<HoaDon> danhSachHoaDon = new ArrayList<>();
    private List<KhachHang> danhSachKhachHang = new ArrayList<>();
    private List<NhanVien> danhSachNhanVien = new ArrayList<>();
    private List<Ban> danhSachBan = new ArrayList<>();
    private List<MonAn> danhSachMonAn = new ArrayList<>();
    private List<ChiTietHoaDonItem> chiTietTam = new ArrayList<>();
    private HoaDon hoaDonDangChon;
    private NhanVien nhanVienDangNhap;

    public HoaDonController(HoaDonView view) {
        this(view, new HoaDonDAOImpl(), new KhachHangDAOImpl(), new NhanVienDAOImpl(), new BanDAOImpl(), new MonAnDAOImpl());
    }

    public HoaDonController(HoaDonView view,
                            HoaDonDAO hoaDonDAO,
                            KhachHangDAO khachHangDAO,
                            NhanVienDAO nhanVienDAO,
                            BanDAO banDAO,
                            MonAnDAO monAnDAO) {
        this.view = view;
        this.hoaDonDAO = hoaDonDAO;
        this.khachHangDAO = khachHangDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.banDAO = banDAO;
        this.monAnDAO = monAnDAO;
        init();
    }

    private void init() {
        registerEvents();
        taiDuLieuDanhMuc();
        taiDanhSachHoaDon();
        lamMoiForm();
        view.showCard(HoaDonView.CARD_LAP_HOA_DON);
    }

    private void registerEvents() {
        view.getBtnTabLapHoaDon().addActionListener(e -> view.showCard(HoaDonView.CARD_LAP_HOA_DON));
        view.getBtnTabLichSu().addActionListener(e -> view.showCard(HoaDonView.CARD_LICH_SU));

        view.getBtnTimKiem().addActionListener(e -> timKiemHoaDon());
        view.getBtnTaiLai().addActionListener(e -> taiDanhSachHoaDon());
        view.getBtnChinhSua().addActionListener(e -> taiHoaDonVaoForm());
        view.getBtnXoaHoaDon().addActionListener(e -> xoaHoaDon());

        view.getBtnThemHoaDon().addActionListener(e -> themHoaDon());
        view.getBtnCapNhatHoaDon().addActionListener(e -> capNhatHoaDon());
        view.getBtnLamMoiForm().addActionListener(e -> lamMoiForm());
        view.getBtnDong().addActionListener(e -> view.dispose());

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
        danhSachKhachHang = new ArrayList<>(khachHangDAO.findAll());
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
        danhSachNhanVien = new ArrayList<>(nhanVienDAO.findAll());

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
        danhSachBan = new ArrayList<>(banDAO.findAll());
        DefaultComboBoxModel<Ban> model = new DefaultComboBoxModel<>();
        for (Ban ban : danhSachBan) {
            model.addElement(ban);
        }
        view.getCboBan().setModel(model);
        view.getCboBan().setSelectedItem(null);
    }

    private void taiMonAn() {
        danhSachMonAn = new ArrayList<>(monAnDAO.findAll());
        DefaultComboBoxModel<MonAn> model = new DefaultComboBoxModel<>();
        for (MonAn monAn : danhSachMonAn) {
            model.addElement(monAn);
        }
        view.getCboMonAn().setModel(model);
        view.getCboMonAn().setSelectedItem(null);
    }

    private void taiDanhSachHoaDon() {
        try {
            danhSachHoaDon = new ArrayList<>(hoaDonDAO.findAll());
            doDuLieuHoaDonLenBang(danhSachHoaDon);
            xoaChiTietLichSu();
            hoaDonDangChon = null;
            capNhatTrangThaiNutLichSu(false);
        } catch (Exception ex) {
            hienThiLoi("Không thể tải danh sách hóa đơn", ex);
        }
    }

    private void timKiemHoaDon() {
        String tuKhoa = view.getTuKhoaTimKiem();
        if (tuKhoa.isEmpty()) {
            taiDanhSachHoaDon();
            return;
        }

        try {
            List<HoaDon> tatCaHoaDon = hoaDonDAO.findAll();
            String normalizedKeyword = tuKhoa.toLowerCase(Locale.ROOT);
            List<HoaDon> ketQua = new ArrayList<>();

            for (HoaDon hoaDon : tatCaHoaDon) {
                String maHd = String.valueOf(hoaDon.getMaHd());
                String ngayLap = hoaDon.getNgayLap() == null ? "" : formatDateTime(hoaDon.getNgayLap()).toLowerCase(Locale.ROOT);
                String tenKh = hoaDon.getKhachHang() == null ? "khách lẻ" : safeLower(hoaDon.getKhachHang().getTenKh());
                String tenNv = hoaDon.getNhanVien() == null ? "" : safeLower(hoaDon.getNhanVien().getHoTen());
                String tenBan = hoaDon.getBan() == null ? "" : safeLower(hoaDon.getBan().getTenBan());

                if (maHd.contains(normalizedKeyword)
                        || ngayLap.contains(normalizedKeyword)
                        || tenKh.contains(normalizedKeyword)
                        || tenNv.contains(normalizedKeyword)
                        || tenBan.contains(normalizedKeyword)) {
                    ketQua.add(hoaDon);
                }
            }

            danhSachHoaDon = ketQua;
            doDuLieuHoaDonLenBang(ketQua);
            xoaChiTietLichSu();
            hoaDonDangChon = null;
            capNhatTrangThaiNutLichSu(false);

            if (ketQua.isEmpty()) {
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
                    hoaDon.getKhachHang() == null ? "Khách lẻ" : hoaDon.getKhachHang().getTenKh(),
                    hoaDon.getNhanVien() == null ? "" : hoaDon.getNhanVien().getHoTen(),
                    hoaDon.getBan() == null ? "" : hoaDon.getBan().getTenBan(),
                    tinhTongSoLuong(hoaDon.getLstChiTietHoaDon()),
                    formatMoney(tinhTongTienEntity(hoaDon.getLstChiTietHoaDon()))
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

        doDuLieuChiTietLichSuLenBang(hoaDonDangChon.getLstChiTietHoaDon());
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
        ChiTietHoaDonItem item = timChiTietTamTheoMaMon(maMon);
        if (item == null) {
            capNhatTrangThaiNutMon(false);
            return;
        }

        selectComboItemById(view.getCboMonAn(), item.monAn().getMaMon(), MonAn::getMaMon);
        view.setSoLuong(String.valueOf(item.soLuong()));
        capNhatTrangThaiNutMon(true);
    }

    private void doDuLieuChiTietFormLenBang() {
        DefaultTableModel tableModel = view.getChiTietFormTableModel();
        tableModel.setRowCount(0);

        for (ChiTietHoaDonItem item : chiTietTam) {
            double thanhTien = item.soLuong() * item.monAn().getDonGia();
            tableModel.addRow(new Object[]{
                    item.monAn().getMaMon(),
                    item.monAn().getTenMon(),
                    formatMoney(item.monAn().getDonGia()),
                    item.soLuong(),
                    formatMoney(thanhTien)
            });
        }

        view.getLblTongTienForm().setText("Tổng tiền: " + formatMoney(tinhTongTienTam(chiTietTam)) + " đ");
    }

    private void doDuLieuChiTietLichSuLenBang(List<ChiTietHD> chiTietHDList) {
        DefaultTableModel tableModel = view.getChiTietLichSuTableModel();
        tableModel.setRowCount(0);

        for (ChiTietHD chiTietHD : chiTietHDList) {
            if (chiTietHD.getMonAn() == null) {
                continue;
            }
            double thanhTien = chiTietHD.getMonAn().getDonGia() * chiTietHD.getSoLuong();
            tableModel.addRow(new Object[]{
                    chiTietHD.getMonAn().getMaMon(),
                    chiTietHD.getMonAn().getTenMon(),
                    formatMoney(chiTietHD.getMonAn().getDonGia()),
                    chiTietHD.getSoLuong(),
                    formatMoney(thanhTien)
            });
        }

        view.getLblTongTienLichSu().setText("Tổng tiền: " + formatMoney(tinhTongTienEntity(chiTietHDList)) + " đ");
    }

    private void xoaChiTietLichSu() {
        view.getChiTietLichSuTableModel().setRowCount(0);
        view.getLblTongTienLichSu().setText("Tổng tiền: 0 đ");
    }

    private void themMonVaoHoaDon() {
        try {
            MonAn monAn = view.getMonAnDangChon();
            int soLuong = parseSoLuong(view.getSoLuong());

            if (monAn == null) {
                throw new ValidationException("Vui lòng chọn món ăn.");
            }

            if (timChiTietTamTheoMaMon(monAn.getMaMon()) != null) {
                throw new ValidationException("Món ăn này đã có trong hóa đơn.");
            }

            chiTietTam.add(new ChiTietHoaDonItem(monAn, soLuong));
            sapXepChiTietTam();
            doDuLieuChiTietFormLenBang();
            view.clearMonAnForm();
            capNhatTrangThaiNutMon(false);
        } catch (ValidationException ex) {
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
            MonAn monAnMoi = view.getMonAnDangChon();
            int soLuongMoi = parseSoLuong(view.getSoLuong());

            if (monAnMoi == null) {
                throw new ValidationException("Vui lòng chọn món ăn.");
            }

            ChiTietHoaDonItem itemKhac = timChiTietTamTheoMaMon(monAnMoi.getMaMon());
            if (itemKhac != null && !maMonCu.equals(monAnMoi.getMaMon())) {
                throw new ValidationException("Món ăn này đã tồn tại trong hóa đơn.");
            }

            for (int i = 0; i < chiTietTam.size(); i++) {
                if (chiTietTam.get(i).monAn().getMaMon().equals(maMonCu)) {
                    chiTietTam.set(i, new ChiTietHoaDonItem(monAnMoi, soLuongMoi));
                    break;
                }
            }

            sapXepChiTietTam();
            doDuLieuChiTietFormLenBang();
            chonChiTietTheoMaMon(monAnMoi.getMaMon());
            capNhatTrangThaiNutMon(true);
        } catch (ValidationException ex) {
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
        chiTietTam.removeIf(item -> item.monAn().getMaMon().equals(maMon));
        doDuLieuChiTietFormLenBang();
        view.clearMonAnForm();
        capNhatTrangThaiNutMon(false);
    }

    private void themHoaDon() {
        try {
            HeaderFormData formData = layVaKiemTraThongTinHoaDon();

            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayLap(LocalDateTime.now());
            hoaDon.setKhachHang(formData.khachHang());
            hoaDon.setNhanVien(formData.nhanVien());
            hoaDon.setBan(formData.ban());
            hoaDon.setLstChiTietHoaDon(taoDanhSachChiTietEntity(hoaDon));

            hoaDonDAO.save(hoaDon);
            taiDanhSachHoaDon();
            lamMoiForm();
            view.showCard(HoaDonView.CARD_LICH_SU);
            chonHoaDonTheoId(hoaDon.getMaHd());
            JOptionPane.showMessageDialog(view, "Lập hóa đơn thành công.");
        } catch (ValidationException ex) {
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
            HoaDon hoaDon = hoaDonDAO.findById(maHd);
            if (hoaDon == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy hóa đơn cần cập nhật.");
                taiDanhSachHoaDon();
                return;
            }

            HeaderFormData formData = layVaKiemTraThongTinHoaDon();
            hoaDon.setKhachHang(formData.khachHang());
            hoaDon.setNhanVien(formData.nhanVien());
            hoaDon.setBan(formData.ban());
            hoaDon.getLstChiTietHoaDon().clear();
            hoaDon.getLstChiTietHoaDon().addAll(taoDanhSachChiTietEntity(hoaDon));

            hoaDonDAO.update(hoaDon);
            taiDanhSachHoaDon();
            view.showCard(HoaDonView.CARD_LICH_SU);
            chonHoaDonTheoId(maHd);
            JOptionPane.showMessageDialog(view, "Cập nhật hóa đơn thành công.");
        } catch (ValidationException ex) {
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

        chiTietTam = chuyenChiTietTuEntity(hoaDonDangChon.getLstChiTietHoaDon());
        doDuLieuChiTietFormLenBang();
        view.setMaHoaDon(String.valueOf(hoaDonDangChon.getMaHd()));
        view.setNgayLap(formatDateTime(hoaDonDangChon.getNgayLap()));
        selectComboItemById(view.getCboKhachHang(),
                hoaDonDangChon.getKhachHang() == null ? null : hoaDonDangChon.getKhachHang().getMaKh(),
                KhachHang::getMaKh);
        selectComboItemById(view.getCboBan(),
                hoaDonDangChon.getBan() == null ? null : hoaDonDangChon.getBan().getMaBan(),
                Ban::getMaBan);
        if (nhanVienDangNhap != null) {
            view.getCboNhanVien().setSelectedIndex(0);
        }
        view.clearMonAnForm();
        capNhatTrangThaiNutForm(true);
        capNhatTrangThaiNutMon(false);
        view.showCard(HoaDonView.CARD_LAP_HOA_DON);
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
            hoaDonDAO.delete(maHd);
            taiDanhSachHoaDon();
            if (String.valueOf(maHd).equals(view.getMaHoaDon())) {
                lamMoiForm();
            }
            JOptionPane.showMessageDialog(view, "Xóa hóa đơn thành công.");
        } catch (Exception ex) {
            hienThiLoi("Không thể xóa hóa đơn", ex);
        }
    }

    private HeaderFormData layVaKiemTraThongTinHoaDon() {
        NhanVien nhanVien = nhanVienDangNhap != null ? nhanVienDangNhap : view.getNhanVienDangChon();
        Ban ban = view.getBanDangChon();
        KhachHang khachHang = view.getKhachHangDangChon();

        if (nhanVien == null) {
            throw new ValidationException("Không xác định được nhân viên đang đăng nhập.");
        }
        if (ban == null) {
            throw new ValidationException("Vui lòng chọn bàn.");
        }
        if (chiTietTam.isEmpty()) {
            throw new ValidationException("Vui lòng thêm ít nhất một món ăn vào hóa đơn.");
        }

        return new HeaderFormData(khachHang, nhanVien, ban);
    }

    private int parseSoLuong(String soLuongText) {
        if (soLuongText == null || soLuongText.isBlank()) {
            throw new ValidationException("Vui lòng nhập số lượng.");
        }
        try {
            int soLuong = Integer.parseInt(soLuongText.trim());
            if (soLuong <= 0) {
                throw new ValidationException("Số lượng phải lớn hơn 0.");
            }
            return soLuong;
        } catch (NumberFormatException ex) {
            throw new ValidationException("Số lượng phải là số nguyên hợp lệ.");
        }
    }

    private List<ChiTietHD> taoDanhSachChiTietEntity(HoaDon hoaDon) {
        List<ChiTietHD> chiTietList = new ArrayList<>();
        for (ChiTietHoaDonItem item : chiTietTam) {
            ChiTietHD chiTietHD = new ChiTietHD();
            chiTietHD.setHoaDon(hoaDon);
            chiTietHD.setMonAn(item.monAn());
            chiTietHD.setSoLuong(item.soLuong());
            chiTietList.add(chiTietHD);
        }
        return chiTietList;
    }

    private List<ChiTietHoaDonItem> chuyenChiTietTuEntity(List<ChiTietHD> chiTietEntities) {
        List<ChiTietHoaDonItem> result = new ArrayList<>();
        if (chiTietEntities == null) {
            return result;
        }

        for (ChiTietHD chiTietHD : chiTietEntities) {
            if (chiTietHD.getMonAn() != null && chiTietHD.getSoLuong() != null) {
                result.add(new ChiTietHoaDonItem(chiTietHD.getMonAn(), chiTietHD.getSoLuong()));
            }
        }

        result.sort(Comparator.comparing(item -> item.monAn().getMaMon()));
        return result;
    }

    private void lamMoiForm() {
        chiTietTam = new ArrayList<>();
        view.clearHoaDonForm();
        doDuLieuChiTietFormLenBang();
        view.setNgayLap(formatDateTime(LocalDateTime.now()));
        if (nhanVienDangNhap != null && view.getCboNhanVien().getItemCount() > 0) {
            view.getCboNhanVien().setSelectedIndex(0);
        }
        capNhatTrangThaiNutForm(false);
        capNhatTrangThaiNutMon(false);
    }

    private void chonHoaDonTheoId(Long maHd) {
        if (maHd == null) {
            return;
        }

        for (int modelRow = 0; modelRow < view.getHoaDonTableModel().getRowCount(); modelRow++) {
            Long value = (Long) view.getHoaDonTableModel().getValueAt(modelRow, 0);
            if (maHd.equals(value)) {
                int viewRow = view.getTblHoaDon().convertRowIndexToView(modelRow);
                view.getTblHoaDon().setRowSelectionInterval(viewRow, viewRow);
                view.getTblHoaDon().scrollRectToVisible(view.getTblHoaDon().getCellRect(viewRow, 0, true));
                hoaDonDangChon = timHoaDonTrongDanhSach(maHd);
                if (hoaDonDangChon != null) {
                    doDuLieuChiTietLichSuLenBang(hoaDonDangChon.getLstChiTietHoaDon());
                    capNhatTrangThaiNutLichSu(true);
                }
                return;
            }
        }
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

    private ChiTietHoaDonItem timChiTietTamTheoMaMon(Long maMon) {
        for (ChiTietHoaDonItem item : chiTietTam) {
            if (item.monAn().getMaMon().equals(maMon)) {
                return item;
            }
        }
        return null;
    }

    private void sapXepChiTietTam() {
        chiTietTam.sort(Comparator.comparing(item -> item.monAn().getMaMon()));
    }

    private int tinhTongSoLuong(List<ChiTietHD> chiTietHDList) {
        int tong = 0;
        for (ChiTietHD chiTietHD : chiTietHDList) {
            tong += chiTietHD.getSoLuong() == null ? 0 : chiTietHD.getSoLuong();
        }
        return tong;
    }

    private double tinhTongTienEntity(List<ChiTietHD> chiTietHDList) {
        double tong = 0;
        for (ChiTietHD chiTietHD : chiTietHDList) {
            if (chiTietHD.getMonAn() != null && chiTietHD.getMonAn().getDonGia() != null && chiTietHD.getSoLuong() != null) {
                tong += chiTietHD.getMonAn().getDonGia() * chiTietHD.getSoLuong();
            }
        }
        return tong;
    }

    private double tinhTongTienTam(List<ChiTietHoaDonItem> chiTietItems) {
        double tong = 0;
        for (ChiTietHoaDonItem item : chiTietItems) {
            tong += item.monAn().getDonGia() * item.soLuong();
        }
        return tong;
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

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
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

    private record HeaderFormData(KhachHang khachHang, NhanVien nhanVien, Ban ban) {
    }

    private record ChiTietHoaDonItem(MonAn monAn, int soLuong) {
    }

    private static class ValidationException extends RuntimeException {
        private ValidationException(String message) {
            super(message);
        }
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
