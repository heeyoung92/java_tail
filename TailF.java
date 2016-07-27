package tail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class TailF implements Runnable {
	private boolean debug = false;

	private int RunEveryNSeconds = 2000; // 2s 마다 로그 기록
	public static final int DefaultStartPosition = 0;

	private long lastKnownPosition = 0;

	private File file = null;
	String postionPath = "";


	private boolean shouldIRun = true;

	public TailF(String mFilePath, String mPostionPath) throws Exception {
		this.file = new File(mFilePath);
		this.postionPath = mPostionPath;
		// this.RunEveryNSeconds = mInterval;

		// last position 값 가져오기
		File posfile = new File(mPostionPath);
		//  파일이 있는지 확인
		if(posfile.isFile()){
			FileInputStream fis = new FileInputStream(mPostionPath);
			DataInputStream dis = new DataInputStream(fis);
			this.lastKnownPosition = dis.readLong(); // 0; // 1057792;
			dis.close();			
		}
		else{
			this.lastKnownPosition = DefaultStartPosition;
			posfile.createNewFile();
			this.saveLastPostion(DefaultStartPosition);
		}
//		System.out.println(file.getName() + " lastKnownPosition : " + lastKnownPosition);
	}

	private void printLine(String message) {
		System.out.println(message);
	}

	private void saveLastPostion(long postion) throws Exception {
		// 마지막 읽은 위치 저장 -> 프로그램 재시작시 종료시점부터 읽기
		FileOutputStream fos = new FileOutputStream(postionPath);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeLong(postion);
		dos.close();
	}

	public void stopRunning() {
		shouldIRun = false;
		// this.printLine("called stopRunning!!!!!!!");
	}

	public void run() {
		String threadname = Thread.currentThread().getName();
		String fileName = file.getName();
		System.out.println(threadname + "-> " + fileName + " Monitoring Start!");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					saveLastPostion(lastKnownPosition);
					System.out.println(Thread.currentThread().getName() + ": Exit the Thread & Save File Offset");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		try {
			// Reading file
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			randomAccessFile.seek(lastKnownPosition); // 마지막 읽은 위치부터
			while (shouldIRun) {
				long fileLength = file.length();
				if (fileLength > lastKnownPosition) {

					String line = null;

					while ((line = randomAccessFile.readLine()) != null) {
						TailTest.reportPerformanceFor("");

						this.printLine("[" + fileName + "]: " + line);
						lastKnownPosition = randomAccessFile.getFilePointer();
					}
				} else {
					if (debug)
						this.printLine("Hmm.. Couldn't found new line ");
				}
				Thread.sleep(RunEveryNSeconds);
			}
			randomAccessFile.close();
		} catch (Exception e) {
			stopRunning();
		}
		// if (debug)
		this.printLine("Exit the program...");
	}

}
