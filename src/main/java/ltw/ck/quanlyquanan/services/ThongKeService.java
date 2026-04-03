package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.dto.BanStatsDto;
import ltw.ck.quanlyquanan.model.dto.HoaDonRowDto;
import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.KhachHangStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;

import java.util.Date;
import java.util.List;

public interface ThongKeService {
    Result thongKe(Date tuNgay, Date denNgay);

    record Result(
            HoaDonStatsDto hoaDonStats,
            List<HoaDonRowDto> hoaDonRows,
            List<MonAnStatsDto> monAnStats,
            List<BanStatsDto> banRows,
            List<KhachHangStatsDto> khachHangRows
    ) {
    }
}
