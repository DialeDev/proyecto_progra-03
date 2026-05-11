package utils;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ExportadorCSV {

    public static void exportarJTable(JTable table, String defaultName, java.awt.Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(defaultName + ".csv"));
        chooser.setFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(chooser.getSelectedFile()), StandardCharsets.UTF_8))) {
                bw.write('\uFEFF');
                TableModel model = table.getModel();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    bw.write(escaparCSV(model.getColumnName(i)));
                    if (i < model.getColumnCount() - 1) bw.write(',');
                }
                bw.newLine();
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object val = model.getValueAt(r, c);
                        bw.write(escaparCSV(val != null ? val.toString() : ""));
                        if (c < model.getColumnCount() - 1) bw.write(',');
                    }
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(parent, "Reporte exportado exitosamente:\n"
                        + chooser.getSelectedFile().getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al exportar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String escaparCSV(String valor) {
        if (valor == null || valor.isEmpty()) return "";
        if (valor.contains("\"") || valor.contains(",") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
