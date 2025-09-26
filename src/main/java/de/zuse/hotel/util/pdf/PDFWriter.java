package de.zuse.hotel.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import de.zuse.hotel.util.ZuseCore;

import java.io.FileOutputStream;
import java.io.IOException;

public class PDFWriter
{
    public static Paragraph createCustomParagraph(int fontSize, String fontType, BaseColor color, String text)
    {
        BaseFont bf = null;

        try
        {
            bf = BaseFont.createFont(fontType, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e)
        {
            ZuseCore.coreAssert(false, e.getMessage());
        }

        Font font = new Font(bf, fontSize);
        return new Paragraph(text, font);
    }
}
