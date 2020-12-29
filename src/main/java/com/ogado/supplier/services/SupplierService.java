package com.ogado.supplier.services;

import java.sql.SQLException;
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
		
		if(errors.size() > 0) {
			supplierResponse.setHttpStatus(HTTPStatus.BAD_REQUEST);
			supplierResponse.setErrors(errors);
			log.error("invalid booking request");
			
			return supplierResponse;
		}
		
		String bookingReference = UUID.randomUUID().toString();

		while (supplierDAO.isBookingExist(bookingReference)) {
			bookingReference = UUID.randomUUID().toString();
		}
		
		bookingInfo.setBookingReference(bookingReference);
		bookingInfo.setStatus(ApplicationConstants.STATUS_CONFIRMED);
		
		BookingInfo dbBooking = supplierDAO.saveBooking(bookingInfo);
		
		if(dbBooking == null) {
			supplierResponse.setHttpStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
			errors.add("");
			supplierResponse.setErrors(errors);
			return supplierResponse;

		}
		
		supplierResponse.setHttpStatus(HTTPStatus.CREATED);
		supplierResponse.setBookingInfo(dbBooking);
		return supplierResponse;
	}

	

	@Override
	public List<BookingInfo> filterBookings(String checkInDate, String checkOutDate, String status) {
		// TODO Auto-generated method stub
		return null;
	}

}
