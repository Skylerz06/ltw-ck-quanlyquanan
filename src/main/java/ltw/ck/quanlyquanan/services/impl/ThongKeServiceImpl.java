package ltw.ck.quanlyquanan.services.impl;

import ltw.ck.quanlyquanan.model.dao.HoaDonDAO;
import ltw.ck.quanlyquanan.model.dao.impl.HoaDonDAOImpl;
import ltw.ck.quanlyquanan.model.dto.BanStatsDto;
import ltw.ck.quanlyquanan.model.dto.HoaDonRowDto;
import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.KhachHangStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;
import ltw.ck.quanlyquanan.services.ThongKeService;
import ltw.ck.quanlyquanan.services.ThongKeService.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeServiceImpl implements ThongKeService {

    private final HoaDonDAO hoaDonDAO;

    public ThongKeServiceImpl() {
        this(new HoaDonDAOImpl());
    }

    public ThongKeServiceImpl(HoaDonDAO hoaDonDAO) {
        this.hoaDonDAO = hoaDonDAO;
    }

    @Override
    public Result thongKe(Date tuNgayDate, Date denNgayDate) {
        DateRange range = layKhoangNgay(tuNgayDate, denNgayDate);
        List<HoaDon> danhSachHoaDon = locHoaDonDaThanhToan(
                hoaDonDAO.findByKhoangNgay(range.from(), range.to())
        );

        HoaDonStatsDto hoaDonStats = tinhThongKeHoaDon(danhSachHoaDon);
        List<HoaDonRowDto> hoaDonRows = taoHoaDonRows(danhSachHoaDon);
        List<MonAnStatsDto> monAnStats = tinhThongKeMonAn(danhSachHoaDon);
        List<BanStatsDto> banRows = tinhThongKeTheoBan(danhSachHoaDon);
        List<KhachHangStatsDto> khachHangRows = tinhThongKeTheoKhachHang(danhSachHoaDon);

        return new Result(hoaDonStats, hoaDonRows, monAnStats, banRows, khachHangRows);
    }

    private DateRange layKhoangNgay(Date tuNgayDate, Date denNgayDate) {
        if (tuNgayDate == null || denNgayDate == null) {
            throw new IllegalArgumentException("Vui lòng chọn đầy đủ từ ngày và đến ngày.");
        }

        LocalDate tuNgay = chuyenDate(tuNgayDate);
        LocalDate denNgay = chuyenDate(denNgayDate);

        if (tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Từ ngày không được lớn hơn đến ngày.");
        }

        return new DateRange(tuNgay.atStartOfDay(), denNgay.atTime(23, 59, 59));
    }

    private List<HoaDon> locHoaDonDaThanhToan(List<HoaDon> dsHoaDon) {
        List<HoaDon> result = new ArrayList<>();
        if (dsHoaDon == null) {
            return result;
        }

        for (HoaDon hoaDon : dsHoaDon) {
            if (hoaDon != null && hoaDon.getTrangThai() == HoaDonStatus.PAID) {
                result.add(hoaDon);
            }
        }
        return result;
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

    private List<HoaDonRowDto> taoHoaDonRows(List<HoaDon> dsHoaDon) {
        List<HoaDonRowDto> result = new ArrayList<>();
        for (HoaDon hoaDon : dsHoaDon) {
            result.add(new HoaDonRowDto(
                    hoaDon.getMaHd(),
                    hoaDon.getNgayLap(),
                    hoaDon.getKhachHang() == null ? "Khách lẻ" : hoaDon.getKhachHang().getTenKh(),
                    hoaDon.getNhanVien() == null ? "" : hoaDon.getNhanVien().getHoTen(),
                    hoaDon.getBan() == null ? "" : hoaDon.getBan().getTenBan(),
                    tinhTongSoLuong(hoaDon),
                    tinhTongTienHoaDon(hoaDon)
            ));
        }
        return result;
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

        result.sort(
                Comparator.comparingLong(MonAnStatsDto::getSoLuongBan).reversed()
                        .thenComparing(MonAnStatsDto::getTenMon)
        );

        return result;
    }

    private List<BanStatsDto> tinhThongKeTheoBan(List<HoaDon> dsHoaDon) {
        Map<String, GroupStat> thongKeTheoBan = new HashMap<>();

        for (HoaDon hoaDon : dsHoaDon) {
            String tenBan = hoaDon.getBan() == null ? "Chưa gán bàn" : hoaDon.getBan().getTenBan();
            GroupStat stat = thongKeTheoBan.computeIfAbsent(tenBan, key -> new GroupStat());
            stat.soHoaDon++;
            stat.tongSoLuong += tinhTongSoLuong(hoaDon);
            stat.tongDoanhThu += tinhTongTienHoaDon(hoaDon);
        }

        List<BanStatsDto> result = new ArrayList<>();
        for (Map.Entry<String, GroupStat> entry : thongKeTheoBan.entrySet()) {
            GroupStat stat = entry.getValue();
            result.add(new BanStatsDto(entry.getKey(), stat.soHoaDon, stat.tongSoLuong, stat.tongDoanhThu));
        }

        result.sort(
                Comparator.comparingDouble(BanStatsDto::getTongDoanhThu).reversed()
                        .thenComparing(BanStatsDto::getTenBan)
        );
        return result;
    }

    private List<KhachHangStatsDto> tinhThongKeTheoKhachHang(List<HoaDon> dsHoaDon) {
        Map<String, GroupStat> thongKeTheoKhachHang = new HashMap<>();

        for (HoaDon hoaDon : dsHoaDon) {
            String tenKhachHang = hoaDon.getKhachHang() == null ? "Khách lẻ" : hoaDon.getKhachHang().getTenKh();
            GroupStat stat = thongKeTheoKhachHang.computeIfAbsent(tenKhachHang, key -> new GroupStat());
            stat.soHoaDon++;
            stat.tongSoLuong += tinhTongSoLuong(hoaDon);
            stat.tongDoanhThu += tinhTongTienHoaDon(hoaDon);
        }

        List<KhachHangStatsDto> result = new ArrayList<>();
        for (Map.Entry<String, GroupStat> entry : thongKeTheoKhachHang.entrySet()) {
            GroupStat stat = entry.getValue();
            result.add(new KhachHangStatsDto(entry.getKey(), stat.soHoaDon, stat.tongSoLuong, stat.tongDoanhThu));
        }

        result.sort(
                Comparator.comparingDouble(KhachHangStatsDto::getTongDoanhThu).reversed()
                        .thenComparing(KhachHangStatsDto::getTenKhachHang)
        );
        return result;
    }

    private int tinhTongSoLuong(HoaDon hoaDon) {
        int tong = 0;
        if (hoaDon.getLstChiTietHoaDon() == null) {
            return tong;
        }

        for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
            tong += chiTietHD.getSoLuong() == null ? 0 : chiTietHD.getSoLuong();
        }
        return tong;
    }

    private double tinhTongTienHoaDon(HoaDon hoaDon) {
        double tong = 0;
        if (hoaDon.getLstChiTietHoaDon() == null) {
            return tong;
        }

        for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
            if (chiTietHD.getMonAn() != null
                    && chiTietHD.getMonAn().getDonGia() != null
                    && chiTietHD.getSoLuong() != null) {
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

    private record DateRange(LocalDateTime from, LocalDateTime to) {
    }

    private static class GroupStat {
        private long soHoaDon;
        private int tongSoLuong;
        private double tongDoanhThu;
    }
}
