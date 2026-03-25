package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.sharedtypes.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.Fertilizer;
import pl.farmapp.backend.entity.Fertigation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FertigationSectionGenerator {

    private static final DecimalFormat df = new DecimalFormat("#,##0.##");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public int generate(
            XWPFDocument document,
            int seasonYear,
            List<Fertigation> fertigations,
            List<Fertilizer> fertilizers,
            Map<Integer, BigDecimal> priceMap,
            Double totalTunnels,
            int tableIndexStart
    ) {

        Map<Integer, Fertilizer> fertilizerMap = fertilizers.stream()
                .collect(Collectors.toMap(Fertilizer::getId, f -> f));

        List<Fertigation> sorted = fertigations.stream()
                .sorted(Comparator.comparing(Fertigation::getFertigationDate))
                .toList();

        // ===== NAGŁÓWEK =====
        XWPFParagraph heading = document.createParagraph();
        heading.setSpacingBefore(600);
        heading.setSpacingAfter(300);
        heading.setIndentationLeft(500);
        heading.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun hRun = heading.createRun();
        hRun.setFontFamily("Times New Roman");
        hRun.setFontSize(14);
        hRun.setBold(true);
        hRun.setText("8. Fertygacja");

        // ===== WSTĘP =====
        int total = sorted.size();

        LocalDate first = sorted.isEmpty() ? null : sorted.get(0).getFertigationDate();
        LocalDate last = sorted.isEmpty() ? null : sorted.get(sorted.size() - 1).getFertigationDate();

        XWPFParagraph intro = document.createParagraph();
        intro.setSpacingBetween(1.5);
        intro.setFirstLineIndent(500);
        intro.setAlignment(ParagraphAlignment.BOTH);
        intro.setSpacingAfter(200);

        XWPFRun run = intro.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(
                "W sezonie " + seasonYear + " przeprowadzono " + total + " zabiegów fertygacji. " +
                        (first != null ? "Pierwszy zabieg wykonano dnia " + dateFormatter.format(first) + ", " : "") +
                        (last != null ? "natomiast ostatni dnia " + dateFormatter.format(last) + ". " : "") +
                        "Szczegółowe zestawienie przedstawiono w tabeli poniżej."
        );

        int tableIndex = tableIndexStart;

        // ===== TABELA 1 =====
        XWPFParagraph tableTitle = document.createParagraph();
        tableTitle.setSpacingBefore(200);

        XWPFRun titleRun = tableTitle.createRun();
        titleRun.setItalic(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(12);
        titleRun.setText("Tabela " + tableIndex +
                ". Zabiegi fertygacji w sezonie " + seasonYear);

        String[] headers = {"Lp", "Data", "Nawóz", "Dawka", "Liczba tuneli"};

        XWPFTable table = document.createTable(1, headers.length);
        setRepeatTableHeader(table.getRow(0));
        table.setWidth("100%");

        for (int i = 0; i < headers.length; i++) {
            styleHeaderCell(table.getRow(0).getCell(i), headers[i]);
        }

        int lp = 1;

        for (Fertigation f : sorted) {

            XWPFTableRow row = table.createRow();
            Fertilizer fertilizer = fertilizerMap.get(f.getFertilizerId());

            String unit = (fertilizer != null && "Płynny".equalsIgnoreCase(fertilizer.getForm()))
                    ? " l/tunel" : " kg/tunel";

            List<String> values = List.of(
                    String.valueOf(lp++),
                    dateFormatter.format(f.getFertigationDate()),
                    fertilizer != null ? fertilizer.getName() : "-",
                    df.format(f.getDose()) + unit,
                    String.valueOf(f.getTunnelCount())
            );

            for (int i = 0; i < values.size(); i++) {
                styleDataCell(row.getCell(i), values.get(i));
            }
        }

        tableIndex++;

        // ===== AKAPIT =====
        XWPFParagraph summaryIntro = document.createParagraph();
        summaryIntro.setSpacingBetween(1.5);
        summaryIntro.setFirstLineIndent(500);
        summaryIntro.setAlignment(ParagraphAlignment.BOTH);
        summaryIntro.setSpacingBefore(300);

        XWPFRun summaryRun = summaryIntro.createRun();
        summaryRun.setFontFamily("Times New Roman");
        summaryRun.setFontSize(12);

        summaryRun.setText(
                "W trakcie sezonu stosowano różne nawozy, których zużycie było uzależnione od fazy rozwoju roślin. " +
                        "Poniżej przedstawiono zestawienie łącznego zużycia oraz kosztów dla poszczególnych nawozów."
        );

        // ===== AGREGACJA =====
        class Summary {
            String name;
            String form;
            BigDecimal totalDose = BigDecimal.ZERO;
            int uses = 0;
            BigDecimal totalCost = BigDecimal.ZERO;
        }

        Map<Integer, Summary> map = new HashMap<>();

        for (Fertigation f : sorted) {

            Fertilizer fert = fertilizerMap.get(f.getFertilizerId());
            if (fert == null) continue;

            Summary s = map.computeIfAbsent(fert.getId(), k -> {
                Summary x = new Summary();
                x.name = fert.getName();
                x.form = fert.getForm();
                return x;
            });

            // 🔥 POPRAWKA KLUCZOWA
            BigDecimal dose = f.getDose();
            BigDecimal tunnels = BigDecimal.valueOf(
                    f.getTunnelCount() != null ? f.getTunnelCount() : 0
            );

            BigDecimal realUsage = dose.multiply(tunnels);

            s.totalDose = s.totalDose.add(realUsage);
            s.uses++;
        }

        // ===== KOSZTY =====
        for (Map.Entry<Integer, Summary> e : map.entrySet()) {

            BigDecimal price = priceMap.get(e.getKey());
            if (price == null) continue;

            e.getValue().totalCost = e.getValue().totalDose.multiply(price);
        }

        List<Summary> summaries = map.values().stream()
                .sorted(Comparator.comparing((Summary s) -> s.totalDose).reversed())
                .toList();

        // ===== TABELA 2 =====
        XWPFParagraph tableTitle2 = document.createParagraph();
        tableTitle2.setSpacingBefore(200);

        XWPFRun titleRun2 = tableTitle2.createRun();
        titleRun2.setItalic(true);
        titleRun2.setFontFamily("Times New Roman");
        titleRun2.setFontSize(12);
        titleRun2.setText("Tabela " + tableIndex +
                ". Zużycie nawozów w sezonie " + seasonYear);

        String[] headers2 = {
                "Lp", "Nawóz", "Zużycie", "Zużycie / tunel",
                "Koszt", "Koszt / tunel", "Użycia"
        };

        XWPFTable table2 = document.createTable(1, headers2.length);
        setRepeatTableHeader(table2.getRow(0));
        table2.setWidth("100%");

        for (int i = 0; i < headers2.length; i++) {
            styleHeaderCell(table2.getRow(0).getCell(i), headers2[i]);
        }

        int lp2 = 1;

        // 🔥 KLUCZOWA POPRAWKA
        double tunnelCount = (totalTunnels != null && totalTunnels > 0)
                ? totalTunnels
                : 1;

        for (Summary s : summaries) {

            boolean liquid = "Płynny".equalsIgnoreCase(s.form);
            String unit = liquid ? " l" : " kg";

            BigDecimal perTunnel = s.totalDose.divide(
                    BigDecimal.valueOf(tunnelCount),
                    2,
                    RoundingMode.HALF_UP
            );

            BigDecimal costPerTunnel = s.totalCost.divide(
                    BigDecimal.valueOf(tunnelCount),
                    2,
                    RoundingMode.HALF_UP
            );
            BigDecimal usageRounded = s.totalDose.setScale(0, RoundingMode.HALF_UP);
            BigDecimal usagePerTunnelRounded = perTunnel.setScale(1, RoundingMode.HALF_UP);
            BigDecimal costRounded = s.totalCost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal costPerTunnelRounded = costPerTunnel.setScale(1, RoundingMode.HALF_UP);

            List<String> values = List.of(
                    String.valueOf(lp2++),
                    s.name,
                    usageRounded + unit,
                    usagePerTunnelRounded + unit,
                    costRounded + " zł",
                    costPerTunnelRounded + " zł",
                    String.valueOf(s.uses)
            );

            XWPFTableRow row = table2.createRow();

            for (int i = 0; i < values.size(); i++) {
                styleDataCell(row.getCell(i), values.get(i));
            }
        }

        return tableIndex + 1;
    }

    private void styleHeaderCell(XWPFTableCell cell, String text) {

        cell.setColor("5fab18");
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        CTTcMar mar = cell.getCTTc().addNewTcPr().addNewTcMar();
        mar.addNewTop().setW(BigInteger.valueOf(150));
        mar.addNewBottom().setW(BigInteger.valueOf(150));

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setFontFamily("Times New Roman");
        r.setFontSize(12);
        r.setColor("FFFFFF");
        r.setText(text);
    }

    private void styleDataCell(XWPFTableCell cell, String text) {

        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        CTTcMar mar = cell.getCTTc().addNewTcPr().addNewTcMar();
        mar.addNewTop().setW(BigInteger.valueOf(150));
        mar.addNewBottom().setW(BigInteger.valueOf(150));

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun r = p.createRun();
        r.setFontFamily("Times New Roman");
        r.setFontSize(12);
        r.setText(text);
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }
}