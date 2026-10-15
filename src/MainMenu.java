import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JFrame {

    // ⭐ Width = 640, Height = 820 → Portrait shape
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 820;

    public MainMenu() {
        setTitle("Coolmax Air Solutions - Management System");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Main.BG_DARK);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    // ─── Header ────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(16, 22, 38));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(45, 55, 80));
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(WINDOW_WIDTH, 190));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 0, 28, 0));

        JLabel logo = new JLabel("❄", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        logo.setForeground(new Color(120, 160, 220));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setPreferredSize(new Dimension(60, 60));
        logo.setMaximumSize(new Dimension(60, 60));

        JLabel title = new JLabel("COOLMAX AIR SOLUTIONS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(235, 240, 250));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sales   •   Service   •   Repair   •   Installation");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(new Color(130, 140, 160));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalGlue());
        header.add(logo);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);
        header.add(Box.createVerticalGlue());

        return header;
    }

    // ─── Center (Compact vertical list) ────────────────────────────────
    private JPanel buildCenter() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Main.BG_DARK);
        wrapper.setBorder(new EmptyBorder(25, 40, 20, 40));

        JLabel sectionTitle = new JLabel("Select Document Type");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionTitle.setForeground(new Color(150, 160, 180));
        sectionTitle.setBorder(new EmptyBorder(0, 5, 14, 0));

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Main.BG_DARK);

        list.add(createMenuItem("INV", "Create Invoice", "Generate customer invoices",
                e -> openForm(new InvoiceForm())));
        list.add(Box.createVerticalStrut(10));

        list.add(createMenuItem("SVC", "Service Bill", "Regular AC service billing",
                e -> openForm(new ServiceBillForm())));
        list.add(Box.createVerticalStrut(10));

        list.add(createMenuItem("RPR", "Repair & Installation", "Repair and install charges",
                e -> openForm(new RepairBillForm())));
        list.add(Box.createVerticalStrut(10));

        list.add(createMenuItem("QTN", "Quotation", "Price estimates for customers",
                e -> openForm(new QuotationForm())));

        wrapper.add(sectionTitle, BorderLayout.NORTH);
        wrapper.add(list, BorderLayout.CENTER);

        return wrapper;
    }

    private void openForm(JFrame form) {
        this.dispose();
        form.setVisible(true);
    }

    // ─── Minimal Menu Item ─────────────────────────────────────────────
    private JPanel createMenuItem(String code, String title, String desc,
                                  java.awt.event.ActionListener action) {
        JPanel item = new JPanel() {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        action.actionPerformed(null);
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (hovered) {
                    g2.setColor(new Color(35, 45, 65));
                } else {
                    g2.setColor(new Color(24, 28, 40));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                if (hovered) {
                    g2.setColor(new Color(120, 160, 220));
                    g2.setStroke(new BasicStroke(1.2f));
                } else {
                    g2.setColor(new Color(45, 55, 75));
                    g2.setStroke(new BasicStroke(1f));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        item.setLayout(new BorderLayout(14, 0));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(10, 18, 10, 18));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        item.setPreferredSize(new Dimension(400, 64));
        item.setMinimumSize(new Dimension(300, 64));

        JLabel codeBadge = new JLabel(code, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(70, 100, 150, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        codeBadge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        codeBadge.setForeground(new Color(150, 190, 240));
        codeBadge.setPreferredSize(new Dimension(50, 42));

        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrap.setOpaque(false);
        leftWrap.add(codeBadge);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(230, 235, 245));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblDesc.setForeground(new Color(130, 140, 160));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(lblDesc);
        textPanel.add(Box.createVerticalGlue());

        JLabel arrow = new JLabel("→", SwingConstants.CENTER);
        arrow.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        arrow.setForeground(new Color(100, 115, 140));
        arrow.setPreferredSize(new Dimension(24, 24));

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        rightWrap.setOpaque(false);
        rightWrap.add(arrow);

        item.add(leftWrap, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        item.add(rightWrap, BorderLayout.EAST);

        return item;
    }

    // ─── Footer ────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Main.BG_DARK);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(30, 35, 48)),
                new EmptyBorder(10, 25, 12, 25)
        ));

        JLabel left = new JLabel("v1.0.0");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.setForeground(new Color(100, 110, 130));

        JLabel right = new JLabel("© 2025 Coolmax Air Solutions");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.setForeground(new Color(100, 110, 130));

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);

        return footer;
    }
}