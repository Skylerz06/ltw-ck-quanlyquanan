package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ThongKeService {
    Result thongKe(Date tuNgay, Date denNgay);

    record Result(
            HoaDonStatsDto hoaDonStats,
            List<HoaDonRow> hoaDonRows,
            List<MonAnStatsDto> monAnStats
    ) {
    }

    record HoaDonRow(
            Long maHd,
            LocalDateTime ngayLap,
            String tenKhachHang,
            String tenNhanVien,
            String tenBan,
            int tongSoLuong,
            double tongTien
    ) {
    }
}
