package com.ogado.supplier.services;

import java.sql.SQLException;

import com.ogado.supplier.models.BookingInfo;
import com.ogado.supplier.models.SupplierResponse;

public interface ISupplierService {
	
	public SupplierResponse createBooking(BookingInfo bookingInfo) throws SQLException, Exception;
	
	public BookingInfo getBookingById(String bookingReference) throws SQLException;
	
	public void randomlyUpdateBooking() throws SQLException, Exception;

}
