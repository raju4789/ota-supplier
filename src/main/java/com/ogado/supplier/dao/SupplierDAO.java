package com.ogado.supplier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ogado.supplier.config.db.DBConnectionManager;
import com.ogado.supplier.constants.ApplicationConstants;
import com.ogado.supplier.exceptions.ConfigurationException;
import com.ogado.supplier.models.BookingInfo;
import com.ogado.supplier.utils.ConfigLoader;

public class SupplierDAO implements ISupplierDAO {

	private Connection dbConnection;
	private static SupplierQueries supplierQueries;

	private static Logger log = Logger.getLogger(SupplierDAO.class);

	public SupplierDAO() throws ConfigurationException, SQLException {
		dbConnection = DBConnectionManager.getDBConnection();
		supplierQueries = ConfigLoader.loadConfiguration(ApplicationConstants.DB_QUERIES_CONFIG_FILE,
				SupplierQueries.class);
		createBookingsTable();
	}

	@Override
	public BookingInfo saveBooking(BookingInfo bookingInfo) throws SQLException {

		if (dbConnection == null) {
			log.error("Failed to acquire db connection");

		}

		String insertQuery = supplierQueries.getInsertBooking();
		PreparedStatement stmt = dbConnection.prepareStatement(insertQuery);
		stmt.setString(1, bookingInfo.getBookingId());
		stmt.setString(2, bookingInfo.getCheckInDate());
		stmt.setString(3, bookingInfo.getCheckOutDate());
		stmt.setString(4, bookingInfo.getHotelName());
		stmt.setInt(5, bookingInfo.getNoOfGuests());
		stmt.setString(6, bookingInfo.getStatus());
		stmt.setString(7, bookingInfo.getBookingReference());
		stmt.setString(8, bookingInfo.getCreatedOn());
		stmt.setString(9, bookingInfo.getUpdatedOn());

		int row = stmt.executeUpdate();

		if (row != 1) {
			log.error("failed to create booking");
			stmt.close();
			return null;
		}

		log.info("Succesfully created booking");

		stmt.close();

		return bookingInfo;

	}

	@Override
	public List<BookingInfo> filterBookings(String checkInDate, String status, int limit) throws SQLException {
		if (dbConnection == null) {
			log.error("Failed to acquire db connection");
		}

		String filterQuery = formSearchQueryByCheckInDate(checkInDate, status, limit);

		Statement stmt = dbConnection.createStatement();

		List<BookingInfo> filteredQueries = new ArrayList<>();
		ResultSet rs = stmt.executeQuery(filterQuery);

		while (rs.next()) {

			BookingInfo curBooking = new BookingInfo();
			curBooking.setBookingId(rs.getString("booking_id"));
			curBooking.setCheckInDate(rs.getString("check_in_date").toString());
			curBooking.setCheckOutDate(rs.getString("check_out_date").toString());
			curBooking.setHotelName(rs.getString("hotel_name"));
			curBooking.setNoOfGuests(rs.getInt("no_of_guests"));
			curBooking.setStatus(rs.getString("status"));
			curBooking.setBookingReference(rs.getString("booking_reference"));
			curBooking.setCreatedOn(rs.getTimestamp("created_on").toString());
			curBooking.setUpdatedOn(rs.getTimestamp("updated_on").toString());

			filteredQueries.add(curBooking);
		}

		return filteredQueries;
	}

	@Override
	public BookingInfo getBookingById(String bookingReference) throws SQLException {

		if (dbConnection == null) {
			log.error("Failed to acquire db connection");

		}
		PreparedStatement stmt = dbConnection.prepareStatement(supplierQueries.getSelectBooking());
		stmt.setString(1, bookingReference);

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			BookingInfo curBooking = new BookingInfo();
			curBooking.setCheckInDate(rs.getString("check_in_date").toString());
			curBooking.setCheckOutDate(rs.getString("check_out_date").toString());
			curBooking.setStatus(rs.getString("status"));
			curBooking.setBookingReference(rs.getString("booking_reference"));
			return curBooking;
		}

		return null;
	}
	
	@Override
	public void updateBookingById(BookingInfo bookingInfo) throws SQLException {

		if (dbConnection == null) {
			log.error("Failed to acquire db connection");

		}
		PreparedStatement stmt = dbConnection.prepareStatement(supplierQueries.getSelectBooking());
		stmt.setString(1, bookingInfo.getBookingReference());

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			stmt = dbConnection.prepareStatement(supplierQueries.getUpdateBooking());
			stmt.setString(1, bookingInfo.getCheckInDate());
			stmt.setString(2, bookingInfo.getCheckOutDate());
			stmt.setString(3, bookingInfo.getStatus());
			stmt.setString(4, bookingInfo.getUpdatedOn());
			stmt.setString(5, bookingInfo.getBookingReference());
			stmt.executeUpdate();
		}else {
			log.error("No booking found with given booking referencee");
		}

		stmt.close();
	}


	private void createBookingsTable() throws SQLException {
		String sql = supplierQueries.getCreateBookingTable();

		Statement stmt = dbConnection.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	private String formSearchQueryByCheckInDate(String checkInDate, String status, int limit) {
		String searchQuery = supplierQueries.getFilterBookings();

		searchQuery += "DATE('" + checkInDate + "') AND status = '"+status.toUpperCase()+ "' ORDER BY DATE(updated_on) DESC limit "+limit;

		return searchQuery;
	}

}
