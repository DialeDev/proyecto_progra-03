package utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

/**
 * Utilidad para aplicar un estilo visual moderno y consistente a JFrames.
 * 
 * <p>Colores, tipografía y efectos hover homogéneos en toda la aplicación.
 * No toca layouts ni componentes específicos de cada formulario.</p>
 */
public final class UIStyles {

    // ── Paleta de colores ──────────────────────────────────────────────
    /** Fondo suave para paneles y content pane. */
    public static final Color BG_SOFT          = new Color(245, 247, 250);
    /** Color de acento principal (botones primarios). */
    public static final Color ACCENT           = new Color(41, 128, 185);
    /** Color de acento oscuro (hover de botones primarios). */
    public static final Color ACCENT_DARK      = new Color(31, 97, 143);
    /** Color neutral para bordes de inputs. */
    public static final Color NEUTRAL          = new Color(200, 200, 200);
    /** Botón secundario / gris. */
    public static final Color BTN_SECONDARY    = new Color(149, 165, 166);
    /** Hover de botón secundario. */
    public static final Color BTN_SECONDARY_DK = new Color(127, 140, 141);
    /** Botón destructivo (rojo). */
    public static final Color BTN_DANGER       = new Color(204, 0, 0);
    /** Botón éxito (verde). */
    public static final Color BTN_SUCCESS      = new Color(0, 153, 0);

    // ── Tipografía ─────────────────────────────────────────────────────
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_INPUT   = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 18);

    // ── Bordes ─────────────────────────────────────────────────────────
    private static final Border INPUT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(NEUTRAL),
            BorderFactory.createEmptyBorder(6, 8, 6, 8));

    private UIStyles() {
        // utilidad — no instanciable
    }

    // ── SVG Icon helper ────────────────────────────────────────────────

    /**
     * Sets a white SVG icon on a button using FlatSVGIcon.
     * The icon is loaded from {@code img/fa[name].svg} via ClassLoader and tinted
     * white so it looks correct on any button background color.
     * <p>If the SVG file does not exist the button is left unchanged.</p>
     *
     * @param btn  the button to apply the icon to
     * @param name the icon base name (e.g. "User" → "img/faUser.svg")
     */
    public static void setSVGIcon(JButton btn, String name) {
        try {
            String path = "img/fa" + name + ".svg";
            FlatSVGIcon icon = new FlatSVGIcon(path, UIStyles.class.getClassLoader());
            icon = icon.derive(18, 18); // standard size
            // White color filter — icons look correct on any button background
            // NOTE: anonymous class required — lambda does not compile in FlatLaf 3.7.1
            icon.setColorFilter(new FlatSVGIcon.ColorFilter() {
                @Override
                public Color filter(Color c) {
                    return Color.WHITE;
                }
            });
            btn.setIcon(icon);
        } catch (Exception e) {
            // SVG not available — button text only
        }
    }

    // ── Método principal ───────────────────────────────────────────────

    /**
     * Aplica estilo moderno a todos los componentes estándar del frame.
     * <p>Recorre recursivamente el árbol de componentes del content pane y
     * estiliza JPanel, JLabel, JButton, JTextField, JPasswordField,
     * JComboBox y JToggleButton.</p>
     *
     * @param frame JFrame al que aplicar el estilo (puede ser {@code null},
     *              en cuyo caso la operación es no-op).
     */
    public static void applyModernStyle(JFrame frame) {
        if (frame == null) return;

        // Fondo general del content pane
        frame.getContentPane().setBackground(BG_SOFT);

        // Recorrer todos los componentes recursivamente
        applyStyleToContainer(frame.getContentPane());
    }

    // ── Estilizado recursivo ───────────────────────────────────────────

    private static void applyStyleToContainer(java.awt.Container container) {
        for (Component comp : container.getComponents()) {
            // ── JPanel ─────────────────────────────────────────────
            if (comp instanceof JPanel panel) {
                // Solo aplicar fondo si no tiene un color explícito ya puesto
                // (ej. paneles con border-painted tienen su propio color)
                // También evitamos sobreescribir paneles con colores oscuros
                Color current = panel.getBackground();
                if (current == null || isDefaultOrTransparent(current)) {
                    panel.setBackground(BG_SOFT);
                }
                applyStyleToContainer(panel);
                continue;
            }

            // ── JLabel ──────────────────────────────────────────────
            if (comp instanceof JLabel label) {
                // Solo aplicar font si no tiene uno custom ya asignado
                // (ej. títulos con font grande). Detectamos si el label
                // ya tiene un font más grande (>=16) o bold para respetarlo.
                Font f = label.getFont();
                if (f == null || isDefaultFont(label)) {
                    label.setFont(FONT_LABEL);
                }
                continue;
            }

            // ── JButton ─────────────────────────────────────────────
            if (comp instanceof JButton button) {
                styleButton(button);
                continue;
            }

            // ── JToggleButton (but NOT JCheckBox) ────────────────────
            // JCheckBox extends JToggleButton; checkboxes must keep their native
            // rendering (content area, text, etc.) and should NOT be styled here.
            if (comp instanceof JToggleButton toggle && !(comp instanceof JCheckBox)) {
                styleToggleButton(toggle);
                continue;
            }

            // ── JTextField / JPasswordField ─────────────────────────
            if (comp instanceof JTextField text) {
                styleTextField(text);
                continue;
            }

            // ── JComboBox ───────────────────────────────────────────
            if (comp instanceof JComboBox<?> combo) {
                styleComboBox(combo);
                continue;
            }

            // Si es otro container (JScrollPane, JSplitPane, etc.),
            // recorrer hijos
            if (comp instanceof java.awt.Container cont) {
                applyStyleToContainer(cont);
            }
        }
    }

    // ── Estilizado individual de componentes ───────────────────────────

    private static void styleButton(JButton button) {
        String text = button.getText();
        Color btnColor;
        Color btnHover;

        if (text == null) {
            btnColor = ACCENT;
            btnHover = ACCENT_DARK;
        } else {
            String t = text.toLowerCase().trim();
            if (t.contains("eliminar") || t.contains("borrar") || t.contains("cerrar sesión")
                    || t.contains("salir") || t.contains("cancelar")
                    || t.contains("desactivar") || t.contains("anular")) {
                btnColor = BTN_DANGER;
                btnHover = darken(BTN_DANGER);
            } else if (t.contains("guardar") || t.contains("registrar") || t.contains("crear")
                    || t.contains("emitir")) {
                btnColor = BTN_SUCCESS;
                btnHover = darken(BTN_SUCCESS);
            } else if (t.contains("limpiar") || t.contains("buscar") || t.contains("volver")
                    || t.contains("reactivar") || t.contains("refrescar")) {
                btnColor = BTN_SECONDARY;
                btnHover = BTN_SECONDARY_DK;
            } else {
                btnColor = ACCENT;
                btnHover = ACCENT_DARK;
            }
        }

        button.setBackground(btnColor);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final Color savedBg = btnColor;
        final Color savedHover = btnHover;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(savedHover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(savedBg);
            }
        });
    }

    private static void styleToggleButton(JToggleButton toggle) {
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setFocusPainted(false);
    }

    private static void styleTextField(JTextField text) {
        text.setFont(FONT_INPUT);
        text.setBorder(INPUT_BORDER);
    }

    private static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_INPUT);
    }

    // ── Helpers ────────────────────────────────────────────────────────

    private static boolean isDefaultOrTransparent(Color c) {
        // UIManager defaults suelen ser:
        //    javax.swing.plaf.ColorUIResource(r=238,g=238,b=238) para paneles
        //    212,208,200 para botones, 51,51,51 para texto
        // Nunca son null pero pueden ser ColorUIResource
        return c == null || c.getAlpha() == 0
                || (c instanceof javax.swing.plaf.ColorUIResource);
    }

    private static boolean isDefaultFont(JLabel label) {
        Font f = label.getFont();
        // Si el label fue diseñado con un font >12 o bold, lo respetamos
        // (probablemente es un título). Si es el default de UIManager, lo reemplazamos.
        if (f.getSize() >= 16 || f.getStyle() == Font.BOLD) return false;
        return f instanceof javax.swing.plaf.FontUIResource;
    }

    /**
     * Oscurece un color en un 15% (para efecto hover).
     */
    private static Color darken(Color c) {
        int r = Math.max(0, c.getRed() - 25);
        int g = Math.max(0, c.getGreen() - 25);
        int b = Math.max(0, c.getBlue() - 25);
        return new Color(r, g, b, c.getAlpha());
    }
}
