package plm.rafaeltorres.irregularenrollmentsystem.utils;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PDFGenerator {
    public static void main(String[] args) throws FileNotFoundException {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")+"/Downloads/"));
//        File directory = dc.showDialog(stage);
//        String path = directory.getAbsolutePath() + "/" + System.currentTimeMillis() + "SER.pdf";
        String path = "C:/Users/raffy/Downloads/SER.pdf";


        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        float col = 285f;
        float col150 = col+150f;
        float[] colWidth = {col150, col};

        Table table = new Table(3).setWidth(525f);
        Table plm = new Table(new float[]{10f});
        plm.addCell(new Cell().add(new Paragraph("PAMANTASAN NG LUNGSOD NG MAYNILA")).setBorder(Border.NO_BORDER).setFontSize(8f).setBold().setMargin(2f));
        plm.addCell(new Cell().add(new Paragraph("(University of the City of Manila)").setFontSize(7f)).setBorder(Border.NO_BORDER));
        plm.addCell(new Cell().add(new Paragraph("Intramuros, Manila").setFontSize(7f)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(plm).setBorder(Border.NO_BORDER));


        Table ser = new Table(1);
        ser.addCell(new Cell().add(new Paragraph("STUDENT ENROLLMENT RECORD")
                .setFontSize(10f).setBold())
                .setBorder(Border.NO_BORDER));
        Table sySem = new Table(new float[]{10f, 10f, 10f, 10f, 10f});
        sySem.addCell(new Cell()
                .add(new Paragraph(""))
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph("1st Semester")
                        .setFontSize(7f))
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph("School Year")
                        .setFontSize(7f))
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph("2023-2024")
                        .setFontSize(7f)
                        .setBold())
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph(""))
                .setBorder(Border.NO_BORDER));
        ser.addCell(new Cell()
                .add(sySem)
                .setBorder(Border.NO_BORDER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER));

        table.addCell(new Cell()
                .add(ser)
                .setBorder(Border.NO_BORDER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER));
        table.addCell(new Cell()
                .add(new Paragraph("Student's Copy")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold())
                .setBackgroundColor(DeviceGray.GRAY)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        Table divider = new Table(new float[] {190*3});
        divider.setBorder(new SolidBorder(DeviceGray.GRAY, 1f/2));

        Table studentInfo = new Table(5).setWidth(525f);
        Table studentNo = new Table(1);
        studentNo.addCell(new Cell()
                .add(new Paragraph("Student No")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        studentNo.addCell(new Cell()
                .add(new Paragraph("2022-34000")
                        .setFontSize(8f))
                        .setBold()
                .setBorder(Border.NO_BORDER));

        studentInfo.addCell(new Cell()
                .add(studentNo));

        Table studentName = new Table(1);
        studentName.addCell(new Cell()
                .add(new Paragraph("Student Name")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        studentName.addCell(new Cell()
                .add(new Paragraph("TORRES, RAFAEL SEBASTIAN DC.")
                        .setFontSize(8f))
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(studentName));

        Table type = new Table(1);
        type.addCell(new Cell()
                .add(new Paragraph("Student Type")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        type.addCell(new Cell()
                .add(new Paragraph("Old")
                        .setFontSize(8f))
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(type));

        Table year = new Table(1);
        year.addCell(new Cell()
                .add(new Paragraph("Year")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        year.addCell(new Cell()
                .add(new Paragraph("2")
                        .setFontSize(8f))
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(year));


        Table registrationStatus = new Table(1);
        registrationStatus.addCell(new Cell()
                .add(new Paragraph("Registration Status")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        registrationStatus.addCell(new Cell()
                .add(new Paragraph("Regular")
                        .setFontSize(8f))
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(registrationStatus));

        Table studentInfoRow2 = new Table(2).setWidth(525f);

        Table college = new Table(1);
        college.addCell(new Cell()
                .add(new Paragraph("College")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        college.addCell(new Cell()
                .add(new Paragraph("CET")
                        .setFontSize(8f))
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfoRow2.addCell(new Cell()
                .add(college));

        Table course = new Table(1);
        course.addCell(new Cell()
                .add(new Paragraph("Course")
                        .setFontSize(6f))
                .setBorder(Border.NO_BORDER));
        course.addCell(new Cell()
                .add(new Paragraph("BSCS")
                        .setFontSize(8f))
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfoRow2.addCell(new Cell()
                .add(course));





        document.add(table);
        document.add(divider);
        document.add(studentInfo);
        document.add(studentInfoRow2);
        document.add(new Paragraph("\n").setFontSize(2f));
        document.add(divider);
        document.add(new Paragraph("\n").setFontSize(2f));


        document.close();
    }
}
