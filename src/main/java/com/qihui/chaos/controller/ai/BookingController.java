package com.qihui.chaos.controller.ai;

import com.qihui.chaos.config.BookingTools;
import com.qihui.chaos.service.FlightBookingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {

    private final FlightBookingService flightBookingService;

    public BookingController(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/api/bookings")
    @ResponseBody
    public List<BookingTools.BookingDetails> getBookings() {
        return flightBookingService.getBookings();
    }
}
