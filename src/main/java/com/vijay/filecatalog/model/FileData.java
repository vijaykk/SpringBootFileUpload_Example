package com.vijay.filecatalog.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * 
 * @author Vijay Koppineedi
 *
 */
public class FileData extends FileMetaData implements Serializable {

    private static final long serialVersionUID = 7763039739806501479L;
	private byte[] fileContent;
    
    public FileData( byte[] fileData, String fileName, String personName) {
        super(fileName, personName);
        this.fileContent = fileData;
    }

    public FileData(Properties properties) {
        super(properties);
    }
    
    public FileData(FileMetaData metadata) {
        super(metadata.getUuid(), metadata.getFileName(),  metadata.getAuthorName());
    }

    public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public FileMetaData getMetadata() {
        return new FileMetaData(getUuid(), getFileName(),  getAuthorName());
    }
    
}
