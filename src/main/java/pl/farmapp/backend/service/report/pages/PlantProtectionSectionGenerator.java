package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.Pesticide;
import pl.farmapp.backend.entity.PesticideType;
import pl.farmapp.backend.entity.Treatment;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PlantProtectionSectionGenerator {

    private static final DecimalFormat df = new DecimalFormat("#,##0.##");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public int generate(
            XWPFDocument document,
            int seasonYear,
            List<Treatment> treatments,
            List<Pesticide> pesticides,
            List<PesticideType> pesticideTypes,
            int tableIndexStart
    ) {

        Map<Integer, Pesticide> pesticideMap = pesticides.stream()
                .collect(Collectors.toMap(Pesticide::getId, p -> p));

        Map<Integer, PesticideType> typeMap = pesticideTypes.stream()
                .collect(Collectors.toMap(PesticideType::getId, t -> t));

        List<Treatment> sorted = treatments.stream()
                .sorted(Comparator.comparing(t -> LocalDateTime.of(t.getTreatmentDate(), t.getTreatmentTime())))
                .toList();

        Map<String, List<Treatment>> byType = new HashMap<>();

        for (Treatment t : sorted) {
            Pesticide p = pesticideMap.get(t.getPesticideId());
            if (p == null) continue;

            PesticideType type = typeMap.get(p.getPesticideTypeId());
            if (type == null) continue;

            byType.computeIfAbsent(type.getName(), k -> new ArrayList<>()).add(t);
        }

        List<String> orderedTypes = new ArrayList<>(List.of(
                "Insektycyd", "Fungicyd", "Herbicyd", "Odżywka", "Stymulator"
        ));

        for (String t : byType.keySet()) {
            if (!orderedTypes.contains(t)) orderedTypes.add(t);
        }

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
        hRun.setText("7. Zabiegi ochrony roślin");

        // ===== WSTĘP =====
        int totalTreatments = sorted.size();

        String typeSummary = orderedTypes.stream()
                .filter(byType::containsKey)
                .map(t -> t.toLowerCase() + " – " + byType.get(t).size())
                .collect(Collectors.joining(", "));

        LocalDateTime first = sorted.isEmpty() ? null :
                LocalDateTime.of(sorted.get(0).getTreatmentDate(), sorted.get(0).getTreatmentTime());

        LocalDateTime last = sorted.isEmpty() ? null :
                LocalDateTime.of(
                        sorted.get(sorted.size() - 1).getTreatmentDate(),
                        sorted.get(sorted.size() - 1).getTreatmentTime()
                );

        XWPFParagraph intro = document.createParagraph();
        intro.setSpacingBetween(1.5);
        intro.setFirstLineIndent(500);
        intro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun introRun = intro.createRun();
        introRun.setFontFamily("Times New Roman");
        introRun.setFontSize(12);

        introRun.setText(
                "W sezonie " + seasonYear + " wykonano łącznie " + totalTreatments + " zabiegów ochrony roślin. " +
                        (typeSummary.isEmpty() ? "" :
                                "Zastosowano środki należące do następujących grup: " + typeSummary + ". ") +
                        (first != null ? "Pierwszy zabieg wykonano dnia " +
                                first.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + ", " : "") +
                        (last != null ? "natomiast ostatni dnia " +
                                last.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "." : "")
        );

        int tableIndex = tableIndexStart;

        for (String typeName : orderedTypes) {

            List<Treatment> list = byType.get(typeName);
            if (list == null || list.isEmpty()) continue;

            boolean isHerbicide = typeName.equalsIgnoreCase("Herbicyd");

            // ===== TYTUŁ =====
            XWPFParagraph tableTitle = document.createParagraph();
            tableTitle.setSpacingBefore(200);

            XWPFRun titleRun = tableTitle.createRun();
            titleRun.setItalic(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(12);
            titleRun.setText("Tabela " + tableIndex +
                    ". Zabiegi typu " + typeName.toLowerCase() + " w sezonie " + seasonYear);

            // ===== HEADERS =====
            List<String> headers = new ArrayList<>(List.of(
                    "Lp",
                    "Data i godzina",
                    "Środek",
                    "Dawka",
                    "Ilość cieczy"
            ));

           headers.add("Liczba tuneli");

            headers.add("Karencja");

            XWPFTable table = document.createTable(1, headers.size());
            setRepeatTableHeader(table.getRow(0));
            table.setWidth("100%");

            XWPFTableRow headerRow = table.getRow(0);

            for (int i = 0; i < headers.size(); i++) {
                XWPFTableCell cell = headerRow.getCell(i);
                styleHeaderCell(cell, headers.get(i));
            }

            // ===== DANE =====
            int lp = 1;

            for (Treatment t : list) {

                XWPFTableRow row = table.createRow();

                Pesticide p = pesticideMap.get(t.getPesticideId());

                LocalDateTime dt = LocalDateTime.of(t.getTreatmentDate(), t.getTreatmentTime());

                List<String> values = new ArrayList<>();

                // LP
                values.add(String.valueOf(lp++));

                // DATA + GODZINA (2 LINIE)
                values.add(dateFormatter.format(dt) + ", " + timeFormatter.format(dt));
                // ŚRODEK
                values.add(p != null ? p.getName() : "-");

                // DAWKA
                String doseUnit = (p != null && Boolean.TRUE.equals(p.getIsLiquid())) ? " ml" : " g";
                values.add(df.format(t.getPesticideDose()) + doseUnit);

                // CIECZ
                values.add(df.format(t.getLiquidVolume()) + " l");

                if (isHerbicide) {
                    values.add("-");
                } else {
                    values.add(t.getTunnelCount() != null ? String.valueOf(t.getTunnelCount()) : "-");
                }

                // KARENCJA
                values.add(p != null ? p.getCarenceDays() + " dni" : "-");

                for (int i = 0; i < values.size(); i++) {
                    XWPFTableCell cell = row.getCell(i);
                    styleDataCell(cell, values.get(i));
                }
            }

            tableIndex++;
        }

        return tableIndex;
    }

    // ===== STYL HEADER =====
    private void styleHeaderCell(XWPFTableCell cell, String text) {

        cell.setColor("5fab18");
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        CTTcPr tcPr = cell.getCTTc().addNewTcPr();

        CTTcMar mar = tcPr.addNewTcMar();
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

    // ===== STYL DANYCH =====
    private void styleDataCell(XWPFTableCell cell, String text) {

        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

        CTTcPr tcPr = cell.getCTTc().addNewTcPr();

        CTTcMar mar = tcPr.addNewTcMar();
        mar.addNewTop().setW(BigInteger.valueOf(100));
        mar.addNewBottom().setW(BigInteger.valueOf(100));

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(ParagraphAlignment.CENTER);

        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            XWPFRun r = p.createRun();
            r.setFontFamily("Times New Roman");
            r.setFontSize(12);
            r.setText(lines[i]);

            if (i < lines.length - 1) {
                r.addBreak();
            }
        }
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }
}