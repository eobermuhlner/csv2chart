package ch.obermuhlner.csv2chart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ApplicationTest {

	@Test
	public void testReferenceCharts() throws IOException {
		Files.list(Paths.get("src/test/resources/csv"))
			.map(path -> path.toFile().getName())
			.filter(name -> name.endsWith(".csv"))
			.map(name -> name.substring(0, name.length() - ".csv".length()))
			.forEach(name -> {
				try {
					assertCsv2Chart(name);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
	}

	private void assertCsv2Chart(String baseFilename) throws IOException {
		runCsv2Chart(
				"--out-dir", "src/test/resources/out_images",
				"src/test/resources/csv/" + baseFilename + ".csv");

		assertImageEquals(
				new File("src/test/resources/ref_images/" + baseFilename + ".png"),
				new File("src/test/resources/out_images/" + baseFilename + ".png"));
	}
	
	private void runCsv2Chart(String...args) {
		Application.main(args);
	}

	private void assertImageEquals(File expectedImageFile, File actualImageFile) throws IOException {
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
