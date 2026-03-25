package pl.farmapp.backend.service.report.pages;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.PointOfSale;
import pl.farmapp.backend.entity.TradeOfPepper;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PepperSalesPageGenerator {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void generate(
            XWPFDocument document,
            List<TradeOfPepper> trades,
            List<PointOfSale> points,
            Integer year,
            Double tunnelCountCurrent
    ) throws IOException, InvalidFormatException {


        // ===== NAGŁÓWEK =====
        XWPFParagraph heading = document.createParagraph();
        heading.setIndentationLeft(500);
        heading.setSpacingAfter(300);
        heading.setSpacingBefore(600);
        heading.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun hRun = heading.createRun();
        hRun.setFontFamily("Times New Roman");
        hRun.setFontSize(14);
        hRun.setBold(true);
        hRun.setText("4. Sprzedaż papryki");

        if (trades == null || trades.isEmpty())
            return;

        // ===== DATY SPRZEDAŻY =====
        LocalDate firstTrade = trades.stream()
                .map(TradeOfPepper::getTradeDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate lastTrade = trades.stream()
                .map(TradeOfPepper::getTradeDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        long days = ChronoUnit.DAYS.between(firstTrade, lastTrade);

        // ===== AKAPIT =====
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBetween(1.5);
        paragraph.setFirstLineIndent(500);
        paragraph.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(
                "Pierwsza transakcja sprzedaży papryki w analizowanym sezonie "
                        + "miała miejsce dnia "
                        + firstTrade.format(FORMAT)
                        + ", natomiast ostatnia została zarejestrowana dnia "
                        + lastTrade.format(FORMAT)
                        + ". Całkowity okres sprzedaży obejmował "
                        + days
                        + " dni."
        );

        document.createParagraph();

        // ===== WPROWADZENIE =====
        XWPFParagraph intro = document.createParagraph();
        intro.setSpacingBetween(1.5);
        intro.setFirstLineIndent(500);
        intro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun run2 = intro.createRun();
        run2.setFontFamily("Times New Roman");
        run2.setFontSize(12);

        run2.setText(
                "W analizowanym sezonie sprzedaż papryki prowadzona była "
                        + "za pośrednictwem następujących punktów sprzedaży:"
        );

        document.createParagraph();

        // ===== LICZENIE TRANSAKCJI =====
        Map<PointOfSale, Long> tradeCount = trades.stream()
                .collect(Collectors.groupingBy(
                        TradeOfPepper::getPointOfSale,
                        Collectors.counting()
                ));

        Map<String, List<PointOfSale>> grouped =
                tradeCount.keySet().stream()
                        .collect(Collectors.groupingBy(PointOfSale::getType));

        BigInteger bulletNumID = createBulletList(document);

        for (String type : grouped.keySet()) {

            XWPFParagraph typeParagraph = document.createParagraph();
            typeParagraph.setIndentationLeft(0);
            typeParagraph.setSpacingAfter(200);

            XWPFRun typeRun = typeParagraph.createRun();
            typeRun.setFontFamily("Times New Roman");
            typeRun.setFontSize(12);
            typeRun.setBold(true);
            typeRun.setText(type + ":");

            List<PointOfSale> sorted = grouped.get(type)
                    .stream()
                    .sorted((a,b) ->
                            Long.compare(
                                    tradeCount.get(b),
                                    tradeCount.get(a)
                            )
                    )
                    .toList();

            for (int i = 0; i < sorted.size(); i++) {

                PointOfSale pos = sorted.get(i);

                XWPFParagraph bullet = document.createParagraph();
                bullet.setIndentationLeft(900);
                bullet.setIndentationHanging(300);
                bullet.setSpacingBetween(1.5);
                bullet.setNumID(bulletNumID);

                XWPFRun r = bullet.createRun();
                r.setFontFamily("Times New Roman");
                r.setFontSize(12);

                String name = pos.getName();

                boolean isLast = i == sorted.size() - 1;

                String ending = isLast ? "." : ",";

                r.setText(name + ", " + pos.getAddress() + ending);
            }

            document.createParagraph();
        }

        long transactionCount = trades.size();

        XWPFParagraph intro2 = document.createParagraph();
        intro2.setSpacingBetween(1.5);
        intro2.setFirstLineIndent(500);
        intro2.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun rIntro = intro2.createRun();
        rIntro.setFontFamily("Times New Roman");
        rIntro.setFontSize(12);

        rIntro.setText(
                "W analizowanym sezonie zarejestrowano łącznie "
                        + transactionCount
                        + " transakcji sprzedaży papryki. "
                        + "Szczegółowe zestawienie poszczególnych transakcji "
                        + "przedstawiono w tabeli 5."
        );

        // ===== TABELA =====

        XWPFParagraph tableTitle = document.createParagraph();
        tableTitle.setSpacingBefore(200);

        XWPFRun titleRun = tableTitle.createRun();
        titleRun.setItalic(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(12);
        titleRun.setText("Tabela 5. Zestawienie transakcji sprzedaży papryki w sezonie " + year);

        int cols = 9;

        XWPFTable table = document.createTable(1, cols);
        table.setWidth("100%");
        table.setTableAlignment(TableRowAlign.CENTER);

        int[] widths = {
                900,
                1400,
                1100,
                1400,
                1200,
                1200,
                1000,
                1500,
                3500
        };

        String[] headers = {
                "Lp",
                "Data",
                "Klasa",
                "Kolor",
                "Cena",
                "Masa",
                "VAT",
                "Suma",
                "Punkt sprzedaży"
        };

        XWPFTableRow header = table.getRow(0);
        header.setRepeatHeader(true);

        for (int i = 0; i < cols; i++) {

            XWPFTableCell cell = header.getCell(i);

            cell.setColor("5fab18");
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            CTTblWidth cellWidth = cell.getCTTc()
                    .addNewTcPr()
                    .addNewTcW();

            cellWidth.setType(STTblWidth.DXA);
            cellWidth.setW(BigInteger.valueOf(widths[i]));

            XWPFParagraph p = cell.getParagraphs().get(0);
            p.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun r = p.createRun();
            r.setBold(true);
            r.setFontSize(12);
            r.setFontFamily("Times New Roman");
            r.setColor("FFFFFF");
            r.setText(headers[i]);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        int lp = 1;

        for (TradeOfPepper t : trades) {

            XWPFTableRow row = table.createRow();
            row.setHeight(100);

            double price = t.getTradePrice().doubleValue();
            double weight = t.getTradeWeight().doubleValue();
            int vat = t.getVatRate();

            double value = price * weight * (1 + vat / 100.0);

            String pos = t.getPointOfSale().getShortName() != null
                    ? t.getPointOfSale().getShortName()
                    : t.getPointOfSale().getName();

            String[] values = {
                    String.valueOf(lp++),
                    t.getTradeDate().format(formatter),
                    String.valueOf(t.getPepperClass()),
                    t.getPepperColor(),
                    String.format("%.2f", price),
                    String.format("%.0f", weight),
                    String.valueOf(vat),
                    String.format("%,.2f", value),
                    pos
            };

            for (int i = 0; i < cols; i++) {

                XWPFTableCell cell = row.getCell(i);

                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTblWidth cellWidth = cell.getCTTc()
                        .addNewTcPr()
                        .addNewTcW();

                cellWidth.setType(STTblWidth.DXA);
                cellWidth.setW(BigInteger.valueOf(widths[i]));

                XWPFParagraph p = cell.getParagraphs().get(0);
                p.setAlignment(ParagraphAlignment.CENTER);

                XWPFRun r = p.createRun();
                r.setFontFamily("Times New Roman");
                r.setFontSize(12);
                r.setText(values[i]);
            }
        }


        //---------------
        // ===== ANALIZA MIESIĘCZNEJ SPRZEDAŻY =====

        class MonthSalesStats {
            double weight = 0;
            double revenue = 0;
        }

        Map<Integer, MonthSalesStats> monthSales = new HashMap<>();

        for (TradeOfPepper t : trades) {

            int month = t.getTradeDate().getMonthValue();

            monthSales.putIfAbsent(month, new MonthSalesStats());

            double price = t.getTradePrice().doubleValue();
            double weight = t.getTradeWeight().doubleValue();
            int vat = t.getVatRate();

            double revenue = price * weight * (1 + vat / 100.0);

            monthSales.get(month).weight += weight;
            monthSales.get(month).revenue += revenue;
        }


// ===== SORTOWANIE MIESIĘCY =====

        List<Integer> sortedMonthsSales =
                monthSales.keySet().stream().sorted().toList();

        String[] monthNames = {
                "", "Styczeń","Luty","Marzec","Kwiecień","Maj","Czerwiec",
                "Lipiec","Sierpień","Wrzesień","Październik","Listopad","Grudzień"
        };


// ===== BUDOWA OPISU =====

        StringBuilder monthDescription = new StringBuilder();

        for (int i = 0; i < sortedMonthsSales.size(); i++) {

            int m = sortedMonthsSales.get(i);
            MonthSalesStats s = monthSales.get(m);

            monthDescription.append(monthNames[m])
                    .append(" – ")
                    .append(String.format("%,.0f", s.weight))
                    .append(" kg, ")
                    .append(String.format("%,.2f", s.revenue))
                    .append(" zł");

            if (i < sortedMonthsSales.size() - 1)
                monthDescription.append(", ");
            else
                monthDescription.append(".");
        }


// ===== AKAPIT =====

        XWPFParagraph monthSalesIntro = document.createParagraph();
        monthSalesIntro.setSpacingBefore(200);
        monthSalesIntro.setSpacingBetween(1.5);
        monthSalesIntro.setFirstLineIndent(500);
        monthSalesIntro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun runMonthSales = monthSalesIntro.createRun();
        runMonthSales.setFontFamily("Times New Roman");
        runMonthSales.setFontSize(12);

        runMonthSales.setText(
                "Analiza miesięcznej sprzedaży papryki wskazuje na zróżnicowanie "
                        + "zarówno masy sprzedaży, jak i uzyskanego przychodu w poszczególnych "
                        + "miesiącach sezonu. W analizowanym okresie wartości te kształtowały "
                        + "się następująco: "
                        + monthDescription
                        + " Zależność pomiędzy wielkością sprzedaży a uzyskanym przychodem "
                        + "przedstawiono na wykresie 2."
        );


// ===== DANE DO WYKRESU =====

        List<String> chartMonths = new ArrayList<>();
        List<Double> chartRevenue = new ArrayList<>();
        List<Double> chartWeight = new ArrayList<>();

        for (Integer m : sortedMonthsSales) {

            chartMonths.add(monthNames[m]);

            chartRevenue.add(monthSales.get(m).revenue);
            chartWeight.add(monthSales.get(m).weight);
        }


// ===== WYKRES =====

        // ===== WYKRES =====

        CategoryChart chart =
                new CategoryChartBuilder()
                        .width(700)
                        .height(400)
                        .title("")
                        .xAxisTitle("Miesiąc sezonu")
                        .yAxisTitle("Dochód (zł)")
                        .build();


// styl

        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBorderVisible(false);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setYAxisDecimalPattern("#,###");
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
        chart.getStyler().setLegendBorderColor(new Color(0,0,0,0));
        chart.getStyler().setAvailableSpaceFill(0.6);


// prawa oś Y

        chart.getStyler().setYAxisGroupPosition(
                1,
                Styler.YAxisPosition.Right
        );

        chart.setYAxisGroupTitle(1, "Zbiory (kg)");

        chart.getStyler().setYAxisDecimalPattern("#,###");


// kolory

        Color[] colors = {
                new Color(95,171,24),
                new Color(180,220,140)
        };

        chart.getStyler().setSeriesColors(colors);


// ===== SERIA DOCHÓD =====

        CategorySeries revenueSeries =
                chart.addSeries("Dochód (zł)", chartMonths, chartRevenue);

        revenueSeries.setYAxisGroup(0);


// ===== SERIA ZBIORY =====

        CategorySeries weightSeries =
                chart.addSeries("Zbiory (kg)", chartMonths, chartWeight);

        weightSeries.setYAxisGroup(1);


// ===== ZAPIS WYKRESU =====

        ByteArrayOutputStream chartStream5 = new ByteArrayOutputStream();

        BitmapEncoder.saveBitmap(
                chart,
                chartStream5,
                BitmapEncoder.BitmapFormat.PNG
        );


// ===== WSTAWIENIE DO WORD =====

        XWPFParagraph chartParagraph5 = document.createParagraph();
        chartParagraph5.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun chartRun5 = chartParagraph5.createRun();

        chartRun5.addPicture(
                new ByteArrayInputStream(chartStream5.toByteArray()),
                XWPFDocument.PICTURE_TYPE_PNG,
                "monthly_sales_chart.png",
                Units.toEMU(450),
                Units.toEMU(260)
        );


// ===== PODPIS =====

        XWPFParagraph caption5 = document.createParagraph();
        caption5.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun captionRun5 = caption5.createRun();
        captionRun5.setFontFamily("Times New Roman");
        captionRun5.setFontSize(12);
        captionRun5.setItalic(true);

        captionRun5.setText(
                "Wykres 2. Zależność dochodu oraz masy sprzedaży papryki w ujęciu miesięcznym w sezonie " + year
        );
        //---------------

        // ===== PODSUMOWANIE SPRZEDAŻY =====

        double totalWeight = trades.stream()
                .mapToDouble(t -> t.getTradeWeight().doubleValue())
                .sum();

        double totalRevenue = trades.stream()
                .mapToDouble(t -> {
                    double price = t.getTradePrice().doubleValue();
                    double weight = t.getTradeWeight().doubleValue();
                    int vat = t.getVatRate();
                    return price * weight * (1 + vat / 100.0);
                })
                .sum();

        double weightedPriceSum = trades.stream()
                .mapToDouble(t -> {
                    double price = t.getTradePrice().doubleValue();
                    double weight = t.getTradeWeight().doubleValue();
                    int vat = t.getVatRate();
                    return price * weight * (1 + vat / 100.0);
                })
                .sum();

        double avgPrice = weightedPriceSum / totalWeight;

        XWPFParagraph summary = document.createParagraph();
        summary.setSpacingBefore(300);
        summary.setSpacingBetween(1.5);
        summary.setFirstLineIndent(500);
        summary.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun rSummary = summary.createRun();
        rSummary.setFontFamily("Times New Roman");
        rSummary.setFontSize(12);

        rSummary.setText(
                "Łączna masa sprzedanej papryki w analizowanym sezonie wyniosła "
                        + String.format("%,.0f", totalWeight)
                        + " kg. Całkowita wartość sprzedaży osiągnęła poziom "
                        + String.format("%,.2f", totalRevenue)
                        + " zł. Średnia cena sprzedaży papryki w sezonie "
                        + year
                        + " wyniosła "
                        + String.format("%.2f", avgPrice)
                        + " zł/kg."
        );

        // ===== AGREGACJA SPRZEDAŻY WG PUNKTÓW =====

        // ===== AKAPIT WPROWADZAJĄCY DO ANALIZY =====

        XWPFParagraph intro3 = document.createParagraph();
        intro3.setSpacingBefore(200);
        intro3.setSpacingBetween(1.5);
        intro3.setFirstLineIndent(500);
        intro3.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun run3 = intro3.createRun();
        run3.setFontFamily("Times New Roman");
        run3.setFontSize(12);

        run3.setText(
                "W celu przedstawienia struktury sprzedaży papryki w analizowanym sezonie "
                        + "sporządzono zestawienie agregujące dane według poszczególnych punktów sprzedaży. "
                        + "W tabeli 6 zaprezentowano łączną masę sprzedanej papryki, uzyskany przychód, "
                        + "średnią cenę sprzedaży oraz procentowy udział danego punktu sprzedaży "
                        + "w całkowitej strukturze sprzedaży."
        );


// ===== AGREGACJA DANYCH =====

        class PointStats {
            double weight = 0;
            double revenue = 0;
        }

        Map<PointOfSale, PointStats> stats = new HashMap<>();

        for (TradeOfPepper t : trades) {

            PointOfSale pos = t.getPointOfSale();

            stats.putIfAbsent(pos, new PointStats());

            double price = t.getTradePrice().doubleValue();
            double weight = t.getTradeWeight().doubleValue();
            int vat = t.getVatRate();

            double revenue = price * weight * (1 + vat / 100.0);

            stats.get(pos).weight += weight;
            stats.get(pos).revenue += revenue;
        }

        double totalWeightAll = stats.values()
                .stream()
                .mapToDouble(s -> s.weight)
                .sum();


// ===== SORTOWANIE MALEJĄCO WG SPRZEDAŻY =====

        List<Map.Entry<PointOfSale, PointStats>> sortedStats =
                stats.entrySet()
                        .stream()
                        .sorted((a,b) ->
                                Double.compare(
                                        b.getValue().weight,
                                        a.getValue().weight
                                ))
                        .toList();


// ===== TYTUŁ TABELI =====

        XWPFParagraph tableTitle6 = document.createParagraph();
        tableTitle6.setSpacingBefore(200);

        XWPFRun titleRun6 = tableTitle6.createRun();
        titleRun6.setItalic(true);
        titleRun6.setFontFamily("Times New Roman");
        titleRun6.setFontSize(12);
        titleRun6.setText("Tabela 6. Struktura sprzedaży papryki według punktów sprzedaży w sezonie " + year);


// ===== TWORZENIE TABELI =====

        int cols6 = 5;

        XWPFTable table6 = document.createTable(1, cols6);
        table6.setWidth("100%");
        table6.setTableAlignment(TableRowAlign.CENTER);


// szerokości kolumn

        int[] widths6 = {
                2700,  // Punkt sprzedaży
                1700,  // Sprzedaż
                1900,  // Przychód
                2800,  // Średnia cena (większa)
                1500   // Udział
        };


// nagłówki

        String[] headers6 = {
                "Punkt sprzedaży",
                "Sprzedaż [kg]",
                "Przychód [zł]",
                "Średnia cena [zł/kg]",
                "Udział [%]"
        };


        XWPFTableRow headerRow6 = table6.getRow(0);
        headerRow6.setRepeatHeader(true);


// ===== STYL NAGŁÓWKA =====

        for (int i = 0; i < cols6; i++) {

            XWPFTableCell cell = headerRow6.getCell(i);

            cell.setColor("5fab18");
            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            CTTcPr tcPr = cell.getCTTc().addNewTcPr();

            CTTblWidth width = tcPr.addNewTcW();
            width.setType(STTblWidth.DXA);
            width.setW(BigInteger.valueOf(widths6[i]));

            // padding góra dół
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
            r.setText(headers6[i]);
        }


// ===== WIERSZE DANYCH =====

        for (Map.Entry<PointOfSale, PointStats> entry : sortedStats) {

            PointOfSale pos = entry.getKey();
            PointStats s = entry.getValue();

            XWPFTableRow row = table6.createRow();

            String name = pos.getShortName() != null
                    ? pos.getShortName()
                    : pos.getName();

            double avgPricePos = s.revenue / s.weight;

            double share = (s.weight / totalWeightAll) * 100;

            String[] values = {
                    name,
                    String.format("%,.0f", s.weight),
                    String.format("%,.2f", s.revenue),
                    String.format("%.2f", avgPricePos),
                    String.format("%.1f", share)
            };

            for (int i = 0; i < cols6; i++) {

                XWPFTableCell cell = row.getCell(i);

                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                CTTcPr tcPr = cell.getCTTc().addNewTcPr();

                CTTblWidth width = tcPr.addNewTcW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(widths6[i]));

                // większy padding pionowy
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
        // ===== AKAPIT WPROWADZAJĄCY DO WYKRESU =====

        XWPFParagraph monthPriceIntro = document.createParagraph();
        monthPriceIntro.setSpacingBefore(200);
        monthPriceIntro.setSpacingBetween(1.5);
        monthPriceIntro.setFirstLineIndent(500);
        monthPriceIntro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun monthRun = monthPriceIntro.createRun();
        monthRun.setFontFamily("Times New Roman");
        monthRun.setFontSize(12);

        monthRun.setText(
                "Średnia cena sprzedaży papryki w analizowanym sezonie wyniosła "
                        + String.format("%.2f", avgPrice)
                        + " zł/kg. Wartość ta była jednak zróżnicowana w zależności od "
                        + "miejsca sprzedaży, co przedstawiono w tabeli 6. W celu "
                        + "zobrazowania zmian poziomu cen w trakcie sezonu sporządzono "
                        + "zestawienie średnich cen sprzedaży w poszczególnych miesiącach "
                        + "sezonu, które zaprezentowano na wykresie 3."
        );


// ===== OBLICZANIE ŚREDNIEJ CENY WEDŁUG MIESIĘCY =====

        class MonthStats {
            double weight = 0;
            double revenue = 0;
        }

        Map<Integer, MonthStats> monthStats = new HashMap<>();

        for (TradeOfPepper t : trades) {

            int month = t.getTradeDate().getMonthValue();

            monthStats.putIfAbsent(month, new MonthStats());

            double price = t.getTradePrice().doubleValue();
            double weight = t.getTradeWeight().doubleValue();
            int vat = t.getVatRate();

            double revenue = price * weight * (1 + vat / 100.0);

            monthStats.get(month).weight += weight;
            monthStats.get(month).revenue += revenue;
        }


// ===== SORTOWANIE MIESIĘCY =====

        List<Integer> sortedMonths = monthStats.keySet()
                .stream()
                .sorted()
                .toList();


// ===== DANE DO WYKRESU =====

        List<Integer> monthNumbers = new ArrayList<>();
        List<Double> avgPricesMonth = new ArrayList<>();

        String[] monthNamess = {
                "", "Styczeń","Luty","Marzec","Kwiecień","Maj","Czerwiec",
                "Lipiec","Sierpień","Wrzesień","Październik","Listopad","Grudzień"
        };

        Map<Integer, Object> tickMap = new HashMap<>();

        for (Integer m : sortedMonths) {

            MonthStats s = monthStats.get(m);

            double avgMonthPrice = s.revenue / s.weight;

            monthNumbers.add(m);
            avgPricesMonth.add(avgMonthPrice);

            tickMap.put(m, monthNamess[m]);
        }


// ===== TWORZENIE WYKRESU =====

        CategoryChart chartt = new CategoryChartBuilder()
                        .width(700)
                        .height(400)
                        .title("")
                        .xAxisTitle("Miesiąc sezonu")
                        .yAxisTitle("Średnia cena [zł/kg]")
                        .build();


// ===== STYL WYKRESU =====

        chartt.getStyler().setChartBackgroundColor(java.awt.Color.WHITE);
        chartt.getStyler().setPlotBackgroundColor(java.awt.Color.WHITE);
        chartt.getStyler().setLegendVisible(false);
        chartt.getStyler().setPlotGridLinesVisible(true);
        chartt.getStyler().setMarkerSize(6);
        chartt.getStyler().setPlotBorderVisible(false);



// kolor linii jak w tabelach

        java.awt.Color farmGreen = new java.awt.Color(95,171,24);

        chartt.getStyler().setDefaultSeriesRenderStyle(
                CategorySeries.CategorySeriesRenderStyle.Line
        );


// ===== SERIA DANYCH =====

        CategorySeries series =
                chartt.addSeries("Cena", chartMonths, avgPricesMonth);

        series.setLineColor(farmGreen);
        series.setMarkerColor(farmGreen);

        series.setLineColor(farmGreen);
        series.setMarkerColor(farmGreen);


// ===== ZAPIS WYKRESU DO OBRAZU =====

        java.io.ByteArrayOutputStream chartStream = new java.io.ByteArrayOutputStream();

        org.knowm.xchart.BitmapEncoder.saveBitmap(
                chartt,
                chartStream,
                org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG
        );


// ===== WSTAWIENIE WYKRESU DO WORD =====

        XWPFParagraph chartParagraph = document.createParagraph();
        chartParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun chartRun = chartParagraph.createRun();

        chartRun.addPicture(
                new java.io.ByteArrayInputStream(chartStream.toByteArray()),
                XWPFDocument.PICTURE_TYPE_PNG,
                "wykres_ceny_miesieczne.png",
                Units.toEMU(450),
                Units.toEMU(260)
        );


// ===== PODPIS WYKRESU =====

        XWPFParagraph chartCaption = document.createParagraph();
        chartCaption.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun captionRun = chartCaption.createRun();
        captionRun.setFontFamily("Times New Roman");
        captionRun.setFontSize(12);
        captionRun.setItalic(true);

        captionRun.setText(
                "Wykres 3. Średnia cena sprzedaży papryki w poszczególnych miesiącach sezonu " + year
        );

        // ===== STRUKTURA JAKOŚCI =====

        Map<Integer, Double> classWeight = new HashMap<>();

        for (TradeOfPepper t : trades) {

            int pepperClass = t.getPepperClass();
            double weight = t.getTradeWeight().doubleValue();

            classWeight.put(
                    pepperClass,
                    classWeight.getOrDefault(pepperClass, 0.0) + weight
            );
        }

        double totalQualityWeight = classWeight.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();


// ===== SORTOWANIE KLAS =====

        List<Map.Entry<Integer,Double>> sortedClasses =
                classWeight.entrySet()
                        .stream()
                        .sorted((a,b)->Double.compare(b.getValue(),a.getValue()))
                        .toList();


// ===== OPIS KLAS =====

        StringBuilder classDescription = new StringBuilder();

        for(int i=0;i<sortedClasses.size();i++){

            int c = sortedClasses.get(i).getKey();
            double w = sortedClasses.get(i).getValue();
            double p = w/totalQualityWeight*100;

            String label;

            if(c==1) label="papryka klasy pierwszej";
            else if(c==2) label="papryka klasy drugiej";
            else label="papryka klasy trzeciej (krojonej)";

            classDescription.append(label)
                    .append(" – ")
                    .append(String.format("%,.0f",w))
                    .append(" kg (")
                    .append(String.format("%.1f",p))
                    .append("%)");

            if(i<sortedClasses.size()-1)
                classDescription.append(", ");
            else
                classDescription.append(".");
        }


// ===== AKAPIT =====

        XWPFParagraph qualityIntro = document.createParagraph();
        qualityIntro.setSpacingBefore(200);
        qualityIntro.setSpacingBetween(1.5);
        qualityIntro.setFirstLineIndent(500);
        qualityIntro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun runQuality = qualityIntro.createRun();
        runQuality.setFontFamily("Times New Roman");
        runQuality.setFontSize(12);

        runQuality.setText(
                "W analizowanym sezonie sprzedano łącznie "
                        + String.format("%,.0f",totalQualityWeight)
                        + " kg papryki. Strukturę sprzedaży według klas jakości "
                        + "przedstawia następujące zestawienie: "
                        + classDescription
                        + " Strukturę jakości sprzedanej papryki przedstawiono "
                        + "na wykresie 4."
        );


// ===== DANE DO WYKRESU =====

        List<String> classLabels=new ArrayList<>();
        List<Double> classValues=new ArrayList<>();

        for(Map.Entry<Integer,Double> e:sortedClasses){

            int c=e.getKey();

            if(c==1) classLabels.add("Klasa I");
            else if(c==2) classLabels.add("Klasa II");
            else classLabels.add("Klasa III");

            classValues.add(e.getValue());
        }


// ===== WYKRES KOŁOWY =====

        PieChart classChart = new PieChartBuilder()
                .width(350)
                .height(250)
                .title("")
                .build();

        classChart.getStyler().setChartBackgroundColor(Color.WHITE);
        classChart.getStyler().setPlotBackgroundColor(Color.WHITE);
        classChart.getStyler().setPlotBorderVisible(false);
        classChart.getStyler().setLegendBorderColor(new Color(0,0,0,0));
        classChart.getStyler().setChartPadding(10);

        Color[] greens={
                new Color(95,171,24),
                new Color(140,200,80),
                new Color(190,230,150)
        };

        classChart.getStyler().setSeriesColors(greens);

        for(int i=0;i<classLabels.size();i++){
            classChart.addSeries(classLabels.get(i),classValues.get(i));
        }


// ===== ZAPIS WYKRESU =====

        ByteArrayOutputStream chartStream3=new ByteArrayOutputStream();

        BitmapEncoder.saveBitmap(classChart,chartStream3,BitmapEncoder.BitmapFormat.PNG);


// ===== WSTAWIENIE =====

        XWPFParagraph chartParagraph3=document.createParagraph();
        chartParagraph3.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun chartRun3=chartParagraph3.createRun();

        chartRun3.addPicture(
                new ByteArrayInputStream(chartStream3.toByteArray()),
                XWPFDocument.PICTURE_TYPE_PNG,
                "quality_chart.png",
                Units.toEMU(320),
                Units.toEMU(220)
        );


// ===== PODPIS =====

        XWPFParagraph caption3=document.createParagraph();
        caption3.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun captionRun3=caption3.createRun();
        captionRun3.setFontFamily("Times New Roman");
        captionRun3.setFontSize(12);
        captionRun3.setItalic(true);

        captionRun3.setText(
                "Wykres 4. Struktura jakości sprzedanej papryki w sezonie "+year
        );

        // ===== STRUKTURA KOLORÓW =====

        Map<String,Double> colorWeight=new HashMap<>();

        for(TradeOfPepper t:trades){

            String color=t.getPepperColor();
            double weight=t.getTradeWeight().doubleValue();

            colorWeight.put(
                    color,
                    colorWeight.getOrDefault(color,0.0)+weight
            );
        }

        double totalColorWeight=colorWeight.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();


// ===== SORTOWANIE =====

        List<Map.Entry<String,Double>> sortedColors=
                colorWeight.entrySet()
                        .stream()
                        .sorted((a,b)->Double.compare(b.getValue(),a.getValue()))
                        .toList();


// ===== OPIS =====

        StringBuilder colorDescription=new StringBuilder();

        for(int i=0;i<sortedColors.size();i++){

            String color=sortedColors.get(i).getKey();
            double w=sortedColors.get(i).getValue();
            double p=w/totalColorWeight*100;

            colorDescription.append(color)
                    .append(" – ")
                    .append(String.format("%,.0f",w))
                    .append(" kg (")
                    .append(String.format("%.1f",p))
                    .append("%)");

            if(i<sortedColors.size()-1)
                colorDescription.append(", ");
            else
                colorDescription.append(".");
        }


// ===== AKAPIT =====

        XWPFParagraph colorIntro=document.createParagraph();
        colorIntro.setSpacingBefore(200);
        colorIntro.setSpacingBetween(1.5);
        colorIntro.setFirstLineIndent(500);
        colorIntro.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun runColor=colorIntro.createRun();
        runColor.setFontFamily("Times New Roman");
        runColor.setFontSize(12);

        runColor.setText(
                "Analiza struktury sprzedaży według koloru papryki "
                        + "wykazała następujący rozkład: "
                        + colorDescription
                        + " Strukturę sprzedaży papryki według kolorów "
                        + "przedstawiono na wykresie 5."
        );


// ===== WYKRES =====

        PieChart colorChart=new PieChartBuilder()
                .width(350)
                .height(250)
                .title("")
                .build();

        colorChart.getStyler().setChartBackgroundColor(Color.WHITE);
        colorChart.getStyler().setPlotBackgroundColor(Color.WHITE);
        colorChart.getStyler().setPlotBorderVisible(false);

        colorChart.getStyler().setLegendBorderColor(Color.WHITE);
        colorChart.getStyler().setLegendBackgroundColor(Color.WHITE);
        colorChart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 10));

        colorChart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);

        colorChart.getStyler().setPlotContentSize(0.95);


        Color[] pepperColors={
                new Color(200,40,40),
                new Color(60,160,60),
                new Color(240,200,40)
        };

        colorChart.getStyler().setSeriesColors(pepperColors);

        for(Map.Entry<String,Double> e:sortedColors){
            colorChart.addSeries(e.getKey(),e.getValue());
        }


// ===== ZAPIS =====

        ByteArrayOutputStream chartStream4=new ByteArrayOutputStream();

        BitmapEncoder.saveBitmap(colorChart,chartStream4,BitmapEncoder.BitmapFormat.PNG);


// ===== WSTAWIENIE =====

        XWPFParagraph chartParagraph4=document.createParagraph();
        chartParagraph4.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun chartRun4=chartParagraph4.createRun();

        chartRun4.addPicture(
                new ByteArrayInputStream(chartStream4.toByteArray()),
                XWPFDocument.PICTURE_TYPE_PNG,
                "color_chart.png",
                Units.toEMU(320),
                Units.toEMU(220)
        );


// ===== PODPIS =====

        XWPFParagraph caption4=document.createParagraph();
        caption4.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun captionRun4=caption4.createRun();
        captionRun4.setFontFamily("Times New Roman");
        captionRun4.setFontSize(12);
        captionRun4.setItalic(true);

        captionRun4.setText(
                "Wykres 5. Struktura kolorów sprzedanej papryki w sezonie "+year
        );

        double avgWeightPerTunnel = totalWeight / tunnelCountCurrent;
        double avgRevenuePerTunnel = totalRevenue / tunnelCountCurrent;

        XWPFParagraph summaryy = document.createParagraph();
        summaryy.setSpacingBefore(300);
        summaryy.setSpacingBetween(1.5);
        summaryy.setFirstLineIndent(500);
        summaryy.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun rSummaryy = summaryy.createRun();
        rSummaryy.setFontFamily("Times New Roman");
        rSummaryy.setFontSize(12);

        rSummaryy.setText(
                "Podsumowując analizę sprzedaży papryki w sezonie "
                        + year
                        + ", łączna masa sprzedanego plonu wyniosła "
                        + String.format("%,.0f", totalWeight)
                        + " kg, natomiast całkowita wartość sprzedaży osiągnęła poziom "
                        + String.format("%,.2f", totalRevenue)
                        + " zł. Oznacza to, że średnia wartość sprzedaży jednego kilograma papryki "
                        + "w analizowanym sezonie wyniosła "
                        + String.format("%.2f", avgPrice)
                        + " zł/kg. W przeliczeniu na jeden tunel foliowy średni zbiór papryki wyniósł "
                        + String.format("%,.0f", avgWeightPerTunnel)
                        + " kg, natomiast średni przychód uzyskany z jednego tunelu osiągnął poziom "
                        + String.format("%,.2f", avgRevenuePerTunnel)
                        + " zł."
        );

    }

    private BigInteger createBulletList(XWPFDocument document) {

        XWPFNumbering numbering = document.createNumbering();

        CTAbstractNum abstractNum = CTAbstractNum.Factory.newInstance();
        abstractNum.setAbstractNumId(BigInteger.ZERO);

        CTLvl lvl = abstractNum.addNewLvl();
        lvl.setIlvl(BigInteger.ZERO);
        lvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
        lvl.addNewLvlText().setVal("•");

        XWPFAbstractNum abs = new XWPFAbstractNum(abstractNum);

        BigInteger abstractNumID = numbering.addAbstractNum(abs);

        return numbering.addNum(abstractNumID);
    }

    private void setRepeatTableHeader(XWPFTableRow row) {
        CTRow ctRow = row.getCtRow();
        CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();

        // 🔥 KLUCZ – bez setVal()
        trPr.addNewTblHeader();
    }


}