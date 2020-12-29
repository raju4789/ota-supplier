package com.ogado.supplier;

import com.ogado.supplier.server.ServerConnectionManager;

public class SupplierApplication {

	public static void main(String[] args) {
		ServerConnectionManager.startServer();
		
	}
	
}
