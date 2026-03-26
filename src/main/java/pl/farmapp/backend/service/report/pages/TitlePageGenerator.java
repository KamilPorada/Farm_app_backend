package pl.farmapp.backend.service.report.pages;

import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigInteger;

@Component
public class TitlePageGenerator {

    public void generate(XWPFDocument document, Integer seasonYear, String locality, String commune, String name, String surname) {

        try {

            // ===== ODSUNIĘCIE OD GÓRY =====
            for (int i = 0; i < 5; i++) {
                document.createParagraph();
            }

            // ===== NAZWA SYSTEMU =====
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun titleRun = title.createRun();
            titleRun.setBold(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(22);
            titleRun.setText("ASYSTENT PRODUCENTA PAPRYKI");
            title.setSpacingAfter(200); // odstęp po tytule


            // ===== PODTYTUŁ =====
            XWPFParagraph subtitle = document.createParagraph();
            subtitle.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun subtitleRun = subtitle.createRun();
            subtitleRun.setFontFamily("Times New Roman");
            subtitleRun.setFontSize(14);
            subtitleRun.setText("System zarządzania produkcją papryki");

            document.createParagraph();
            document.createParagraph();

            // ===== LOGO =====
            XWPFParagraph logoParagraph = document.createParagraph();
            logoParagraph.setAlignment(ParagraphAlignment.CENTER);
            logoParagraph.setSpacingAfter(200); // odstęp po tytule


            XWPFRun logoRun = logoParagraph.createRun();

            InputStream logo = getClass()
                    .getClassLoader()
                    .getResourceAsStream("report/logo.png");

            logoRun.addPicture(
                    logo,
                    Document.PICTURE_TYPE_PNG,
                    "logo",
                    Units.toEMU(110),
                    Units.toEMU(110)
            );

            document.createParagraph();
            document.createParagraph();

            // ===== TYTUŁ RAPORTU =====
            XWPFParagraph reportTitle = document.createParagraph();
            reportTitle.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun reportRun = reportTitle.createRun();
            reportRun.setBold(true);
            reportRun.setFontFamily("Times New Roman");
            reportRun.setFontSize(20);
            reportRun.setText("Raport produkcji papryki");
            reportTitle.setSpacingAfter(100); // odstęp po tytule


            // ===== SEZON =====
            XWPFParagraph season = document.createParagraph();
            season.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun seasonRun = season.createRun();
            seasonRun.setFontFamily("Times New Roman");
            seasonRun.setFontSize(16);
            seasonRun.setText("Sezon " + seasonYear);

            XWPFParagraph spacer = document.createParagraph();
            spacer.setSpacingAfter(2000);

            // ===== TABELA DOLNA (2 kolumny) =====
            XWPFTable table = document.createTable(2, 2);
            table.setWidth("100%");
            table.removeBorders();

// ===== WIERSZ 1 =====
            XWPFTableRow row1 = table.getRow(0);

// LEWA KOMÓRKA
            XWPFParagraph leftTop = row1.getCell(0).getParagraphs().get(0);
            leftTop.setAlignment(ParagraphAlignment.LEFT);

            XWPFRun nameRun = leftTop.createRun();
            nameRun.setFontFamily("Times New Roman");
            nameRun.setFontSize(12);
            nameRun.setText(name + " " + surname);

// PRAWA KOMÓRKA
            XWPFParagraph rightTop = row1.getCell(1).getParagraphs().get(0);
            rightTop.setAlignment(ParagraphAlignment.RIGHT);

            XWPFRun generatedLabel = rightTop.createRun();
            generatedLabel.setFontFamily("Times New Roman");
            generatedLabel.setFontSize(12);
            generatedLabel.setItalic(true);
            generatedLabel.setText("Raport wygenerowano:");

// ===== WIERSZ 2 =====
            XWPFTableRow row2 = table.getRow(1);

// LEWA KOMÓRKA
            XWPFParagraph leftBottom = row2.getCell(0).getParagraphs().get(0);
            leftBottom.setAlignment(ParagraphAlignment.LEFT);

            XWPFRun locationRun = leftBottom.createRun();
            locationRun.setFontFamily("Times New Roman");
            locationRun.setFontSize(12);
            locationRun.setText(locality + ", gmina " + commune);

// PRAWA KOMÓRKA
            XWPFParagraph rightBottom = row2.getCell(1).getParagraphs().get(0);
            rightBottom.setAlignment(ParagraphAlignment.RIGHT);

            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

            XWPFRun dateRun = rightBottom.createRun();
            dateRun.setFontFamily("Times New Roman");
            dateRun.setFontSize(12);
            dateRun.setText(now.format(formatter));

            // ===== SEKCJA – brak stopki na 1 stronie =====
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            sectPr.addNewTitlePg();

// ===== POLICY =====
            XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

// ===== STOPKA (od strony 2) =====
            XWPFFooter footer = policy.createFooter(STHdrFtr.DEFAULT);

// ===== PARAGRAF =====
            XWPFParagraph footerParagraph = footer.createParagraph();
            footerParagraph.setAlignment(ParagraphAlignment.LEFT);
            footerParagraph.setSpacingBefore(50);

// ===== XML =====
            CTP ctp = footerParagraph.getCTP();
            CTPPr ppr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();

// ===== LINIA =====
            CTPBdr borders = ppr.isSetPBdr() ? ppr.getPBdr() : ppr.addNewPBdr();

            CTBorder topBorder = borders.addNewTop();
            topBorder.setVal(STBorder.SINGLE);
            topBorder.setSz(BigInteger.valueOf(30));
            topBorder.setColor("234a0c");
// ===== TAB =====
            CTTabs tabs = ppr.isSetTabs() ? ppr.getTabs() : ppr.addNewTabs();

            CTTabStop rightTab = tabs.addNewTab();
            rightTab.setVal(STTabJc.RIGHT);
            rightTab.setPos(BigInteger.valueOf(9400));

// ===== LEWA STRONA =====
            XWPFRun leftRun = footerParagraph.createRun();
            footerParagraph.setStyle("Normal"); // 🔥 KLUCZ
            leftRun.setFontFamily("Times New Roman");
            leftRun.setFontSize(12);
            leftRun.setBold(true);
            leftRun.setText("Asystent Producenta Papryki 2.0");

// 🔥 WYMUSZENIE CZARNEGO (kompatybilne)
            CTRPr leftRpr = leftRun.getCTR().isSetRPr() ? leftRun.getCTR().getRPr() : leftRun.getCTR().addNewRPr();
            CTColor leftColor = leftRpr.addNewColor();
            leftColor.setVal("000000");

// ===== TAB =====
            footerParagraph.createRun().addTab();

// ===== PRAWA STRONA =====
            XWPFRun labelRun = footerParagraph.createRun();
            labelRun.setFontFamily("Times New Roman");
            labelRun.setFontSize(12);
            labelRun.setBold(true);
            labelRun.setText("Strona ");

// 🔥 WYMUSZENIE CZARNEGO
            CTRPr labelRpr = labelRun.getCTR().isSetRPr() ? labelRun.getCTR().getRPr() : labelRun.getCTR().addNewRPr();
            CTColor labelColor = labelRpr.addNewColor();
            labelColor.setVal("000000");

// ===== NUMER STRONY =====
            XWPFRun pageRun = footerParagraph.createRun();
            pageRun.getCTR().addNewFldChar().setFldCharType(STFldCharType.BEGIN);

            XWPFRun instrRun = footerParagraph.createRun();
            instrRun.getCTR().addNewInstrText().setStringValue("PAGE");

            XWPFRun endRun = footerParagraph.createRun();
            endRun.getCTR().addNewFldChar().setFldCharType(STFldCharType.END);

            XWPFParagraph pageBreak = document.createParagraph();
            pageBreak.setPageBreak(true);

        } catch (Exception e) {
            throw new RuntimeException("Błąd generowania strony tytułowej!", e);
        }
    }
}