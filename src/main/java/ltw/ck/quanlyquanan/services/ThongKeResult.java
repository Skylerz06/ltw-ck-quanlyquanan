package ltw.ck.quanlyquanan.services;

import ltw.ck.quanlyquanan.model.dto.HoaDonStatsDto;
import ltw.ck.quanlyquanan.model.dto.MonAnStatsDto;
import ltw.ck.quanlyquanan.model.entity.HoaDon;

import java.util.List;

public record ThongKeResult(
        HoaDonStatsDto hoaDonStats,
        List<HoaDon> danhSachHoaDon,
        List<MonAnStatsDto> monAnStats
) {
}