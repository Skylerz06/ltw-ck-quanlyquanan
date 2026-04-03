package ltw.ck.quanlyquanan.services.impl;

import ltw.ck.quanlyquanan.model.dto.ChiTietHDDto;
import ltw.ck.quanlyquanan.model.dao.BanDAO;
import ltw.ck.quanlyquanan.model.dao.HoaDonDAO;
import ltw.ck.quanlyquanan.model.dao.KhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.LoaiKhachHangDAO;
import ltw.ck.quanlyquanan.model.dao.MonAnDAO;
import ltw.ck.quanlyquanan.model.dao.NhanVienDAO;
import ltw.ck.quanlyquanan.model.dao.impl.BanDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.HoaDonDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.KhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.LoaiKhachHangDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.MonAnDAOImpl;
import ltw.ck.quanlyquanan.model.dao.impl.NhanVienDAOImpl;
import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.ChiTietHD;
import ltw.ck.quanlyquanan.model.entity.HoaDon;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.LoaiKH;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;
import ltw.ck.quanlyquanan.services.HoaDonService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HoaDonServiceImpl implements HoaDonService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final HoaDonDAO hoaDonDAO;
    private final KhachHangDAO khachHangDAO;
    private final LoaiKhachHangDAO loaiKhachHangDAO;
    private final NhanVienDAO nhanVienDAO;
    private final BanDAO banDAO;
    private final MonAnDAO monAnDAO;

    public HoaDonServiceImpl() {
        this(
                new HoaDonDAOImpl(),
                new KhachHangDAOImpl(),
                new LoaiKhachHangDAOImpl(),
                new NhanVienDAOImpl(),
                new BanDAOImpl(),
                new MonAnDAOImpl()
        );
    }

    public HoaDonServiceImpl(HoaDonDAO hoaDonDAO,
                             KhachHangDAO khachHangDAO,
                             LoaiKhachHangDAO loaiKhachHangDAO,
                             NhanVienDAO nhanVienDAO,
                             BanDAO banDAO,
                             MonAnDAO monAnDAO) {
        this.hoaDonDAO = hoaDonDAO;
        this.khachHangDAO = khachHangDAO;
        this.loaiKhachHangDAO = loaiKhachHangDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.banDAO = banDAO;
        this.monAnDAO = monAnDAO;
    }

    @Override
    public DanhMucData loadDanhMuc() {
        return new DanhMucData(
                new ArrayList<>(khachHangDAO.findAll()),
                new ArrayList<>(nhanVienDAO.findAll()),
                new ArrayList<>(banDAO.findAll()),
                new ArrayList<>(monAnDAO.findAll())
        );
    }

    @Override
    public List<HoaDon> findAll() {
        return new ArrayList<>(hoaDonDAO.findAll());
    }

    @Override
    public List<HoaDon> search(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return findAll();
        }

        List<HoaDon> tatCaHoaDon = hoaDonDAO.findAll();
        String normalizedKeyword = tuKhoa.toLowerCase(Locale.ROOT);
        List<HoaDon> ketQua = new ArrayList<>();

        for (HoaDon hoaDon : tatCaHoaDon) {
            String maHd = String.valueOf(hoaDon.getMaHd());
            String ngayLap = hoaDon.getNgayLap() == null
                    ? ""
                    : formatDateTime(hoaDon.getNgayLap()).toLowerCase(Locale.ROOT);
            String trangThai = hoaDon.getTrangThai() == null
                    ? ""
                    : hoaDon.getTrangThai().name().toLowerCase(Locale.ROOT);
            String tenKh = hoaDon.getKhachHang() == null
                    ? "khách lẻ"
                    : safeLower(hoaDon.getKhachHang().getTenKh());
            String tenNv = hoaDon.getNhanVien() == null
                    ? ""
                    : safeLower(hoaDon.getNhanVien().getHoTen());
            String tenBan = hoaDon.getBan() == null
                    ? ""
                    : safeLower(hoaDon.getBan().getTenBan());

            if (maHd.contains(normalizedKeyword)
                    || ngayLap.contains(normalizedKeyword)
                    || trangThai.contains(normalizedKeyword)
                    || tenKh.contains(normalizedKeyword)
                    || tenNv.contains(normalizedKeyword)
                    || tenBan.contains(normalizedKeyword)) {
                ketQua.add(hoaDon);
            }
        }

        return ketQua;
    }

    @Override
    public HoaDon findById(Long maHd) {
        if (maHd == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn.");
        }
        return hoaDonDAO.findById(maHd);
    }

    @Override
    public void delete(Long maHd) {
        HoaDon hoaDon = findById(maHd);
        validateEditable(hoaDon);
        hoaDonDAO.delete(maHd);
    }

    @Override
    public HoaDon create(KhachHang khachHang,
                         NhanVien nhanVien,
                         Ban ban,
                         HoaDonStatus trangThai,
                         List<ItemData> items) {
        validateHeader(nhanVien, ban, trangThai, items);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon.setTrangThai(trangThai);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setBan(ban);
        hoaDon.setLstChiTietHoaDon(taoDanhSachChiTietEntity(hoaDon, items));

        hoaDonDAO.save(hoaDon);
        return hoaDon;
    }

    @Override
    public HoaDon update(Long maHd,
                         KhachHang khachHang,
                         NhanVien nhanVien,
                         Ban ban,
                         HoaDonStatus trangThai,
                         List<ItemData> items) {
        HoaDon hoaDon = findById(maHd);
        validateEditable(hoaDon);
        validateHeader(nhanVien, ban, trangThai, items);

        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setBan(ban);
        hoaDon.setTrangThai(trangThai);
        hoaDon.getLstChiTietHoaDon().clear();
        hoaDon.getLstChiTietHoaDon().addAll(taoDanhSachChiTietEntity(hoaDon, items));

        hoaDonDAO.update(hoaDon);
        return hoaDonDAO.findById(maHd);
    }

    @Override
    public KhachHang resolveKhachHang(KhachHang khachHangDangChon, String tenKhachHangNhap) {
        String tenKhachHang = normalizeText(tenKhachHangNhap);
        if (tenKhachHang.isEmpty()) {
            return null;
        }

        if (khachHangDangChon != null
                && tenKhachHang.equalsIgnoreCase(normalizeText(khachHangDangChon.getTenKh()))) {
            return khachHangDangChon;
        }

        KhachHang khachHangTonTai = timKhachHangTheoTenChinhXac(tenKhachHang);
        if (khachHangTonTai != null) {
            return khachHangTonTai;
        }

        LoaiKH loaiKhachHangMacDinh = layLoaiKhachHangMacDinh();
        KhachHang khachHangMoi = new KhachHang();
        khachHangMoi.setTenKh(tenKhachHang);
        khachHangMoi.setLoaiKhachHang(loaiKhachHangMacDinh);
        khachHangDAO.save(khachHangMoi);
        return khachHangMoi;
    }

    @Override
    public FormData toFormData(HoaDon hoaDon) {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn.");
        }

        return new FormData(
                hoaDon.getMaHd(),
                hoaDon.getNgayLap(),
                hoaDon.getTrangThai(),
                hoaDon.getKhachHang() == null ? null : hoaDon.getKhachHang().getMaKh(),
                hoaDon.getBan() == null ? null : hoaDon.getBan().getMaBan(),
                toItemDataList(hoaDon)
        );
    }

    @Override
    public List<HistoryRow> toHistoryRows(List<HoaDon> hoaDons) {
        List<HistoryRow> result = new ArrayList<>();
        if (hoaDons == null) {
            return result;
        }

        for (HoaDon hoaDon : hoaDons) {
            List<ItemData> items = toItemDataList(hoaDon);
            result.add(new HistoryRow(
                    hoaDon.getMaHd(),
                    hoaDon.getNgayLap(),
                    hoaDon.getTrangThai(),
                    hoaDon.getKhachHang() == null ? "Khách lẻ" : hoaDon.getKhachHang().getTenKh(),
                    hoaDon.getNhanVien() == null ? "" : hoaDon.getNhanVien().getHoTen(),
                    hoaDon.getBan() == null ? "" : hoaDon.getBan().getTenBan(),
                    tinhTongSoLuong(items),
                    tinhTongTien(items),
                    isEditable(hoaDon)
            ));
        }

        return result;
    }

    @Override
    public List<ItemData> toItemDataList(HoaDon hoaDon) {
        List<ItemData> result = new ArrayList<>();
        if (hoaDon == null || hoaDon.getLstChiTietHoaDon() == null) {
            return result;
        }

        for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
            if (chiTietHD.getMonAn() != null && chiTietHD.getSoLuong() != null) {
                result.add(new ItemData(
                        chiTietHD.getMonAn().getMaMon(),
                        chiTietHD.getMonAn().getTenMon(),
                        chiTietHD.getMonAn().getDonGia(),
                        chiTietHD.getSoLuong()
                ));
            }
        }

        result.sort(Comparator.comparing(ItemData::maMon));
        return result;
    }

    @Override
    public List<ChiTietHDDto> toChiTietHDDtos(HoaDon hoaDon) {
        return toChiTietHDDtos(toItemDataList(hoaDon));
    }

    @Override
    public List<ChiTietHDDto> toChiTietHDDtos(List<ItemData> items) {
        List<ChiTietHDDto> result = new ArrayList<>();
        if (items == null) {
            return result;
        }

        for (ItemData item : items) {
            result.add(new ChiTietHDDto(
                    item.maMon(),
                    item.tenMon(),
                    item.soLuong(),
                    item.donGia() == null ? 0 : item.donGia()
            ));
        }

        return result;
    }

    @Override
    public List<ItemData> addItem(List<ItemData> currentItems, MonAn monAn, String soLuongText) {
        if (monAn == null) {
            throw new IllegalArgumentException("Vui lòng chọn món ăn.");
        }

        int soLuong = parseSoLuong(soLuongText);
        List<ItemData> result = saoChep(currentItems);

        if (timItemTheoMaMon(result, monAn.getMaMon()) != null) {
            throw new IllegalArgumentException("Món ăn này đã có trong hóa đơn.");
        }

        result.add(new ItemData(
                monAn.getMaMon(),
                monAn.getTenMon(),
                monAn.getDonGia(),
                soLuong
        ));

        result.sort(Comparator.comparing(ItemData::maMon));
        return result;
    }

    @Override
    public List<ItemData> updateItem(List<ItemData> currentItems, Long maMonCu, MonAn monAnMoi, String soLuongText) {
        if (maMonCu == null) {
            throw new IllegalArgumentException("Vui lòng chọn món cần cập nhật.");
        }
        if (monAnMoi == null) {
            throw new IllegalArgumentException("Vui lòng chọn món ăn.");
        }

        int soLuongMoi = parseSoLuong(soLuongText);
        List<ItemData> result = saoChep(currentItems);

        ItemData itemKhac = timItemTheoMaMon(result, monAnMoi.getMaMon());
        if (itemKhac != null && !maMonCu.equals(monAnMoi.getMaMon())) {
            throw new IllegalArgumentException("Món ăn này đã tồn tại trong hóa đơn.");
        }

        boolean found = false;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).maMon().equals(maMonCu)) {
                result.set(i, new ItemData(
                        monAnMoi.getMaMon(),
                        monAnMoi.getTenMon(),
                        monAnMoi.getDonGia(),
                        soLuongMoi
                ));
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("Không tìm thấy món cần cập nhật.");
        }

        result.sort(Comparator.comparing(ItemData::maMon));
        return result;
    }

    @Override
    public List<ItemData> removeItem(List<ItemData> currentItems, Long maMon) {
        if (maMon == null) {
            throw new IllegalArgumentException("Vui lòng chọn món cần xóa.");
        }

        List<ItemData> result = saoChep(currentItems);
        result.removeIf(item -> item.maMon().equals(maMon));
        return result;
    }

    @Override
    public int tinhTongSoLuong(List<ItemData> items) {
        int tong = 0;
        if (items == null) {
            return tong;
        }

        for (ItemData item : items) {
            tong += item.soLuong();
        }
        return tong;
    }

    @Override
    public double tinhTongTien(List<ItemData> items) {
        double tong = 0;
        if (items == null) {
            return tong;
        }

        for (ItemData item : items) {
            tong += item.thanhTien();
        }
        return tong;
    }

    @Override
    public double tinhTongTienHoaDon(HoaDon hoaDon) {
        return tinhTongTien(toItemDataList(hoaDon));
    }

    @Override
    public boolean isEditable(HoaDon hoaDon) {
        if (hoaDon == null) {
            return false;
        }

        HoaDonStatus trangThai = hoaDon.getTrangThai();
        return trangThai != HoaDonStatus.PAID && trangThai != HoaDonStatus.CANCELLED;
    }

    @Override
    public void validateEditable(HoaDon hoaDon) {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn.");
        }

        if (!isEditable(hoaDon)) {
            throw new IllegalArgumentException("Hóa đơn đã thanh toán hoặc đã hủy nên không thể chỉnh sửa hoặc xóa.");
        }
    }

    private void validateHeader(NhanVien nhanVien,
                                Ban ban,
                                HoaDonStatus trangThai,
                                List<ItemData> items) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("Không xác định được nhân viên đang đăng nhập.");
        }
        if (ban == null) {
            throw new IllegalArgumentException("Vui lòng chọn bàn.");
        }
        if (trangThai == null) {
            throw new IllegalArgumentException("Vui lòng chọn trạng thái hóa đơn.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng thêm ít nhất một món ăn vào hóa đơn.");
        }
    }

    private int parseSoLuong(String soLuongText) {
        if (soLuongText == null || soLuongText.isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập số lượng.");
        }

        try {
            int soLuong = Integer.parseInt(soLuongText.trim());
            if (soLuong <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
            }
            return soLuong;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Số lượng phải là số nguyên hợp lệ.");
        }
    }

    private List<ChiTietHD> taoDanhSachChiTietEntity(HoaDon hoaDon, List<ItemData> items) {
        List<ChiTietHD> result = new ArrayList<>();

        for (ItemData item : items) {
            MonAn monAn = monAnDAO.findById(item.maMon());
            if (monAn == null) {
                throw new IllegalArgumentException("Không tìm thấy món ăn với mã: " + item.maMon());
            }

            ChiTietHD chiTietHD = new ChiTietHD();
            chiTietHD.setHoaDon(hoaDon);
            chiTietHD.setMonAn(monAn);
            chiTietHD.setSoLuong(item.soLuong());
            result.add(chiTietHD);
        }

        return result;
    }

    private KhachHang timKhachHangTheoTenChinhXac(String tenKhachHang) {
        for (KhachHang khachHang : khachHangDAO.findByTenKh(tenKhachHang)) {
            if (tenKhachHang.equalsIgnoreCase(normalizeText(khachHang.getTenKh()))) {
                return khachHang;
            }
        }
        return null;
    }

    private LoaiKH layLoaiKhachHangMacDinh() {
        List<LoaiKH> loaiKhs = loaiKhachHangDAO.findAll();
        if (loaiKhs.isEmpty()) {
            throw new IllegalStateException(
                    "Không thể tạo khách hàng mới vì chưa có loại khách hàng nào trong hệ thống."
            );
        }
        return loaiKhs.get(0);
    }

    private ItemData timItemTheoMaMon(List<ItemData> items, Long maMon) {
        for (ItemData item : items) {
            if (item.maMon().equals(maMon)) {
                return item;
            }
        }
        return null;
    }

    private List<ItemData> saoChep(List<ItemData> items) {
        return items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    private String normalizeText(String value) {
        return value == null ? "" : value.trim();
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }
}
