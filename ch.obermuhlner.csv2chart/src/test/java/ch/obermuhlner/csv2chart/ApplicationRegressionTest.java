package ch.obermuhlner.csv2chart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ApplicationRegressionTest {

	private static final String OS_NAME_FOR_REFERENCE_IMAGES = "Windows 10";

	@Test
	public void testHelp() {
		runCsv2Chart();
	}
	
	@Test
	public void testReferenceChartsLog() throws IOException {
		assertReferenceCharts(ImageFormat.LOG);
	}

	@Test
	public void testReferenceChartsSvg() throws IOException {
		if (isRunningOnSystemWithCorrectReferenceImages()) {
			assertReferenceCharts(ImageFormat.SVG);
		}
	}
	
	@Test
	public void testReferenceChartsPng() throws IOException {
		if (isRunningOnSystemWithCorrectReferenceImages()) {
			assertReferenceCharts(ImageFormat.PNG);
		}
	}

	private boolean isRunningOnSystemWithCorrectReferenceImages() {
		return OS_NAME_FOR_REFERENCE_IMAGES.equals(System.getProperty("os.name"));
	}

	public void assertReferenceCharts(ImageFormat imageFormat) throws IOException {
		Files.list(Paths.get("src/test/resources/csv"))
			.map(path -> path.toFile().getName())
			.filter(name -> name.endsWith(".csv"))
			.map(name -> name.substring(0, name.length() - ".csv".length()))
			.forEach(name -> {
				try {
					assertCsv2Chart(name, imageFormat);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
	}

	private void assertCsv2Chart(String baseFilename, ImageFormat imageFormat) throws IOException {
		runCsv2Chart(
				"--out-dir", "src/test/resources/out_images",
				"--format", imageFormat.toString(),
				"src/test/resources/csv/" + baseFilename + ".csv");

		assertImageEquals(
				imageFormat,
				new File("src/test/resources/ref_images/" + baseFilename + "." + imageFormat.getExtension()),
				new File("src/test/resources/out_images/" + baseFilename + "." + imageFormat.getExtension()));
	}
	
	private void runCsv2Chart(String...args) {
		Application.main(args);
	}

	private void assertImageEquals(ImageFormat imageFormat, File expectedImageFile, File actualImageFile) throws IOException {
		if (imageFormat.equals(ImageFormat.SVG)) {
			assertSvgImageEquals(expectedImageFile, actualImageFile);
		} else if (imageFormat.equals(ImageFormat.LOG)) {
			assertLogImageEquals(expectedImageFile, actualImageFile);
		} else {
			assertBitmapImageEquals(expectedImageFile, actualImageFile);
		}
	}

	private void assertSvgImageEquals(File expectedImageFile, File actualImageFile) throws IOException {
		String expectedSvg = cleanupSvg(new String(Files.readAllBytes(expectedImageFile.toPath())));
		String actualSvg = cleanupSvg(new String(Files.readAllBytes(actualImageFile.toPath())));

		assertEquals("SVG file content: " + expectedImageFile + " != " + actualImageFile, expectedSvg, actualSvg);
	}

	private void assertLogImageEquals(File expectedImageFile, File actualImageFile) throws IOException {
		List<String> expectedContent = Files.readAllLines(expectedImageFile.toPath());
		List<String> actualContent = Files.readAllLines(actualImageFile.toPath());

		assertAllLines("File content: " + expectedImageFile + " != " + actualImageFile, expectedContent, actualContent);
	}

	private void assertAllLines(String message, List<String> expectedContent, List<String> actualContent) {
		int n = Math.min(expectedContent.size(), actualContent.size());
		
		for (int i = 0; i < n; i++) {
			assertEquals(message + " Line: " + (i+1), expectedContent.get(i), actualContent.get(i));
		}

		assertEquals("Expected file too long" , n, expectedContent.size());
		assertEquals("Actual file too long" , n, actualContent.size());
	}

	private String cleanupSvg(String svg) {
		// Example for random string in generated svg: 1704036023229044clip-0
		
		svg = svg.replaceAll("[0-9]*clip-[0-9]*", "randomclip");
		
		return svg;
	}

	private void assertBitmapImageEquals(File expectedImageFile, File actualImageFile) throws IOException {
		BufferedImage expectedImage = ImageIO.read(expectedImageFile);
		BufferedImage actualImage = ImageIO.read(actualImageFile);
		
		assertEquals("width", expectedImage.getWidth(), actualImage.getWidth());
		assertEquals("height", expectedImage.getHeight(), actualImage.getHeight());
		
		BufferedImage diffImage = new BufferedImage(actualImage.getWidth(), actualImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		int diffCount = 0;
		for (int y = 0; y < expectedImage.getHeight(); y++) {
			for (int x = 0; x < expectedImage.getWidth(); x++) {
				int expectedPixel = expectedImage.getRGB(x, y);
				int actualPixel = actualImage.getRGB(x, y);
				
				if (expectedPixel == actualPixel) {
					diffImage.setRGB(x,  y,  Color.LIGHT_GRAY.getRGB());
				} else {
					diffImage.setRGB(x, y, actualPixel);
					diffCount++;
				}
			}
		}
		
		if (diffCount > 0) {
			File parentFile = actualImageFile.getParentFile();
			File diffFile = new File(parentFile, "DIFF_" + actualImageFile.getName());
			ImageIO.write(diffImage, "png", diffFile);
			fail("Difference in image: " + diffCount + " pixels: " + actualImageFile + "\nSee difference in: " + diffFile);
		}
	}


}
