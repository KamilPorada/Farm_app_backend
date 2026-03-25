package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.springframework.stereotype.Component;

@Component
public class TableOfContentsGenerator {

    public void generate(XWPFDocument document) {

        // ===== NOWA STRONA =====
        XWPFParagraph pageBreak = document.createParagraph();
        pageBreak.setPageBreak(true);

        // ===== TYTUŁ =====
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.LEFT);
        title.setSpacingAfter(300);

        XWPFRun titleRun = title.createRun();
        titleRun.setBold(true);
        titleRun.setFontFamily("Times New Roman");
        titleRun.setFontSize(14);
        titleRun.setText("Spis treści");

        // ===== SPIS TREŚCI =====
        XWPFParagraph tocParagraph = document.createParagraph();
        tocParagraph.setSpacingBetween(1.5);
        tocParagraph.setAlignment(ParagraphAlignment.LEFT);

        CTP ctP = tocParagraph.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();

        // 🔥 Heading1–Heading3
        toc.setInstr("TOC \\o \"1-3\" \\h \\z \\u");

        // ===== NOWA STRONA PO TOC =====
        XWPFParagraph pageBreak2 = document.createParagraph();
        pageBreak2.setPageBreak(true);
    }
}