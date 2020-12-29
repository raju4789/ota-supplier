package com.ogado.supplier.utils;

import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ogado.supplier.exceptions.ConfigurationException;

public class ConfigLoader {

	private static Logger log = Logger.getLogger(ConfigLoader.class);

	public static <A> A loadConfiguration(String fileName, Class<A> cls) throws ConfigurationException {
		try (FileReader fileReader = new FileReader(fileName)) {
			StringBuffer sb = new StringBuffer();
			int i;
			while ((i = fileReader.read()) != -1) {
				sb.append((char) i);
			}

			return JsonMapper.parse(sb.toString(), cls);

		} catch (JsonProcessingException e) {
			log.error("Failed to parse configuration file: " + e.getMessage());
			throw new ConfigurationException(e.getMessage());
		} catch (IOException e) {
			log.error("Failed to read configuration file: " + e.getMessage());
			throw new ConfigurationException(e.getMessage());
		}
	}

}
