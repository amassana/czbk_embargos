package es.commerzbank.ice.embargos.utils;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DownloadReportFile {

	private String filename;
	private String filenameAux;
	private String pdfSavedFullPath;
	private HttpHeaders respHeaders;
	private File jasperReportFile;
	private final String PDF_EXTENSION = ".pdf";
	private final String SLASH = "/";

	public void setTempFileName(String fileNameTemp) {

		String randomUUID = UUID.randomUUID().toString();
		filenameAux = fileNameTemp;
		filename = fileNameTemp + "_" + randomUUID.split("-")[0] + PDF_EXTENSION;

	}

	public void setFileTempPath(String pdfSavedPath) {
		pdfSavedFullPath = pdfSavedPath + SLASH + getTodayDate() + SLASH + filename;
		jasperReportFile = new File(pdfSavedFullPath);
	}

	private boolean areBytesNull = false;

	public void writeFile(byte[] fileBytes) throws Exception {

		FileOutputStream fos = null;

		if (fileBytes != null) {

			// if directory does not exists, it is created dynamically
			jasperReportFile.getParentFile().mkdirs();

			try {

				fos = new FileOutputStream(jasperReportFile);

				fos.write(fileBytes);

				setRequestHeader(jasperReportFile.length(), filenameAux);

			} catch (IOException ex) {
				throw new Exception("Exception DownloadFile.writeFile", ex);
			} finally {

				fos.flush();
				fos.close();
			}

		} else {
			areBytesNull = true;
		}

	}

	public void setRequestHeader(Long fileLength, String fileNameSuggestion) {

		respHeaders = new HttpHeaders();

		MediaType mediaType = MediaType.parseMediaType("application/pdf");

		respHeaders.setContentType(mediaType);
		respHeaders.setContentLength(fileLength);
		respHeaders.setContentDispositionFormData("attachment", fileNameSuggestion.concat(PDF_EXTENSION));
	}

	public ResponseEntity<InputStreamResource> returnToDownloadFile() throws FileNotFoundException {
		FileInputStream fis = null;
		
		if (areBytesNull) {
			
			areBytesNull = false;
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		fis = new FileInputStream(jasperReportFile);

		InputStreamResource isr = new InputStreamResource(fis);

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	private String getTodayDate() {
		return simpleDateFormat.format(new Date());
	}

}
