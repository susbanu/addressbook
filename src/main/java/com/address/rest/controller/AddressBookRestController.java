package com.address.rest.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.address.dto.AddressDetailDto;
import com.address.dto.AddressDto;
import com.address.dto.ResponseMessage;
import com.address.service.AddressBookService;
import com.address.util.CsvUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Validated
@Tag(name = "AddressBook", description = "Address book API")
public class AddressBookRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressBookRestController.class);

	@Autowired
	private AddressBookService addressBookService;

	@PostMapping("/address")
	@Operation(summary = "Save address", description = "Save one address", tags = { "AddressBook" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Address created", content = @Content(schema = @Schema(implementation = AddressDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "409", description = "Address already exists") })
	public AddressDto saveAddress(@Valid @RequestBody AddressDto addressDto) {
		LOGGER.info("save address : {}", addressDto);
		return addressBookService.saveAddress(addressDto);
	}

	@GetMapping("/address")
	@Operation(summary = "Search address", description = "Search address by any address fields", tags = { "AddressBook" })
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "successful operation",
	                content = @Content(schema = @Schema(implementation = AddressDetailDto.class))),
	        @ApiResponse(responseCode = "404", description = "Address not found for address field"),
	        @ApiResponse(responseCode = "400", description = "Address field is empty")})
	public AddressDetailDto searchAddress(@RequestParam(value = "field") @NotBlank String addressField) {
		LOGGER.info("Search address, Addresss field : {}", addressField);
		return addressBookService.searchAddress(addressField);
	}
	
	@PostMapping("/address/import")
	@Operation(summary = "Load addresses", description = "Load address from CSV", tags = { "AddressBook" })
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Import address successfully",  content = @Content(schema = @Schema(implementation = MultipartFile.class))),
	        @ApiResponse(responseCode = "400", description = "CSV file format is invalid")})
	public ResponseEntity<ResponseMessage> loadAddress(@RequestParam("file") MultipartFile file) {
		LOGGER.debug("Import address");
		String message;
		if (CsvUtil.isCSVFormat(file)) {
			try {
				addressBookService.loadAddress(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
}
