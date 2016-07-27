package tail;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 참고: http://crunchify.com/log-file-tailer-tail-f-implementation-in-java-best-way-to-tail-any-file-programmatically/
 * 
 * 마지막 출력(lastKnownPosition)위치 부터 시작 readWriteFileAccess.seek(lastKnownPosition)
 * & readWriteFileAccess.getFilePointer() 이용
 */

public class TailTest {

	static final long refTime=System.currentTimeMillis(); 
	public static void main(String argv[]) throws Exception {

		// main 종료시 호출
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("ShutdownHook: Main Finished...");
			}
		});

		// 성능(시간,메모리)측정
//		time = System.currentTimeMillis();
		reportPerformanceFor(" Starting at");

		// 설정파일 값 읽어오기
		String path = "C://Users//medialog//Desktop//tail//config//config.properties";
		Properties props = new Properties();
		 props.load( (TailTest.class).getResourceAsStream("config.properties")); 	 	// properties, inside .jar
//		FileInputStream fis = new FileInputStream(path);						// properties, outside .jar
//		 props.load(fis);

		int file_cnt = Integer.parseInt(props.getProperty("FILE_CNT"));
		int nline = Integer.parseInt(props.getProperty("N_LINE"));

		// file 갯수만큼 monitoring thread 생성
		ExecutorService executor = Executors.newFixedThreadPool(file_cnt);

		String filepath = "";
		String postion = "";
		if (nline == 0) {
			for (int i = 0; i < file_cnt; i++) {
				filepath = props.getProperty("FILEPATH_" + i);
				postion = props.getProperty("POSPAHT_" + i);

				TailF tail = new TailF(filepath, postion);
				executor.execute(tail);
			}
		} 
		else if(nline >0) {     // 파일 끝에 n번째줄 만큼 출력
			for (int i = 0; i < file_cnt; i++) {
				filepath = props.getProperty("FILEPATH_" + i);
			
				TailLine tailLine = new TailLine(filepath, nline);
				executor.execute(tailLine);
			}
		}
		else {
			System.out.println("잘못된 N_Line 값입니다.");
		}
	 executor.shutdown();
	}
	
	
	// 성능(시간,메모리)측정
	public static void reportPerformanceFor(String msg) {
		double time = (System.currentTimeMillis() - refTime) / 1000.0;
		double mem = usedMemory() / (1024.0 * 1024.0);
		mem = Math.round(mem * 100) / 100.0;
		System.out.println(msg + " (" + time + " sec, " + mem + "MB)");
	}

	private static long usedMemory() {
		Runtime rt = Runtime.getRuntime();
		return rt.totalMemory() - rt.freeMemory();
	}

}