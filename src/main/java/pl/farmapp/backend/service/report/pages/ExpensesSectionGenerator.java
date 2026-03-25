package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.farmapp.backend.entity.Expense;
import pl.farmapp.backend.entity.ExpenseCategory;
import pl.farmapp.backend.entity.Employee;
import pl.farmapp.backend.entity.WorkTime;

@Component
public class ExpensesSectionGenerator {

    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    public int generate(
            XWPFDocument document,
            int seasonYear,
            List<Expense> expenses,
            List<ExpenseCategory> categories,
            List<Employee> employees,
            List<WorkTime> workTimes,
            int tableIndexStart
    ) {

        // ===== NAGŁÓWEK SEKCJI =====
        XWPFParagraph heading = document.createParagraph();
        heading.setSpacingAfter(300);
        heading.setSpacingBefore(600);
        heading.setIndentationLeft(500);
        heading.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun hRun = heading.createRun();
        hRun.setFontFamily("Times New Roman");
        hRun.setFontSize(14);
        hRun.setBold(true);
        hRun.setText("5. Wydatki");

        // ===== MAPOWANIE KATEGORII =====
        Map<Integer, ExpenseCategory> categoryMap = categories.stream()
                .collect(Collectors.toMap(ExpenseCategory::getId, c -> c));

        // ===== WYDATKI NA KOSZTY PRODUKCJI =====
        List<Expense> productionExpenses = expenses.stream()
                .filter(e -> {
                    ExpenseCategory cat = categoryMap.get(e.getExpenseCategoryId());
                    return cat != null && Boolean.TRUE.equals(cat.getProductionCost());
                })
                .toList();

        // ===== POZOSTAŁE WYDATKI =====
        List<Expense> otherExpenses = expenses.stream()
                .filter(e -> {
                    ExpenseCategory cat = categoryMap.get(e.getExpenseCategoryId());
                    return cat == null || !Boolean.TRUE.equals(cat.getProductionCost());
                })
                .toList();

        // ===== KOSZT PRACOWNIKÓW =====
        BigDecimal seasonalWorkersCost = workTimes.stream()
                .map(w -> w.getPaidAmount() != null ? w.getPaidAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ===== OBLICZENIE SUM WYDATKÓW =====
        BigDecimal totalProduction = productionExpenses.stream()
                .map(e -> e.getPrice().multiply(e.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(seasonalWorkersCost);

        BigDecimal totalOther = otherExpenses.stream()
                .map(e -> e.getPrice().multiply(e.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ===== AKAPIT WPROWADZAJĄCY =====
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBetween(1.5);
        paragraph.setFirstLineIndent(500);
        paragraph.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        String introText = String.format(
                "W sezonie %d zarejestrowano wydatki w gospodarstwie, które można przeanalizować w dwóch kategoriach. " +
                        "Są to koszty produkcji, obejmujące wszystkie wydatki związane bezpośrednio z uprawą papryki, które wyniosły %s zł. " +
                        "oraz pozostałe wydatki, odpowiedzialne za funkcjonowanie gospodarstwa, na które przeznaczono %s zł.",
                seasonYear,
                df.format(totalProduction),
                df.format(totalOther)
        );

        run.setText(introText);

        // ===== PODSUMOWANIE KOSZTÓW PRODUKCJI =====
        Map<String, BigDecimal> productionByCategory = productionExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> categoryMap.get(e.getExpenseCategoryId()).getName(),
                        Collectors.mapping(
                                e -> e.getPrice().multiply(e.getQuantity()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        if (!productionByCategory.isEmpty() || seasonalWorkersCost.compareTo(BigDecimal.ZERO) > 0) {

            XWPFParagraph prodTitle = document.createParagraph();
            prodTitle.setSpacingBetween(1.5);

            XWPFRun runProdTitle = prodTitle.createRun();
            runProdTitle.setFontFamily("Times New Roman");
            runProdTitle.setFontSize(12);
            runProdTitle.setBold(true);
            runProdTitle.setText("Koszty produkcji:");

            // Kategorie użytkownika
            productionByCategory.forEach((cat, total) -> {
                XWPFParagraph bullet = document.createParagraph();
                bullet.setSpacingBetween(1.5);
                bullet.setIndentationLeft(500);
                bullet.setNumID(createBulletList(document));

                XWPFRun bRun = bullet.createRun();
                bRun.setFontFamily("Times New Roman");
                bRun.setFontSize(12);
                bRun.setText(cat + " – " + df.format(total) + " zł");
            });

            // ===== PRACOWNICY SEZONOWI (ZAWSZE NA KOŃCU) =====
            if (seasonalWorkersCost.compareTo(BigDecimal.ZERO) > 0) {
                XWPFParagraph bullet = document.createParagraph();
                bullet.setSpacingBetween(1.5);
                bullet.setIndentationLeft(500);
                bullet.setNumID(createBulletList(document));

                XWPFRun bRun = bullet.createRun();
                bRun.setFontFamily("Times New Roman");
                bRun.setFontSize(12);
                bRun.setText("Pracownicy sezonowi – " + df.format(seasonalWorkersCost) + " zł");
            }
        }

        // ===== POZOSTAŁE WYDATKI =====
        Map<String, BigDecimal> otherByCategory = otherExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> {
                            ExpenseCategory cat = categoryMap.get(e.getExpenseCategoryId());
                            return cat != null ? cat.getName() : "Pozostałe";
                        },
                        Collectors.mapping(
                                e -> e.getPrice().multiply(e.getQuantity()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        if (!otherByCategory.isEmpty()) {
            XWPFParagraph otherTitle = document.createParagraph();
            otherTitle.setSpacingBetween(1.5);

            XWPFRun runOtherTitle = otherTitle.createRun();
            runOtherTitle.setFontFamily("Times New Roman");
            runOtherTitle.setFontSize(12);
            runOtherTitle.setBold(true);
            runOtherTitle.setText("Pozostałe wydatki:");

            otherByCategory.forEach((cat, total) -> {
                XWPFParagraph bullet = document.createParagraph();
                bullet.setSpacingBetween(1.5);
                bullet.setIndentationLeft(500);
                bullet.setNumID(createBulletList(document));

                XWPFRun bRun = bullet.createRun();
                bRun.setFontFamily("Times New Roman");
                bRun.setFontSize(12);
                bRun.setText(cat + " – łączny koszt: " + df.format(total) + " zł");
            });
        }

        // ===== SADZONKA =====

// znajdź kategorię "Sadzonka"
        List<Expense> seedlingExpenses = productionExpenses.stream()
                .filter(e -> {
                    ExpenseCategory cat = categoryMap.get(e.getExpenseCategoryId());
                    return cat != null && cat.getName().equalsIgnoreCase("Sadzonka");
                })
                .sorted((a, b) -> a.getExpenseDate().compareTo(b.getExpenseDate()))
                .toList();

        if (!seedlingExpenses.isEmpty()) {

            BigDecimal totalSeedlings = seedlingExpenses.stream()
                    .map(e -> e.getPrice().multiply(e.getQuantity()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ===== AKAPIT =====
            XWPFParagraph introSeedlings = document.createParagraph();
            introSeedlings.setSpacingBefore(200);
            introSeedlings.setSpacingBetween(1.5);
            introSeedlings.setFirstLineIndent(500);
            introSeedlings.setAlignment(ParagraphAlignment.BOTH);

            XWPFRun runSeedlings = introSeedlings.createRun();
            runSeedlings.setFontFamily("Times New Roman");
            runSeedlings.setFontSize(12);

            runSeedlings.setText(
                    "Jednym z podstawowych kosztów produkcji papryki jest zakup rozsady, która " +
                            "stanowi punkt wyjścia całego procesu uprawy. W analizowanym sezonie łączny koszt " +
                            "zakupu rozsady wyniósł " + df.format(totalSeedlings) + " zł. " +
                            "Szczegółowe zestawienie przedstawiono w tabeli 7."
            );

            // ===== TYTUŁ TABELI =====
            XWPFParagraph tableTitle = document.createParagraph();
            tableTitle.setSpacingBefore(200);

            XWPFRun titleRun = tableTitle.createRun();
            titleRun.setItalic(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(12);
            titleRun.setText("Tabela 7. Koszty zakupu rozsady papryki w sezonie " + seasonYear);

            // ===== TABELA =====

            int cols = 6;

            XWPFTable table = document.createTable(1, cols);
            setRepeatTableHeader(table.getRow(0));
            table.setWidth("100%");
            table.setTableAlignment(TableRowAlign.CENTER);


            int[] widths = {
                    800,   // Lp
                    1400,  // Data
                    2500,  // Nazwa
                    1500,  // Ilość
                    1500,  // Cena
                    1800   // Suma
            };

            String[] headers = {
                    "Lp",
                    "Data",
                    "Nazwa",
                    "Ilość",
                    "Cena",
                    "Suma"
            };

            XWPFTableRow header = table.getRow(0);
            header.setRepeatHeader(true);

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = header.getCell(i);

                cell.setColor("5fab18");
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                CTTblWidth width = tcPr.addNewTcW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(widths[i]));

                CTTcBorders borders = tcPr.addNewTcBorders();
                borders.addNewTop().setVal(STBorder.NONE);
                borders.addNewBottom().setVal(STBorder.NONE);
                borders.addNewLeft().setVal(STBorder.NONE);
                borders.addNewRight().setVal(STBorder.NONE);

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
                r.setText(headers[i]);
            }

            // ===== DANE =====

            int lp = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (Expense e : seedlingExpenses) {

                XWPFTableRow row = table.createRow();

                BigDecimal total = e.getPrice().multiply(e.getQuantity());

                String[] values = {
                        String.valueOf(lp++),
                        e.getExpenseDate().format(formatter),
                        e.getTitle(),
                        String.format("%,.0f", e.getQuantity()),
                        df.format(e.getPrice()) + " zł",
                        df.format(total) + " zł"
                };

                for (int i = 0; i < cols; i++) {

                    XWPFTableCell cell = row.getCell(i);

                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                    CTTblWidth width = tcPr.addNewTcW();
                    width.setType(STTblWidth.DXA);
                    width.setW(BigInteger.valueOf(widths[i]));

                    CTTcMar mar = tcPr.addNewTcMar();
                    mar.addNewTop().setW(BigInteger.valueOf(100));
                    mar.addNewBottom().setW(BigInteger.valueOf(100));

                    XWPFParagraph p = cell.getParagraphs().get(0);
                    p.setAlignment(ParagraphAlignment.CENTER);

                    XWPFRun r = p.createRun();
                    r.setFontFamily("Times New Roman");
                    r.setFontSize(12);
                    r.setText(values[i]);
                }
            }
        }

        // ===== NAWOZY =====

// znajdź kategorię "Nawozy"
        List<Expense> fertilizerExpenses = productionExpenses.stream()
                .filter(e -> {
                    ExpenseCategory cat = categoryMap.get(e.getExpenseCategoryId());
                    return cat != null && cat.getName().equalsIgnoreCase("Nawozy");
                })
                .sorted((a, b) -> a.getExpenseDate().compareTo(b.getExpenseDate()))
                .toList();

        if (!fertilizerExpenses.isEmpty()) {

            BigDecimal totalFertilizers = fertilizerExpenses.stream()
                    .map(e -> e.getPrice().multiply(e.getQuantity()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ===== AKAPIT =====
            XWPFParagraph introFertilizers = document.createParagraph();
            introFertilizers.setSpacingBefore(200);
            introFertilizers.setSpacingBetween(1.5);
            introFertilizers.setFirstLineIndent(500);
            introFertilizers.setAlignment(ParagraphAlignment.BOTH);

            XWPFRun runFertilizers = introFertilizers.createRun();
            runFertilizers.setFontFamily("Times New Roman");
            runFertilizers.setFontSize(12);

            runFertilizers.setText(
                    "Istotnym elementem kosztów produkcji papryki są wydatki na nawozy, " +
                            "które bezpośrednio wpływają na wzrost roślin oraz wielkość i jakość plonu. " +
                            "W analizowanym sezonie łączny koszt nawożenia wyniósł " +
                            df.format(totalFertilizers) + " zł. " +
                            "Szczegółowe zestawienie przedstawiono w tabeli 8."
            );

            // ===== TYTUŁ TABELI =====
            XWPFParagraph tableTitle = document.createParagraph();
            tableTitle.setSpacingBefore(200);

            XWPFRun titleRun = tableTitle.createRun();
            titleRun.setItalic(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(12);
            titleRun.setText("Tabela 8. Koszty nawożenia w sezonie " + seasonYear);

            // ===== TABELA =====

            int cols = 6;

            XWPFTable table = document.createTable(1, cols);
            setRepeatTableHeader(table.getRow(0));
            table.setWidth("100%");
            table.setTableAlignment(TableRowAlign.CENTER);

            int[] widths = {
                    800,
                    1400,
                    3000,
                    1500,
                    2000,
                    1800
            };

            String[] headers = {
                    "Lp",
                    "Data",
                    "Nazwa",
                    "Ilość",
                    "Cena",
                    "Suma"
            };

            XWPFTableRow header = table.getRow(0);
            header.setRepeatHeader(true);

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = header.getCell(i);

                cell.setColor("5fab18");
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                CTTblWidth width = tcPr.addNewTcW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(widths[i]));

                CTTcBorders borders = tcPr.addNewTcBorders();
                borders.addNewTop().setVal(STBorder.NONE);
                borders.addNewBottom().setVal(STBorder.NONE);
                borders.addNewLeft().setVal(STBorder.NONE);
                borders.addNewRight().setVal(STBorder.NONE);

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
                r.setText(headers[i]);
            }

            // ===== DANE =====

            int lp = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (Expense e : fertilizerExpenses) {

                XWPFTableRow row = table.createRow();

                BigDecimal total = e.getPrice().multiply(e.getQuantity());

                String[] values = {
                        String.valueOf(lp++),
                        e.getExpenseDate().format(formatter),
                        e.getTitle(),
                        String.format("%,.2f", e.getQuantity()),
                        df.format(e.getPrice()) + " "+ e.getUnit(),
                        df.format(total) + " zł"
                };

                for (int i = 0; i < cols; i++) {

                    XWPFTableCell cell = row.getCell(i);

                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                    CTTblWidth width = tcPr.addNewTcW();
                    width.setType(STTblWidth.DXA);
                    width.setW(BigInteger.valueOf(widths[i]));

                    CTTcMar mar = tcPr.addNewTcMar();
                    mar.addNewTop().setW(BigInteger.valueOf(100));
                    mar.addNewBottom().setW(BigInteger.valueOf(100));

                    XWPFParagraph p = cell.getParagraphs().get(0);
                    p.setAlignment(ParagraphAlignment.CENTER);

                    XWPFRun r = p.createRun();
                    r.setFontFamily("Times New Roman");
                    r.setFontSize(12);
                    r.setText(values[i]);
                }
            }
        }

        // ===== PRACOWNICY =====

// znajdź kategorię "Pracownicy"
        List<Expense> workerExpenses = productionExpenses.stream()
                .filter(e -> {
                    ExpenseCategory cat = categoryMap.get(e.getExpenseCategoryId());
                    return cat != null && cat.getName().equalsIgnoreCase("Pracownicy");
                })
                .toList();

// grupowanie po nazwie BEZ numerów (H1, #2 itd.)
        Map<String, BigDecimal> grouped = workerExpenses.stream()
                .collect(Collectors.groupingBy(
                        e -> normalizeName(e.getTitle()),
                        Collectors.mapping(
                                e -> e.getPrice().multiply(e.getQuantity()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        if (!grouped.isEmpty()) {

            BigDecimal totalWorkers = grouped.values().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ===== AKAPIT =====
            XWPFParagraph intro = document.createParagraph();
            intro.setSpacingBefore(200);
            intro.setSpacingBetween(1.5);
            intro.setFirstLineIndent(500);
            intro.setAlignment(ParagraphAlignment.BOTH);

            XWPFRun runn = intro.createRun();
            runn.setFontFamily("Times New Roman");
            runn.setFontSize(12);

            runn.setText(
                    "Koszty związane z pracownikami w analizowanym sezonie wyniosły łącznie "
                            + df.format(totalWorkers) + " zł. "
                            + "Zestawienie obejmuje wszystkie wykonane prace, zgrupowane według ich rodzaju."
            );

            // ===== TYTUŁ =====
            XWPFParagraph tableTitle = document.createParagraph();
            tableTitle.setSpacingBefore(200);

            XWPFRun titleRun = tableTitle.createRun();
            titleRun.setItalic(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(12);
            titleRun.setText("Tabela 9. Koszty prac w sezonie " + seasonYear);

            // ===== TABELA =====
            int cols = 3;

            XWPFTable table = document.createTable(1, cols);
            setRepeatTableHeader(table.getRow(0));
            table.setWidth("100%");
            table.setTableAlignment(TableRowAlign.CENTER);

            int[] widths = {
                    1000,
                    4000,
                    2000
            };

            String[] headers = {
                    "Lp",
                    "Nazwa",
                    "Suma"
            };

            XWPFTableRow header = table.getRow(0);
            header.setRepeatHeader(true);

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = header.getCell(i);
                cell.setColor("5fab18");
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                CTTblWidth width = tcPr.addNewTcW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(widths[i]));

                CTTcBorders borders = tcPr.addNewTcBorders();
                borders.addNewTop().setVal(STBorder.NONE);
                borders.addNewBottom().setVal(STBorder.NONE);
                borders.addNewLeft().setVal(STBorder.NONE);
                borders.addNewRight().setVal(STBorder.NONE);

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
                r.setText(headers[i]);
            }

            // ===== DANE =====
            int lp = 1;

            for (Map.Entry<String, BigDecimal> entry : grouped.entrySet()) {

                XWPFTableRow row = table.createRow();

                String[] values = {
                        String.valueOf(lp++),
                        entry.getKey(),
                        df.format(entry.getValue()) + " zł"
                };

                for (int i = 0; i < cols; i++) {

                    XWPFTableCell cell = row.getCell(i);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                    CTTblWidth width = tcPr.addNewTcW();
                    width.setType(STTblWidth.DXA);
                    width.setW(BigInteger.valueOf(widths[i]));

                    CTTcMar mar = tcPr.addNewTcMar();
                    mar.addNewTop().setW(BigInteger.valueOf(100));
                    mar.addNewBottom().setW(BigInteger.valueOf(100));

                    XWPFParagraph p = cell.getParagraphs().get(0);
                    p.setAlignment(ParagraphAlignment.CENTER);

                    XWPFRun r = p.createRun();
                    r.setFontFamily("Times New Roman");
                    r.setFontSize(12);
                    r.setText(values[i]);
                }
            }
        }

        // ===== POZOSTAŁE KOSZTY PRODUKCJI =====

        XWPFParagraph introOtherProd = document.createParagraph();
        introOtherProd.setSpacingBefore(200);
        introOtherProd.setSpacingBetween(1.5);
        introOtherProd.setFirstLineIndent(500);
        introOtherProd.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun runOtherProd = introOtherProd.createRun();
        runOtherProd.setFontFamily("Times New Roman");
        runOtherProd.setFontSize(12);

        runOtherProd.setText(
                "Pozostałe koszty produkcji obejmują wydatki bezpośrednio związane z prowadzeniem uprawy, " +
                        "które nie zostały wyszczególnione w powyższych sekcjach. " +
                        "Poniżej przedstawiono szczegółowe zestawienie tych kosztów w podziale na kategorie."
        );

        List<ExpenseCategory> otherProductionCategories = categories.stream()
                .filter(c ->
                        Boolean.TRUE.equals(c.getProductionCost())
                                && !c.getName().equalsIgnoreCase("Sadzonka")
                                && !c.getName().equalsIgnoreCase("Nawozy")
                                && !c.getName().equalsIgnoreCase("Pracownicy")
                )
                .toList();

        int tableIndex = tableIndexStart;

        for (ExpenseCategory category : otherProductionCategories) {

            List<Expense> categoryExpenses = productionExpenses.stream()
                    .filter(e -> e.getExpenseCategoryId().equals(category.getId()))
                    .sorted((a, b) -> a.getExpenseDate().compareTo(b.getExpenseDate()))
                    .toList();

            if (categoryExpenses.isEmpty()) continue;

            BigDecimal total = categoryExpenses.stream()
                    .map(e -> e.getPrice().multiply(e.getQuantity()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ===== AKAPIT =====
            XWPFParagraph intro = document.createParagraph();
            intro.setSpacingBefore(200);
            intro.setSpacingBetween(1.5);
            intro.setFirstLineIndent(500);
            intro.setAlignment(ParagraphAlignment.BOTH);

            XWPFRun runnn = intro.createRun();
            runnn.setFontFamily("Times New Roman");
            runnn.setFontSize(12);

            runnn.setText(
                    "W analizowanym sezonie koszty w kategorii \"" + category.getName() +
                            "\" wyniosły łącznie " + df.format(total) + " zł."
            );

            // ===== TYTUŁ =====
            XWPFParagraph tableTitle = document.createParagraph();
            tableTitle.setSpacingBefore(200);

            XWPFRun titleRun = tableTitle.createRun();
            titleRun.setItalic(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(12);
            titleRun.setText("Tabela " + tableIndex++ + ". " + category.getName() + " w sezonie " + seasonYear);

            // ===== TABELA =====
            int cols = 6;

            XWPFTable table = document.createTable(1, cols);
            setRepeatTableHeader(table.getRow(0));
            table.setWidth("100%");
            table.setTableAlignment(TableRowAlign.CENTER);

            int[] widths = {
                    800,
                    1400,
                    3000,
                    1500,
                    2000,
                    1800
            };

            String[] headers = {
                    "Lp",
                    "Data",
                    "Nazwa",
                    "Ilość",
                    "Cena",
                    "Suma"
            };

            XWPFTableRow header = table.getRow(0);
            header.setRepeatHeader(true);

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = header.getCell(i);
                cell.setColor("5fab18");
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                CTTblWidth width = tcPr.addNewTcW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(widths[i]));

                CTTcBorders borders = tcPr.addNewTcBorders();
                borders.addNewTop().setVal(STBorder.NONE);
                borders.addNewBottom().setVal(STBorder.NONE);
                borders.addNewLeft().setVal(STBorder.NONE);
                borders.addNewRight().setVal(STBorder.NONE);

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
                r.setText(headers[i]);
            }

            // ===== DANE =====
            int lp = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (Expense e : categoryExpenses) {

                XWPFTableRow row = table.createRow();

                BigDecimal sum = e.getPrice().multiply(e.getQuantity());

                String[] values = {
                        String.valueOf(lp++),
                        e.getExpenseDate().format(formatter),
                        e.getTitle(),
                        String.format("%,.2f", e.getQuantity()),
                        df.format(e.getPrice()) + " " + e.getUnit(),
                        df.format(sum) + " zł"
                };

                for (int i = 0; i < cols; i++) {

                    XWPFTableCell cell = row.getCell(i);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                    CTTblWidth width = tcPr.addNewTcW();
                    width.setType(STTblWidth.DXA);
                    width.setW(BigInteger.valueOf(widths[i]));

                    CTTcMar mar = tcPr.addNewTcMar();
                    mar.addNewTop().setW(BigInteger.valueOf(100));
                    mar.addNewBottom().setW(BigInteger.valueOf(100));

                    XWPFParagraph p = cell.getParagraphs().get(0);
                    p.setAlignment(ParagraphAlignment.CENTER);

                    XWPFRun r = p.createRun();
                    r.setFontFamily("Times New Roman");
                    r.setFontSize(12);
                    r.setText(values[i]);
                }
            }
        }

        // ===== POZOSTAŁE WYDATKI (NIEPRODUKCYJNE) =====

        XWPFParagraph introNonProd = document.createParagraph();
        introNonProd.setSpacingBefore(200);
        introNonProd.setSpacingBetween(1.5);
        introNonProd.setFirstLineIndent(500);
        introNonProd.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun runNonProd = introNonProd.createRun();
        runNonProd.setFontFamily("Times New Roman");
        runNonProd.setFontSize(12);

        runNonProd.setText(
                "Poza kosztami bezpośrednio związanymi z produkcją papryki, gospodarstwo ponosiło również wydatki " +
                        "związane z jego ogólnym funkcjonowaniem. Poniżej przedstawiono szczegółowe zestawienie " +
                        "tych wydatków w podziale na kategorie."
        );

        int tableIndexNonProd = tableIndex; // żeby nie kolidowało z wcześniejszymi tabelami

        for (ExpenseCategory category : categories.stream()
                .filter(c -> !Boolean.TRUE.equals(c.getProductionCost()))
                .toList()) {

            List<Expense> categoryExpenses = otherExpenses.stream()
                    .filter(e -> e.getExpenseCategoryId().equals(category.getId()))
                    .sorted((a, b) -> a.getExpenseDate().compareTo(b.getExpenseDate()))
                    .toList();

            if (categoryExpenses.isEmpty()) continue;

            BigDecimal total = categoryExpenses.stream()
                    .map(e -> e.getPrice().multiply(e.getQuantity()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ===== AKAPIT =====
            XWPFParagraph intro = document.createParagraph();
            intro.setSpacingBefore(200);
            intro.setSpacingBetween(1.5);
            intro.setFirstLineIndent(500);
            intro.setAlignment(ParagraphAlignment.BOTH);

            XWPFRun runnnn = intro.createRun();
            runnnn.setFontFamily("Times New Roman");
            runnnn.setFontSize(12);

            runnnn.setText(
                    "W analizowanym sezonie koszty w kategorii \"" + category.getName() +
                            "\" wyniosły łącznie " + df.format(total) + " zł."
            );

            // ===== TYTUŁ =====
            XWPFParagraph tableTitle = document.createParagraph();
            tableTitle.setSpacingBefore(200);

            XWPFRun titleRun = tableTitle.createRun();
            titleRun.setItalic(true);
            titleRun.setFontFamily("Times New Roman");
            titleRun.setFontSize(12);
            titleRun.setText("Tabela " + tableIndexNonProd++ + ". " + category.getName() + " w sezonie " + seasonYear);

            // ===== TABELA =====
            int cols = 6;

            XWPFTable table = document.createTable(1, cols);
            setRepeatTableHeader(table.getRow(0));
            table.setWidth("100%");
            table.setTableAlignment(TableRowAlign.CENTER);

            int[] widths = {
                    800,
                    1400,
                    3000,
                    1500,
                    2000,
                    1800
            };

            String[] headers = {
                    "Lp",
                    "Data",
                    "Nazwa",
                    "Ilość",
                    "Cena",
                    "Suma"
            };

            XWPFTableRow header = table.getRow(0);
            header.setRepeatHeader(true);

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = header.getCell(i);
                cell.setColor("5fab18");
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                CTTblWidth width = tcPr.addNewTcW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(widths[i]));

                CTTcBorders borders = tcPr.addNewTcBorders();
                borders.addNewTop().setVal(STBorder.NONE);
                borders.addNewBottom().setVal(STBorder.NONE);
                borders.addNewLeft().setVal(STBorder.NONE);
                borders.addNewRight().setVal(STBorder.NONE);

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
                r.setText(headers[i]);
            }

            // ===== DANE =====
            int lp = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (Expense e : categoryExpenses) {

                XWPFTableRow row = table.createRow();

                BigDecimal sum = e.getPrice().multiply(e.getQuantity());

                String[] values = {
                        String.valueOf(lp++),
                        e.getExpenseDate().format(formatter),
                        e.getTitle(),
                        String.format("%,.2f", e.getQuantity()),
                        df.format(e.getPrice()) + " " + e.getUnit(),
                        df.format(sum) + " zł"
                };

                for (int i = 0; i < cols; i++) {

                    XWPFTableCell cell = row.getCell(i);
                    cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                    CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                    CTTblWidth width = tcPr.addNewTcW();
                    width.setType(STTblWidth.DXA);
                    width.setW(BigInteger.valueOf(widths[i]));

                    CTTcMar mar = tcPr.addNewTcMar();
                    mar.addNewTop().setW(BigInteger.valueOf(100));
                    mar.addNewBottom().setW(BigInteger.valueOf(100));

                    XWPFParagraph p = cell.getParagraphs().get(0);
                    p.setAlignment(ParagraphAlignment.CENTER);

                    XWPFRun r = p.createRun();
                    r.setFontFamily("Times New Roman");
                    r.setFontSize(12);
                    r.setText(values[i]);
                }
            }
        }
        return tableIndexNonProd;
    }

    private BigInteger createBulletList(XWPFDocument doc) {
        XWPFNumbering numbering = doc.createNumbering();

        CTAbstractNum abstractNum = CTAbstractNum.Factory.newInstance();
        abstractNum.setAbstractNumId(BigInteger.valueOf(0));

        CTLvl lvl = abstractNum.addNewLvl();
        lvl.setIlvl(BigInteger.ZERO);
        lvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
        lvl.addNewLvlText().setVal("•");
        lvl.addNewStart().setVal(BigInteger.ONE);

        XWPFAbstractNum xwpfAbstractNum = new XWPFAbstractNum(abstractNum);
        BigInteger abstractNumID = numbering.addAbstractNum(xwpfAbstractNum);

        return numbering.addNum(abstractNumID);
    }

    private String normalizeName(String name) {
        if (name == null) return "";

        return name
                .replaceAll("\\s*[#Hh]?\\d+$", "") // usuwa H1, #2 itd.
                .trim();
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }
}