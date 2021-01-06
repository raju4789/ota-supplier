package com.ogado.supplier.handlers;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.ogado.supplier.constants.HTTPStatus;
import com.ogado.supplier.models.BookingInfo;
import com.ogado.supplier.models.BookingResponse;
import com.ogado.supplier.services.ISupplierService;
import com.ogado.supplier.services.SupplierService;
import com.ogado.supplier.utils.JsonMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FilterBookingsHandler implements HttpHandler {

	private static Logger log = Logger.getLogger(FilterBookingsHandler.class);

	public void handle(HttpExchange httpExchange) throws IOException {

		BookingResponse bookingResponse = new BookingResponse();
		try {
			ISupplierService supplierService = new SupplierService();

			String bookingReference = httpExchange.getRequestURI().getPath().split("/")[4];
			if (bookingReference == null) {
				log.error("failed to fetch booking by bookingReference : bookingReference is mandatory");
				bookingResponse.setHttpStatus(HTTPStatus.BAD_REQUEST);
				bookingResponse
						.setErrorMessage("failed to fetch booking by bookingReference : bookingReference is mandatory");
			} else {
				BookingInfo bookingInfo = supplierService.getBookingById(bookingReference);

				if (bookingInfo == null) {
					bookingResponse.setHttpStatus(HTTPStatus.BAD_REQUEST);
					bookingResponse.setErrorMessage("failed to fetch booking by bookingReference : No record found");
				}

				bookingResponse.setHttpStatus(HTTPStatus.OK);
				bookingResponse.setBookingInfo(bookingInfo);
				log.info("successfully fetched bookings by bookingReference");

			}

		} catch (Exception e) {
			log.error("failed to fetch bookings by bookingReference: " + e.getMessage());
			bookingResponse.setHttpStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
			bookingResponse.setErrorMessage("failed to fetch booking by bookingId ");
		}

		try {
			String bookingsStr = JsonMapper.stringifyPretty(bookingResponse);
			OutputStream outputStream = httpExchange.getResponseBody();
			httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
			httpExchange.sendResponseHeaders(bookingResponse.getHttpStatus(), bookingsStr.length());
			outputStream.write(bookingsStr.getBytes());

			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			log.error("failed to fetch bookings by bookingReference : " + e.getMessage());
		}

	}

}
