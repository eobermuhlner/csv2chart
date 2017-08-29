package ch.obermuhlner.csv2chart.model.csv;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import ch.obermuhlner.csv2chart.Parameters;
import ch.obermuhlner.csv2chart.model.DataModel;
import ch.obermuhlner.csv2chart.model.DataVector;

public class CsvDataModelLoaderTest {

	@Test
	public void testLoad_Empty() {
		CsvDataModelLoader dataModelLoader = new CsvDataModelLoader();
		
		Matrix<String> matrix = new Matrix<>();
		
		Parameters parameters = new Parameters();
		DataModel dataModel = dataModelLoader.load(matrix, parameters);
		
		assertEquals(null, dataModel.getCategory());
		
		assertEquals(0, dataModel.getValues().size());
	}

	@Test
	public void testLoad_HeaderRow() {
		CsvDataModelLoader dataModelLoader = new CsvDataModelLoader();
		
		Matrix<String> matrix = new Matrix<>();
		
		int row = 0;
		matrix.setRow(row++, "Col1", "Col2");
		matrix.setRow(row++, "1.1", "1.2");
		matrix.setRow(row++, "2.1", "2.2");
		
		Parameters parameters = new Parameters();
		DataModel dataModel = dataModelLoader.load(matrix, parameters);
		
		assertEquals(null, dataModel.getCategory());
		
		assertEquals(2, dataModel.getValues().size());
		assertEquals(new DataVector(Arrays.asList("Col1"), Arrays.asList("1.1", "2.1")), dataModel.getValues().get(0));
		assertEquals(new DataVector(Arrays.asList("Col2"), Arrays.asList("1.2", "2.2")), dataModel.getValues().get(1));
	}

	@Test
	public void testLoad_HeaderCol() {
		CsvDataModelLoader dataModelLoader = new CsvDataModelLoader();
		
		Matrix<String> matrix = new Matrix<>();
		
		int row = 0;
		matrix.setRow(row++, "Row1", "1.1", "1.2");
		matrix.setRow(row++, "Row2", "2.1", "2.2");
		
		Parameters parameters = new Parameters();
		DataModel dataModel = dataModelLoader.load(matrix, parameters);
		
		assertEquals(new DataVector(Arrays.asList(), Arrays.asList("Row1", "Row2")), dataModel.getCategory());
		
		assertEquals(2, dataModel.getValues().size());
		assertEquals(new DataVector(Arrays.asList(), Arrays.asList("1.1", "2.1")), dataModel.getValues().get(0));
		assertEquals(new DataVector(Arrays.asList(), Arrays.asList("1.2", "2.2")), dataModel.getValues().get(1));
	}

	@Test
	public void testLoad_HeaderRow_HeaderCol() {
		CsvDataModelLoader dataModelLoader = new CsvDataModelLoader();
		
		Matrix<String> matrix = new Matrix<>();
		
		int row = 0;
		matrix.setRow(row++, "Rows", "Col1", "Col2");
		matrix.setRow(row++, "Row1", "1.1", "1.2");
		matrix.setRow(row++, "Row2", "2.1", "2.2");
		
		Parameters parameters = new Parameters();
		DataModel dataModel = dataModelLoader.load(matrix, parameters);
		
		assertEquals(new DataVector(Arrays.asList("Rows"), Arrays.asList("Row1", "Row2")), dataModel.getCategory());
		
		assertEquals(2, dataModel.getValues().size());
		assertEquals(new DataVector(Arrays.asList("Col1"), Arrays.asList("1.1", "2.1")), dataModel.getValues().get(0));
		assertEquals(new DataVector(Arrays.asList("Col2"), Arrays.asList("1.2", "2.2")), dataModel.getValues().get(1));
	}

	@Test
	public void testLoad_HeaderRow2_HeaderCol() {
		CsvDataModelLoader dataModelLoader = new CsvDataModelLoader();
		
		Matrix<String> matrix = new Matrix<>();
		
		int row = 0;
		matrix.setRow(row++, "Rows", "Columns", "Columns");
		matrix.setRow(row++, "Col0", "Col1", "Col2");
		matrix.setRow(row++, "Row1", "1.1", "1.2");
		matrix.setRow(row++, "Row2", "2.1", "2.2");
		
		Parameters parameters = new Parameters();
		DataModel dataModel = dataModelLoader.load(matrix, parameters);
		
		assertEquals(new DataVector(Arrays.asList("Rows", "Col0"), Arrays.asList("Row1", "Row2")), dataModel.getCategory());
		
		assertEquals(2, dataModel.getValues().size());
		assertEquals(new DataVector(Arrays.asList("Columns", "Col1"), Arrays.asList("1.1", "2.1")), dataModel.getValues().get(0));
		assertEquals(new DataVector(Arrays.asList("Columns", "Col2"), Arrays.asList("1.2", "2.2")), dataModel.getValues().get(1));
	}
}
