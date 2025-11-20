package com.maffy.Vista;

import com.maffy.models.Venta;
import com.maffy.services.VentaService;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Formulario de reportes que genera PDFs reales usando ventas completadas.
 */
public class FrmReporte extends JFrame {

	private JRadioButton rbDiario;
	private JRadioButton rbSemanal;
	private JRadioButton rbMensual;
	private JButton btnGenerar;
	private JLabel lblStatus;

	public FrmReporte() {
		setTitle("Reportes");
		setSize(480, 220);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel top = new JPanel();
		top.add(new JLabel("Seleccione tipo de reporte:"));

		rbDiario = new JRadioButton("Diario");
		rbSemanal = new JRadioButton("Semanal");
		rbMensual = new JRadioButton("Mensual");
		rbDiario.setSelected(true);

		ButtonGroup g = new ButtonGroup();
		g.add(rbDiario);
		g.add(rbSemanal);
		g.add(rbMensual);

		top.add(rbDiario);
		top.add(rbSemanal);
		top.add(rbMensual);

		getContentPane().add(top, BorderLayout.NORTH);

		JPanel center = new JPanel();
		btnGenerar = new JButton("Generar PDF");
		lblStatus = new JLabel(" ");
		center.add(btnGenerar);
		center.add(lblStatus);
		getContentPane().add(center, BorderLayout.CENTER);

		btnGenerar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnGenerar.setEnabled(false);
				lblStatus.setText("Generando...");
				SwingUtilities.invokeLater(() -> {
					String tipo = rbDiario.isSelected() ? "Diario" : (rbSemanal.isSelected() ? "Semanal" : "Mensual");
					try {
						File out = generatePdfReport(tipo);
						lblStatus.setText("PDF generado: " + out.getName());
					} catch (Exception ex) {
						lblStatus.setText("Error: " + ex.getMessage());
						ex.printStackTrace();
					} finally {
						btnGenerar.setEnabled(true);
					}
				});
			}
		});
	}
	private File generatePdfReport(String tipo) throws IOException {
		// determinar rango según tipo
		LocalDate hoy = LocalDate.now();
		LocalDate desde;
		LocalDate hasta;
		if ("Diario".equalsIgnoreCase(tipo)) {
			desde = hoy;
			hasta = hoy;
		} else if ("Semanal".equalsIgnoreCase(tipo)) {
			hasta = hoy;
			desde = hoy.minusDays(6); // 7 dias incluyendo hoy
		} else { // Mensual
			desde = hoy.withDayOfMonth(1);
			hasta = hoy.withDayOfMonth(hoy.lengthOfMonth());
		}

		VentaService service = new VentaService();
		List<Venta> ventas = service.obtenerVentasPorRango(desde, hasta);

		String reportsDir = "reports";
		File dir = new File(reportsDir);
		if (!dir.exists()) dir.mkdirs();

		String fileName = String.format("reporte_%s_%d.pdf", tipo.toLowerCase(), System.currentTimeMillis());
		File outFile = new File(dir, fileName);

		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		BigDecimal totalVentas = BigDecimal.ZERO;

		try (PDDocument doc = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.LETTER);
			doc.addPage(page);

			PDPageContentStream cs = new PDPageContentStream(doc, page);

			// Encabezado
			cs.beginText();
			cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
			cs.newLineAtOffset(70, 740);
			cs.showText("Reporte " + tipo);
			cs.endText();

			cs.beginText();
			cs.setFont(PDType1Font.HELVETICA, 12);
			cs.newLineAtOffset(70, 720);
			cs.showText("Periodo: " + desde.format(df) + " - " + hasta.format(df));
			cs.endText();

			cs.beginText();
			cs.setFont(PDType1Font.HELVETICA, 12);
			cs.newLineAtOffset(70, 700);
			cs.showText("Generado por: Yamilet Cooky");
			cs.endText();

			// Cabecera de tabla
			int y = 670;
			cs.beginText();
			cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
			cs.newLineAtOffset(70, y);
			cs.showText("ID    FECHA        ESTADO       TOTAL");
			cs.endText();

			y -= 18;
			cs.setFont(PDType1Font.HELVETICA, 11);
			for (Venta v : ventas) {
				if (y < 80) {
					cs.close();
					doc.addPage(new PDPage(PDRectangle.LETTER));
					cs = new PDPageContentStream(doc, doc.getPage(doc.getNumberOfPages() - 1));
					y = 740;
				}
				cs.beginText();
				cs.newLineAtOffset(70, y);
				String linea = String.format("%d    %s    %s    %s",
					v.getId(),
					v.getFechaVenta() != null ? v.getFechaVenta().toLocalDate().format(df) : "",
					v.getEstado(),
					nf.format(v.getTotal()));
				cs.showText(linea);
				cs.endText();
				y -= 16;
				if (v.getTotal() != null) totalVentas = totalVentas.add(v.getTotal());
			}

			// Total final
			if (y < 120) {
				cs.close();
				doc.addPage(new PDPage(PDRectangle.LETTER));
				cs = new PDPageContentStream(doc, doc.getPage(doc.getNumberOfPages() - 1));
				y = 740;
			}
			cs.beginText();
			cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
			cs.newLineAtOffset(70, y - 10);
			cs.showText("TOTAL VENTAS: " + nf.format(totalVentas));
			cs.endText();

			cs.close();
			doc.save(outFile);
		}

		return outFile;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			FrmReporte f = new FrmReporte();
			f.setVisible(true);
		});
	}
}

