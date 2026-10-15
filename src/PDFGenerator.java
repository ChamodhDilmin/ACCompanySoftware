import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFGenerator {

    public static void generate(String docType, String customerName, String customerPhone,
                                String customerAddress, List<String[]> items,
                                double discount, double advance, String notes) {
        try {
            String folder = "output";
            new File(folder).mkdirs();

            String fileName = folder + "/" + docType.replace(" ", "_").replace("&", "and") + "_" +
                    System.currentTimeMillis() + ".pdf";

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, new BaseColor(13, 71, 161));
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font smallFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);

            Paragraph company = new Paragraph("AC COMPANY", titleFont);
            company.setAlignment(Element.ALIGN_CENTER);
            document.add(company);

            Paragraph tagline = new Paragraph("Air Conditioning Sales • Service • Repair • Installation",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY));
            tagline.setAlignment(Element.ALIGN_CENTER);
            document.add(tagline);

            Paragraph contact = new Paragraph(
                    "No. 123, Main Street, Colombo  |  Tel: 011-2345678  |  Email: info@accompany.lk",
                    smallFont);
            contact.setAlignment(Element.ALIGN_CENTER);
            document.add(contact);

            document.add(new Paragraph(" "));

            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            PdfPCell lineCell = new PdfPCell(new Phrase(" "));
            lineCell.setBackgroundColor(new BaseColor(13, 71, 161));
            lineCell.setBorder(Rectangle.NO_BORDER);
            lineCell.setFixedHeight(3);
            line.addCell(lineCell);
            document.add(line);
            document.add(new Paragraph(" "));

            Paragraph docTitle = new Paragraph(docType.toUpperCase(),
                    new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK));
            docTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(docTitle);
            document.add(new Paragraph(" "));

            String billNo = docType.substring(0, Math.min(3, docType.length())).toUpperCase() + "-" +
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1.5f, 1f});

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setPadding(5);
            leftCell.addElement(new Paragraph("Bill To:", boldFont));
            leftCell.addElement(new Paragraph(customerName != null ? customerName : "-", normalFont));
            leftCell.addElement(new Paragraph(
                    customerPhone != null && !customerPhone.isEmpty() ? customerPhone : "-", normalFont));
            leftCell.addElement(new Paragraph(
                    customerAddress != null && !customerAddress.isEmpty() ? customerAddress : "-", normalFont));

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setPadding(5);
            Paragraph p1 = new Paragraph("No: " + billNo, boldFont);
            p1.setAlignment(Element.ALIGN_RIGHT);
            Paragraph p2 = new Paragraph("Date: " + dateStr, normalFont);
            p2.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(p1);
            rightCell.addElement(p2);

            infoTable.addCell(leftCell);
            infoTable.addCell(rightCell);
            document.add(infoTable);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{0.5f, 3f, 1f, 1.3f, 1.5f});

            String[] headers = {"#", "Description", "Qty", "Unit Price", "Amount"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new BaseColor(13, 71, 161));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                cell.setBorderColor(BaseColor.WHITE);
                table.addCell(cell);
            }

            double subTotal = 0;
            int idx = 1;
            for (String[] item : items) {
                String desc = item[0];
                int qty = Integer.parseInt(item[1]);
                double price = Double.parseDouble(item[2]);
                double amount = qty * price;
                subTotal += amount;

                addCell(table, String.valueOf(idx++), normalFont, Element.ALIGN_CENTER);
                addCell(table, desc, normalFont, Element.ALIGN_LEFT);
                addCell(table, String.valueOf(qty), normalFont, Element.ALIGN_CENTER);
                addCell(table, String.format("%.2f", price), normalFont, Element.ALIGN_RIGHT);
                addCell(table, String.format("%.2f", amount), normalFont, Element.ALIGN_RIGHT);
            }

            document.add(table);
            document.add(new Paragraph(" "));

            double discountAmt = subTotal * (discount / 100.0);
            double afterDiscount = subTotal - discountAmt;
            double balance = afterDiscount - advance;

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(45);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{2f, 1.5f});

            addTotalRow(totalTable, "Sub Total:", String.format("Rs. %.2f", subTotal), normalFont, false);
            if (discount > 0) {
                addTotalRow(totalTable, "Discount (" + discount + "%):",
                        String.format("- Rs. %.2f", discountAmt), normalFont, false);
            }
            if (advance > 0) {
                addTotalRow(totalTable, "Advance Paid:",
                        String.format("- Rs. %.2f", advance), normalFont, false);
                addTotalRow(totalTable, "Balance Due:",
                        String.format("Rs. %.2f", balance), boldFont, true);
            } else {
                addTotalRow(totalTable, "Grand Total:",
                        String.format("Rs. %.2f", afterDiscount), boldFont, true);
            }

            document.add(totalTable);
            document.add(new Paragraph(" "));

            if (notes != null && !notes.trim().isEmpty()) {
                document.add(new Paragraph("Notes:", boldFont));
                document.add(new Paragraph(notes, normalFont));
                document.add(new Paragraph(" "));
            }

            document.add(new Paragraph(" "));
            Paragraph thanks = new Paragraph("Thank you for your business!",
                    new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC, new BaseColor(13, 71, 161)));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            Paragraph warranty = new Paragraph(
                    "Warranty: 3 months for service, 1 year for installation (T&C apply).",
                    smallFont);
            warranty.setAlignment(Element.ALIGN_CENTER);
            document.add(warranty);

            document.close();

            JOptionPane.showMessageDialog(null,
                    "PDF created successfully!\n\nSaved to:\n" + new File(fileName).getAbsolutePath(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            try {
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(new File(fileName));
                }
            } catch (Exception ex) {
                // ignore
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void addCell(PdfPTable table, String text, Font font, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private static void addTotalRow(PdfPTable table, String label, String value, Font font, boolean highlight) {
        PdfPCell c1 = new PdfPCell(new Phrase(label, font));
        PdfPCell c2 = new PdfPCell(new Phrase(value, font));
        c1.setPadding(6);
        c2.setPadding(6);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        if (highlight) {
            c1.setBackgroundColor(new BaseColor(230, 240, 250));
            c2.setBackgroundColor(new BaseColor(230, 240, 250));
        }
        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);
        table.addCell(c1);
        table.addCell(c2);
    }
}