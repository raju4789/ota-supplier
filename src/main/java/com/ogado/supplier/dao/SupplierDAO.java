package com.ogado.supplier.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

		LocalDateTime checkInDate = LocalDateTime.parse(bookingInfo.getCheckInDate());
		LocalDateTime checkOutDate = LocalDateTime.parse(bookingInfo.getCheckOutDate());
		
		LocalDateTime createdOn = LocalDateTime.parse(bookingInfo.getCreatedOn());
		LocalDateTime updatedOn = LocalDateTime.parse(bookingInfo.getUpdatedOn());

		String insertQuery = supplierQueries.getInsertBooking();
		PreparedStatement stmt = dbConnection.prepareStatement(insertQuery);
		stmt.setString(1, bookingInfo.getBookingId());
		stmt.setTimestamp(2, Timestamp.valueOf(checkInDate));
		stmt.setTimestamp(3, Timestamp.valueOf(checkOutDate));
		stmt.setString(4, bookingInfo.getHotelName());
		stmt.setInt(5, bookingInfo.getNoOfGuests());
		stmt.setString(6, bookingInfo.getStatus());
		stmt.setString(7, bookingInfo.getBookingReference());
		stmt.setTimestamp(8, Timestamp.valueOf(createdOn));
		stmt.setTimestamp(9, Timestamp.valueOf(updatedOn));


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
	public List<BookingInfo> filterBookings(String checkInDate, String checkOutDate, String status) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isBookingExist(String bookingReference) throws SQLException {

		if (dbConnection == null) {
			log.error("Failed to acquire db connection");

		}
		PreparedStatement stmt = dbConnection.prepareStatement(supplierQueries.getSelectBooking());
		stmt.setString(1, bookingReference);

		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			return true;
		}

		return false;
	}

	private void createBookingsTable() throws SQLException {
		String sql = supplierQueries.getCreateBookingTable();

		Statement stmt = dbConnection.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

}
