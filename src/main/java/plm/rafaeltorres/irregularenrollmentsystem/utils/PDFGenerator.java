package plm.rafaeltorres.irregularenrollmentsystem.utils;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.model.Student;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {
    public static boolean generateSER(Stage stage, Student student, ResultSet rs) throws IOException, SQLException {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")+"/Downloads/"));

        File directory = dc.showDialog(stage);
        String path = directory.getAbsolutePath() + "/registration_form_" + student.getStudentNo() + "_"+DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()) + ".pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        PdfFont timesNewRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

        ImageData imageData = ImageDataFactory.create(MainScene.class.getResource("assets/img/PLM_Seal_2013.png"));
        com.itextpdf.layout.element.Image image = new Image(imageData);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setFont(timesNewRoman);

        float x = pdfDocument.getDefaultPageSize().getWidth()/2;
        float y = pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setFixedPosition(x - image.getImageWidth() / 2, y);
        image.setOpacity(0.1f);

        document.add(image);

        Table table = getHeader("STUDENT ENROLLMENT RECORD");
        Table studentInfo = getStudentInfoRow1(student);
        Table studentInfoRow2 = getStudentInfoRow2(student);
        Table divider = createDivider();

        Table grades = new Table(6).setWidth(525f);
        float headerSize = 8.5f;
        grades.addHeaderCell(new Cell()
                .add(new Paragraph("Subject Code")
                        .setFontSize(headerSize)
                        .setBold()

                )
                .setBackgroundColor(DeviceGray.GRAY)
        );
        grades.addHeaderCell(new Cell()
                .add(new Paragraph("Section")
                        .setFontSize(headerSize)
                        .setBold())
                .setBackgroundColor(DeviceGray.GRAY)
        );
        grades.addHeaderCell(new Cell()
                .add(new Paragraph("Subject Title")
                        .setFontSize(headerSize)
                        .setBold()
                )
                .setBackgroundColor(DeviceGray.GRAY)
        );
        grades.addHeaderCell(new Cell()
                .add(new Paragraph("Units")
                        .setFontSize(headerSize)
                        .setBold()
                )
                .setBackgroundColor(DeviceGray.GRAY)
        );
        grades.addHeaderCell(new Cell()
                .add(new Paragraph("Schedule")
                        .setFontSize(headerSize)
                        .setBold()
                )
                .setBackgroundColor(DeviceGray.GRAY)
        );
        grades.addHeaderCell(new Cell()
                .add(new Paragraph("Professor")
                        .setFontSize(headerSize)
                        .setBold()
                )
                .setBackgroundColor(DeviceGray.GRAY)
        );
        grades.getHeader().setTextAlignment(TextAlignment.CENTER);


        int totalUnits = 0;
        while(rs.next()){
            for(int i = 1; i <= rs.getMetaData().getColumnCount(); ++i){
                if(rs.getMetaData().getColumnName(i).equals("CREDITS")){
                    totalUnits += rs.getInt(i);
                }
                grades.addCell(new Cell()
                        .add(new Paragraph(rs.getString(i))
                                .setFontSize(7)
                                ));
            }
        }

        Table remarks = new Table(4).setWidth(525f);
        remarks.addCell(new Cell()
                .add(new Paragraph("Remarks: This enrollment becomes official until all requirements are complied with")
                        .setFontSize(7)

                ));
        Table unitsLabel = new Table(1);
        unitsLabel.addCell(new Cell()
                .add(new Paragraph("Total Units:")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        unitsLabel.addCell(new Cell()
                .add(new Paragraph(totalUnits+"")
                    .setFontSize(7f)

                    .setBold())
                .setBorder(Border.NO_BORDER));
        remarks.addCell(new Cell().add(unitsLabel));

        Table date = new Table(1);
        date.addCell(new Cell()
                .add(new Paragraph("Date:")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        date.addCell(new Cell()
                .add(new Paragraph(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()))
                        .setFontSize(7f)
                        .setBold()

                )
                .setBorder(Border.NO_BORDER));
        remarks.addCell(date);

        Table encoder = new Table(1);
        encoder.addCell(new Cell()
                .add(new Paragraph("Encoder:")
                        .setFontSize(6f)
                )
                .setBorder(Border.NO_BORDER));
        encoder.addCell(new Cell()
                .add(new Paragraph("")
                        .setFontSize(7f))
                .setBorder(Border.NO_BORDER));

        remarks.addCell(encoder);

        document.add(table);
        document.add(divider);
        document.add(studentInfo);
        document.add(studentInfoRow2);
        document.add(new Paragraph("\n").setFontSize(2f));
        document.add(divider);
        document.add(new Paragraph("\n").setFontSize(2f));
        document.add(grades);
        document.add(remarks);
        document.close();

        return pdfDocument.isClosed();
    }
    public static boolean generateTuitionSummary(Stage stage, Student student, TableView<ObservableList<String>> tblTuitionFees, int totalUnits) throws IOException {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")+"/Downloads/"));
        File directory = dc.showDialog(stage);
        String path = directory.getAbsolutePath() + "/tuition_invoice_" + student.getStudentNo() + "_"+DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()) + ".pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        PdfFont timesNewRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

        ImageData imageData = ImageDataFactory.create(MainScene.class.getResource("assets/img/PLM_Seal_2013.png"));
        com.itextpdf.layout.element.Image image = new Image(imageData);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setFont(timesNewRoman);


        float x = pdfDocument.getDefaultPageSize().getWidth()/2;
        float y = pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setFixedPosition(x - image.getImageWidth() / 2, y);
        image.setOpacity(0.1f);

        document.add(image);

        Table table = getHeader("STUDENT TUITION INVOICE");
        Table studentInfo = getStudentInfoRow1(student);
        Table studentInfoRow2 = getStudentInfoRow2(student);
        Table divider = createDivider();
        Table tuition = getTuitionInvoice(tblTuitionFees, totalUnits);

        document.add(table);
        document.add(studentInfo);
        document.add(studentInfoRow2);
        document.add(new Paragraph("\n").setFontSize(2f));
        document.add(divider);
        document.add(new Paragraph("\n").setFontSize(2f));
        document.add(tuition);
        document.close();
        return pdfDocument.isClosed();
    }

    private static Table getHeader(String title){
        Table table = new Table(3).setWidth(525f);
        Table plm = new Table(new float[]{10f});
        plm.addCell(new Cell().add(new Paragraph("PAMANTASAN NG LUNGSOD NG MAYNILA")).setBorder(Border.NO_BORDER).setFontSize(8f).setBold().setMargin(2f));
        plm.addCell(new Cell().add(new Paragraph("(University of the City of Manila)").setFontSize(7f)).setBorder(Border.NO_BORDER));
        plm.addCell(new Cell().add(new Paragraph("Intramuros, Manila").setFontSize(7f)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(plm).setBorder(Border.NO_BORDER));

        Table ser = new Table(1);
        ser.addCell(new Cell().add(new Paragraph(title)
                        .setFontSize(10f)

                        .setBold())
                .setBorder(Border.NO_BORDER));
        Table sySem = new Table(new float[]{10f, 10f, 10f, 10f, 10f});
        sySem.addCell(new Cell()
                .add(new Paragraph(""))
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph(StringUtils.integerToPlace(Integer.parseInt(Maintenance.getInstance().getCurrentSem())) + " Semester")
                        .setFontSize(7f)

                )
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph("School Year")
                        .setFontSize(7f)

                )
                .setBorder(Border.NO_BORDER));
        sySem.addCell(new Cell()
                .add(new Paragraph(Maintenance.getInstance().getCurrentSY())
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
        return table;
    }

    private static Table getStudentInfoRow1(Student student){
        Table studentInfo = new Table(5).setWidth(525f);
        Table studentNo = new Table(1);
        studentNo.addCell(new Cell()
                .add(new Paragraph("Student No")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        studentNo.addCell(new Cell()
                .add(new Paragraph(student.getStudentNo())
                        .setFontSize(8f)

                )
                .setBold()
                .setBorder(Border.NO_BORDER));

        studentInfo.addCell(new Cell()
                .add(studentNo));

        Table studentName = new Table(1);
        studentName.addCell(new Cell()
                .add(new Paragraph("Student Name")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        studentName.addCell(new Cell()
                .add(new Paragraph((student.getLastName() +", "+student.getFirstName()).toUpperCase())
                        .setFontSize(8f)

                )
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(studentName));

        Table type = new Table(1);
        type.addCell(new Cell()
                .add(new Paragraph("Student Type")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        type.addCell(new Cell()
                .add(new Paragraph((Integer.parseInt(student.getStudentNo().substring(0, 4)) == Integer.parseInt(Maintenance.getInstance().getCurrentSY().substring(0,4))) ? "New" : "Old")
                        .setFontSize(8f)

                )
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(type));

        Table year = new Table(1);
        year.addCell(new Cell()
                .add(new Paragraph("Year")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        year.addCell(new Cell()
                .add(new Paragraph(""+(1+Integer.parseInt(Maintenance.getInstance().getCurrentSY().substring(0,4)) - Integer.parseInt(student.getStudentNo().substring(0, 4))))
                        .setFontSize(8f)

                )
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(year));


        Table registrationStatus = new Table(1);
        registrationStatus.addCell(new Cell()
                .add(new Paragraph("Registration Status")
                        .setFontSize(6f)

                )
                .setBorder(Border.NO_BORDER));
        registrationStatus.addCell(new Cell()
                .add(new Paragraph(student.getRegistrationStatus())
                        .setFontSize(8f)

                )
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfo.addCell(new Cell()
                .add(registrationStatus));

        return studentInfo;
    }
    private static Table getStudentInfoRow2(Student student){
        Table studentInfoRow2 = new Table(2).setWidth(525f);

        Table college = new Table(1);
        college.addCell(new Cell()
                .add(new Paragraph("College")
                        .setFontSize(6f)
                )
                .setBorder(Border.NO_BORDER));
        college.addCell(new Cell()
                .add(new Paragraph(student.getCollege())
                        .setFontSize(8f)
                )
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfoRow2.addCell(new Cell()
                .add(college));

        Table course = new Table(1);
        course.addCell(new Cell()
                .add(new Paragraph("Course")
                        .setFontSize(6f)
                )
                .setBorder(Border.NO_BORDER));
        course.addCell(new Cell()
                .add(new Paragraph(student.getCourse())
                        .setFontSize(8f)
                )
                .setBold()
                .setBorder(Border.NO_BORDER));
        studentInfoRow2.addCell(new Cell()
                .add(course));

        return studentInfoRow2;
    }

    private static Table createDivider(){
        Table divider = new Table(new float[] {190*3});
        divider.setBorder(new SolidBorder(DeviceGray.GRAY, 1f/2));
        return divider;
    }
    private static Table getTuitionInvoice(TableView<ObservableList<String>> tblTuitionFees, int totalUnits){
        double total = 0.00;
        Table tuition = new Table(2).setWidth(525f);
        tuition.addHeaderCell(new Cell()
                .add(new Paragraph("Description")
                        .setFontSize(7f)
                        .setBold())
                .setBackgroundColor(DeviceGray.GRAY));
        tuition.addHeaderCell(new Cell()
                .add(new Paragraph("Amount")
                        .setFontSize(7f)
                        .setBold())
                .setBackgroundColor(DeviceGray.GRAY));
        tuition.getHeader().setTextAlignment(TextAlignment.CENTER);

        for(ObservableList<String> item : tblTuitionFees.getItems()){
            if(item.get(0).equalsIgnoreCase("Tuition Fee (Price per Unit)")){
                total += Double.parseDouble(item.get(1)) * totalUnits;
                tuition.addCell(new Cell()
                        .add(new Paragraph("Tuition Fee (" + totalUnits + " units)")
                                .setFontSize(6f)));
                tuition.addCell(new Cell()
                        .add(new Paragraph(String.format("%,.2f", totalUnits*Double.parseDouble(item.get(1))))
                                .setFontSize(6f)));
            }
            else{
                total += Double.parseDouble(item.get(1));
                tuition.addCell(new Cell()
                        .add(new Paragraph(item.get(0))
                                .setFontSize(6f)));
                tuition.addCell(new Cell()
                        .add(new Paragraph(String.format("%,.2f", Double.parseDouble(item.get(1))))
                                .setFontSize(6f)));
            }
        }

        tuition.addCell(new Cell()
                .add(new Paragraph("TOTAL AMOUNT DUE")
                        .setFontSize(6f)
                        .setBold())
                .setBorder(Border.NO_BORDER));
        tuition.addCell(new Cell()
                .add(new Paragraph(String.format("%,.2f", total))
                        .setFontSize(6f)
                        .setBold())
                .setBorder(Border.NO_BORDER));

        tuition.addCell(new Cell()
                .add(new Paragraph("SUBSIDIZED BY THE CITY OF MANILA")
                        .setFontSize(6f)
                        .setBold())
                .setBorder(Border.NO_BORDER)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT));
        tuition.addCell(new Cell()
                .add(new Paragraph(String.format("%,.2f", total * 0.65))
                        .setFontSize(6f)
                        .setBold())
                .setBorder(Border.NO_BORDER));

        tuition.addCell(new Cell()
                .add(new Paragraph("Billed to CHED FHE")
                        .setFontSize(6f)
                        .setBold())
                .setBorder(Border.NO_BORDER)
                .setHorizontalAlignment(HorizontalAlignment.RIGHT));
        tuition.addCell(new Cell()
                .add(new Paragraph(String.format("%,.2f", total * 0.35))
                        .setFontSize(6f)
                        .setBold())
                .setBorder(Border.NO_BORDER));

        return tuition;
    }
}