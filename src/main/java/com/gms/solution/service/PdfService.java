///*
// * PdfService.java
// *
// * Copyright (c) 2025 Nguyen. All rights reserved.
// * This software is the confidential and proprietary information of Nguyen.
// */
//
//package com.gms.solution.service;
//
//import com.gms.solution.model.entity.Order;
//import com.gms.solution.model.entity.OrderItems;
//import com.lowagie.text.*;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import org.springframework.stereotype.Service;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.util.stream.Stream;
//
///**
// * PdfService.java
// *
// * @author Nguyen
// */
//@Service
//public class PdfService {
//    public ByteArrayInputStream generateInvoice(Order order) {
//        Document document = new Document();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        try {
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            // Tieu de tai lieu
//            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
//            Paragraph title = new Paragraph("HÓA ĐƠN MUA HÀNG", font);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//
//            document.add(new Paragraph(" "));
//
//            document.add(new Paragraph("Người đặt hàng: " + order.getUser().getFullName()));
//            document.add(new Paragraph("Ngày tạo: " + order.getUser().getCreatedAt()));
//            document.add(new Paragraph(" "));
//
//            PdfPTable table = new PdfPTable(5);
//            table.setWidthPercentage(100);
//            table.setWidths(new int[]{1, 4, 2, 2, 2});
//
//            Stream.of("STT", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền")
//                    .forEach(headerTitle -> {
//                        PdfPCell header = new PdfPCell();
//                        header.setPhrase(new Phrase(headerTitle));
//                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
//                        table.addCell(header);
//                    });
//
//            int index = 1;
//            for () {
//                table.addCell(String.valueOf(index++));
//                table.addCell();
//                table.addCell();
//                table.addCell();
//                table.addCell();
//            }
//
//            document.add(table);
//
//            document.add(new Paragraph(" "));
//            document.add(new Paragraph("Tổng tiền: ") +  + " đ");
//
//            document.close();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//
//        return new ByteArrayInputStream(outputStream.toByteArray());
//    }
//}
