package de.zuse.hotel.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import de.zuse.hotel.core.Booking;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.core.Room;
import de.zuse.hotel.util.ZuseCore;

import javafx.geometry.Insets;
import javafx.scene.control.Cell;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.function.Consumer;

import static java.time.temporal.ChronoUnit.DAYS;

public class InvoicePdf implements PdfFile
{
    private Booking booking;
    private static final String EURO_SYMOBL = "€";

    public InvoicePdf(Booking booking)
    {
        ZuseCore.check(booking != null, "can not generate PDF file because Booking is null!");

        this.booking = booking;
    }

    @Override
    public void saveFile(String filePath)
    {
        Document document = new Document(PageSize.A5);

        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.addTitle(HotelCore.get().getHotelName());

            //Set Title
            {
                Paragraph title = PDFWriter.createCustomParagraph(24, BaseFont.HELVETICA_BOLD,
                        BaseColor.BLACK, HotelCore.get().getHotelName());

                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                Chunk linebreak = new Chunk(new LineSeparator());
                document.add(linebreak);
            }

            //Booking details
            {
                Paragraph title = PDFWriter.createCustomParagraph(20, BaseFont.TIMES_BOLDITALIC,
                        BaseColor.BLACK, "Booking-Details");

                document.add(title);
                document.add(new Paragraph("Check-in: " + booking.getStartDate()));
                document.add(new Paragraph("Check-out: " + booking.getEndDate()));

                //TODO (Basel): maybe add many Guests
                document.add(new Paragraph("Guest: " + booking.getGuestName()));
                document.add(new Paragraph("Room: " + booking.getRoomNumber()));
                document.add(new Paragraph("Floor: " + booking.getFloorNumber()));
                document.add(new Paragraph("Guests-Number: " + booking.getGuestsNum()));
                Chunk linebreak = new Chunk(new DottedLineSeparator());
                document.add(linebreak);
            }

            // Booked By
            {
                Paragraph title = PDFWriter.createCustomParagraph(20, BaseFont.TIMES_BOLDITALIC,
                        BaseColor.BLACK, "Booked By");

                document.add(title);
                document.add(new Paragraph("Name: " + booking.getGuestName()));
                document.add(new Paragraph("Email: " + booking.getGuest().getEmail()));
                document.add(new Paragraph("Tel.Nr: " + booking.getGuest().getTelNumber()));
                Chunk linebreak = new Chunk(new DottedLineSeparator());
                document.add(linebreak);
            }

            // Price Description
            {
                Paragraph description = PDFWriter.createCustomParagraph(20, BaseFont.TIMES_BOLDITALIC,
                        BaseColor.BLACK, "Description:");

                document.add(description);

                Room room = HotelCore.get().getRoomByRoomNr(booking.getFloorNumber(), booking.getRoomNumber());
                double roomPrice = room.getPrice();
                document.add(new Paragraph("Room (" + room.getRoomType() + "): " + roomPrice + EURO_SYMOBL + " (For one day)"));

                double totalPrice = booking.coastPerNight(roomPrice);
                long daysBetween = DAYS.between(booking.getStartDate(), booking.getEndDate());

                String extraServices = "";
                for (String service : booking.getBookedServices())
                {
                    if (!extraServices.trim().isEmpty())
                        extraServices += ", ";

                    extraServices += service;
                }

                document.add(new Paragraph("Total: " + Math.abs(daysBetween) + " Day(s) * "
                        + roomPrice + " + Extra services: ("+extraServices+") = " + (totalPrice) + EURO_SYMOBL));
            }

            document.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
