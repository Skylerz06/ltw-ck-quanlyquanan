package ltw.ck.quanlyquanan.view;

import ltw.ck.quanlyquanan.model.entity.LoaiKH;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KhachHangPanel extends JPanel {

    private final JTextField txtMaKH = new JTextField(20);
    private final JTextField txtTenKH = new JTextField(20);
    private final JTextField txtTimKiem = new JTextField(20);

    private final JComboBox<LoaiKH> cboLoaiKH = new JComboBox<>();

    private final JButton btnTimKiem = new JButton("Tìm kiếm");
    private final JButton btnTaiLai = new JButton("Tải lại");
    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnCapNhat = new JButton("Cập nhật");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Mã KH", "Tên khách hàng", "Loại khách hàng"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tblKhachHang = new JTable(tableModel);

    public KhachHangPanel() {
        setLayout(new BorderLayout());
        
        txtMaKH.setEditable(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm khách hàng"));
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        searchPanel.add(btnTaiLai);

        tblKhachHang.setRowHeight(24);
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhachHang.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        addFormRow(formPanel, gbc, 0, "Mã khách hàng:", txtMaKH);
        addFormRow(formPanel, gbc, 1, "Tên khách hàng:", txtTenKH);
        addFormRow(formPanel, gbc, 2, "Loại khách hàng:", cboLoaiKH);

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
        splitPane.setDividerLocation(620);


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

    public JTextField getTxtMaKH() {
        return txtMaKH;
    }

    public JTextField getTxtTenKH() {
        return txtTenKH;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JComboBox<LoaiKH> getCboLoaiKH() {
        return cboLoaiKH;
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

    public JTable getTblKhachHang() {
        return tblKhachHang;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public String getMaKhachHang() {
        return txtMaKH.getText().trim();
    }

    public String getTenKhachHang() {
        return txtTenKH.getText().trim();
    }

    public String getTuKhoaTimKiem() {
        return txtTimKiem.getText().trim();
    }

    public LoaiKH getLoaiKhachHangDangChon() {
        return (LoaiKH) cboLoaiKH.getSelectedItem();
    }

    public void setFormData(String maKH, String tenKH, LoaiKH loaiKH) {
        txtMaKH.setText(maKH);
        txtTenKH.setText(tenKH);
        cboLoaiKH.setSelectedItem(loaiKH);
    }

    public void clearForm() {
        setFormData("", "", null);
        tblKhachHang.clearSelection();
        txtTenKH.requestFocus();
    }
}



