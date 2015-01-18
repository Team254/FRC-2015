package com.team254.lib.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj.Timer;

public class Logger implements Serializable {

	public static final double WRITE_TIME = 0.5; // Write every .5 seconds
	
	private static Logger inst = null;
	
	protected File logFile = null;
	private BufferedWriter writer;
	private ArrayBlockingQueue<String> logMessages = new ArrayBlockingQueue<String>(300);
	Thread consumer;
	
	public static Logger getInstance() {
		if (inst == null) {
			inst = new Logger();
		}
		return inst;
	}
	
	Runnable consumerTask = new Runnable() {
		public void run() {
			double lastWriteTime = Timer.getFPGATimestamp();
			while (true) {
				try {
					String msg = logMessages.take();
					writer.write(msg);
					if (Timer.getFPGATimestamp() >= lastWriteTime + WRITE_TIME) {
						writer.flush();
						lastWriteTime = Timer.getFPGATimestamp();
					}
				} catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	private Logger() {
		File baseDrive = determineMountPoint();
		File logDir = new File(baseDrive, "logs");
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		
		File lastBoot = getLastBootLogFile();
		int number = 0;
		if (lastBoot != null) {
			String name = lastBoot.getName();
			String numberStr = name.substring(0, name.lastIndexOf('.'));
			number = Integer.parseInt(numberStr);
		}
		logFile = new File(logDir, String.format("%04d.log", number + 1));
		try {
			 writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(logFile), "utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		consumer = new Thread(consumerTask);
		consumer.start();
	}

	protected static File determineMountPoint() {
		char iter = 'a';
		for (int i= 0; i < 26; i++) {
			File f = new File("/mnt/sd" + iter);
			if (f.exists() && f.isDirectory()) {
				return f;
			}
			iter++;
		}
		return null;
	}

	public static File getLogDirectory() {
		File baseDrive = determineMountPoint();
		File logDir = new File(baseDrive, "logs");
		return logDir;
	}
	
	public static File getLastBootLogFile() {
		List<String> fileNames = getAllLogFiles().stream().map(l -> l.getName()).collect(Collectors.toList());
		fileNames.sort(null);
		if (fileNames.size() <= 0)
			return null;
		String lastFileName = fileNames.get(fileNames.size() - 1);
		return new File(getLogDirectory(), lastFileName);
	}

	public static Collection<File> getAllLogFiles() {
		FilenameFilter logFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				return lowercaseName.matches("\\d\\d\\d\\d.log");
			}
		};
		File logDir = getLogDirectory();
		File[] files= logDir.listFiles(logFilter);
		if (files == null) {
			return new ArrayList<File>();
		} else {
			return Arrays.asList(files);
		}
	}

	protected File getCurrentLogFile() {
		return logFile;
	}

	public static File getLogFile() {
		return getInstance().getCurrentLogFile();
	}

	private boolean printLocal(String s) {
		return logMessages.offer(s);
	}
	
	public static boolean print(String s) {
		return getInstance().printLocal(s);
	}
	
	private boolean printlnLocal(String s) {
		return logMessages.offer(s+'\n');
	}
	
	public static boolean println(String s) {
		return getInstance().printlnLocal(s);
	}

	@Override
	public Object getState() {
		return logFile != null && writer != null;
	}

	@Override
	public String getName() {
		return "logger";
	}

	@Override
	public String getType() {
		return Boolean.class.getName();
	}

}
