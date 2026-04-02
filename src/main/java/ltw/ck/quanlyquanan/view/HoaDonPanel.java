package ltw.ck.quanlyquanan.view;

import ltw.ck.quanlyquanan.model.entity.Ban;
import ltw.ck.quanlyquanan.model.entity.KhachHang;
import ltw.ck.quanlyquanan.model.entity.MonAn;
import ltw.ck.quanlyquanan.model.entity.NhanVien;
import ltw.ck.quanlyquanan.model.enums.HoaDonStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HoaDonPanel extends JPanel {

    public static final String CARD_LAP_HOA_DON = "lapHoaDon";
    public static final String CARD_LICH_SU = "lichSuHoaDon";

    private final JButton btnTabLapHoaDon = new JButton("Lập hóa đơn");
    private final JButton btnTabLichSu = new JButton("Lịch sử hóa đơn");

    private final JPanel cardPanel = new JPanel(new CardLayout());

    private final JTextField txtMaHD = new JTextField(18);
    private final JTextField txtNgayLap = new JTextField(18);
    private final JTextField txtSoLuong = new JTextField("1", 8);
    private final JTextField txtTimKiem = new JTextField(20);

    private final JComboBox<KhachHang> cboKhachHang = new JComboBox<>();
    private final JComboBox<NhanVien> cboNhanVien = new JComboBox<>();
    private final JComboBox<Ban> cboBan = new JComboBox<>();
    private final JComboBox<MonAn> cboMonAn = new JComboBox<>();
    private final JComboBox<HoaDonStatus> cboTrangThai = new JComboBox<>(HoaDonStatus.values());

    private final JButton btnThemMon = new JButton("Thêm món");
    private final JButton btnCapNhatMon = new JButton("Cập nhật món");
    private final JButton btnXoaMon = new JButton("Xóa món");

    private final JButton btnThemHoaDon = new JButton("Thêm hóa đơn");
    private final JButton btnCapNhatHoaDon = new JButton("Cập nhật hóa đơn");
    private final JButton btnLamMoiForm = new JButton("Làm mới");

    private final JButton btnTimKiem = new JButton("Tìm kiếm");
    private final JButton btnTaiLai = new JButton("Tải lại");
    private final JButton btnChinhSua = new JButton("Chỉnh sửa");
    private final JButton btnXoaHoaDon = new JButton("Xóa hóa đơn");

    private final JLabel lblTongTienForm = new JLabel("Tổng tiền: 0 đ");
    private final JLabel lblTongTienLichSu = new JLabel("Tổng tiền: 0 đ");

    private final DefaultTableModel chiTietFormTableModel = new DefaultTableModel(
            new Object[]{"Mã món", "Tên món", "Đơn giá", "Số lượng", "Thành tiền"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel hoaDonTableModel = new DefaultTableModel(
            new Object[]{"Mã HD", "Ngày lập", "Trạng thái", "Khách hàng", "Nhân viên", "Bàn", "Số món", "Tổng tiền"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel chiTietLichSuTableModel = new DefaultTableModel(
            new Object[]{"Mã món", "Tên món", "Đơn giá", "Số lượng", "Thành tiền"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tblChiTietForm = new JTable(chiTietFormTableModel);
    private final JTable tblHoaDon = new JTable(hoaDonTableModel);
    private final JTable tblChiTietLichSu = new JTable(chiTietLichSuTableModel);

    public HoaDonPanel() {
        setLayout(new BorderLayout());

        txtMaHD.setEditable(false);
        txtNgayLap.setEditable(false);
        cboTrangThai.setEnabled(false);

        JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(createTabPanel(), BorderLayout.SOUTH);

        cardPanel.add(createLapHoaDonPanel(), CARD_LAP_HOA_DON);
        cardPanel.add(createLichSuPanel(), CARD_LICH_SU);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        contentPanel.add(cardPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        configTables();
        showCard(CARD_LAP_HOA_DON);
    }

    private JPanel createTabPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.add(btnTabLapHoaDon);
        panel.add(btnTabLichSu);
        return panel;
    }

    private JPanel createLapHoaDonPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Thao tác hóa đơn"));
        actionPanel.add(btnThemHoaDon);
        actionPanel.add(btnCapNhatHoaDon);
        actionPanel.add(btnLamMoiForm);

        JPanel hoaDonInfoPanel = new JPanel(new GridBagLayout());
        hoaDonInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        GridBagConstraints gbc = createGbc();
        addFormRow(hoaDonInfoPanel, gbc, 0, "Mã hóa đơn:", txtMaHD);
        addFormRow(hoaDonInfoPanel, gbc, 1, "Ngày lập:", txtNgayLap);
        addFormRow(hoaDonInfoPanel, gbc, 2, "Trạng thái:", cboTrangThai);
        addFormRow(hoaDonInfoPanel, gbc, 3, "Khách hàng:", cboKhachHang);
        addFormRow(hoaDonInfoPanel, gbc, 4, "Nhân viên:", cboNhanVien);
        addFormRow(hoaDonInfoPanel, gbc, 5, "Bàn:", cboBan);

        JLabel lblKhachLe = new JLabel("<html><i>Có thể bỏ trống khách hàng cho hóa đơn khách lẻ.</i></html>");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        hoaDonInfoPanel.add(lblKhachLe, gbc);

        JPanel monAnPanel = new JPanel(new GridBagLayout());
        monAnPanel.setBorder(BorderFactory.createTitledBorder("Nhập món ăn"));

        GridBagConstraints itemGbc = createGbc();
        addFormRow(monAnPanel, itemGbc, 0, "Món ăn:", cboMonAn);
        addFormRow(monAnPanel, itemGbc, 1, "Số lượng:", txtSoLuong);

        JPanel itemActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        itemActionPanel.add(btnThemMon);
        itemActionPanel.add(btnCapNhatMon);
        itemActionPanel.add(btnXoaMon);

        itemGbc.gridx = 0;
        itemGbc.gridy = 2;
        itemGbc.gridwidth = 2;
        monAnPanel.add(itemActionPanel, itemGbc);

        JPanel leftPanel = new JPanel(new BorderLayout(0, 12));
        leftPanel.add(hoaDonInfoPanel, BorderLayout.NORTH);
        leftPanel.add(monAnPanel, BorderLayout.CENTER);

        JPanel chiTietFormPanel = new JPanel(new BorderLayout(0, 10));
        chiTietFormPanel.setBorder(BorderFactory.createTitledBorder("Danh sách món đã chọn"));
        chiTietFormPanel.add(new JScrollPane(tblChiTietForm), BorderLayout.CENTER);

        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        lblTongTienForm.setFont(new Font("Arial", Font.BOLD, 16));
        tongTienPanel.add(lblTongTienForm);
        chiTietFormPanel.add(tongTienPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, chiTietFormPanel);
        splitPane.setResizeWeight(0.43);
        splitPane.setDividerLocation(530);

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.add(actionPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLichSuPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm hóa đơn"));
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        searchPanel.add(btnTaiLai);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Thao tác lịch sử"));
        actionPanel.add(btnChinhSua);
        actionPanel.add(btnXoaHoaDon);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        JPanel hoaDonTablePanel = new JPanel(new BorderLayout());
        hoaDonTablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
        hoaDonTablePanel.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        JPanel detailPanel = new JPanel(new BorderLayout(0, 10));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn đã chọn"));
        detailPanel.add(new JScrollPane(tblChiTietLichSu), BorderLayout.CENTER);

        JPanel tongTienPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        lblTongTienLichSu.setFont(new Font("Arial", Font.BOLD, 16));
        tongTienPanel.add(lblTongTienLichSu);
        detailPanel.add(tongTienPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, hoaDonTablePanel, detailPanel);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerLocation(700);

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private void configTables() {
        tblChiTietForm.setRowHeight(24);
        tblChiTietForm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChiTietForm.setAutoCreateRowSorter(true);

        tblHoaDon.setRowHeight(24);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.setAutoCreateRowSorter(true);

        tblChiTietLichSu.setRowHeight(24);
        tblChiTietLichSu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChiTietLichSu.setAutoCreateRowSorter(true);
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    public void showCard(String cardName) {
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, cardName);
        boolean lapHoaDonActive = CARD_LAP_HOA_DON.equals(cardName);
        btnTabLapHoaDon.setEnabled(!lapHoaDonActive);
        btnTabLichSu.setEnabled(lapHoaDonActive);
    }

    public JButton getBtnTabLapHoaDon() {
        return btnTabLapHoaDon;
    }

    public JButton getBtnTabLichSu() {
        return btnTabLichSu;
    }

    public JTextField getTxtNgayLap() {
        return txtNgayLap;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JTextField getTxtSoLuong() {
        return txtSoLuong;
    }

    public JComboBox<KhachHang> getCboKhachHang() {
        return cboKhachHang;
    }

    public JComboBox<NhanVien> getCboNhanVien() {
        return cboNhanVien;
    }

    public JComboBox<Ban> getCboBan() {
        return cboBan;
    }

    public JComboBox<MonAn> getCboMonAn() {
        return cboMonAn;
    }

    public JComboBox<HoaDonStatus> getCboTrangThai() {
        return cboTrangThai;
    }

    public JButton getBtnThemMon() {
        return btnThemMon;
    }

    public JButton getBtnCapNhatMon() {
        return btnCapNhatMon;
    }

    public JButton getBtnXoaMon() {
        return btnXoaMon;
    }

    public JButton getBtnThemHoaDon() {
        return btnThemHoaDon;
    }

    public JButton getBtnCapNhatHoaDon() {
        return btnCapNhatHoaDon;
    }

    public JButton getBtnLamMoiForm() {
        return btnLamMoiForm;
    }

    public JButton getBtnTimKiem() {
        return btnTimKiem;
    }

    public JButton getBtnTaiLai() {
        return btnTaiLai;
    }

    public JButton getBtnChinhSua() {
        return btnChinhSua;
    }

    public JButton getBtnXoaHoaDon() {
        return btnXoaHoaDon;
    }

    public JLabel getLblTongTienForm() {
        return lblTongTienForm;
    }

    public JLabel getLblTongTienLichSu() {
        return lblTongTienLichSu;
    }

    public JTable getTblChiTietForm() {
        return tblChiTietForm;
    }

    public JTable getTblHoaDon() {
        return tblHoaDon;
    }

    public JTable getTblChiTietLichSu() {
        return tblChiTietLichSu;
    }

    public DefaultTableModel getChiTietFormTableModel() {
        return chiTietFormTableModel;
    }

    public DefaultTableModel getHoaDonTableModel() {
        return hoaDonTableModel;
    }

    public DefaultTableModel getChiTietLichSuTableModel() {
        return chiTietLichSuTableModel;
    }

    public String getMaHoaDon() {
        return txtMaHD.getText().trim();
    }

    public void setMaHoaDon(String maHoaDon) {
        txtMaHD.setText(maHoaDon);
    }

    public String getTuKhoaTimKiem() {
        return txtTimKiem.getText().trim();
    }

    public String getSoLuong() {
        return txtSoLuong.getText().trim();
    }

    public void setNgayLap(String ngayLap) {
        txtNgayLap.setText(ngayLap);
    }

    public void setSoLuong(String soLuong) {
        txtSoLuong.setText(soLuong);
    }

    public KhachHang getKhachHangDangChon() {
        return (KhachHang) cboKhachHang.getSelectedItem();
    }

    public NhanVien getNhanVienDangChon() {
        return (NhanVien) cboNhanVien.getSelectedItem();
    }

    public Ban getBanDangChon() {
        return (Ban) cboBan.getSelectedItem();
    }

    public MonAn getMonAnDangChon() {
        return (MonAn) cboMonAn.getSelectedItem();
    }

    public HoaDonStatus getTrangThaiDangChon() {
        return (HoaDonStatus) cboTrangThai.getSelectedItem();
    }

    public void setTrangThai(HoaDonStatus trangThai) {
        cboTrangThai.setSelectedItem(trangThai);
    }

    public void setTrangThaiEditable(boolean editable) {
        cboTrangThai.setEnabled(editable);
    }

    public void clearHoaDonForm() {
        txtMaHD.setText("");
        txtNgayLap.setText("");
        clearMonAnForm();
        txtSoLuong.setText("1");
        cboKhachHang.setSelectedItem(null);
        cboBan.setSelectedItem(null);
        cboTrangThai.setSelectedItem(HoaDonStatus.CREATED);
        cboTrangThai.setEnabled(false);
        cboKhachHang.requestFocus();
    }

    public void clearMonAnForm() {
        cboMonAn.setSelectedItem(null);
        txtSoLuong.setText("1");
        tblChiTietForm.clearSelection();
    }

    public void clearLichSuSelection() {
        tblHoaDon.clearSelection();
        tblChiTietLichSu.clearSelection();
    }
}


