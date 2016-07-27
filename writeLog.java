// 5초 마다 파일 기록 

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class writeLog implements Runnable {
	String filePath;
	boolean shouldIRun;
	int crunchifyRunEveryNSeconds;

	public writeLog(String filePath, boolean shouldIRun, int crunchifyRunEveryNSeconds) throws Exception {
			this.filePath = filePath;
			this.shouldIRun = shouldIRun;
			this.crunchifyRunEveryNSeconds = crunchifyRunEveryNSeconds;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		FileWriter fileWritter;
 
		try {
			while (shouldIRun) {
				Thread.sleep(crunchifyRunEveryNSeconds);
				fileWritter = new FileWriter(filePath, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
 
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
				String data = sdf.format(d)+" Writing "+filePath.substring(37)+"\n";
		
				bufferWritter.write(data);
				bufferWritter.close();
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String argv[]) throws Exception {	
		ExecutorService executor = Executors.newFixedThreadPool(4);

		String filePath = "/tail/LogFile/monitor.log";
		String filePath2 = "/tail/LogFile/monitor2.log";
		String filePath3 = "/tail/LogFile/monitor3.log";
		
		writeLog write = new writeLog(filePath, true, 5000);
		writeLog write2 = new writeLog(filePath2, true, 5000);
		writeLog write3 = new writeLog(filePath3, true, 5000);

		executor.execute(write);
		executor.execute(write2);
		executor.execute(write3);

	}

	
}
