package com.vijay.filecatalog.service;

import java.util.List;

import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;
/**
 * 
 * Service method to handle below operations on File Catalog/Store
 * 1. Store a new file
 * 2. Retrieve all the files from the Catalog
 * 3. Retrieve single file for a given id.
 * 
 *  @author Vijay Koppineedi
 *
 */
public interface FileCatalogService
{

	 FileMetaData store(FileData document);
	   
	 List<FileMetaData> findFilesByAuthor(String personName);
	 
	 FileData downloadFile(String id);
}
