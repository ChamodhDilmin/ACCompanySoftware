import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Coolmax Air Solutions - Management System");
        setSize(1080, 740);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 40, 90),
                        getWidth(), getHeight(), new Color(13, 71, 161)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(1080, 200));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(25, 0, 22, 0));

        JLabel logo = new JLabel("❄", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(82, 156, 255, 60));
                int size = Math.min(getWidth(), getHeight());
                g2.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setPreferredSize(new Dimension(84, 84));
        logo.setMaximumSize(new Dimension(84, 84));
        logo.setMinimumSize(new Dimension(84, 84));

        // ⭐ අලුත් නම
        JLabel title = new JLabel("COOLMAX AIR SOLUTIONS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sales   •   Service   •   Repair   •   Installation");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(180, 210, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        return header;
    }

    // ─── Center Cards ──────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Main.BG_DARK);
        wrapper.setBorder(new EmptyBorder(25, 50, 20, 50));

        JLabel sectionTitle = new JLabel("Select Document Type");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(Main.TEXT_PRIMARY);
        sectionTitle.setBorder(new EmptyBorder(0, 5, 18, 0));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(Main.BG_DARK);

        grid.add(createCard("INV", "Create Invoice", "Generate customer invoices",
                new Color(66, 165, 245),
                e -> openForm(new InvoiceForm())));

        grid.add(createCard("SVC", "Service Bill", "Regular AC service billing",
                new Color(102, 187, 106),
                e -> openForm(new ServiceBillForm())));

        grid.add(createCard("RPR", "Repair & Installation", "Repair and install charges",
                new Color(255, 167, 38),
                e -> openForm(new RepairBillForm())));

        grid.add(createCard("QTN", "Quotation", "Price estimates for customers",
                new Color(186, 104, 200),
                e -> openForm(new QuotationForm())));

        wrapper.add(sectionTitle, BorderLayout.NORTH);
        wrapper.add(grid, BorderLayout.CENTER);

        return wrapper;
    }

    /**
     * Opens a form and CLOSES this main menu window.
     */
    private void openForm(JFrame form) {
        this.dispose();
        form.setVisible(true);
    }

    private JPanel createCard(String icon, String title, String desc, Color accent,
                              java.awt.event.ActionListener action) {
        JPanel card = new JPanel() {
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

                // Shadow
                if (hovered) {
                    g2.setColor(new Color(0, 0, 0, 90));
                    g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 18, 18);
                } else {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 6, 18, 18);
                }

                // Card bg (dark)
                if (hovered) {
                    g2.setColor(new Color(45, 45, 62));
                } else {
                    g2.setColor(Main.BG_CARD);
                }
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 18, 18);

                // Glow border on hover
                if (hovered) {
                    g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 120));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth() - 7, getHeight() - 7, 18, 18);
                }

                // Accent top bar
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth() - 6, 6, 18, 18);
                g2.fillRect(0, 3, getWidth() - 6, 3);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(18, 5));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(28, 28, 28, 28));

        // Icon (with letter-based icons to avoid emoji rendering issues)
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 45));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconLabel.setForeground(accent);
        iconLabel.setPreferredSize(new Dimension(76, 76));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconWrap.setOpaque(false);
        iconWrap.add(iconLabel);

        // Text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Main.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(Main.TEXT_SECONDARY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(Box.createVerticalGlue());
        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(lblDesc);
        textPanel.add(Box.createVerticalGlue());

        card.add(iconWrap, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    // ─── Footer ────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Main.BG_DARK);
        footer.setBorder(new EmptyBorder(10, 30, 15, 30));

        JLabel left = new JLabel("v1.0.0");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        left.setForeground(Main.TEXT_MUTED);

        JLabel right = new JLabel("© 2025 Coolmax Air Solutions. All Rights Reserved.");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        right.setForeground(Main.TEXT_MUTED);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);

        return footer;
    }
}