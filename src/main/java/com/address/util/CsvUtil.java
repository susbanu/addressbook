package com.address.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.address.dto.AddressDto;

public class CsvUtil {

	private static final String TYPE = "text/csv";
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvUtil.class);

	private CsvUtil() {
	}

	public static boolean isCSVFormat(MultipartFile file) {
		return TYPE.equals(file.getContentType());
	}

	public static List<AddressDto> csvToAddress(MultipartFile file) {
		try (InputStream is = file.getInputStream();
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<AddressDto> addresses = new ArrayList<>();
			List<CSVRecord> csvRecords = csvParser.getRecords();
			csvRecords.forEach( csvRecord -> addresses.add(new AddressDto(csvRecord.get("FirstName"), csvRecord.get("LastName"),
					csvRecord.get("Street"), csvRecord.get("City"), csvRecord.get("State"), csvRecord.get("Zip"))));
			return addresses;
		} catch (IOException e) {
			LOGGER.error("fail to parse CSV file: {} ", e.getMessage());
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}
}
