package com.vijay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.vijay.filecatalog.client.FileCatalogServiceClient;
import com.vijay.filecatalog.dao.FileStoreDAO;
import com.vijay.filecatalog.model.FileData;
import com.vijay.filecatalog.model.FileMetaData;
import com.vijay.filecatalog.scheduler.FilePublisher;
import com.vijay.filecatalog.service.FileCatalogService;

/**
 * Integration test for File Catalog application 1. upload file to storage
 * directory 2. retrieve files from storage directory 3. Sends Email with newly
 * added files.
 * 
 * @author Vijay Koppineedi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Configuration
@PropertySource("classpath:application.properties")
public class FilecatalogApplicationTests {

	private static final Logger LOG = Logger.getLogger(FilecatalogApplicationTests.class);

	private static final String TEST_FILE_DIR = "test-images";

	private static final String TEST_FILE_AUTHOR_NAME = "IntegrationTest";

	ApplicationContext applicationContext;

	FileCatalogService client;

	@Autowired
	private FilePublisher filePublisher;

	@Value("${filestore.storage.directory}")
	private String storageDirecoty;

	@Before
	public void setUp() throws IOException {
		client = new FileCatalogServiceClient();

	}

	@Test
	public void test1_Upload() throws IOException {
		List<String> fileList = getFileList();
		for (String fileName : fileList) {
			uploadFile(fileName);
		}
		test3_FindDocuments();
	}

	// @After
	public void tearDown() {
		deleteDirectory(new File(getStorageDirecoty()));
	}

	private void uploadFile(String fileName) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(TEST_FILE_DIR).append(File.separator).append(fileName);
		Path path = Paths.get(sb.toString());
		byte[] fileContent = Files.readAllBytes(path);
		FileMetaData metadata = client.store(new FileData(fileContent, fileName, TEST_FILE_AUTHOR_NAME));
		if (LOG.isDebugEnabled()) {
			LOG.debug("Document saved, uuid: " + metadata.getUuid());
		}
	}

	/*@Test
	public void test2_sendEmail() {
		filePublisher.publishNewFiles();
	}*/
	
	@Test
	public void test3_FindDocuments() {
		List<FileMetaData> result = client.findFilesByAuthor(TEST_FILE_AUTHOR_NAME);
		assertNotNull("Result is null", result);
		assertTrue("Result is empty", !result.isEmpty());
		for (FileMetaData metadata : result) {

			assertEquals("Author name is not " + TEST_FILE_AUTHOR_NAME, TEST_FILE_AUTHOR_NAME,
					metadata.getAuthorName());

		}
	}

	 private List<String> getFileList() {
		File file = new File(TEST_FILE_DIR);
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		return Arrays.asList(files);
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public String getStorageDirecoty() {
		return storageDirecoty;
	}

	public void setStorageDirecoty(String storageDirecoty) {
		this.storageDirecoty = storageDirecoty;
	}

	public FilePublisher getFilePublisher() {
		return filePublisher;
	}

	public void setFilePublisher(FilePublisher filePublisher) {
		this.filePublisher = filePublisher;
	}

}
