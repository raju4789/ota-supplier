package com.ogado.supplier.dao;

import java.sql.SQLException;
import java.util.List;

import com.ogado.supplier.models.BookingInfo;

public interface ISupplierDAO {
	
	public BookingInfo saveBooking(BookingInfo bookingInfo) throws SQLException;
	
	public List<BookingInfo> filterBookings(String checkInDate, String checkOutDate, String status);
	

	boolean isBookingExist(String bookingId) throws SQLException;

}
