package pl.farmapp.backend.service.report.pages;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.VarietySeason;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

@Component
public class CropStructurePageGenerator {

    public void generate(XWPFDocument document, List<VarietySeason> varieties, Integer year) throws Exception {

        varieties.sort(Comparator.comparing(VarietySeason::getTunnelCount).reversed());

        double totalTunnels = varieties.stream()
                .mapToDouble(v -> v.getTunnelCount().doubleValue())
                .sum();

        VarietySeason dominant = varieties.get(0);

        // ===== TYTUŁ =====
        XWPFParagraph heading = document.createParagraph();
        heading.setIndentationLeft(500);
        heading.setSpacingAfter(300);
        heading.setSpacingBefore(600);
        heading.getCTP().getPPr().addNewOutlineLvl().setVal(BigInteger.ZERO);

        XWPFRun hRun = heading.createRun();
        hRun.setFontFamily("Times New Roman");
        hRun.setFontSize(14);
        hRun.setBold(true);
        hRun.setText("2. Struktura upraw");

        // ===== TEKST =====
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.BOTH);
        paragraph.setSpacingBetween(1.5);
        paragraph.setFirstLineIndent(500);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        String text =
                "W sezonie " + year +
                        " uprawa papryki prowadzona była w " +
                        String.valueOf(totalTunnels).replace(".", ",") +
                        " tunelach foliowych. Produkcja obejmowała " +
                        varieties.size() +
                        " odmiany papryki."
                        +
                        " Szczegółową strukturę upraw w sezonie " +
                        year +
                        " przedstawiono w tabeli 2 oraz na wykresie 1.";

        run.setText(text);


        // ===== TABELA ODMIAN =====
        createVarietiesTable(document, varieties, year);
        document.createParagraph();

        // ===== KOMENTARZ DO STRUKTURY =====
        XWPFParagraph commentParagraph = document.createParagraph();
        commentParagraph.setSpacingBetween(1.5);
        commentParagraph.setFirstLineIndent(500);
        commentParagraph.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun commentRun = commentParagraph.createRun();
        commentRun.setFontFamily("Times New Roman");
        commentRun.setFontSize(12);

        commentRun.setText(generateStructureComment(varieties));

        document.createParagraph();

        // ===== WYKRES =====
        byte[] chartImage = createPieChart(varieties);

        XWPFParagraph chartParagraph = document.createParagraph();
        chartParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun chartRun = chartParagraph.createRun();

        chartRun.addPicture(
                new ByteArrayInputStream(chartImage),
                Document.PICTURE_TYPE_PNG,
                "chart.png",
                Units.toEMU(320),
                Units.toEMU(220)
        );

        // ===== PODPIS WYKRESU =====
        XWPFParagraph caption = document.createParagraph();
        caption.setAlignment(ParagraphAlignment.CENTER);
        caption.setSpacingBefore(400);

        XWPFRun captionRun = caption.createRun();
        captionRun.setFontFamily("Times New Roman");
        captionRun.setFontSize(12);
        captionRun.setItalic(true);
        captionRun.setText("Wykres 1. Struktura odmian papryki w sezonie " + year);
    }

    private String generateStructureComment(List<VarietySeason> varieties) {

        double total = varieties.stream()
                .mapToDouble(v -> v.getTunnelCount().doubleValue())
                .sum();

        VarietySeason dominant = varieties.get(0);

        double dominantTunnels = dominant.getTunnelCount().doubleValue();
        double dominantPercent = (dominantTunnels / total) * 100;

        double remainingTunnels = total - dominantTunnels;
        double remainingPercent = 100 - dominantPercent;

        return "Największy udział w strukturze upraw miała odmiana "
                + dominant.getName()
                + ", która zajmowała "
                + String.format("%.1f", dominantTunnels).replace(".", ",")
                + " z "
                + String.format("%.1f", total).replace(".", ",")
                + " tuneli, co stanowiło "
                + String.format("%.1f", dominantPercent).replace(".", ",")
                + "% całej powierzchni uprawy. "
                + "Pozostałe odmiany łącznie zajmowały "
                + String.format("%.1f", remainingTunnels).replace(".", ",")
                + " tuneli, czyli "
                + String.format("%.1f", remainingPercent).replace(".", ",")
                + "%.";
    }

    private void createVarietiesTable(XWPFDocument document, List<VarietySeason> varieties, Integer year) {

        double total = varieties.stream()
                .mapToDouble(v -> v.getTunnelCount().doubleValue())
                .sum();

        // ===== PODPIS =====
        XWPFParagraph caption = document.createParagraph();
        caption.setSpacingBefore(300);

        XWPFRun run = caption.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setItalic(true);
        run.setText("Tabela 2. Struktura odmian papryki w sezonie " + year);

        // ===== TABELA =====
        XWPFTable table = document.createTable(1,3);
        setRepeatTableHeader(table.getRow(0));
        table.setWidth("100%");

        setHeader(table.getRow(0).getCell(0), "Odmiana");
        setHeader(table.getRow(0).getCell(1), "Liczba tuneli");
        setHeader(table.getRow(0).getCell(2), "Udział [%]");

        for (VarietySeason v : varieties) {

            XWPFTableRow row = table.createRow();

            double tunnels = v.getTunnelCount().doubleValue();
            double percent = (tunnels / total) * 100;

            setCell(row.getCell(0), v.getName(), false);
            setCell(row.getCell(1), String.valueOf(tunnels).replace(".", ","), true);
            setCell(row.getCell(2), String.format("%.1f", percent).replace(".", ","), true);
        }

        // ===== SUMA =====
        XWPFTableRow sumRow = table.createRow();

        setCell(sumRow.getCell(0), "Suma", true);
        setCell(sumRow.getCell(1), String.valueOf(total).replace(".", ","), true);
        setCell(sumRow.getCell(2), "100", true);

        for (XWPFRun r : sumRow.getCell(0).getParagraphs().get(0).getRuns()) {
            r.setBold(true);
        }
        for (XWPFRun r : sumRow.getCell(1).getParagraphs().get(0).getRuns()) {
            r.setBold(true);
        }
        for (XWPFRun r : sumRow.getCell(2).getParagraphs().get(0).getRuns()) {
            r.setBold(true);
        }
    }

    private byte[] createPieChart(List<VarietySeason> varieties) throws Exception {

        PieChart chart = new PieChartBuilder()
                .width(350)
                .height(250)
                .build();

        PieStyler styler = chart.getStyler();

        styler.setChartBackgroundColor(Color.WHITE);
        styler.setPlotBackgroundColor(Color.WHITE);
        styler.setPlotBorderVisible(false);

        styler.setLegendBorderColor(Color.WHITE);
        styler.setLegendBackgroundColor(Color.WHITE);
        styler.setLegendFont(new Font("Arial", Font.PLAIN, 10));

        styler.setLegendPosition(Styler.LegendPosition.OutsideE);

        styler.setPlotContentSize(0.95);

        Color[] colors = {
                new Color(27,94,32),
                new Color(46,125,50),
                new Color(67,160,71),
                new Color(129,199,132)
        };

        styler.setSeriesColors(colors);

        for (VarietySeason v : varieties) {

            chart.addSeries(
                    v.getName(),
                    v.getTunnelCount().doubleValue()
            );
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        BitmapEncoder.saveBitmap(chart, out, BitmapEncoder.BitmapFormat.PNG);

        return out.toByteArray();
    }

    private void setHeader(XWPFTableCell cell, String text) {

        cell.setColor("5fab18");

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setSpacingBefore(120);
        p.setSpacingAfter(120);
        p.setIndentationLeft(200);
        p.setIndentationRight(200);
        p.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun run = p.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setColor("FFFFFF");
        run.setText(text);
    }

    private void setCell(XWPFTableCell cell, String text, boolean center) {

        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setSpacingBefore(120);
        p.setSpacingAfter(120);
        p.setIndentationLeft(200);
        p.setIndentationRight(200);

        p.setAlignment(ParagraphAlignment.LEFT);

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

