package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.CultivationCalendar;
import pl.farmapp.backend.entity.Harvest;
import pl.farmapp.backend.entity.VarietySeason;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class HarvestSummaryPageGenerator {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void generate(
            XWPFDocument document,
            CultivationCalendar calendar,
            List<Harvest> harvests,
            List<VarietySeason> varieties,
            Double boxWeightKg
    ) {

        // ===== MAPA odmian =====
        Map<Integer,String> varietyMap = new HashMap<>();

        for (VarietySeason v : varieties) {
            varietyMap.put(v.getId(), v.getName());
        }

        // ===== MAPA miesięcy =====
        Map<Integer,String> monthNames = Map.of(
                7,"Lipiec",
                8,"Sierpień",
                9,"Wrzesień",
                10,"Październik",
                11,"Listopad"
        );

        List<Integer> months = List.of(7,8,9,10,11);

        Map<Integer, Map<String,Double>> monthly = new HashMap<>();

        for (Integer m : months)
            monthly.put(m,new HashMap<>());

        Map<String,Double> totals = new HashMap<>();

        // ===== LICZENIE ZBIORÓW =====
        for (Harvest h : harvests) {

            int month = h.getHarvestDate().getMonthValue();

            if(!months.contains(month))
                continue;

            String name = varietyMap.get(h.getVarietySeasonId());

            if(name == null)
                continue;

            double boxes = h.getBoxCount().doubleValue();

            Map<String,Double> map = monthly.get(month);

            map.put(name,map.getOrDefault(name,0.0)+boxes);

            totals.put(name,totals.getOrDefault(name,0.0)+boxes);
        }

        // ===== NAGŁÓWEK =====
        XWPFParagraph heading = document.createParagraph();
        heading.setStyle("Heading1");
        heading.setIndentationLeft(500);
        heading.setSpacingAfter(300);
        heading.setSpacingBefore(600);
        heading.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun hRun = heading.createRun();
        hRun.setFontFamily("Times New Roman");
        hRun.setFontSize(14);
        hRun.setBold(true);
        hRun.setText("3. Zbiory");

        // ===== OPIS SEZONU =====
        long days = ChronoUnit.DAYS.between(
                calendar.getHarvestStartDate(),
                calendar.getHarvestEndDate()
        );

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBetween(1.5);
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setFirstLineIndent(500);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(
                "Zbiory papryki w sezonie "
                        + calendar.getSeasonYear()
                        + " rozpoczęły się dnia "
                        + calendar.getHarvestStartDate().format(FORMAT)
                        + " i trwały do dnia "
                        + calendar.getHarvestEndDate().format(FORMAT)
                        + ". Okres zbiorów obejmował "
                        + days
                        + " dni. W analizowanym sezonie prowadzono zbiory "
                        + varieties.size()
                        + " odmian papryki. Szczegółowe zestawienie liczby zebranych skrzyń "
                        + "w poszczególnych miesiącach przedstawiono w tabeli 3."
        );


        // ===== TABELA 3 =====
        XWPFParagraph caption = document.createParagraph();

        XWPFRun cap = caption.createRun();
        cap.setFontFamily("Times New Roman");
        cap.setFontSize(12);
        cap.setItalic(true);
        cap.setText("Tabela 3. Liczba zebranych skrzyń w poszczególnych miesiącach");

        int cols = varieties.size()+2;

        XWPFTable table = document.createTable(months.size()+2,cols);
        setRepeatTableHeader(table.getRow(0));
        table.setWidth("100%");

        setHeader(table.getRow(0).getCell(0),"Miesiąc");

        int col = 1;

        for (VarietySeason v : varieties)
            setHeader(table.getRow(0).getCell(col++),v.getName());

        setHeader(table.getRow(0).getCell(col),"Suma");

        int rowIndex = 1;
        Map<String,Double> columnTotals = new HashMap<>();

        for (VarietySeason v : varieties)
            columnTotals.put(v.getName(),0.0);

        for(Integer m : months){

            XWPFTableRow row = table.getRow(rowIndex++);

            setCell(row.getCell(0),monthNames.get(m));

            double sum = 0;

            int c = 1;

            for(VarietySeason v : varieties){

                double val = monthly.get(m).getOrDefault(v.getName(),0.0);
                columnTotals.put(
                        v.getName(),
                        columnTotals.get(v.getName()) + val
                );

                setCell(row.getCell(c++),format(val));

                sum += val;
            }

            setCell(row.getCell(c),format(sum));
        }

        XWPFTableRow sumRow = table.getRow(months.size()+1);

        setCell(sumRow.getCell(0),"Suma");

        double grandTotal = 0;

        int c = 1;

        for (VarietySeason v : varieties){

            double val = columnTotals.get(v.getName());

            setCell(sumRow.getCell(c++), format(val));

            grandTotal += val;
        }

        setCell(sumRow.getCell(c), format(grandTotal));

        document.createParagraph();

        // ===== OPIS ZBIORÓW =====
        double totalBoxes = totals.values().stream().mapToDouble(Double::doubleValue).sum();

        double maxBoxes = 0;
        String maxVariety = "";

        for(var entry : totals.entrySet()){
            if(entry.getValue()>maxBoxes){
                maxBoxes = entry.getValue();
                maxVariety = entry.getKey();
            }
        }

        XWPFParagraph text2 = document.createParagraph();
        text2.setSpacingBetween(1.5);
        text2.setAlignment(ParagraphAlignment.BOTH);
        text2.setFirstLineIndent(500);

        XWPFRun run2 = text2.createRun();
        run2.setFontFamily("Times New Roman");
        run2.setFontSize(12);

        run2.setText(
                "Łącznie w analizowanym sezonie zebrano "
                        + format(totalBoxes)
                        + " skrzyń papryki. Największy udział w zbiorach miała odmiana "
                        + maxVariety
                        + ", z której zebrano "
                        + format(maxBoxes)
                        + " skrzyń. Pozostałe odmiany stanowiły łącznie "
                        + format(totalBoxes-maxBoxes)
                        + " skrzyń produkcji."
        );


        // ===== INFORMACJA O WADZE SKRZYNI =====
        XWPFParagraph weightInfo = document.createParagraph();
        weightInfo.setSpacingBetween(1.5);
        weightInfo.setAlignment(ParagraphAlignment.BOTH);
        weightInfo.setFirstLineIndent(500);

        XWPFRun run3 = weightInfo.createRun();
        run3.setFontFamily("Times New Roman");
        run3.setFontSize(12);

        run3.setText(
                "Produkcję papryki w kilogramach obliczono na podstawie liczby zebranych skrzyń. "
                        + "W systemie zarządzania gospodarstwem użytkownik określił masę jednej skrzyni "
                        + "na poziomie "
                        + format(boxWeightKg)
                        + " kg. Przyjęta wartość została wykorzystana do przeliczenia liczby skrzyń "
                        + "na całkowitą masę zebranej papryki."
        );


        // ===== TABELA 4 =====
        XWPFParagraph caption2 = document.createParagraph();

        XWPFRun cap2 = caption2.createRun();
        cap2.setFontFamily("Times New Roman");
        cap2.setFontSize(12);
        cap2.setItalic(true);
        cap2.setText("Tabela 4. Produkcja papryki w kilogramach");

        XWPFTable table2 = document.createTable(varieties.size()+1,4);
        setRepeatTableHeader(table2.getRow(0));
        table2.setWidth("100%");

        setHeader(table2.getRow(0).getCell(0),"Odmiana");
        setHeader(table2.getRow(0).getCell(1),"Liczba skrzyń");
        setHeader(table2.getRow(0).getCell(2),"Produkcja [kg]");
        setHeader(table2.getRow(0).getCell(3),"Średnia z tunelu [kg]");

        int r = 1;

        for(VarietySeason v : varieties){

            double boxes = totals.getOrDefault(v.getName(),0.0);

            double kg = boxes * boxWeightKg;

            double perTunnel = Math.round(kg / v.getTunnelCount().doubleValue());

            XWPFTableRow row = table2.getRow(r++);

            setCell(row.getCell(0),v.getName());
            setCell(row.getCell(1),format(boxes));
            setCell(row.getCell(2),format(kg));
            setCell(row.getCell(3),format(perTunnel));
        }
    }

    private void setHeader(XWPFTableCell cell,String text){

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

    private void setCell(XWPFTableCell cell,String text){

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

    private String format(double value){

        return String.format("%,.0f", value)
                .replace(",", " ")
                .replace(".", ",");
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }
}