
package repast.simphony.jasperReports;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.view.JasperViewer;
import repast.simphony.data2.FileDataSink;
import saf.core.ui.util.FileChooserUtilities;

public class JRTools {
	private static ArrayList<DataSourceFinder> finders;

	static {
		finders = new ArrayList<DataSourceFinder>();
		addFinder(new DefaultDataSourceFinder());
	}

	public static void addFinder(DataSourceFinder consumer) {
		finders.add(consumer);
	}

	public static JRDataSource getDataSource(FileDataSink outputter) {
		JRDataSource dataSource = null;

		Iterator<DataSourceFinder> iter = finders.iterator();
		while (dataSource == null && iter.hasNext()) {
			dataSource = iter.next().getDataSource(outputter);
		}

		return dataSource;
	}


	// TODO: move this to the jasper report plugin stuff
	public static void runReport(JRDataSource dataSource,
			String reportFileName, String destFileName, JROutputType outputType,
			JROutputType... outputTypes) throws JRException {

		// Specify to use the groovy compiler, otherwise need Java JDK
		System.setProperty("jasper.reports.compiler.class","net.sf.jasperreports.compilers.JRGroovyCompiler");

		JasperReport report = JasperCompileManager.compileReport(reportFileName);

		JasperPrint filledReport = JasperFillManager.fillReport(report, null,
				dataSource);

		outputReport(filledReport, destFileName, outputType);

		for (JROutputType curOutputType : outputTypes) {
			outputReport(filledReport, reportFileName, curOutputType);
		}

	}

	public static File browseForJasperReportDef(String defaultFileName, JComponent parent) {
		File file = FileChooserUtilities.getOpenFile(new File(defaultFileName), new FileFilter() {
			public boolean accept(File f) {
				if (f.getName().toLowerCase().endsWith(".jrxml") 
						|| f.getName().toLowerCase().endsWith(".xml")
						|| f.isDirectory()) {
					return true;
				}
				return false;
			}
			public String getDescription() {
				return "JasperReports XML (*.xml, *.jrxml)";
			}
		});

		return file;
	}

	public static void outputReport(JasperPrint report, String destFileName, JROutputType outputType) throws JRException {
		switch(outputType) {
		case PDF:
			JasperExportManager.exportReportToPdfFile(report, destFileName);
			break;
		case HTML:
			JasperExportManager.exportReportToHtmlFile(report, destFileName);
			break;
		case XML:
			JasperExportManager.exportReportToXmlFile(report, destFileName, true);
			break;
		case VIEWER:
			JasperViewer.viewReport(report);
			break;
		default:
			// TODO: update this
			throw new RuntimeException("Error");
		}
	}

	public static void main(String[] args){

		String csvFileName = "testdata.csv";
		String reportFileName = "testreport.jrxml";
		String destFileName = "testReport";

		JRCsvDataSource dataCsv = null;

		try {
			dataCsv = new JRCsvDataSource(new File(csvFileName));
			dataCsv.setUseFirstRowAsHeader(true);
			dataCsv.setFieldDelimiter(',');

			JRTools.runReport(dataCsv, reportFileName, destFileName+".pdf", JROutputType.PDF);
//			JRTools.runReport(dataCsv, reportFileName, destFileName+".html", JROutputType.HTML);
//			JRTools.runReport(dataCsv, reportFileName, destFileName+".xml", JROutputType.XML);
//			JRTools.runReport(dataCsv, reportFileName, destFileName+"viewer", JROutputType.VIEWER);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (JRException e) {
			e.printStackTrace();
		}

	}
}
