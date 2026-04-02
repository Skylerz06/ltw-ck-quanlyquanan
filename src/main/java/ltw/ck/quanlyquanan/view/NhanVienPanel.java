package ltw.ck.quanlyquanan.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NhanVienPanel extends JPanel {

    private final JTextField txtMaNV = new JTextField(20);
    private final JTextField txtHoTen = new JTextField(20);
    private final JTextField txtSdt = new JTextField(20);
    private final JTextField txtDiaChi = new JTextField(20);
    private final JTextField txtTenDangNhap = new JTextField(20);
    private final JPasswordField txtMatKhau = new JPasswordField(20);
    private final JTextField txtTimKiem = new JTextField(20);

    private final JButton btnTimKiem = new JButton("Tìm kiếm");
    private final JButton btnTaiLai = new JButton("Tải lại");
    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnCapNhat = new JButton("Cập nhật");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Mã NV", "Họ tên", "SĐT", "Địa chỉ", "Tên đăng nhập"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tblNhanVien = new JTable(tableModel);

    public NhanVienPanel() {
        setLayout(new BorderLayout());

        txtMaNV.setEditable(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm nhân viên"));
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        searchPanel.add(btnTaiLai);

        tblNhanVien.setRowHeight(24);
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        addFormRow(formPanel, gbc, 0, "Mã nhân viên:", txtMaNV);
        addFormRow(formPanel, gbc, 1, "Họ tên:", txtHoTen);
        addFormRow(formPanel, gbc, 2, "Số điện thoại:", txtSdt);
        addFormRow(formPanel, gbc, 3, "Địa chỉ:", txtDiaChi);
        addFormRow(formPanel, gbc, 4, "Tên đăng nhập:", txtTenDangNhap);
        addFormRow(formPanel, gbc, 5, "Mật khẩu:", txtMatKhau);

        JLabel lblNote = new JLabel("<html><i>Để tạo tài khoản, nhập đầy đủ tên đăng nhập và mật khẩu.</i></html>");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(lblNote, gbc);

        JPanel actionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Thao tác"));
        actionPanel.add(btnThem);
        actionPanel.add(btnCapNhat);
        actionPanel.add(btnXoa);
        actionPanel.add(btnLamMoi);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, rightPanel);
        splitPane.setResizeWeight(0.62);
        splitPane.setDividerLocation(650);


        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        contentPanel.add(splitPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
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

    public JTextField getTxtMaNV() {
        return txtMaNV;
    }

    public JTextField getTxtHoTen() {
        return txtHoTen;
    }

    public JTextField getTxtSdt() {
        return txtSdt;
    }

    public JTextField getTxtDiaChi() {
        return txtDiaChi;
    }

    public JTextField getTxtTenDangNhap() {
        return txtTenDangNhap;
    }

    public JPasswordField getTxtMatKhau() {
        return txtMatKhau;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JButton getBtnTimKiem() {
        return btnTimKiem;
    }

    public JButton getBtnTaiLai() {
        return btnTaiLai;
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnCapNhat() {
        return btnCapNhat;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public JButton getBtnLamMoi() {
        return btnLamMoi;
    }

    public JTable getTblNhanVien() {
        return tblNhanVien;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public String getMaNhanVien() {
        return txtMaNV.getText().trim();
    }

    public String getHoTen() {
        return txtHoTen.getText().trim();
    }

    public String getSdt() {
        return txtSdt.getText().trim();
    }

    public String getDiaChi() {
        return txtDiaChi.getText().trim();
    }

    public String getTenDangNhap() {
        return txtTenDangNhap.getText().trim();
    }

    public String getMatKhau() {
        return new String(txtMatKhau.getPassword()).trim();
    }

    public String getTuKhoaTimKiem() {
        return txtTimKiem.getText().trim();
    }

    public void setFormData(String maNV, String hoTen, String sdt, String diaChi, String tenDangNhap, String matKhau) {
        txtMaNV.setText(maNV);
        txtHoTen.setText(hoTen);
        txtSdt.setText(sdt);
        txtDiaChi.setText(diaChi);
        txtTenDangNhap.setText(tenDangNhap);
        txtMatKhau.setText(matKhau);
    }

    public void clearForm() {
        setFormData("", "", "", "", "", "");
        tblNhanVien.clearSelection();
        txtHoTen.requestFocus();
    }
}



