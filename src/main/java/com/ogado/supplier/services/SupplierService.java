package com.ogado.supplier.services;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.ogado.supplier.constants.ApplicationConstants;
import com.ogado.supplier.constants.HTTPStatus;
import com.ogado.supplier.dao.ISupplierDAO;
import com.ogado.supplier.dao.SupplierDAO;
import com.ogado.supplier.exceptions.ConfigurationException;
import com.ogado.supplier.models.BookingInfo;
import com.ogado.supplier.models.SupplierResponse;
import com.ogado.supplier.utils.APIValidationUtil;

public class SupplierService implements ISupplierService {

	private static Logger log = Logger.getLogger(SupplierService.class);

	private ISupplierDAO supplierDAO;

	public SupplierService() throws ConfigurationException, SQLException {

		supplierDAO = new SupplierDAO();

	}

	@Override
	public SupplierResponse createBooking(BookingInfo bookingInfo) throws Exception {

		SupplierResponse supplierResponse = new SupplierResponse();

		List<String> errors = APIValidationUtil.validateRequest(bookingInfo);

		if (errors.size() > 0) {
			supplierResponse.setHttpStatus(HTTPStatus.BAD_REQUEST);
			supplierResponse.setErrors(errors);
			log.error("invalid booking request");

			return supplierResponse;
		}

		String bookingReference = UUID.randomUUID().toString();

		while (supplierDAO.getBookingById(bookingReference) != null) {
			bookingReference = UUID.randomUUID().toString();
		}

		bookingInfo.setBookingReference(bookingReference);
		bookingInfo.setStatus(ApplicationConstants.STATUS_CONFIRMED);

		BookingInfo dbBooking = supplierDAO.saveBooking(bookingInfo);

		if (dbBooking == null) {
			supplierResponse.setHttpStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
			errors.add("failed to create booking");
			supplierResponse.setErrors(errors);
			return supplierResponse;

		}

		supplierResponse.setHttpStatus(HTTPStatus.CREATED);
		supplierResponse.setBookingInfo(dbBooking);
		return supplierResponse;
	}

	@Override
	public BookingInfo getBookingById(String bookingReference) throws SQLException {
		return supplierDAO.getBookingById(bookingReference);
	}

	@Override
	public void randomlyUpdateBooking() throws SQLException, Exception {
		List<BookingInfo> confirmedBookings = supplierDAO.filterBookings(Date.valueOf(LocalDate.now()).toString(), ApplicationConstants.STATUS_CONFIRMED, 3);
		
		for (BookingInfo bookingInfo : confirmedBookings) {
			bookingInfo.setStatus(ApplicationConstants.STATUS_CANCELED);
			bookingInfo.setUpdatedOn(LocalDateTime.now().toString());

			supplierDAO.updateBookingById(bookingInfo);
		}
		
		List<BookingInfo> cancelledBookings = supplierDAO.filterBookings(Date.valueOf(LocalDate.now()).toString(), ApplicationConstants.STATUS_CANCELED, 2);
		for (BookingInfo bookingInfo : cancelledBookings) {
			bookingInfo.setStatus(ApplicationConstants.STATUS_CONFIRMED);
			supplierDAO.updateBookingById(bookingInfo);
			bookingInfo.setUpdatedOn(LocalDateTime.now().toString());

		}

		
	}

}
