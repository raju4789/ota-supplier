package com.ogado.supplier.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.ogado.supplier.constants.HTTPStatus;
import com.ogado.supplier.models.BookingInfo;
import com.ogado.supplier.models.SupplierResponse;
import com.ogado.supplier.services.ISupplierService;
import com.ogado.supplier.services.SupplierService;
import com.ogado.supplier.utils.JsonMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CreateBookingsHandler implements HttpHandler {

	private static Logger log = Logger.getLogger(CreateBookingsHandler.class);

	public void handle(HttpExchange httpExchange) {
		SupplierResponse supplierResponse = null;
		try {
			ISupplierService supplierService = new SupplierService();
			BookingInfo bookingInfo = getRequestObject(httpExchange.getRequestBody());

			log.info("createBookingsHandler called with request: " + bookingInfo);

			supplierResponse = supplierService.createBooking(bookingInfo);
		} catch (Exception e) {
			log.error("failed to create booking: " + e.getMessage());

			supplierResponse = new SupplierResponse();
			supplierResponse.setHttpStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			String supplierResponseStr = JsonMapper.stringifyPretty(supplierResponse);
			OutputStream outputStream = httpExchange.getResponseBody();
			httpExchange.getResponseHeaders().set("Content-Type", "appication/json");
			httpExchange.sendResponseHeaders(supplierResponse.getHttpStatus(), supplierResponseStr.length());
			outputStream.write(supplierResponseStr.getBytes());

			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			log.error("failed to send response: " + e.getMessage());
		}

	}

	private BookingInfo getRequestObject(InputStream inputStream) throws IOException {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}

		return JsonMapper.parse(sb.toString(), BookingInfo.class);

	}

}
