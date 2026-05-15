package com.activosfijos.servicio;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ReportesServicio {

    public void exportarTablaAPdf(TableModel model, File archivoDestino) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(archivoDestino));
        document.open();
        document.add(new Paragraph("Reporte de Activos"));
        document.add(new Paragraph(" "));

        PdfPTable tablaPdf = new PdfPTable(model.getColumnCount());
        for (int i = 0; i < model.getColumnCount(); i++) {
            tablaPdf.addCell(new PdfPCell(new Phrase(model.getColumnName(i))));
        }

        for (int fila = 0; fila < model.getRowCount(); fila++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object valor = model.getValueAt(fila, col);
                tablaPdf.addCell(valor == null ? "" : valor.toString());
            }
        }

        document.add(tablaPdf);
        document.close();
    }

    public void exportarTablaACsv(TableModel model, File archivoDestino) throws Exception {
        try (PrintWriter writer = new PrintWriter(archivoDestino, StandardCharsets.UTF_8)) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.print(escaparCsv(model.getColumnName(i)));
                if (i < model.getColumnCount() - 1) {
                    writer.print(",");
                }
            }
            writer.println();

            for (int fila = 0; fila < model.getRowCount(); fila++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object valor = model.getValueAt(fila, col);
                    writer.print(escaparCsv(valor == null ? "" : valor.toString()));
                    if (col < model.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        }
    }

    private String escaparCsv(String valor) {
        return '"' + valor.replace("\"", "\"\"") + '"';
    }
}
