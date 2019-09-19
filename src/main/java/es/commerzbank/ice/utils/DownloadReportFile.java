package es.commerzbank.ice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class DownloadReportFile {

	private static String filename;
	private static String filenameAux;
	private static String pdfSavedFullPath;
	private static HttpHeaders respHeaders;
	private static File jasperReportFile;
	private static final String PDF_EXTENSION = ".pdf";

	public static void setTempFileName(String fileNameTemp) {

		String randomUUID = UUID.randomUUID().toString();
		filenameAux = fileNameTemp;
		filename = fileNameTemp + "_" + randomUUID.split("-")[0] + PDF_EXTENSION;

	}

	public static void setFileTempPath(String pdfSavedPath) {
		pdfSavedFullPath = pdfSavedPath + getTodayDate() + "/" + filename;
		jasperReportFile = new File(pdfSavedFullPath);
	}

	private static boolean areBytesNull = false;

	public static void writeFile(byte[] fileBytes) throws Exception {

		FileOutputStream fos = null;

		if (fileBytes != null) {

			// if directory does not exists, it is created dynamically
			jasperReportFile.getParentFile().mkdirs();

			try {

				fos = new FileOutputStream(jasperReportFile);

				fos.write(fileBytes);

				setRequestHeader(jasperReportFile.length(), filenameAux);

			} catch (IOException ex) {
				throw new Exception("Exception DownloadFile.writeFile: " + ex);
			} finally {

				fos.flush();
				fos.close();
			}

		} else {
			areBytesNull = true;
		}

	}

	public static void setRequestHeader(Long fileLength, String fileNameSuggestion) {

		respHeaders = new HttpHeaders();

		MediaType mediaType = MediaType.parseMediaType("application/pdf");

		respHeaders.setContentType(mediaType);
		respHeaders.setContentLength(fileLength);
		respHeaders.setContentDispositionFormData("attachment", fileNameSuggestion.concat(PDF_EXTENSION));
	}

	public static ResponseEntity<InputStreamResource> returnToDownloadFile() throws FileNotFoundException {
		FileInputStream fis = null;
		
		if (areBytesNull) {
			
			areBytesNull = false;
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		fis = new FileInputStream(jasperReportFile);

		InputStreamResource isr = new InputStreamResource(fis);

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

	private static String getTodayDate() {

		Calendar calendar = Calendar.getInstance();

		int month = calendar.get(Calendar.MONTH);

		return String
				.valueOf(calendar.get(Calendar.YEAR) + "" + (month + 1) + "" + calendar.get(Calendar.DAY_OF_MONTH));
	}

}
