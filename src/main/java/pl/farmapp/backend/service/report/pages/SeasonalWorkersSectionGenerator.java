package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.Employee;
import pl.farmapp.backend.entity.WorkTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeasonalWorkersSectionGenerator {

    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    public int generate(
            XWPFDocument document,
            int seasonYear,
            List<Employee> employees,
            List<WorkTime> workTimes,
            int tableIndexStart
    ) {

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
        hRun.setText("6. Pracownicy sezonowi");

        // ===== MAPA PRACOWNIKÓW =====
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, e -> e));

        Map<Integer, List<WorkTime>> workByEmployee = workTimes.stream()
                .collect(Collectors.groupingBy(WorkTime::getEmployeeId));

        List<RowData> rows = new ArrayList<>();

        for (Map.Entry<Integer, List<WorkTime>> entry : workByEmployee.entrySet()) {

            Employee emp = employeeMap.get(entry.getKey());
            if (emp == null) continue;

            List<WorkTime> works = entry.getValue();

            String name = emp.getFirstName() + " " + emp.getLastName();

            BigDecimal totalEarn = works.stream()
                    .map(w -> w.getPaidAmount() != null ? w.getPaidAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalHours = works.stream()
                    .map(w -> w.getHoursWorked() != null ? w.getHoursWorked() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long workDays = works.stream()
                    .map(WorkTime::getWorkDate)
                    .distinct()
                    .count();

            LocalDate minDate = works.stream()
                    .map(WorkTime::getWorkDate)
                    .min(LocalDate::compareTo)
                    .orElse(null);

            LocalDate maxDate = works.stream()
                    .map(WorkTime::getWorkDate)
                    .max(LocalDate::compareTo)
                    .orElse(null);

            rows.add(new RowData(name, totalHours, workDays, minDate, maxDate, totalEarn));
        }

        BigDecimal totalSeasonalCost = rows.stream()
                .map(r -> r.totalEarn)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ===== AKAPIT =====
        XWPFParagraph intro = document.createParagraph();
        intro.setSpacingBetween(1.5);
        intro.setFirstLineIndent(500); // ✅ wcięcie jak w sprzedaży
        intro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun run = intro.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(
                "Wydatki przedstawione w poprzednim rozdziale obejmowały głównie koszty pracy krótkoterminowej. " +
                        "W niniejszym rozdziale zaprezentowano pracowników sezonowych zatrudnianych na dłuższy okres. " +
                        "Łączny koszt wynagrodzeń wyniósł " +
                        df.format(totalSeasonalCost) + " zł. Szczegółowe zestawienie przedstawiono w tabeli poniżej."
        );

        // ===== PODPIS TABELI =====
        XWPFParagraph tableTitle = document.createParagraph();
        tableTitle.setSpacingBefore(200);
        tableTitle.setIndentationLeft(0); // ❗ brak wcięcia
        tableTitle.setFirstLineIndent(0);

        XWPFRun titleRun = tableTitle.createRun();
        titleRun.setItalic(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(12);
        titleRun.setText("Tabela " + tableIndexStart +
                ". Zestawienie pracowników sezonowych w sezonie " + seasonYear);

        // ===== TABELA =====
        int cols = 6;

        XWPFTable table = document.createTable(1, cols);
        setRepeatTableHeader(table.getRow(0));
        table.setWidth("100%");
        table.setTableAlignment(TableRowAlign.CENTER);

        // ❗ wymuszenie braku przesunięcia tabeli
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        if (tblPr == null) tblPr = table.getCTTbl().addNewTblPr();

        CTTblWidth tblInd = tblPr.addNewTblInd();
        tblInd.setW(BigInteger.ZERO);
        tblInd.setType(STTblWidth.DXA);

        int[] widths = {400, 2500, 1800, 1500, 2500, 1500};

        String[] headers = {
                "Lp",
                "Pracownik",
                "Czas pracy [h]",
                "Dni pracy",
                "Okres zatrudnienia",
                "Zarobek"
        };

        XWPFTableRow header = table.getRow(0);

        for (int i = 0; i < cols; i++) {

            XWPFTableCell cell = header.getCell(i);
            cell.setColor("5fab18");
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            CTTcPr tcPr = cell.getCTTc().addNewTcPr();

            CTTcMar mar = tcPr.addNewTcMar();
            mar.addNewTop().setW(BigInteger.valueOf(120));
            mar.addNewBottom().setW(BigInteger.valueOf(120));

            CTTblWidth width = tcPr.addNewTcW();
            width.setType(STTblWidth.DXA);
            width.setW(BigInteger.valueOf(widths[i]));

            XWPFParagraph p = cell.getParagraphs().get(0);
            p.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun r = p.createRun();
            r.setBold(true);
            r.setFontFamily("Times New Roman");
            r.setFontSize(12);
            r.setColor("FFFFFF");
            r.setText(headers[i]);
        }

        // ===== DANE =====
        int lp = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (RowData r : rows) {

            XWPFTableRow row = table.createRow();

            String period = (r.minDate != null && r.maxDate != null)
                    ? formatter.format(r.minDate) + " - " + formatter.format(r.maxDate)
                    : "-";

            String[] values = {
                    String.valueOf(lp++),
                    r.name,
                    r.totalHours.stripTrailingZeros().toPlainString() + " h",
                    String.valueOf(r.workDays),
                    period,
                    df.format(r.totalEarn) + " zł"
            };

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = row.getCell(i);
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                XWPFParagraph p = cell.getParagraphs().get(0);
                p.setAlignment(ParagraphAlignment.CENTER);

                XWPFRun runCell = p.createRun();
                runCell.setFontFamily("Times New Roman");
                runCell.setFontSize(12);
                runCell.setText(values[i]);
            }
        }

        return tableIndexStart + 1;
    }

    private static class RowData {
        String name;
        BigDecimal totalHours;
        long workDays;
        LocalDate minDate;
        LocalDate maxDate;
        BigDecimal totalEarn;

        public RowData(String name, BigDecimal totalHours, long workDays,
                       LocalDate minDate, LocalDate maxDate, BigDecimal totalEarn) {
            this.name = name;
            this.totalHours = totalHours;
            this.workDays = workDays;
            this.minDate = minDate;
            this.maxDate = maxDate;
            this.totalEarn = totalEarn;
        }
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }
}