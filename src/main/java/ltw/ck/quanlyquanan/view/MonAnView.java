package ltw.ck.quanlyquanan.view;

import ltw.ck.quanlyquanan.model.entity.LoaiMonAn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MonAnView extends BaseSubView {

    private final JTextField txtMaMon = new JTextField(20);
    private final JTextField txtTenMon = new JTextField(20);
    private final JTextField txtDonGia = new JTextField(20);
    private final JTextField txtTimKiem = new JTextField(20);

    private final JComboBox<LoaiMonAn> cboLoaiMonAn = new JComboBox<>();

    private final JButton btnTimKiem = new JButton("Tìm kiếm");
    private final JButton btnTaiLai = new JButton("Tải lại");
    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnCapNhat = new JButton("Cập nhật");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnLamMoi = new JButton("Làm mới");
    private final JButton btnDong = new JButton("Đóng");

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Mã món", "Tên món", "Đơn giá", "Loại món"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tblMonAn = new JTable(tableModel);

    public MonAnView() {

        txtMaMon.setEditable(false);

        JLabel lblTitle = new JLabel("QUẢN LÝ MÓN ĂN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm món ăn"));
        searchPanel.add(new JLabel("Từ khóa:"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        searchPanel.add(btnTaiLai);

        tblMonAn.setRowHeight(24);
        tblMonAn.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMonAn.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tblMonAn);
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin món ăn"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        addFormRow(formPanel, gbc, 0, "Mã món:", txtMaMon);
        addFormRow(formPanel, gbc, 1, "Tên món:", txtTenMon);
        addFormRow(formPanel, gbc, 2, "Đơn giá:", txtDonGia);
        addFormRow(formPanel, gbc, 3, "Loại món:", cboLoaiMonAn);

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
        splitPane.setDividerLocation(640);


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

    public JTextField getTxtMaMon() {
        return txtMaMon;
    }

    public JTextField getTxtTenMon() {
        return txtTenMon;
    }

    public JTextField getTxtDonGia() {
        return txtDonGia;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }

    public JComboBox<LoaiMonAn> getCboLoaiMonAn() {
        return cboLoaiMonAn;
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

    public JButton getBtnDong() {
        return btnDong;
    }

    public JTable getTblMonAn() {
        return tblMonAn;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public String getMaMonAn() {
        return txtMaMon.getText().trim();
    }

    public String getTenMonAn() {
        return txtTenMon.getText().trim();
    }

    public String getDonGia() {
        return txtDonGia.getText().trim();
    }

    public String getTuKhoaTimKiem() {
        return txtTimKiem.getText().trim();
    }

    public LoaiMonAn getLoaiMonAnDangChon() {
        return (LoaiMonAn) cboLoaiMonAn.getSelectedItem();
    }

    public void setFormData(String maMon, String tenMon, String donGia, LoaiMonAn loaiMonAn) {
        txtMaMon.setText(maMon);
        txtTenMon.setText(tenMon);
        txtDonGia.setText(donGia);
        cboLoaiMonAn.setSelectedItem(loaiMonAn);
    }

    public void clearForm() {
        setFormData("", "", "", null);
        tblMonAn.clearSelection();
        txtTenMon.requestFocus();
    }
}



