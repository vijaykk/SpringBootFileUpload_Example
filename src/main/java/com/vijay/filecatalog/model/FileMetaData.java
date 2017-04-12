package com.vijay.filecatalog.model;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * 
 * @author Vijay Koppineedi
 *
 */
public class FileMetaData implements Serializable {

	private static final long serialVersionUID = -1648329448870557717L;

	private static final Logger LOG = Logger.getLogger(FileMetaData.class);

	public static final String PROP_UUID = "uuid";
	public static final String PROP_AUTHOR_NAME = "author-name";
	public static final String PROP_FILE_NAME = "file-name";
	public static final String PROP_FILE_TYPE = "file-type";
	public static final String PROP_FILE_PUBLISHED = "file-published";

	protected String uuid;
	protected String fileName;
	protected String authorName;
	protected String fileType;

	public FileMetaData() {
		super();
	}

	public FileMetaData(String fileName, String authorName) {
		this(UUID.randomUUID().toString(), fileName, authorName);
	}

	public FileMetaData(String uuid, String fileName, String personName) {
		super();
		this.uuid = uuid;
		this.fileName = fileName;
		this.authorName = personName;
	}

	public FileMetaData(Properties properties) {
		this(properties.getProperty(PROP_UUID), properties.getProperty(PROP_FILE_NAME),
				properties.getProperty(PROP_AUTHOR_NAME));

	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Properties createProperties() {
		Properties props = new Properties();
		props.setProperty(PROP_UUID, getUuid());
		props.setProperty(PROP_FILE_NAME, getFileName());
		props.setProperty(PROP_AUTHOR_NAME, getAuthorName());
		props.setProperty(PROP_FILE_PUBLISHED, "N");
		return props;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
