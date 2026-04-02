package ltw.ck.quanlyquanan.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class ThongKePanel extends JPanel {

    public static final String CARD_TONG_QUAN = "tongQuan";
    public static final String CARD_MON_AN = "monAn";

    private final JButton btnTabTongQuan = new JButton("Tổng quan");
    private final JButton btnTabMonAn = new JButton("Món ăn bán chạy");

    private final JSpinner spnTuNgay = new JSpinner(new SpinnerDateModel());
    private final JSpinner spnDenNgay = new JSpinner(new SpinnerDateModel());

    private final JButton btnThongKe = new JButton("Thống kê");
    private final JButton btnLamMoi = new JButton("Làm mới");

    private final JLabel lblTongHoaDon = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lblTongDoanhThu = new JLabel("0 đ", SwingConstants.CENTER);
    private final JLabel lblHoaDonHomNay = new JLabel("0", SwingConstants.CENTER);

    private final JPanel cardPanel = new JPanel(new CardLayout());

    private final DefaultTableModel hoaDonTableModel = new DefaultTableModel(
            new Object[]{"Mã HD", "Ngày lập", "Khách hàng", "Nhân viên", "Bàn", "Số món", "Tổng tiền"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel monAnTableModel = new DefaultTableModel(
            new Object[]{"Top", "Tên món", "Số lượng bán"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tblHoaDon = new JTable(hoaDonTableModel);
    private final JTable tblMonAn = new JTable(monAnTableModel);

    public ThongKePanel() {
        setLayout(new BorderLayout());

        JSpinner.DateEditor tuNgayEditor = new JSpinner.DateEditor(spnTuNgay, "dd/MM/yyyy");
        spnTuNgay.setEditor(tuNgayEditor);
        JSpinner.DateEditor denNgayEditor = new JSpinner.DateEditor(spnDenNgay, "dd/MM/yyyy");
        spnDenNgay.setEditor(denNgayEditor);

        JLabel lblTitle = new JLabel("THỐNG KÊ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(createTopBar(), BorderLayout.SOUTH);

        cardPanel.add(createTongQuanPanel(), CARD_TONG_QUAN);
        cardPanel.add(createMonAnPanel(), CARD_MON_AN);


        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        contentPanel.add(cardPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        configTables();
        macDinhNgay();
        showCard(CARD_TONG_QUAN);
    }

    private JPanel createTopBar() {
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));

        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabPanel.add(btnTabTongQuan);
        tabPanel.add(btnTabMonAn);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.add(new JLabel("Từ ngày:"));
        filterPanel.add(spnTuNgay);
        filterPanel.add(new JLabel("Đến ngày:"));
        filterPanel.add(spnDenNgay);
        filterPanel.add(btnThongKe);
        filterPanel.add(btnLamMoi);

        wrapper.add(tabPanel, BorderLayout.WEST);
        wrapper.add(filterPanel, BorderLayout.EAST);
        return wrapper;
    }

    private JPanel createTongQuanPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        summaryPanel.add(createStatCard("Tổng hóa đơn", lblTongHoaDon));
        summaryPanel.add(createStatCard("Tổng doanh thu", lblTongDoanhThu));
        summaryPanel.add(createStatCard("Hóa đơn hôm nay", lblHoaDonHomNay));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn trong khoảng thống kê"));
        tablePanel.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.add(summaryPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMonAnPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Top món ăn bán chạy"));
        panel.add(new JScrollPane(tblMonAn), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private void configTables() {
        tblHoaDon.setRowHeight(24);
        tblHoaDon.setAutoCreateRowSorter(true);
        tblMonAn.setRowHeight(24);
        tblMonAn.setAutoCreateRowSorter(true);
    }

    public void macDinhNgay() {
        Date now = new Date();
        spnTuNgay.setValue(now);
        spnDenNgay.setValue(now);
    }

    public void showCard(String cardName) {
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, cardName);
        boolean tongQuanActive = CARD_TONG_QUAN.equals(cardName);
        btnTabTongQuan.setEnabled(!tongQuanActive);
        btnTabMonAn.setEnabled(tongQuanActive);
    }

    public JButton getBtnTabTongQuan() {
        return btnTabTongQuan;
    }

    public JButton getBtnTabMonAn() {
        return btnTabMonAn;
    }

    public JSpinner getSpnTuNgay() {
        return spnTuNgay;
    }

    public JSpinner getSpnDenNgay() {
        return spnDenNgay;
    }

    public JButton getBtnThongKe() {
        return btnThongKe;
    }

    public JButton getBtnLamMoi() {
        return btnLamMoi;
    }

    public JLabel getLblTongHoaDon() {
        return lblTongHoaDon;
    }

    public JLabel getLblTongDoanhThu() {
        return lblTongDoanhThu;
    }

    public JLabel getLblHoaDonHomNay() {
        return lblHoaDonHomNay;
    }

    public JTable getTblHoaDon() {
        return tblHoaDon;
    }

    public JTable getTblMonAn() {
        return tblMonAn;
    }

    public DefaultTableModel getHoaDonTableModel() {
        return hoaDonTableModel;
    }

    public DefaultTableModel getMonAnTableModel() {
        return monAnTableModel;
    }
}




