package com.vijay.filecatalog.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.filecatalog.mail.EmailSender;
import com.vijay.filecatalog.model.FileMetaData;

/**
 * This Class will pick up the new files and publish them in Email.
 * 
 * @author Vijay Koppineedi
 *
 */
@Service
public class FilePublisher {

	@Autowired
	EmailSender emailService;

	@Autowired
	FileCatalogParser fileCatalogParser;

	private static final String NEWLINE = "\n";

	public void publishNewFiles() {
		List<FileMetaData> filesList = fileCatalogParser.parseAndFindUnPublishedFiles();
		if (null != filesList && filesList.size() > 0) {
			composeEmail(filesList);
			fileCatalogParser.markAllFilesAsPublished();
		}

	}

	private void composeEmail(List<FileMetaData> filesList) {
		StringBuilder bodyString = prepareEmailBody(filesList);
		String subject = "File Catalog Notification: New Files Added";
		emailService.sendMail(subject, bodyString.toString());

	}

	private StringBuilder prepareEmailBody(List<FileMetaData> filesList) {
		StringBuilder bodyString = new StringBuilder();
		bodyString.append("Below files were added recently:");
		bodyString.append(NEWLINE);
		bodyString.append(NEWLINE);
		for (FileMetaData metadata : filesList) {
			bodyString.append("\n");
			bodyString.append("File Name:" + metadata.getFileName());
			bodyString.append(NEWLINE);
			bodyString.append("Author Name:" + metadata.getAuthorName());
			bodyString.append(NEWLINE);
			bodyString.append("File UUID:" + metadata.getUuid());
			bodyString.append(NEWLINE);
			bodyString.append(NEWLINE);
			bodyString.append(NEWLINE);

		}
		bodyString.append("######### End of Message ####################");
		return bodyString;
	}

}
