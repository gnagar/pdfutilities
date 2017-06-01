package pdfutils.protection;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pdfutils.protection.exception.InvalidPasswordException;
import pdfutils.protection.exception.InvalidSuffixException;
import pdfutils.protection.exception.NotAFileException;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfReader;

public class TestPdfProtection {

	private static final String TEST_PDF_FOLDER = "./src/test/resources/";
	private static final String TEST_CUSTOM_SUFFIX = "_suffixed";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@After
	public void tearDown() {

		File testFileFolder = new File(TEST_PDF_FOLDER);

		deleteProtectedFiles(testFileFolder,
				PDFProtectionUtil.DEFAULT_SUFFIX_FOR_PROTECTED_FILES);

		deleteProtectedFiles(testFileFolder, TEST_CUSTOM_SUFFIX);
	}

	private static void deleteProtectedFiles(final File file,
			final String protectedFileSuffix) {

		File[] protectedFiles = file.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File file, String fileName) {
				if (fileName.contains(protectedFileSuffix)) {
					return true;
				}

				return false;
			}
		});

		for (File protectedFile : protectedFiles) {
			protectedFile.delete();
		}

	}

	@Test
	public void testThatANonExistantFileThrowsFileNotFoundException()
			throws Exception {

		expectedException.expect(FileNotFoundException.class);
		expectedException.expectMessage(containsString("does not exist"));

		PDFProtectionUtil.protectPdf("Non Existent File.pdf", "password");
	}

	@Test
	public void testThatAnInvalidFileThrowsNotAFileException() throws Exception {

		expectedException.expect(NotAFileException.class);
		expectedException.expectMessage(containsString("is not a file!"));

		PDFProtectionUtil.protectPdf(".", "password");
	}

	@Test
	public void testThatAnInvalidPDFFileThrowsInvalidPdfException()
			throws Exception {

		expectedException.expect(InvalidPdfException.class);

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "NotAPdf.txt",
				"password");
	}

	@Test
	public void testThatAValidPDFFileIsCopiedInSameFolderWith_protectedSuffixIfNoSuffixIsPassed()
			throws Exception {

		assertFalse(new File(TEST_PDF_FOLDER + "WIRED"
				+ PDFProtectionUtil.DEFAULT_SUFFIX_FOR_PROTECTED_FILES + ".pdf")
				.exists());

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", "password");

		assertTrue(new File(TEST_PDF_FOLDER + "WIRED"
				+ PDFProtectionUtil.DEFAULT_SUFFIX_FOR_PROTECTED_FILES + ".pdf")
				.exists());
	}

	@Test
	public void testThatAValidPDFFileIsCopiedInSameFolderWithTheGivenSuffixIfSuffixIsPassed()
			throws Exception {

		assertFalse(new File(TEST_PDF_FOLDER + "WIRED" + TEST_CUSTOM_SUFFIX
				+ ".pdf").exists());

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", "password",
				TEST_CUSTOM_SUFFIX);

		assertTrue(new File(TEST_PDF_FOLDER + "WIRED" + TEST_CUSTOM_SUFFIX
				+ ".pdf").exists());
	}

	@Test
	public void testThatThePDFFileIsProperlyProtectedWithPassword()
			throws Exception {

		String password = "password";
		
		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", password);

		new PdfReader(
				new FileInputStream(TEST_PDF_FOLDER + "WIRED"
						+ PDFProtectionUtil.DEFAULT_SUFFIX_FOR_PROTECTED_FILES
						+ ".pdf"), password.getBytes()).close();

	}

	@Test
	public void testThatANullPasswordThrowsInvalidPasswordException()
			throws Exception {
		expectedException.expect(InvalidPasswordException.class);

		expectedException.expectMessage(containsString("Password is null!"));

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", null);
	}

	@Test
	public void testThatABlankPasswordThrowsInvalidPasswordException()
			throws Exception {
		expectedException.expect(InvalidPasswordException.class);

		expectedException.expectMessage(containsString("Password is blank!"));

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", "");
	}

	@Test
	public void testThatANullSuffixThrowsInvalidSuffixException()
			throws Exception {
		expectedException.expect(InvalidSuffixException.class);

		expectedException.expectMessage(containsString("Suffix is null!"));

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", "password",
				null);
	}

	@Test
	public void testThatABlankSuffixThrowsInvalidSuffixException()
			throws Exception {
		expectedException.expect(InvalidSuffixException.class);

		expectedException.expectMessage(containsString("Suffix is blank!"));

		PDFProtectionUtil.protectPdf(TEST_PDF_FOLDER + "WIRED.pdf", "password",
				" ");
	}
	

}
