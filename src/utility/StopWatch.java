package utility;

import java.util.*;
import java.text.*;

public class StopWatch {
	
	long startTime = 0;
	long endTime = 0;
	String process = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	
	public StopWatch() {
	}
	
	public StopWatch(String process) {
		this.process = process;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
		System.out.println();
		System.out.println(process +" [Started     = "+ sdf.format(new Date(startTime)) +"]");
	}
	
	public void stop() {
		endTime = System.currentTimeMillis();
		System.out.println(process +" [Finished    = "+ sdf.format(new Date(endTime)) +"]");
		elapsed();
		System.out.println();
	}
	
	public void elapsed() {
		int elapsed = (int) (endTime - startTime);
		int SS = elapsed % 1000;
		int ss = (elapsed / 1000) % 60;
		int mm = ((elapsed / 1000) % 3600) / 60;
		int HH = (elapsed / 1000) / 3600;
		
		System.out.println("[ ElapsedTime = "+ (endTime - startTime) +" (ms) ]");
//		System.out.println(process +" [ElapsedTime = " + String.format("%02d", HH) + 
//				                          ":" + String.format("%02d", mm) +
//				                          ":" + String.format("%02d", ss) +
//				                          ":" + String.format("%03d", SS) +"]");
	}

}
