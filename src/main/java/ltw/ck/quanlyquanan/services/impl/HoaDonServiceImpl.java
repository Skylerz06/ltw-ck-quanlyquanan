package ltw.ck.quanlyquanan.services.impl;

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
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;
import ltw.ck.quanlyquanan.services.HoaDonItemData;
import ltw.ck.quanlyquanan.services.HoaDonService;
import ltw.ck.quanlyquanan.services.ServiceException;

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
    private final NhanVienDAO nhanVienDAO;
    private final BanDAO banDAO;
    private final MonAnDAO monAnDAO;

    public HoaDonServiceImpl() {
        this(
                new HoaDonDAOImpl(),
                new KhachHangDAOImpl(),
                new NhanVienDAOImpl(),
                new BanDAOImpl(),
                new MonAnDAOImpl()
        );
    }

    public HoaDonServiceImpl(HoaDonDAO hoaDonDAO,
                             KhachHangDAO khachHangDAO,
                             NhanVienDAO nhanVienDAO,
                             BanDAO banDAO,
                             MonAnDAO monAnDAO) {
        this.hoaDonDAO = hoaDonDAO;
        this.khachHangDAO = khachHangDAO;
        this.nhanVienDAO = nhanVienDAO;
        this.banDAO = banDAO;
        this.monAnDAO = monAnDAO;
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
    public List<KhachHang> findAllKhachHang() {
        return new ArrayList<>(khachHangDAO.findAll());
    }

    @Override
    public List<NhanVien> findAllNhanVien() {
        return new ArrayList<>(nhanVienDAO.findAll());
    }

    @Override
    public List<Ban> findAllBan() {
        return new ArrayList<>(banDAO.findAll());
    }

    @Override
    public List<MonAn> findAllMonAn() {
        return new ArrayList<>(monAnDAO.findAll());
    }

    @Override
    public HoaDon findById(Long maHd) {
        if (maHd == null) {
            throw new ServiceException("Không tìm thấy hóa đơn.");
        }
        return hoaDonDAO.findById(maHd);
    }

    @Override
    public void delete(Long maHd) {
        if (maHd == null) {
            throw new ServiceException("Vui lòng chọn hóa đơn cần xóa.");
        }
        hoaDonDAO.delete(maHd);
    }

    @Override
    public HoaDon create(KhachHang khachHang,
                         NhanVien nhanVien,
                         Ban ban,
                         HoaDonStatus trangThai,
                         List<HoaDonItemData> items) {
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
                         List<HoaDonItemData> items) {
        if (maHd == null) {
            throw new ServiceException("Vui lòng chọn hóa đơn cần cập nhật.");
        }

        HoaDon hoaDon = hoaDonDAO.findById(maHd);
        if (hoaDon == null) {
            throw new ServiceException("Không tìm thấy hóa đơn cần cập nhật.");
        }

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
    public List<HoaDonItemData> toItemDataList(HoaDon hoaDon) {
        List<HoaDonItemData> result = new ArrayList<>();
        if (hoaDon == null || hoaDon.getLstChiTietHoaDon() == null) {
            return result;
        }

        for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
            if (chiTietHD.getMonAn() != null && chiTietHD.getSoLuong() != null) {
                result.add(new HoaDonItemData(
                        chiTietHD.getMonAn().getMaMon(),
                        chiTietHD.getMonAn().getTenMon(),
                        chiTietHD.getMonAn().getDonGia(),
                        chiTietHD.getSoLuong()
                ));
            }
        }

        result.sort(Comparator.comparing(HoaDonItemData::maMon));
        return result;
    }

    @Override
    public List<HoaDonItemData> addItem(List<HoaDonItemData> currentItems, MonAn monAn, String soLuongText) {
        if (monAn == null) {
            throw new ServiceException("Vui lòng chọn món ăn.");
        }

        int soLuong = parseSoLuong(soLuongText);
        List<HoaDonItemData> result = saoChep(currentItems);

        if (timItemTheoMaMon(result, monAn.getMaMon()) != null) {
            throw new ServiceException("Món ăn này đã có trong hóa đơn.");
        }

        result.add(new HoaDonItemData(
                monAn.getMaMon(),
                monAn.getTenMon(),
                monAn.getDonGia(),
                soLuong
        ));

        result.sort(Comparator.comparing(HoaDonItemData::maMon));
        return result;
    }

    @Override
    public List<HoaDonItemData> updateItem(List<HoaDonItemData> currentItems, Long maMonCu, MonAn monAnMoi, String soLuongText) {
        if (maMonCu == null) {
            throw new ServiceException("Vui lòng chọn món cần cập nhật.");
        }
        if (monAnMoi == null) {
            throw new ServiceException("Vui lòng chọn món ăn.");
        }

        int soLuongMoi = parseSoLuong(soLuongText);
        List<HoaDonItemData> result = saoChep(currentItems);

        HoaDonItemData itemKhac = timItemTheoMaMon(result, monAnMoi.getMaMon());
        if (itemKhac != null && !maMonCu.equals(monAnMoi.getMaMon())) {
            throw new ServiceException("Món ăn này đã tồn tại trong hóa đơn.");
        }

        boolean found = false;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).maMon().equals(maMonCu)) {
                result.set(i, new HoaDonItemData(
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
            throw new ServiceException("Không tìm thấy món cần cập nhật.");
        }

        result.sort(Comparator.comparing(HoaDonItemData::maMon));
        return result;
    }

    @Override
    public List<HoaDonItemData> removeItem(List<HoaDonItemData> currentItems, Long maMon) {
        if (maMon == null) {
            throw new ServiceException("Vui lòng chọn món cần xóa.");
        }

        List<HoaDonItemData> result = saoChep(currentItems);
        result.removeIf(item -> item.maMon().equals(maMon));
        return result;
    }

    @Override
    public int tinhTongSoLuong(List<HoaDonItemData> items) {
        int tong = 0;
        if (items == null) return tong;

        for (HoaDonItemData item : items) {
            tong += item.soLuong();
        }
        return tong;
    }

    @Override
    public double tinhTongTien(List<HoaDonItemData> items) {
        double tong = 0;
        if (items == null) return tong;

        for (HoaDonItemData item : items) {
            tong += item.thanhTien();
        }
        return tong;
    }

    @Override
    public double tinhTongTienHoaDon(HoaDon hoaDon) {
        if (hoaDon == null || hoaDon.getLstChiTietHoaDon() == null) {
            return 0;
        }

        double tong = 0;
        for (ChiTietHD chiTietHD : hoaDon.getLstChiTietHoaDon()) {
            if (chiTietHD.getMonAn() != null
                    && chiTietHD.getMonAn().getDonGia() != null
                    && chiTietHD.getSoLuong() != null) {
                tong += chiTietHD.getMonAn().getDonGia() * chiTietHD.getSoLuong();
            }
        }
        return tong;
    }

    private void validateHeader(NhanVien nhanVien,
                                Ban ban,
                                HoaDonStatus trangThai,
                                List<HoaDonItemData> items) {
        if (nhanVien == null) {
            throw new ServiceException("Không xác định được nhân viên đang đăng nhập.");
        }
        if (ban == null) {
            throw new ServiceException("Vui lòng chọn bàn.");
        }
        if (trangThai == null) {
            throw new ServiceException("Vui lòng chọn trạng thái hóa đơn.");
        }
        if (items == null || items.isEmpty()) {
            throw new ServiceException("Vui lòng thêm ít nhất một món ăn vào hóa đơn.");
        }
    }

    private int parseSoLuong(String soLuongText) {
        if (soLuongText == null || soLuongText.isBlank()) {
            throw new ServiceException("Vui lòng nhập số lượng.");
        }

        try {
            int soLuong = Integer.parseInt(soLuongText.trim());
            if (soLuong <= 0) {
                throw new ServiceException("Số lượng phải lớn hơn 0.");
            }
            return soLuong;
        } catch (NumberFormatException ex) {
            throw new ServiceException("Số lượng phải là số nguyên hợp lệ.");
        }
    }

    private List<ChiTietHD> taoDanhSachChiTietEntity(HoaDon hoaDon, List<HoaDonItemData> items) {
        List<ChiTietHD> result = new ArrayList<>();

        for (HoaDonItemData item : items) {
            MonAn monAn = monAnDAO.findById(item.maMon());
            if (monAn == null) {
                throw new ServiceException("Không tìm thấy món ăn với mã: " + item.maMon());
            }

            ChiTietHD chiTietHD = new ChiTietHD();
            chiTietHD.setHoaDon(hoaDon);
            chiTietHD.setMonAn(monAn);
            chiTietHD.setSoLuong(item.soLuong());
            result.add(chiTietHD);
        }

        return result;
    }

    private HoaDonItemData timItemTheoMaMon(List<HoaDonItemData> items, Long maMon) {
        for (HoaDonItemData item : items) {
            if (item.maMon().equals(maMon)) {
                return item;
            }
        }
        return null;
    }

    private List<HoaDonItemData> saoChep(List<HoaDonItemData> items) {
        return items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }
}