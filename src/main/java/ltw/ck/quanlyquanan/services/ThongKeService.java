package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;
import ltw.ck.quanlyquanan.model.entity.HoaDon;

import java.util.Date;
import java.util.List;

public interface ThongKeService {
    Result thongKe(Date tuNgay, Date denNgay);

    record Result(
            HoaDonStatsDto hoaDonStats,
            List<HoaDon> danhSachHoaDon,
            List<MonAnStatsDto> monAnStats
    ) {
    }
}
