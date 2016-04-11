package pdfutils.protection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pdfutils.protection.exception.InvalidPasswordException;
import pdfutils.protection.exception.InvalidSuffixException;
import pdfutils.protection.exception.NotAFileException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFProtectionUtil {

	static final String DEFAULT_SUFFIX_FOR_PROTECTED_FILES = "_protected";

	public static void protectPdf(String absolutePathToPdf, String password)
			throws DocumentException, IOException {

		protectPdf(absolutePathToPdf, password,
				DEFAULT_SUFFIX_FOR_PROTECTED_FILES);

	}

	public static void protectPdf(String absolutePathToPdf, String password,
			String suffixForProtectedFile) throws DocumentException,
			IOException {

		File file = new File(absolutePathToPdf);

		checkThatFileExists(file);

		checkThatFileIsAValidFile(file);

		checkThatPasswordIsValid(password);
		
		checkThatSuffixIsValid(suffixForProtectedFile);

		protectPdfWithGivenPassword(file, password, suffixForProtectedFile);

	}

	private static void checkThatSuffixIsValid(String suffixForProtectedFile) {
		if (suffixForProtectedFile == null) {
			throw new InvalidSuffixException("Suffix is null!");
		}

		if (suffixForProtectedFile.trim().length() == 0) {
			throw new InvalidSuffixException("Suffix is blank!");
		}
	}

	private static void checkThatPasswordIsValid(String password) {
		if (password == null) {
			throw new InvalidPasswordException("Password is null!");
		}

		if (password.trim().length() == 0) {
			throw new InvalidPasswordException("Password is blank!");
		}
	}

	private static void protectPdfWithGivenPassword(File file, String password,
			String suffixForProtectedFile) throws DocumentException,
			IOException {

		PdfReader pdfReader = new PdfReader(new FileInputStream(file));

		StringBuilder protectedFileName = new StringBuilder(
				file.getAbsolutePath());

		protectedFileName.insert(protectedFileName.lastIndexOf(".pdf"),
				suffixForProtectedFile);

		PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(
				protectedFileName.toString()));

		pdfStamper.setEncryption(password.getBytes(), password.getBytes(),
				PdfWriter.ALLOW_COPY, PdfWriter.ENCRYPTION_AES_256);

		pdfStamper.close();

		pdfReader.close();

	}

	private static void checkThatFileIsAValidFile(File file) {
		if (!file.isFile()) {
			throw new NotAFileException(file.getAbsolutePath()
					+ " is not a file!");
		}
	}

	private static void checkThatFileExists(File file)
			throws FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath()
					+ " does not exist");
		}
	}

}
