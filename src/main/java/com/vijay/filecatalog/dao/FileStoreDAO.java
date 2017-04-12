package com.vijay.filecatalog.dao;

import java.util.List;

import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;

/**
 *  Data Access Object to handle below operations
 *  1. Insert file to File storage
 *  2. Retrieve all files from File Storage
 *  3. Retrieve single file for a given id
 *  
 * @author Vijay Koppineedi
 *
 */
public interface FileStoreDAO 
{
		public void insert(FileData document);
	    
	    public List<FileMetaData> findByAuthorName(String authorName);
	    
	    public FileData load(String uuid);
}
