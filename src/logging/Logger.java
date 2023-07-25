package logging;

import main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	public static SimpleDateFormat formatter;

	private static FileWriter fw;

	public static void init() {
		try {
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			File jarFile = Main.jarFile;
			long time = new Date().getTime();
			File logsFile = new File(jarFile.getParent() + "/logs");
			logsFile.mkdirs();
			File logFile = new File(logsFile.getPath() + "/" + time +".txt");
			System.out.println(logFile);
			logFile.delete();
			logFile.createNewFile();
			fw = new FileWriter(logFile);
			log("----------");
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void shutdown() {
		try {
			log("----------");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void log(String text) {
		Date date = new Date();
		System.out.println("[" + formatter.format(date) + "] " + text);
		try {
			fw.append(text + System.lineSeparator());
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
