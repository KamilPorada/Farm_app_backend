package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.*;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProfitabilitySectionGenerator {

    private static final DecimalFormat df = new DecimalFormat("#,##0.##");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void generate(
            XWPFDocument document,
            int year,
            List<TradeOfPepper> trades,
            List<TradeOfPepper> tradesPrev,
            List<PointOfSale> pointsOfSale,
            List<Expense> expenses,
            List<ExpenseCategory> categories,
            List<WorkTime> workTimes,
            Double tunnelCurrent,
            Double tunnelPrevious,
            LocalDate plantingStart,
            LocalDate harvestEnd
    ) {

        if (tunnelCurrent == null || tunnelCurrent == 0) tunnelCurrent = 1.0;

        // ===== SPRZEDAŻ =====
        double totalKg = trades.stream()
                .mapToDouble(t -> t.getTradeWeight().doubleValue())
                .sum();

        double totalRevenue = trades.stream()
                .mapToDouble(t -> {
                    double net = t.getTradePrice().doubleValue() * t.getTradeWeight().doubleValue();
                    return net * (1 + t.getVatRate() / 100.0);
                }).sum();

        double avgPrice = totalKg > 0 ? totalRevenue / totalKg : 0;
        double kgPerTunnel = totalKg / tunnelCurrent;
        double revenuePerTunnel = totalRevenue / tunnelCurrent;

        // ===== POPRZEDNI ROK =====
        double totalKgPrev = tradesPrev.stream()
                .mapToDouble(t -> t.getTradeWeight().doubleValue())
                .sum();

        double totalRevenuePrev = tradesPrev.stream()
                .mapToDouble(t -> {
                    double net = t.getTradePrice().doubleValue() * t.getTradeWeight().doubleValue();
                    return net * (1 + t.getVatRate() / 100.0);
                }).sum();

        double avgPricePrev = totalKgPrev > 0 ? totalRevenuePrev / totalKgPrev : 0;

        double kgPerTunnelPrev = (tunnelPrevious != null && tunnelPrevious > 0)
                ? totalKgPrev / tunnelPrevious : 0;

        double revenuePerTunnelPrev = (tunnelPrevious != null && tunnelPrevious > 0)
                ? totalRevenuePrev / tunnelPrevious : 0;

        // ===== OKRES HANDLU =====
        LocalDate firstTrade = null;
        LocalDate lastTrade = null;
        long tradeDays = 0;

        if (!trades.isEmpty()) {
            var sorted = trades.stream()
                    .sorted(Comparator.comparing(TradeOfPepper::getTradeDate))
                    .toList();

            firstTrade = sorted.get(0).getTradeDate();
            lastTrade = sorted.get(sorted.size() - 1).getTradeDate();
            tradeDays = ChronoUnit.DAYS.between(firstTrade, lastTrade) + 1;
        }

        // ===== NAJLEPSZY I NAJGORSZY PUNKT =====
        Map<Integer, Double> revenueByPos = trades.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPointOfSale().getId(),
                        Collectors.summingDouble(t ->
                                t.getTradePrice().doubleValue()
                                        * t.getTradeWeight().doubleValue()
                                        * (1 + t.getVatRate() / 100.0)
                        )
                ));

        Integer bestPosId = revenueByPos.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Integer worstPosId = revenueByPos.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        String bestPosName = getPosName(pointsOfSale, bestPosId);
        String worstPosName = getPosName(pointsOfSale, worstPosId);

        // ===== KOSZTY =====
        Map<Integer, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(ExpenseCategory::getId, ExpenseCategory::getName));

        Set<Integer> productionCategoryIds = categories.stream()
                .filter(ExpenseCategory::getProductionCost)
                .map(ExpenseCategory::getId)
                .collect(Collectors.toSet());

        Map<String, Double> costByCategory = new LinkedHashMap<>();

        for (Expense e : expenses) {
            if (!productionCategoryIds.contains(e.getExpenseCategoryId())) continue;

            String name = categoryMap.getOrDefault(e.getExpenseCategoryId(), "Inne");
            double value = e.getQuantity().doubleValue() * e.getPrice().doubleValue();

            costByCategory.merge(name, value, Double::sum);
        }

        double productionCosts = costByCategory.values().stream().mapToDouble(Double::doubleValue).sum();

        double workersCosts = workTimes.stream()
                .mapToDouble(w -> w.getPaidAmount() != null ? w.getPaidAmount().doubleValue() : 0)
                .sum();

        double totalCosts = productionCosts + workersCosts;
        double costPerTunnel = totalCosts / tunnelCurrent;

        double profit = totalRevenue - totalCosts;
        double profitPerTunnel = profit / tunnelCurrent;

        // ===== NAGŁÓWEK =====
        createHeading(document, "10. Podsumowanie rentowności sezonu");

        // ===== AKAPIT 1 =====
        addParagraph(document,
                "Sezon " + year + " rozpoczął się dnia " + format(plantingStart) +
                        " i zakończył dnia " + format(harvestEnd) + ". " +
                        "Uprawa była prowadzona w " + df.format(tunnelCurrent) + " tunelach" +
                        (tunnelPrevious != null
                                ? ", co stanowiło zmianę względem poprzedniego sezonu (" + df.format(tunnelPrevious) + " tuneli)."
                                : ".")
        );

        // ===== AKAPIT 2 (JEDEN CIĄG - jak chciałeś) =====
        addParagraph(document,
                "W analizowanym sezonie sprzedano łącznie " + df.format(totalKg) + " kg papryki, uzyskując przychód " +
                        df.format(totalRevenue) + " zł. Średnia cena wyniosła " + df.format(avgPrice) +
                        " zł/kg. Handel był prowadzony od " + format(firstTrade) + " do " + format(lastTrade) +
                        " (" + tradeDays + " dni). " +

                        "Średnia sprzedaż na tunel wyniosła " + df.format(kgPerTunnel) + " kg, co oznacza " +
                        (kgPerTunnel >= kgPerTunnelPrev ? "wzrost" : "spadek") +
                        " względem poprzedniego sezonu (" + df.format(kgPerTunnelPrev) + " kg). " +

                        "Średni przychód na tunel wyniósł " + df.format(revenuePerTunnel) + " zł, co stanowi " +
                        (revenuePerTunnel >= revenuePerTunnelPrev ? "więcej" : "mniej") +
                        " niż w poprzednim sezonie (" + df.format(revenuePerTunnelPrev) + " zł). " +

                        "Średnia cena sprzedaży " +
                        (avgPrice >= avgPricePrev ? "wzrosła" : "spadła") +
                        " względem poprzedniego sezonu (" + df.format(avgPricePrev) + " zł/kg). " +

                        "Największy udział w sprzedaży odnotowano w punkcie \"" + bestPosName +
                        "\", natomiast najmniejszy w punkcie \"" + worstPosName + "\"."
        );

        // ===== AKAPIT 3 =====
        addParagraph(document,
                "Łączne koszty produkcji wyniosły " + df.format(totalCosts) +
                        " zł, co w przeliczeniu na jeden tunel daje " +
                        df.format(costPerTunnel) + " zł. Na koszty produkcji składały się następujące pozycje:"
        );

        // ===== KATEGORIE (Z WCIĘCIEM) =====
        costByCategory.forEach((name, value) ->
                addIndentedBullet(document, name + " – " + df.format(value) + " zł")
        );

        addIndentedBullet(document, "Pracownicy sezonowi – " + df.format(workersCosts) + " zł");

        // ===== AKAPIT 4 =====
        XWPFParagraph p = createParagraph(document);

        run(p, "Ostateczny wynik finansowy sezonu wyniósł ");
        runBold(p, df.format(profit) + " zł");
        run(p, ", co przełożyło się na ");
        runBold(p, df.format(profitPerTunnel) + " zł");
        run(p, " zysku na jeden tunel. Średnia cena sprzedaży ");

        run(p, avgPrice >= avgPricePrev ? "wzrosła o " : "spadła o ");
        runBold(p, df.format(Math.abs(avgPrice - avgPricePrev)) + " zł/kg");
        run(p, " względem poprzedniego sezonu.");
    }

    // ===== HELPERY =====

    private void createHeading(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingBefore(600);
        p.setSpacingAfter(300);
        p.setIndentationLeft(500);
        p.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setFontSize(14);
        r.setFontFamily("Times New Roman");
        r.setText(text);
    }

    private void addParagraph(XWPFDocument doc, String text) {
        XWPFParagraph p = createParagraph(doc);

        XWPFRun r = p.createRun();
        r.setFontFamily("Times New Roman");
        r.setFontSize(12);
        r.setText(text);
    }

    private XWPFParagraph createParagraph(XWPFDocument doc) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingBetween(1.5);
        p.setFirstLineIndent(500);
        p.setAlignment(ParagraphAlignment.BOTH);

        return p;
    }

    private void addIndentedBullet(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        p.setSpacingBetween(1.5);
        p.setIndentationLeft(700);

        XWPFRun r = p.createRun();
        r.setFontFamily("Times New Roman");
        r.setFontSize(12);
        r.setText("• " + text);
    }

    private void run(XWPFParagraph p, String text) {
        XWPFRun r = p.createRun();
        r.setFontFamily("Times New Roman");
        r.setFontSize(12);
        r.setText(text);
    }

    private void runBold(XWPFParagraph p, String text) {
        XWPFRun r = p.createRun();
        r.setBold(true);
        r.setFontFamily("Times New Roman");
        r.setFontSize(12);
        r.setText(text);
    }

    private String format(LocalDate date) {
        return date != null ? dateFormatter.format(date) : "-";
    }

    private String getPosName(List<PointOfSale> list, Integer id) {
        return list.stream()
                .filter(p -> id != null && p.getId().equals(id))
                .map(PointOfSale::getName)
                .findFirst()
                .orElse("-");
    }
}