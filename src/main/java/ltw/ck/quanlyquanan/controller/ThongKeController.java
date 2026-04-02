package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dao.HoaDonDAO;
import ltw.ck.quanlyquanan.model.dao.impl.HoaDonDAOImpl;
import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.view.ThongKeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.##");

    private final ThongKeView view;
    private final HoaDonDAO hoaDonDAO;
    private List<HoaDon> danhSachHoaDon = new ArrayList<>();

    public ThongKeController(ThongKeView view) {
        this(view, new HoaDonDAOImpl());
    }

    public ThongKeController(ThongKeView view, HoaDonDAO hoaDonDAO) {
        this.view = view;
        this.hoaDonDAO = hoaDonDAO;
        init();
    }

    private void init() {
        registerEvents();
        thongKe();
    }

    private void registerEvents() {
        view.getBtnTabTongQuan().addActionListener(e -> view.showCard(ThongKeView.CARD_TONG_QUAN));
        view.getBtnTabMonAn().addActionListener(e -> view.showCard(ThongKeView.CARD_MON_AN));
        view.getBtnThongKe().addActionListener(e -> thongKe());
        view.getBtnLamMoi().addActionListener(e -> lamMoiBoLoc());
    }

    private void thongKe() {
        try {
            DateRange range = layKhoangNgay();
            danhSachHoaDon = new ArrayList<>(hoaDonDAO.findByKhoangNgay(range.from(), range.to()));

            HoaDonStatsDto hoaDonStats = tinhThongKeHoaDon(danhSachHoaDon);
            List<MonAnStatsDto> monAnStats = tinhThongKeMonAn(danhSachHoaDon);

            hienThiThongKeHoaDon(hoaDonStats, danhSachHoaDon);
            hienThiThongKeMonAn(monAnStats);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Dữ liệu chưa hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            hienThiLoi("Không thể thống kê dữ liệu", ex);
        }
    }

    private DateRange layKhoangNgay() {
        LocalDate tuNgay = chuyenDate((Date) view.getSpnTuNgay().getValue());
        LocalDate denNgay = chuyenDate((Date) view.getSpnDenNgay().getValue());

        if (tuNgay.isAfter(denNgay)) {
            throw new ValidationException("Từ ngày không được lớn hơn đến ngày.");
        }

        return new DateRange(tuNgay.atStartOfDay(), denNgay.atTime(23, 59, 59));
    }

    private HoaDonStatsDto tinhThongKeHoaDon(List<HoaDon> dsHoaDon) {
        long tongHoaDon = dsHoaDon.size();
        double tongDoanhThu = 0;
        LocalDate homNay = LocalDate.now();
        long hoaDonHomNay = 0;

        for (HoaDon hoaDon : dsHoaDon) {
            tongDoanhThu += tinhTongTienHoaDon(hoaDon);
            if (hoaDon.getNgayLap() != null && hoaDon.getNgayLap().toLocalDate().equals(homNay)) {
                hoaDonHomNay++;
            }
        }

        return new HoaDonStatsDto(tongHoaDon, tongDoanhThu, hoaDonHomNay);
    }

    private List<MonAnStatsDto> tinhThongKeMonAn(List<HoaDon> dsHoaDon) {
        Map<String, Long> thongKeMonAn = new HashMap<>();

        for (HoaDon hoaDon : dsHoaDon) {
            for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
                if (chiTietHD.getMonAn() == null || chiTietHD.getSoLuong() == null) {
                    continue;
                }
                String tenMon = chiTietHD.getMonAn().getTenMon();
                long soLuong = chiTietHD.getSoLuong();
                thongKeMonAn.merge(tenMon, soLuong, Long::sum);
            }
        }

        List<MonAnStatsDto> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : thongKeMonAn.entrySet()) {
            result.add(new MonAnStatsDto(entry.getKey(), entry.getValue()));
        }

        result.sort(Comparator.comparingLong(MonAnStatsDto::getSoLuongBan).reversed()
                .thenComparing(MonAnStatsDto::getTenMon));
        return result;
    }

    private void hienThiThongKeHoaDon(HoaDonStatsDto stats, List<HoaDon> dsHoaDon) {
        view.getLblTongHoaDon().setText(String.valueOf(stats.getTotalHoaDon()));
        view.getLblTongDoanhThu().setText(MONEY_FORMAT.format(stats.getTongDoanhThu()) + " đ");
        view.getLblHoaDonHomNay().setText(String.valueOf(stats.getHoaDonHomNay()));

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
                    MONEY_FORMAT.format(tinhTongTienHoaDon(hoaDon))
            });
        }
    }

    private void hienThiThongKeMonAn(List<MonAnStatsDto> dsMonAnStats) {
        DefaultTableModel tableModel = view.getMonAnTableModel();
        tableModel.setRowCount(0);

        int top = 1;
        for (MonAnStatsDto statsDto : dsMonAnStats) {
            tableModel.addRow(new Object[]{
                    top++,
                    statsDto.getTenMon(),
                    statsDto.getSoLuongBan()
            });
        }
    }

    private void lamMoiBoLoc() {
        view.macDinhNgay();
        thongKe();
    }

    private int tinhTongSoLuong(List<ChiTietHD> dsChiTiet) {
        int tong = 0;
        for (ChiTietHD chiTietHD : dsChiTiet) {
            tong += chiTietHD.getSoLuong() == null ? 0 : chiTietHD.getSoLuong();
        }
        return tong;
    }

    private double tinhTongTienHoaDon(HoaDon hoaDon) {
        double tong = 0;
        for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
            if (chiTietHD.getMonAn() != null && chiTietHD.getMonAn().getDonGia() != null && chiTietHD.getSoLuong() != null) {
                tong += chiTietHD.getMonAn().getDonGia() * chiTietHD.getSoLuong();
            }
        }
        return tong;
    }

    private LocalDate chuyenDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }

    private void hienThiLoi(String message, Exception ex) {
        JOptionPane.showMessageDialog(view, message + ": " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showThongKeView() {
        view.setVisible(true);
    }

    private record DateRange(LocalDateTime from, LocalDateTime to) {
    }

    private static class ValidationException extends RuntimeException {
        private ValidationException(String message) {
            super(message);
        }
    }
}
