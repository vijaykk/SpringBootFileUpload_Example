package com.vijay.filecatalog.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;
import com.vijay.filecatalog.service.FileCatalogService;

/**
 * REST client to invoke REST services defined in Rest Controller.
 * 
 * @author Vijay Koppineedi
 *
 */
public class FileCatalogServiceClient implements FileCatalogService {

	private static final Logger LOG = Logger.getLogger(FileCatalogServiceClient.class);

	RestTemplate restTemplate;

	private String protocol = "http";
	private Integer port = 8080;
	private String baseUrl = "fileStore";
	private String hostname = "localhost";

	@Override
	public FileMetaData store(FileData FileData) {
		try {
			return doSave(FileData);
		} catch (RuntimeException e) {
			LOG.error("Error while uploading file", e);
			throw e;
		} catch (IOException e) {
			LOG.error("Error while uploading file", e);
			throw new RuntimeException("Error while uploading file", e);
		}

	}

	private FileMetaData doSave(FileData FileData) throws IOException, FileNotFoundException {
		String tempFilePath = writeFileDataToTempFile(FileData);
		MultiValueMap<String, Object> parts = createMultipartFileParam(tempFilePath);
		FileMetaData FileMetaData = getRestTemplate().postForObject(getServiceUrl() + "/upload?person={test}", parts,
				FileMetaData.class, FileData.getAuthorName());
		return FileMetaData;
	}

	@Override
	public FileData downloadFile(String id) {
		return getRestTemplate().getForObject(getServiceUrl() + "/file/{id}", FileData.class, id);
	}

	@Override
	public List<FileMetaData> findFilesByAuthor(String authorName) {
		FileMetaData[] result = getRestTemplate().getForObject(getServiceUrl() + "files?person={name}",
				FileMetaData[].class, authorName);
		return Arrays.asList(result);
	}

	private MultiValueMap<String, Object> createMultipartFileParam(String tempFilePath) {
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		parts.add("file", new FileSystemResource(tempFilePath));
		return parts;
	}

	private String writeFileDataToTempFile(FileData FileData) throws IOException, FileNotFoundException {
		Path path;
		path = Files.createTempDirectory(FileData.getUuid());
		String tempDirPath = path.toString();
		File file = new File(tempDirPath, FileData.getFileName());
		FileOutputStream fo = new FileOutputStream(file);
		fo.write(FileData.getFileContent());
		fo.close();
		return file.getPath();
	}

	public String getServiceUrl() {
		StringBuilder sb = new StringBuilder();
		sb.append(getProtocol()).append("://");
		sb.append(getHostname());
		if (getPort() != null) {
			sb.append(":").append(getPort());
		}
		sb.append("/").append(getBaseUrl()).append("/");
		return sb.toString();
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public RestTemplate getRestTemplate() {
		if (restTemplate == null) {
			restTemplate = createRestTemplate();
		}
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private RestTemplate createRestTemplate() {
		restTemplate = new RestTemplate();
		return restTemplate;
	}

}
