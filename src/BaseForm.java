import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseForm extends JFrame {

    protected JTextField txtName, txtPhone, txtAddress;
    protected JTable itemTable;
    protected DefaultTableModel tableModel;
    protected JTextField txtDiscount, txtAdvance;
    protected JTextArea txtNotes;
    protected String docType;
    private JLabel statusLabel;
    private boolean updating = false;

    public BaseForm(String title, String docType) {
        this.docType = docType;
        setTitle(title);
        setSize(1080, 820);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Main.BG_DARK);

        add(buildHeader(title), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(Main.BG_DARK);
        main.setBorder(new EmptyBorder(15, 20, 10, 20));

        JPanel topSection = new JPanel(new BorderLayout(12, 12));
        topSection.setOpaque(false);
        topSection.add(buildCustomerCard(), BorderLayout.NORTH);
        topSection.add(buildItemsCard(), BorderLayout.CENTER);

        main.add(topSection, BorderLayout.CENTER);
        main.add(buildBottomCard(), BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });

        tableModel.addRow(new Object[]{"1", "", "1", "0", "0.00"});
        updateStatus();
    }

    // ─── Header ────────────────────────────────────────────────────────
    private JPanel buildHeader(String title) {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 40, 90),
                        getWidth(), 0, new Color(13, 71, 161)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(1080, 76));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(0, 15, 0, 15));

        JButton btnBack = makeHeaderButton("←  Back");
        btnBack.addActionListener(e -> goBack());

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel badge = new JLabel(docType.toUpperCase());
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(new Color(82, 156, 255, 120));
        badge.setBorder(new EmptyBorder(7, 16, 7, 16));

        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 22));
        leftWrap.setOpaque(false);
        leftWrap.add(btnBack);

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 24));
        rightWrap.setOpaque(false);
        rightWrap.add(badge);

        header.add(leftWrap, BorderLayout.WEST);
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(rightWrap, BorderLayout.EAST);

        return header;
    }

    private JButton makeHeaderButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255, 255, 255, 40));
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(255, 255, 255, 80)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(255, 255, 255, 40)); }
        });
        return btn;
    }

    // ─── Card factory ──────────────────────────────────────────────────
    private JPanel createCard(String title, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, 14, 14);
                g2.setColor(Main.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 14, 14);
                g2.setColor(Main.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 15, 15, 15),
                icon + "  " + title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                Main.PRIMARY_LIGHT
        ));
        return card;
    }

    // ─── Customer Card ─────────────────────────────────────────────────
    private JPanel buildCustomerCard() {
        JPanel card = createCard("Customer Details", "C");
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lName = makeLabel("Name *");
        JLabel lPhone = makeLabel("Phone");
        JLabel lAddress = makeLabel("Address");

        txtName = makeTextField();
        txtPhone = makeTextField();
        txtAddress = makeTextField();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        card.add(lName, gbc);
        gbc.gridx = 1; gbc.weightx = 2;
        card.add(txtName, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        card.add(lPhone, gbc);
        gbc.gridx = 3; gbc.weightx = 1.5;
        card.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        card.add(lAddress, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        card.add(txtAddress, gbc);

        return card;
    }

    // ─── Items Card ────────────────────────────────────────────────────
    private JPanel buildItemsCard() {
        JPanel card = createCard("Items / Services", "L");
        card.setLayout(new BorderLayout(10, 10));

        String[] cols = {"#", "Description", "Qty", "Unit Price (Rs.)", "Amount (Rs.)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 0 && col != 4;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (updating) { super.setValueAt(value, row, col); return; }
                super.setValueAt(value, row, col);
                if (col == 2 || col == 3) recalcRow(row);
                updateStatus();
            }
        };

        itemTable = new JTable(tableModel);
        itemTable.setRowHeight(34);
        itemTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemTable.setBackground(Main.BG_CARD);
        itemTable.setForeground(Main.TEXT_PRIMARY);
        itemTable.setGridColor(Main.BORDER);
        itemTable.setShowGrid(true);
        itemTable.setIntercellSpacing(new Dimension(0, 1));
        itemTable.setSelectionBackground(new Color(41, 121, 255, 130));
        itemTable.setSelectionForeground(Color.WHITE);
        itemTable.setFillsViewportHeight(true);

        itemTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        itemTable.getColumnModel().getColumn(1).setPreferredWidth(360);
        itemTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        itemTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        itemTable.getColumnModel().getColumn(4).setPreferredWidth(130);

        JTableHeader header = itemTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Main.BG_ELEVATED);
        header.setForeground(Main.TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Main.BORDER));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(Main.BG_ELEVATED);
                lbl.setForeground(Main.TEXT_PRIMARY);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 0, 1, Main.BORDER),
                        new EmptyBorder(0, 8, 0, 8)
                ));
                return lbl;
            }
        });

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        centerR.setBackground(Main.BG_CARD);
        centerR.setForeground(Main.TEXT_PRIMARY);

        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(SwingConstants.RIGHT);
        rightR.setBackground(Main.BG_CARD);
        rightR.setForeground(Main.TEXT_PRIMARY);
        rightR.setBorder(new EmptyBorder(0, 0, 0, 12));

        DefaultTableCellRenderer leftR = new DefaultTableCellRenderer();
        leftR.setHorizontalAlignment(SwingConstants.LEFT);
        leftR.setBackground(Main.BG_CARD);
        leftR.setForeground(Main.TEXT_PRIMARY);
        leftR.setBorder(new EmptyBorder(0, 10, 0, 0));

        itemTable.getColumnModel().getColumn(0).setCellRenderer(centerR);
        itemTable.getColumnModel().getColumn(1).setCellRenderer(leftR);
        itemTable.getColumnModel().getColumn(2).setCellRenderer(centerR);
        itemTable.getColumnModel().getColumn(3).setCellRenderer(rightR);
        itemTable.getColumnModel().getColumn(4).setCellRenderer(rightR);

        JScrollPane scroll = new JScrollPane(itemTable);
        scroll.setBorder(BorderFactory.createLineBorder(Main.BORDER));
        scroll.getViewport().setBackground(Main.BG_CARD);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnBar.setOpaque(false);

        JButton btnAdd = makeOutlineButton("+  Add Item", Main.PRIMARY_LIGHT);
        JButton btnDel = makeOutlineButton("-  Remove", Main.DANGER);

        btnAdd.addActionListener(e -> {
            tableModel.addRow(new Object[]{String.valueOf(tableModel.getRowCount() + 1), "", "1", "0", "0.00"});
            int last = tableModel.getRowCount() - 1;
            itemTable.setRowSelectionInterval(last, last);
        });

        btnDel.addActionListener(e -> {
            int row = itemTable.getSelectedRow();
            if (row >= 0) {
                tableModel.removeRow(row);
                renumberRows();
                updateStatus();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row first.",
                        "No selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnBar.add(btnAdd);
        btnBar.add(btnDel);

        card.add(scroll, BorderLayout.CENTER);
        card.add(btnBar, BorderLayout.SOUTH);

        return card;
    }

    // ─── Bottom Card ───────────────────────────────────────────────────
    private JPanel buildBottomCard() {
        JPanel card = createCard("Payment & Notes", "P");
        card.setLayout(new BorderLayout(15, 12));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lDiscount = makeLabel("Discount (%)");
        JLabel lAdvance = makeLabel("Advance (Rs.)");
        JLabel lNotes = makeLabel("Notes");

        txtDiscount = makeTextField();
        txtDiscount.setPreferredSize(new Dimension(90, 34));
        txtDiscount.setText("0");

        txtAdvance = makeTextField();
        txtAdvance.setPreferredSize(new Dimension(150, 34));
        txtAdvance.setText("0");

        txtNotes = new JTextArea(3, 20);
        txtNotes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setBackground(Main.BG_ELEVATED);
        txtNotes.setForeground(Main.TEXT_PRIMARY);
        txtNotes.setCaretColor(Main.PRIMARY_LIGHT);
        txtNotes.setBorder(new EmptyBorder(6, 10, 6, 10));

        JScrollPane notesScroll = new JScrollPane(txtNotes);
        notesScroll.setBorder(BorderFactory.createLineBorder(Main.BORDER));
        notesScroll.setPreferredSize(new Dimension(500, 75));
        notesScroll.getViewport().setBackground(Main.BG_ELEVATED);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        leftPanel.add(lDiscount, gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        leftPanel.add(txtDiscount, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        leftPanel.add(lAdvance, gbc);
        gbc.gridx = 3; gbc.weightx = 0;
        leftPanel.add(txtAdvance, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        leftPanel.add(lNotes, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        leftPanel.add(notesScroll, gbc);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        JButton btnClear = makeFilledButton("CLEAR ALL", Main.DANGER, Color.WHITE);
        btnClear.setMaximumSize(new Dimension(230, 44));
        btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClear.addActionListener(e -> clearAll());

        JButton btnGen = makeFilledButton("GENERATE PDF", Main.SUCCESS, Color.WHITE);
        btnGen.setMaximumSize(new Dimension(230, 48));
        btnGen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGen.addActionListener(e -> onGenerate());

        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(btnClear);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnGen);
        rightPanel.add(Box.createVerticalGlue());

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    // ─── Status Bar ────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Main.BG_ELEVATED);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Main.BORDER),
                new EmptyBorder(7, 20, 7, 20)
        ));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(Main.TEXT_SECONDARY);

        JLabel hint = new JLabel("Fields marked with * are required");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(Main.TEXT_MUTED);

        bar.add(statusLabel, BorderLayout.WEST);
        bar.add(hint, BorderLayout.EAST);

        return bar;
    }

    private void updateStatus() {
        if (statusLabel == null) return;
        int count = 0;
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int qty = Integer.parseInt(String.valueOf(tableModel.getValueAt(i, 2)).trim());
                double price = Double.parseDouble(String.valueOf(tableModel.getValueAt(i, 3)).trim());
                if (qty > 0) count++;
                total += qty * price;
            } catch (Exception ignored) {
            }
        }
        statusLabel.setText("Items: " + count + "     |     Subtotal: Rs. " + String.format("%.2f", total));
    }

    private void recalcRow(int row) {
        try {
            updating = true;
            int qty = Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 2)).trim());
            double price = Double.parseDouble(String.valueOf(tableModel.getValueAt(row, 3)).trim());
            tableModel.setValueAt(String.format("%.2f", qty * price), row, 4);
        } catch (Exception e) {
            updating = true;
            tableModel.setValueAt("0.00", row, 4);
        } finally {
            updating = false;
        }
    }

    private void renumberRows() {
        updating = true;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(String.valueOf(i + 1), i, 0);
        }
        updating = false;
    }

    // ─── UI Helpers ────────────────────────────────────────────────────
    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(Main.TEXT_PRIMARY);
        return l;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(200, 34));
        tf.setBackground(Main.BG_ELEVATED);
        tf.setForeground(Main.TEXT_PRIMARY);
        tf.setCaretColor(Main.PRIMARY_LIGHT);
        tf.setSelectionColor(Main.PRIMARY);
        tf.setSelectedTextColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Main.BORDER),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private JButton makeOutlineButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(color);
        btn.setBackground(Main.BG_ELEVATED);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1),
                new EmptyBorder(7, 18, 7, 18)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Main.BG_ELEVATED);
            }
        });
        return btn;
    }

    private JButton makeFilledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(11, 22, 11, 22));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    // ─── Actions ───────────────────────────────────────────────────────
    private void goBack() {
        this.dispose();
        new MainMenu().setVisible(true);
    }

    private void clearAll() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Clear all entered data?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            txtName.setText("");
            txtPhone.setText("");
            txtAddress.setText("");
            txtDiscount.setText("0");
            txtAdvance.setText("0");
            txtNotes.setText("");
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"1", "", "1", "0", "0.00"});
            updateStatus();
        }
    }

    protected void onGenerate() {
        try {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer name is required!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String[]> items = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String desc = String.valueOf(tableModel.getValueAt(i, 1)).trim();
                String qtyStr = String.valueOf(tableModel.getValueAt(i, 2)).trim();
                String priceStr = String.valueOf(tableModel.getValueAt(i, 3)).trim();

                if (desc.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) continue;
                try {
                    Integer.parseInt(qtyStr);
                    Double.parseDouble(priceStr);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid number in row " + (i + 1),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                items.add(new String[]{desc, qtyStr, priceStr});
            }

            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add at least one item!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double discount = parseDouble(txtDiscount.getText());
            double advance = parseDouble(txtAdvance.getText());

            DataManager.saveCustomer(name, txtPhone.getText(), txtAddress.getText());

            PDFGenerator.generate(docType, name, txtPhone.getText(), txtAddress.getText(),
                    items, discount, advance, txtNotes.getText());

            if (statusLabel != null) {
                statusLabel.setText("PDF generated successfully");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}