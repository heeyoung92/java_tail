# java_tail

## config.properties
N_LINE // 0 : tail f option & n: tail n (from the end of the file)
 
FILE_CNT=3                              // monitored file count 
FILEPATH_0=./LogFile/monitor2.log       // monitored file path
POSPAHT_0=./LogFile/lastpostion2.txt		// file position path
FILEPATH_1=./LogFile/monitor3.log
POSPAHT_1=./LogFile/lastpostion3.txt
FILEPATH_2=./LogFile/monitor.log
POSPAHT_2=./LogFile/lastpostion.txt

ctr-c(종료) : 마지막 출력 위치(postion) 저장
재시작시 이전 출력 위치 다음부터 출력 시작
