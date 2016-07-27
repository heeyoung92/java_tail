package tail;

import java.io.File;
import java.io.RandomAccessFile;

public class TailLine implements Runnable {

	private long lastKnownPosition = 0;
	private File file = null;
	private int LinesToRead = 0;

	public TailLine(String mFilePath, int nLinesToRead) throws Exception {
		this.file = new File(mFilePath);
		this.LinesToRead = nLinesToRead;

	}

	private void printLine(String message) {
		System.out.println(message);
	}

	public void stopRunning() {
		this.printLine("called  stopRunning!!!!!!!");

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String threadname = Thread.currentThread().getName();
		String fileName = file.getName();
		System.out.println("[" +threadname + "] " + fileName + " Monitoring Start!");

		long counterFromEnd = 1L;
		long newlineCounterGoal = LinesToRead;
		int newlineCounter = 0;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("ShutdownHook: ["+threadname + "] Exit the TailLine Thread..");
			}
		});

		try {
			// Reading file
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			long fileLength = file.length();

			if (fileLength > 0) {
				randomAccessFile.seek(fileLength - 1L); // 파일끝부터 읽기
				char currentChar = (char) randomAccessFile.readByte();

				if (currentChar == '\n') {
					newlineCounterGoal++;
				}

				while (counterFromEnd <= fileLength) {
					randomAccessFile.seek(fileLength - counterFromEnd);
					if (randomAccessFile.readByte() == '\n') {
						newlineCounter++;
					}
					if (newlineCounter == newlineCounterGoal) {
						lastKnownPosition = randomAccessFile.getFilePointer();
						break;
					}
					counterFromEnd++;
				}
			}

			randomAccessFile.seek(lastKnownPosition); // 마지막 읽은 위치부터
			if (fileLength > lastKnownPosition) {
				String line = null;
				while ((line = randomAccessFile.readLine()) != null) {
					TailTest.reportPerformanceFor("");
					this.printLine("[" + fileName + "]: " + line);
					lastKnownPosition = randomAccessFile.getFilePointer();
				}
				randomAccessFile.close();
			}
		} catch (Exception e) {
			stopRunning();
		}
	}

}
