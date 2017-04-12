package com.vijay.filecatalog.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.vijay.filecatalog.config.AppConfig;
import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;

/**
 * 
 * DAO Implementation class for FileStoreDAO Interface.
 * This Class stores the files to a directory and retrieve.
 * 
 * @author Vijay Koppineedi
 *
 */
@Service("fileStoreDAO")
public class FileStoreDAOImpl implements FileStoreDAO 
{
	@Autowired
	private AppConfig appConfig;
	
	private static final Logger logger = Logger.getLogger(FileStoreDAOImpl.class);

	
	@PostConstruct
	public void init() {
		createDirectory(getAppConfig().getStorageDirecoty());
	}

	@Override
	public void insert(FileData fileData) {
		try {
			createDirectory(fileData);
			saveFileData(fileData);
			saveMetaData(fileData);
		} catch (IOException e) {
			String message = "Error while inserting document";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	private void saveMetaData(FileData fileData) throws IOException {
		String path = getDirectoryPath(fileData);
		Properties props = fileData.createProperties();
		File f = new File(new File(path), getAppConfig().getMetaDataFileName());
		System.out.println(f.getAbsolutePath());
		OutputStream out = new FileOutputStream(f);
		props.store(out, "Document meta data");
	}

	private void saveFileData(FileData fileData) throws IOException {
		String filePath = getDirectoryPath(fileData);
		try(BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(new File(new File(filePath), fileData.getFileName()))))
		{
			stream.write(fileData.getFileContent());
		}
	}

	private String createDirectory(FileData fileData) {
		String path = getDirectoryPath(fileData);
		createDirectory(path);
		return path;
	}

	private String getDirectoryPath(FileData fileData) {
		return getDirectoryPath(fileData.getUuid());
	}

	private String getDirectoryPath(String uuid) {
		StringBuilder sb = new StringBuilder();
		sb.append(getAppConfig().getStorageDirecoty()).append(File.separator).append(uuid);
		String path = sb.toString();
		return path;
	}

	@Override
	public List<FileMetaData> findByAuthorName(String authorName) 
	{
		try {
            return searchFileByAuthor(authorName);
        } catch (IOException e) {
             throw new RuntimeException( e);
        }
	}

	private List<FileMetaData> searchFileByAuthor(String authorName) throws IOException{
		List<String> uuidList = getUuidList();
        List<FileMetaData> metadataList = new ArrayList<FileMetaData>(uuidList.size());
        for (String uuid : uuidList) 
        {
            FileMetaData metadata = loadMetadataFromFileSystem(uuid);         
            if(isMatched(metadata, authorName)) {
                metadataList.add(metadata);
            }
        }
        return metadataList;
	}

	private boolean isMatched(FileMetaData metadata, String authorName)
	{
		if(metadata==null) {
            return false;
        }
        boolean match = true;
        if(authorName!=null) {
            match = (authorName.equalsIgnoreCase(metadata.getAuthorName()));
        }
        return match;
	}

	private List<String> getUuidList() {
        File file = new File(getAppConfig().getStorageDirecoty());
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        return Arrays.asList(directories);
    }
	
	private FileMetaData loadMetadataFromFileSystem(String uuid) throws IOException {
		  FileMetaData document = null;
	        String dirPath = getDirectoryPath(uuid);
	        File file = new File(dirPath);
	        if(file.exists()) {
	            Properties properties = readProperties(uuid);
	            document = new FileMetaData(properties);
	            
	        } 
	        return document;
	}

	private Properties readProperties(String uuid) throws IOException {
        Properties prop = new Properties();
        try(InputStream input = new FileInputStream(new File(getDirectoryPath(uuid),getAppConfig().getMetaDataFileName())))
       	{
        	 prop.load(input);
       	}
         return prop;
    }
	
	@Override
	public FileData load(String uuid) {
		try {
            return loadFromFileSystem(uuid);
        } catch (IOException e) {
            String message = "Error while loading document with id: " + uuid;
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
        
	}

	private FileData loadFromFileSystem(String uuid) throws IOException 
	{
		 FileMetaData metadata = loadMetadataFromFileSystem(uuid);
	       if(metadata==null) {
	           return null;
	       }
	       Path path = Paths.get(getFilePath(metadata));
	       FileData document = new FileData(metadata);
	       document.setFileContent(Files.readAllBytes(path));
	       return document;
	}

	 private String getFilePath(FileMetaData metadata) {
	        String dirPath = getDirectoryPath(metadata.getUuid());
	        StringBuilder sb = new StringBuilder();
	        sb.append(dirPath).append(File.separator).append(metadata.getFileName());
	        return sb.toString();
	    }
	 
	private void createDirectory(String path) {
		File file = new File(path);
		file.mkdirs();
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	
		
}
