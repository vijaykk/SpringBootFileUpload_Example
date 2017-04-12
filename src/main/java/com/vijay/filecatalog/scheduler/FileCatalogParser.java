package com.vijay.filecatalog.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vijay.filecatalog.config.AppConfig;
import com.vijay.filecatalog.model.FileMetaData;

/**
 * This Class serves as a handler to parse the storage directory to perform
 * below tasks 1. parse Meta data properties of each file and identifies the New
 * files which needs to be published in Email. 2. Marks the files as published.
 * 
 * @author Vijay Koppineedi
 *
 */
@Component
public class FileCatalogParser {

	private static final Logger logger = Logger.getLogger(FileCatalogParser.class);

	private List<FileMetaData> fileMetaDataList = new ArrayList<FileMetaData>();

	@Autowired
	private AppConfig appConfig;
	
	
	public List<FileMetaData> parseAndFindUnPublishedFiles() {
		parseRecursive(false, new File(getAppConfig().getStorageDirecoty()));
		return fileMetaDataList;
	}

	public void markAllFilesAsPublished() {
		parseRecursive(true, new File(getAppConfig().getStorageDirecoty()));

	}

	/**
	 * This method recursively lists all .properties files in a directory
	 */
	private void parseRecursive(boolean markAsPublished, File dir) {
		Arrays.stream(dir
				.listFiles((f, n) -> !n.startsWith(".") && (new File(f, n).isDirectory() || n.endsWith(".properties"))))
				.forEach(unchecked((file) -> {

					if (markAsPublished)
						updatePublishedProperty(file);
					else
						filterUnpublishedFiles(file);

					logger.info(file.getCanonicalPath().substring(new File(".").getCanonicalPath().length()));

					if (file.isDirectory()) {
						parseRecursive(markAsPublished, file);
					}
				}));
	}

	private void filterUnpublishedFiles(File file) {
		if (file.getPath().endsWith(".properties")) {
			Properties props = new Properties();
			try (FileInputStream in = new FileInputStream(file)) {
				props.load(in);
				String value = props.getProperty("file-published");
				if ("N".equalsIgnoreCase(value)) {
					FileMetaData fmData = new FileMetaData();
					fmData.setAuthorName(props.getProperty("author-name"));
					fmData.setUuid(props.getProperty("uuid"));
					fmData.setFileName(props.getProperty("file-name"));
					fileMetaDataList.add(fmData);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void updatePublishedProperty(File file) {
		if (file.getPath().endsWith(".properties")) {
			PropertiesConfiguration conf;
			try {
				conf = new PropertiesConfiguration(file);
				conf.setProperty("file-published", "Y");
				conf.save();
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * This utility simply wraps a functional interface that throws a checked
	 * exception into a Java 8 Consumer
	 */
	private <T> Consumer<T> unchecked(CheckedConsumer<T> consumer) {
		return t -> {
			try {
				consumer.accept(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	@FunctionalInterface
	private interface CheckedConsumer<T> {
		void accept(T t) throws Exception;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	

}
