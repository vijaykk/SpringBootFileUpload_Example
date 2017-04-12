package com.vijay.filecatalog.scheduler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This is a Scheduler class which gets fired based on cron expression. This
 * class triggers Email Publisher to send email with new files added.
 * 
 * @author Vijay Koppineedi
 *
 */
@Component
public class EmailScheduler {
	@Autowired
	private FilePublisher filePublisher;

	private static final Logger logger = Logger.getLogger(EmailScheduler.class);

	// For Testing, below expression runs for every 30 secons
	// To run for each hour use "0 0/60 * * * ?"
	@Scheduled(cron = "0,30 * * * * *")
	public void cronJob() {
		logger.info("> cronJob");

		filePublisher.publishNewFiles();
		logger.info("<cron Job");
	}

	public FilePublisher getFilePublisher() {
		return filePublisher;
	}

	public void setFilePublisher(FilePublisher filePublisher) {
		this.filePublisher = filePublisher;
	}

}
