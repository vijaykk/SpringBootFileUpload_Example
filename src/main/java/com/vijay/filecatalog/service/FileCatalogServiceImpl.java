package com.vijay.filecatalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.filecatalog.dao.FileStoreDAO;
import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;

/**
 * 
 * Service Implementation class for FileCatalogService
 * @author Vijay Koppineedi
 *
 */
@Service("fileCatalogService")
public class FileCatalogServiceImpl implements FileCatalogService {

	 
    @Autowired
    private FileStoreDAO fileStoreDAO;

	@Override
	public FileMetaData store(FileData fileData) {
			fileStoreDAO.insert(fileData); 
	        return fileData.getMetadata();
	}

	@Override
	public List<FileMetaData> findFilesByAuthor(String author) {
		return fileStoreDAO.findByAuthorName(author);
	}

	@Override
	public FileData downloadFile(String id) {
		FileData file = fileStoreDAO.load(id);
        if(file!=null) {
            return file;
        } else {
            return null;
        }
	}

	public FileStoreDAO getFileStoreDAO() {
		return fileStoreDAO;
	}

	public void setFileStoreDAO(FileStoreDAO fileStoreDAO) {
		this.fileStoreDAO = fileStoreDAO;
	}

	
}
