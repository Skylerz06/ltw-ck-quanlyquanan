package ltw.ck.quanlyquanan.controller;

import ltw.ck.quanlyquanan.model.dto.BanStatsDto;
import ltw.ck.quanlyquanan.model.dto.HoaDonRowDto;
import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.KhachHangStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;
import ltw.ck.quanlyquanan.services.ThongKeService;
import ltw.ck.quanlyquanan.services.ThongKeService.Result;
import ltw.ck.quanlyquanan.services.impl.ThongKeServiceImpl;
import ltw.ck.quanlyquanan.view.ThongKePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThongKeController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.##");

    private final ThongKePanel view;
    private final ThongKeService thongKeService;

    public ThongKeController(ThongKePanel view) {
        this(view, new ThongKeServiceImpl());
    }

    public ThongKeController(ThongKePanel view, ThongKeService thongKeService) {
        this.view = view;
        this.thongKeService = thongKeService;
        init();
    }

    private void init() {
        registerEvents();
        thongKe();
    }

    private void registerEvents() {
        view.getBtnTabTongQuan().addActionListener(e -> view.showCard(ThongKePanel.CARD_TONG_QUAN));
        view.getBtnTabMonAn().addActionListener(e -> view.showCard(ThongKePanel.CARD_MON_AN));
        view.getBtnTabBan().addActionListener(e -> view.showCard(ThongKePanel.CARD_BAN));
        view.getBtnTabKhachHang().addActionListener(e -> view.showCard(ThongKePanel.CARD_KHACH_HANG));
        view.getBtnThongKe().addActionListener(e -> thongKe());
        view.getBtnLamMoi().addActionListener(e -> lamMoiBoLoc());
    }

    private void thongKe() {
        try {
            Result result = thongKeService.thongKe(
                    (java.util.Date) view.getSpnTuNgay().getValue(),
                    (java.util.Date) view.getSpnDenNgay().getValue()
            );

            hienThiThongKeHoaDon(result.hoaDonStats(), result.hoaDonRows());
            hienThiThongKeMonAn(result.monAnStats());
            hienThiThongKeTheoBan(result.banRows());
            hienThiThongKeTheoKhachHang(result.khachHangRows());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    ex.getMessage(),
                    "Dữ liệu chưa hợp lệ",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            hienThiLoi("Không thể thống kê dữ liệu", ex);
        }
    }

    private void hienThiThongKeHoaDon(HoaDonStatsDto stats, List<HoaDonRowDto> rows) {
        view.getLblTongHoaDon().setText(String.valueOf(stats.getTotalHoaDon()));
        view.getLblTongDoanhThu().setText(MONEY_FORMAT.format(stats.getTongDoanhThu()) + " đ");
        view.getLblHoaDonHomNay().setText(String.valueOf(stats.getHoaDonHomNay()));

        DefaultTableModel tableModel = view.getHoaDonTableModel();
        tableModel.setRowCount(0);

        for (HoaDonRowDto row : rows) {
            tableModel.addRow(new Object[]{
                    row.getMaHd(),
                    formatDateTime(row.getNgayLap()),
                    row.getTenKhachHang(),
                    row.getTenNhanVien(),
                    row.getTenBan(),
                    row.getTongSoLuong(),
                    MONEY_FORMAT.format(row.getTongTien())
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

    private void hienThiThongKeTheoBan(List<BanStatsDto> rows) {
        DefaultTableModel tableModel = view.getBanTableModel();
        tableModel.setRowCount(0);

        for (BanStatsDto row : rows) {
            tableModel.addRow(new Object[]{
                    row.getTenBan(),
                    row.getSoHoaDon(),
                    row.getTongSoLuong(),
                    MONEY_FORMAT.format(row.getTongDoanhThu())
            });
        }
    }

    private void hienThiThongKeTheoKhachHang(List<KhachHangStatsDto> rows) {
        DefaultTableModel tableModel = view.getKhachHangTableModel();
        tableModel.setRowCount(0);

        for (KhachHangStatsDto row : rows) {
            tableModel.addRow(new Object[]{
                    row.getTenKhachHang(),
                    row.getSoHoaDon(),
                    row.getTongSoLuong(),
                    MONEY_FORMAT.format(row.getTongDoanhThu())
            });
        }
    }

    private void lamMoiBoLoc() {
        view.macDinhNgay();
        thongKe();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }

    private void hienThiLoi(String message, Exception ex) {
        JOptionPane.showMessageDialog(
                view,
                message + ": " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void showThongKeView() {
        view.setVisible(true);
    }
}
