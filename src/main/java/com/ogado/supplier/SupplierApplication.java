package com.ogado.supplier;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.ogado.supplier.constants.ApplicationConstants;
import com.ogado.supplier.server.ServerConnectionManager;
import com.ogado.supplier.services.ISupplierService;
import com.ogado.supplier.services.SupplierService;

public class SupplierApplication {
	private static Logger log = Logger.getLogger(SupplierApplication.class);

	public static void main(String[] args) {

		/**
		 * Below function is not part of core logic. It is added to simulate real world
		 * behavior. Below function randomly changes state of bookings.
		 */
		bookingsUpdater();

		ServerConnectionManager.startServer();
	}

	public static void bookingsUpdater() {
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					ISupplierService supplierService = new SupplierService();
					supplierService.randomlyUpdateBooking();
					log.info("bookings updater running");
				} catch (Exception e) {
					log.error("failed to updated bookings.operations witll be re-tried again.");
				}

			}
		};
		Timer timer = new Timer("Bookings Updater");

		timer.schedule(task, 0, ApplicationConstants.TIMER_INTERVAL);
	}

}
