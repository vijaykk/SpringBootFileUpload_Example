package com.vijay.filecatalog.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vijay.filecatalog.mail.EmailSender;
import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;
import com.vijay.filecatalog.service.FileCatalogService;
/**
 * Rest Controller
 * @author Vijay Koppineedi
 *
 */
@Controller
@RequestMapping(value = "/fileStore")
public class FileCatalogRestController {

	private static final Logger logger = Logger.getLogger(FileCatalogRestController.class);

	@Autowired
	FileCatalogService fileCatalogService;

	/**
	 * Rest service method to handle File Upload POST requests.
	 * @param file
	 * @param author
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody FileMetaData handleFileUpload(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "person", required = true) String author) {

		try {
			FileData fileData = new FileData(file.getBytes(), file.getOriginalFilename(), author);
			getFileCatalogService().store(fileData);
			logger.info("File Received and stored");
			return fileData.getMetadata();
		} catch (RuntimeException e) {
			logger.error("Error while uploading.", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error while uploading.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Rest method to serve GET request to retrieve all files from File store
	 * @param authorName
	 * @param date
	 * @return
	 */
	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public HttpEntity<List<FileMetaData>> findDocument(
			@RequestParam(value = "person", required = false) String authorName,
			@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		HttpHeaders httpHeaders = new HttpHeaders();
		return new ResponseEntity<List<FileMetaData>>(getFileCatalogService().findFilesByAuthor(authorName),
				httpHeaders, HttpStatus.OK);
	}

	
	/**
	 * Rest service GET method to download file content as stream. 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
	public HttpEntity<byte[]> getDocument(@PathVariable String id) {
		// send it back to the client
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.IMAGE_JPEG);
		FileData fileData = getFileCatalogService().downloadFile(id);
		return new ResponseEntity<byte[]>(fileData.getFileContent(), httpHeaders, HttpStatus.OK);

	}

	public FileCatalogService getFileCatalogService() {
		return fileCatalogService;
	}

	public void setFileCatalogService(FileCatalogService fileCatalogService) {
		this.fileCatalogService = fileCatalogService;
	}


}
