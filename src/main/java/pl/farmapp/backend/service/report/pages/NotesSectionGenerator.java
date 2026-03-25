package pl.farmapp.backend.service.report.pages;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import pl.farmapp.backend.entity.Note;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
public class NotesSectionGenerator {

    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void generate(
            XWPFDocument document,
            int seasonYear,
            List<Note> notes
    ) {

        List<Note> sorted = notes.stream()
                .sorted(Comparator.comparing(Note::getNoteDate))
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
        hRun.setText("9. Notatki");

        // ===== WSTĘP =====
        XWPFParagraph intro = document.createParagraph();
        intro.setSpacingBetween(1.5);
        intro.setFirstLineIndent(500);
        intro.setAlignment(ParagraphAlignment.BOTH);
        intro.setSpacingAfter(200);

        XWPFRun introRun = intro.createRun();
        introRun.setFontFamily("Times New Roman");
        introRun.setFontSize(12);

        introRun.setText(
                "Asystent producenta papryki umożliwia prowadzenie bieżących notatek z przebiegu sezonu. " +
                        "Poniżej przedstawiono wszystkie zapiski z roku " + seasonYear + ":"
        );

        if (sorted.isEmpty()) {
            XWPFParagraph empty = document.createParagraph();
            empty.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun r = empty.createRun();
            r.setFontFamily("Times New Roman");
            r.setFontSize(12);
            r.setItalic(true);
            r.setText("Brak notatek w danym sezonie.");

            return;
        }

        // ===== NOTATKI =====
        for (Note note : sorted) {

            XWPFParagraph p = document.createParagraph();
            p.setSpacingBetween(1.5); // 🔥 KLUCZ
            p.setAlignment(ParagraphAlignment.BOTH);

            // •
            XWPFRun bullet = p.createRun();
            bullet.setFontFamily("Times New Roman");
            bullet.setFontSize(12);
            bullet.setText("• ");

            // 🔥 TYTUŁ + DATA (BOLD)
            XWPFRun title = p.createRun();
            title.setFontFamily("Times New Roman");
            title.setFontSize(12);
            title.setBold(true);
            title.setText(
                    note.getTitle() +
                            " (" + dateFormatter.format(note.getNoteDate()) + ")"
            );

            // 🔥 MYŚLNIK + TREŚĆ (NORMAL)
            XWPFRun content = p.createRun();
            content.setFontFamily("Times New Roman");
            content.setFontSize(12);
            content.setText(" - " + note.getContent());
        }
    }
}