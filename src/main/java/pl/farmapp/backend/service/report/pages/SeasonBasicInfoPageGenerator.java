package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.CultivationCalendar;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class SeasonBasicInfoPageGenerator {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void generate(
            XWPFDocument document,
            CultivationCalendar calendar,
            Double tunnelCountCurrent,
            Double tunnelCountPrevious
    ) {

        document.createParagraph().setPageBreak(true);

        LocalDate prickingStart = calendar.getPrickingStartDate();
        LocalDate prickingEnd = calendar.getPrickingEndDate();
        LocalDate plantingStart = calendar.getPlantingStartDate();
        LocalDate plantingEnd = calendar.getPlantingEndDate();
        LocalDate harvestStart = calendar.getHarvestStartDate();
        LocalDate harvestEnd = calendar.getHarvestEndDate();

        long prickingDays = ChronoUnit.DAYS.between(prickingStart, prickingEnd);
        long seedlingDays = ChronoUnit.DAYS.between(prickingEnd, plantingStart);
        long plantingDays = ChronoUnit.DAYS.between(plantingStart, plantingEnd);
        long tunnelDays = ChronoUnit.DAYS.between(plantingEnd, harvestStart);
        long harvestDays = ChronoUnit.DAYS.between(harvestStart, harvestEnd);

        // ===== NAGŁÓWEK =====
        XWPFParagraph heading = document.createParagraph();
        heading.setSpacingAfter(300);
        heading.setIndentationLeft(500);
        heading.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun hRun = heading.createRun();
        hRun.setFontFamily("Times New Roman");
        hRun.setFontSize(14);
        hRun.setBold(true);
        hRun.setText("1. Podstawowe informacje o sezonie");

        // ===== PARAGRAF =====
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBetween(1.5);
        paragraph.setFirstLineIndent(500);
        paragraph.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        StringBuilder text = new StringBuilder();

        String tunnelCurrent = tunnelCountCurrent.toString().replace(".", ",");
        String diffText = null;

        if (tunnelCountPrevious != null) {

            double diff = tunnelCountCurrent - tunnelCountPrevious;
            diffText = String.valueOf(diff).replace(".", ",");

            if (diff > 0) {
                text.append("W sezonie ")
                        .append(calendar.getSeasonYear())
                        .append(" produkcja papryki prowadzona była w ")
                        .append(tunnelCurrent)
                        .append(" tunelach foliowych. W porównaniu z poprzednim sezonem liczba tuneli zwiększyła się o ")
                        .append(diffText)
                        .append(". ");
            } else if (diff < 0) {
                text.append("W sezonie ")
                        .append(calendar.getSeasonYear())
                        .append(" produkcja papryki prowadzona była w ")
                        .append(tunnelCurrent)
                        .append(" tunelach foliowych. W porównaniu z poprzednim sezonem liczba tuneli zmniejszyła się o ")
                        .append(diffText.replace("-", ""))
                        .append(". ");
            } else {
                text.append("W sezonie ")
                        .append(calendar.getSeasonYear())
                        .append(" produkcja papryki prowadzona była w ")
                        .append(tunnelCurrent)
                        .append(" tunelach foliowych, czyli w takiej samej liczbie jak w poprzednim sezonie. ");
            }

        } else {

            text.append("W sezonie ")
                    .append(calendar.getSeasonYear())
                    .append(" produkcja papryki prowadzona była w ")
                    .append(tunnelCurrent)
                    .append(" tunelach foliowych. ");

        }

        text.append("Sezon produkcji papryki rozpoczął się dnia ")
                .append(prickingStart.format(FORMAT))
                .append(". Pierwszym etapem sezonu było pikowanie rozsady papryki. Proces rozpoczął się ")
                .append(prickingStart.format(FORMAT))
                .append(" i trwał ")
                .append(prickingDays)
                .append(" dni, do dnia ")
                .append(prickingEnd.format(FORMAT))
                .append(". Po zakończeniu pikowania rośliny pozostawały w pikówce przez ")
                .append(seedlingDays)
                .append(" dni. Sadzenie roślin do tuneli rozpoczęto dnia ")
                .append(plantingStart.format(FORMAT))
                .append(" i zakończono dnia ")
                .append(plantingEnd.format(FORMAT))
                .append(". Proces sadzenia trwał ")
                .append(plantingDays)
                .append(" dni. Pierwsze zbiory papryki rozpoczęły się dnia ")
                .append(harvestStart.format(FORMAT))
                .append(", czyli po ")
                .append(tunnelDays)
                .append(" dniach od zakończenia sadzenia. Okres zbiorów trwał ")
                .append(harvestDays)
                .append(" dni i zakończył się dnia ")
                .append(harvestEnd.format(FORMAT))
                .append(". Tym samym zakończono sezon produkcyjny. Zestawienie podstawowych dat dotyczących sezonu ")
                .append(calendar.getSeasonYear())
                .append(" przedstawiono w tabeli 1.");

        run.setText(text.toString());

        document.createParagraph();

        // ===== PODPIS TABELI =====
        XWPFParagraph caption = document.createParagraph();

        XWPFRun capRun = caption.createRun();
        capRun.setFontFamily("Times New Roman");
        capRun.setFontSize(12);
        capRun.setItalic(true);
        capRun.setText("Tabela 1. Podstawowe daty sezonu");

        // ===== TABELA =====
        XWPFTable table = document.createTable(4, 3);
        setRepeatTableHeader(table.getRow(0));
        table.setWidth("100%");

        setHeader(table.getRow(0).getCell(0), "Etap");
        setHeader(table.getRow(0).getCell(1), "Data rozpoczęcia");
        setHeader(table.getRow(0).getCell(2), "Data zakończenia");

        setCell(table.getRow(1).getCell(0), "Pikowanie");
        setCell(table.getRow(1).getCell(1), prickingStart.format(FORMAT));
        setCell(table.getRow(1).getCell(2), prickingEnd.format(FORMAT));

        setCell(table.getRow(2).getCell(0), "Sadzenie");
        setCell(table.getRow(2).getCell(1), plantingStart.format(FORMAT));
        setCell(table.getRow(2).getCell(2), plantingEnd.format(FORMAT));

        setCell(table.getRow(3).getCell(0), "Zbiory");
        setCell(table.getRow(3).getCell(1), harvestStart.format(FORMAT));
        setCell(table.getRow(3).getCell(2), harvestEnd.format(FORMAT));
    }


    private void setHeader(XWPFTableCell cell, String text) {

        cell.setColor("5fab18");

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setSpacingBefore(120);
        p.setSpacingAfter(120);
        p.setIndentationLeft(200);
        p.setIndentationRight(200);

        XWPFRun run = p.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setColor("FFFFFF");
        run.setText(text);
    }


    private void setCell(XWPFTableCell cell, String text) {

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setSpacingBefore(120);
        p.setSpacingAfter(120);
        p.setIndentationLeft(200);
        p.setIndentationRight(200);

        XWPFRun run = p.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(text);
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }
}