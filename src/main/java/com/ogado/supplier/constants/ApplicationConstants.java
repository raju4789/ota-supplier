package com.ogado.supplier.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApplicationConstants {

	public static final String SERVER_CONFIG_FILE = "src/main/resources/server-config.json";
	public static final String DB_QUERIES_CONFIG_FILE = "src/main/resources/supplier-queries.json";

	public static final String BOOK_URI = "/supplier/v1/book";
	public static final String STATUS_URI = "/supplier/v1/status";

	public static final String DEV_ENV = "dev";

	private static Map<String, String> dbMap = new HashMap<String, String>();

	static {
		dbMap.put("dev", "src/main/resources/dev-db-config.json");
		dbMap.put("prod", "src/main/resources/prod-db-config.json");
		dbMap.put(null, "src/main/resources/dev-db-config.json");

	}

	public static final Map<String, String> DB_CONFIG = Collections.unmodifiableMap(dbMap);
	public static final String STATUS_CONFIRMED = "CONFIRMED";
	public static final String STATUS_CANCELED = "CANCELED";

	public static final int TIMER_INTERVAL = 10 * 60 * 10;

}
