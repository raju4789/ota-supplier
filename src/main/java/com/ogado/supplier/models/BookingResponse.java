package com.ogado.supplier.models;

public class BookingResponse {

	private int httpStatus;
	private BookingInfo bookingInfo;
	private String errorMessage;

	public BookingResponse() {
	}

	public BookingResponse(int httpStatus, BookingInfo bookingInfo, String errorMessage) {
		super();
		this.httpStatus = httpStatus;
		this.bookingInfo = bookingInfo;
		this.errorMessage = errorMessage;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public BookingInfo getBookingInfo() {
		return bookingInfo;
	}

	public void setBookingInfo(BookingInfo bookingInfo) {
		this.bookingInfo = bookingInfo;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
