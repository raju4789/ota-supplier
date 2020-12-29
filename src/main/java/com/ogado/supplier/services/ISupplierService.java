package com.ogado.supplier.services;

import java.sql.SQLException;
import java.util.List;

import com.ogado.supplier.models.BookingInfo;
import com.ogado.supplier.models.SupplierResponse;

public interface ISupplierService {
	
	public SupplierResponse createBooking(BookingInfo bookingInfo) throws SQLException, Exception;
	
	public List<BookingInfo> filterBookings(String checkInDate, String checkOutDate, String status);
	

}
