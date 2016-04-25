package com.treelogic.proteus.flink.examples.utils;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebBrowser {

	private static Log logger = LogFactory.getLog(WebBrowser.class);

	public  static void start(String web) throws IOException{
		String browser = requireBrowserInput();
		String path = ResourcePOJO.class.getResource("launch-browser").getPath();
		ProcessBuilder b = new ProcessBuilder(path + "/run.sh", web, path, browser);
		
		try {
			b.start();
		} catch (IOException e) {
			throw e;
		}
	}

	private static String requireBrowserInput() {
		String browser = "";
		logger.info("ACTION REQUIRED!: Please, indicate your installed and prefered web browser: [firefox, safari]");
		browser = readInput();
		return browser;
	}

	//private static boolean wrongBrowserInput(String browser) {
	//	return !(browser.equals("firefox") || browser.equals("safari"));
	//}

	private static String readInput() {
		Scanner input = new Scanner(System.in);
		String inputString = input.next();
		input.close();
		return inputString;
	}

}
