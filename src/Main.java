import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class Main {

    // 🌙 DARK THEME COLOR PALETTE
    public static final Color BG_DARK = new Color(18, 18, 24);
    public static final Color BG_CARD = new Color(30, 30, 40);
    public static final Color BG_ELEVATED = new Color(40, 40, 54);
    public static final Color PRIMARY = new Color(41, 121, 255);
    public static final Color PRIMARY_LIGHT = new Color(82, 156, 255);
    public static final Color ACCENT = new Color(0, 200, 220);
    public static final Color TEXT_PRIMARY = new Color(240, 240, 245);
    public static final Color TEXT_SECONDARY = new Color(170, 175, 190);
    public static final Color TEXT_MUTED = new Color(120, 125, 140);
    public static final Color BORDER = new Color(55, 55, 70);
    public static final Color SUCCESS = new Color(0, 200, 120);
    public static final Color DANGER = new Color(240, 80, 90);
    public static final Color WARNING = new Color(255, 160, 60);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Nimbus look and feel with heavy overrides
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

                // Bigger, clearer font
                FontUIResource font = new FontUIResource("Segoe UI", Font.PLAIN, 14);
                Enumeration<Object> keys = UIManager.getDefaults().keys();
                while (keys.hasMoreElements()) {
                    Object key = keys.nextElement();
                    Object value = UIManager.get(key);
                    if (value instanceof FontUIResource) {
                        UIManager.put(key, font);
                    }
                }

                // ─── GLOBAL DARK THEME OVERRIDES ─────────────
                UIManager.put("control", new ColorUIResource(BG_CARD));
                UIManager.put("info", new ColorUIResource(BG_CARD));
                UIManager.put("nimbusBase", new ColorUIResource(BG_ELEVATED));
                UIManager.put("nimbusAlertYellow", new ColorUIResource(WARNING));
                UIManager.put("nimbusDisabledText", new ColorUIResource(TEXT_MUTED));
                UIManager.put("nimbusFocus", new ColorUIResource(PRIMARY));
                UIManager.put("nimbusGreen", new ColorUIResource(SUCCESS));
                UIManager.put("nimbusInfoBlue", new ColorUIResource(PRIMARY));
                UIManager.put("nimbusLightBackground", new ColorUIResource(BG_DARK));
                UIManager.put("nimbusOrange", new ColorUIResource(WARNING));
                UIManager.put("nimbusRed", new ColorUIResource(DANGER));
                UIManager.put("nimbusSelectedText", new ColorUIResource(Color.WHITE));
                UIManager.put("nimbusSelectionBackground", new ColorUIResource(PRIMARY));
                UIManager.put("text", new ColorUIResource(TEXT_PRIMARY));

                // Panel / Label / etc.
                UIManager.put("Panel.background", new ColorUIResource(BG_DARK));
                UIManager.put("OptionPane.background", new ColorUIResource(BG_CARD));
                UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("Label.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("Label.background", new ColorUIResource(BG_DARK));

                // TextField / TextArea / Password
                UIManager.put("TextField.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("TextField.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("TextField.caretForeground", new ColorUIResource(PRIMARY));
                UIManager.put("TextField.inactiveForeground", new ColorUIResource(TEXT_MUTED));
                UIManager.put("TextField.border", BorderFactory.createLineBorder(BORDER));

                UIManager.put("TextArea.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("TextArea.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("TextArea.caretForeground", new ColorUIResource(PRIMARY));

                UIManager.put("FormattedTextField.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("FormattedTextField.foreground", new ColorUIResource(TEXT_PRIMARY));

                // Table
                UIManager.put("Table.background", new ColorUIResource(BG_CARD));
                UIManager.put("Table.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("Table.gridColor", new ColorUIResource(BORDER));
                UIManager.put("Table.selectionBackground", new ColorUIResource(PRIMARY));
                UIManager.put("Table.selectionForeground", new ColorUIResource(Color.WHITE));
                UIManager.put("Table.alternateRowColor", new ColorUIResource(new Color(35, 35, 48)));

                UIManager.put("TableHeader.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("TableHeader.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("TableHeader.cellBorder",
                        BorderFactory.createLineBorder(BORDER));

                // ScrollBar
                UIManager.put("ScrollBar.background", new ColorUIResource(BG_DARK));
                UIManager.put("ScrollBar.thumb", new ColorUIResource(BORDER));
                UIManager.put("ScrollBar.track", new ColorUIResource(BG_DARK));

                UIManager.put("ScrollPane.background", new ColorUIResource(BG_DARK));
                UIManager.put("Viewport.background", new ColorUIResource(BG_CARD));

                // Button
                UIManager.put("Button.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("Button.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("Button.highlight", new ColorUIResource(PRIMARY));
                UIManager.put("Button.disabledText", new ColorUIResource(TEXT_MUTED));

                // ComboBox
                UIManager.put("ComboBox.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("ComboBox.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("ComboBox.selectionBackground", new ColorUIResource(PRIMARY));
                UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.WHITE));

                // Tooltip
                UIManager.put("ToolTip.background", new ColorUIResource(BG_ELEVATED));
                UIManager.put("ToolTip.foreground", new ColorUIResource(TEXT_PRIMARY));
                UIManager.put("ToolTip.border", BorderFactory.createLineBorder(BORDER));

                // Misc
                UIManager.put("Table.rowHeight", 34);
                UIManager.put("ScrollBar.width", 12);
                UIManager.put("TitledBorder.titleColor", new ColorUIResource(PRIMARY_LIGHT));
                UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(BORDER));

            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainMenu().setVisible(true);
        });
    }
}