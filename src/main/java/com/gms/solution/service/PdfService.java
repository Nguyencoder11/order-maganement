/*
 * PdfService.java
 *
 * Copyright (c) 2025 Nguyen. All rights reserved.
 * This software is the confidential and proprietary information of Nguyen.
 */

package com.gms.solution.service;

import com.gms.solution.model.entity.Order;
import com.gms.solution.model.entity.OrderItems;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * PdfService.java
 *
 * @author Nguyen
 */
@Service
public class PdfService {
    public byte[] generateInvoice(Order order) throws IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DecimalFormat currencyFormat = new DecimalFormat("#,###");

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            BaseFont bf;
            // Tieu de tai lieu
            try {
                ClassPathResource fontResource = new ClassPathResource("/fonts/Roboto-Regular.ttf");

                File tempFile = File.createTempFile("roboto", ".ttf");
                try (InputStream in = fontResource.getInputStream()) {
                    FileOutputStream out = new FileOutputStream(tempFile);
                    in.transferTo(out);
                }

                bf = BaseFont.createFont(tempFile.getAbsolutePath(),
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED);
            } catch (Exception e) {
                throw new RuntimeException("Khong load duoc font Roboto", e);
            }

            Font font = new Font(bf, 16, Font.BOLD);
            Paragraph title = new Paragraph("HÓA ĐƠN MUA HÀNG", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Người đặt hàng: " + order.getUser().getFullName()));
            document.add(new Paragraph("Ngày tạo: " + order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 4, 2, 2, 2});

            Stream.of("STT", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setPhrase(new Phrase(headerTitle));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            int index = 1;
            for (OrderItems item : order.getItems()) {
                table.addCell(String.valueOf(index++));
                table.addCell(item.getProduct().getName());
                table.addCell(currencyFormat.format(item.getPrice()) + " đ");
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(currencyFormat.format(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))) + " đ");
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tổng tiền: " + currencyFormat.format(order.getTotalAmount()) + " đ"));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
