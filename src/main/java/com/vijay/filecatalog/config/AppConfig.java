package com.vijay.filecatalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *  Application Configuration file.
 * @author Vijay Koppineedi
 *
 */

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig 
{

	@Value("${filestore.storage.directory}")
	private String storageDirecoty;
	
	
	@Value("${filestore.storage.metadata.filename}")
	private String metaDataFileName;


	public String getStorageDirecoty() {
		return storageDirecoty;
	}


	public void setStorageDirecoty(String storageDirecoty) {
		this.storageDirecoty = storageDirecoty;
	}


	public String getMetaDataFileName() {
		return metaDataFileName;
	}


	public void setMetaDataFileName(String metaDataFileName) {
		this.metaDataFileName = metaDataFileName;
	}
	
	
}
