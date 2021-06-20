package com.github.hhhzzzsss.chathacks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.Session;

public class Chatter implements ActionListener{
	
	public static JFrame frame;
	public static Container pane;
	static JTextArea textArea;
	static JTextArea playerList;
	static JButton reloadButton;
	static JScrollPane commandScroll;
	static JTextArea commandTextArea;
	static JTextArea usernameList;
	static JScrollPane usernameScroll;
	
	static Chatter chatter = new Chatter();
	
	public static String chatText = "";
	static boolean firstUpdateFlag = false;
	static int loopTimer = 0;
	static int sloopTimer = 0;
	
	public static StringBuilder commandSpyText = new StringBuilder(12800);
	public static int commandSpyStart = 0;
	public static String commandSpyLog = "";
	
	public static String usernameText = "";
	public static File uuidLog = new File("uuid_log.txt");
	public static HashMap<String, String> uuidMap = new HashMap<String, String>();
	
	public static void init() {
		System.setProperty("java.awt.headless", "false");
		
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(400, 600));
		
		playerList = new JTextArea();
		playerList.setPreferredSize(new Dimension(200, 600));
		playerList.setBackground(Color.LIGHT_GRAY);
		
		reloadButton = new JButton("Reload");
		reloadButton.setActionCommand("reload");
		reloadButton.addActionListener(chatter);
		reloadButton.setPreferredSize(new Dimension(1000, 50));
		
		commandTextArea = new JTextArea(256, 256);
		commandTextArea.setMaximumSize(new Dimension(256, 256));
		commandTextArea.setEditable(false);
		commandScroll = new JScrollPane(commandTextArea);
		commandScroll.setPreferredSize(new Dimension(400, 600));
		
		usernameList = new JTextArea();
		usernameList.setEditable(false);
		usernameScroll = new JScrollPane(usernameList);
		usernameScroll.setPreferredSize(new Dimension(1000, 150));
		
		frame = new JFrame("Chatter");
		pane = frame.getContentPane();
		pane.add(textArea, BorderLayout.CENTER);
		pane.add(playerList, BorderLayout.LINE_START);
		pane.add(reloadButton, BorderLayout.PAGE_END);
		pane.add(commandScroll, BorderLayout.LINE_END);
		pane.add(usernameScroll, BorderLayout.PAGE_START);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		
		for (int i = 0; i<commandSpyText.capacity(); i++) {
			commandSpyText.append("\0");
		}
		
		boolean newFile = true;
		try {
			newFile = uuidLog.createNewFile();
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(uuidLog),Charset.forName("UTF-8").newEncoder());
			writer.write("{\n}");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		if (!newFile) {
			try {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(uuidLog),Charset.forName("UTF-8").newDecoder());
				char charArray[] = new char[(int) uuidLog.length()];
				reader.read(charArray);
				reader.close();
				String logString;
				logString = new String(charArray);
				logString = logString.substring(logString.indexOf('{'), logString.lastIndexOf('}')+1); // Removes Encoding Header/Footer
				String[] lineList = logString.split("\\r?\\n");
				for (String line : lineList) {
					if (line.length() < 10) {
						continue;
					}
					String[] lineParts = line.split(" ", 2);
					uuidMap.put(lineParts[0], lineParts[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		Thread displayUpdater = new Thread() {
			long startTime;
			public void run() {
				while (true) {
					startTime = System.currentTimeMillis();
					if (!commandTextArea.getText().equals(commandSpyLog))
						commandTextArea.setText(commandSpyLog);
					if (!usernameList.getText().equals(usernameText))
						usernameList.setText(usernameText);
					int timePassed = (int) (System.currentTimeMillis() - startTime);
					try {
						Thread.sleep(Math.max(200-timePassed,0));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		displayUpdater.start();
	}
	
	public static void saveUUIDs() {
		try {
			uuidLog.createNewFile();
		} catch (IOException e) {
			ChatHacks.addChatMessage("Failed to create log file: " + e.getMessage());
			return;
		}
		try {
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(uuidLog),Charset.forName("UTF-8").newEncoder());
			writer.write("{\n");
			for (Map.Entry<String, String> entry : uuidMap.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue() + "\n");
			}
			writer.write('}');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			ChatHacks.addChatMessage("Failed to write to log file: " + e.getMessage());
			return;
		}
	}
	
	public static void update(ClientPlayerEntity player) {
		String[] messages = chatText.split("\\r?\\n");
		for (String s : messages) {
			if (s.length()>5 && s.substring(0, 6).equalsIgnoreCase("floop ")) {
				player.sendChatMessage(s.substring(6));
			}
			else if (s.length()>4 && s.substring(0, 5).equalsIgnoreCase("loop ")) {
				if (loopTimer==0) {
					player.sendChatMessage(s.substring(5));
				}
			}
			else if (s.length()>5 && s.substring(0, 6).equalsIgnoreCase("sloop ")) {
				if (sloopTimer==0) {
					player.sendChatMessage(s.substring(6));
				}
			}
			else if (s.startsWith("setusername ") && firstUpdateFlag) {
				ChatHacks.username = s.substring(12).replaceAll("&", "§");
				ChatHacks.session = new Session(ChatHacks.username, "", "", "mojang");
				System.out.println("Username set to " + ChatHacks.username);
			}
			else if (s.length()>0 && firstUpdateFlag){
				player.sendChatMessage(s);
			}
		}
		firstUpdateFlag = false;
		
		loopTimer--;
		if (loopTimer<0) {
			loopTimer=10;
		}
		sloopTimer--;
		if (sloopTimer<0) {
			sloopTimer=100;
		}
	}
	
	public static void processCommandSpy(String text) {
		int l=text.length(); //length to be inserted
		commandSpyStart = (commandSpyText.capacity()+commandSpyStart-(l+1)) % commandSpyText.capacity(); //extra space for newline character
		int l2 = commandSpyText.capacity()-commandSpyStart; //length available at the end
		if (l<=l2) {
			commandSpyText.replace(commandSpyStart, commandSpyStart + l, text.substring(0, 0+l));
		}
		else {
			commandSpyText.replace(commandSpyStart, commandSpyStart+l2, text.substring(0, 0+l2));
			commandSpyText.replace(0, l-l2, text.substring(0+l2, 0+l));
		}
		commandSpyText.setCharAt((commandSpyStart+l)%commandSpyText.capacity(), '\n');
		commandSpyLog = (commandSpyText.substring(commandSpyStart) + commandSpyText.substring(0, commandSpyStart)).trim();
	}
	
	/*public static void processUsername(String text) {
		usernameList.append(text.substring(2,text.length()-2) + "\n");
	}*/
	
	public static void updateUsernameList() {
		StringBuilder builder = new StringBuilder();
		Collection<PlayerListEntry> players = MinecraftClient.getInstance().player.networkHandler.getPlayerList();
		int uuidindex = 0;
		for (PlayerListEntry s : players) {
			String name = s.getProfile().getName();
			String uuid = s.getProfile().getId().toString();
			if (uuidMap.containsKey(uuid)) {
				builder.append(String.format("%02d: %s - %s (%s)\n", uuidindex, name, uuid, uuidMap.get(uuid)));
			}
			else {
				builder.append(String.format("%02d: %s - %s\n", uuidindex, name, uuid));
			}
			uuidindex++;
		}
		usernameText = builder.toString();
	}
	
	public static void clearLogs() {
		for (int i = 0; i<commandSpyText.capacity(); i++) {
			commandSpyText.setCharAt(i, '\0');
		}
		commandSpyLog = (commandSpyText.substring(commandSpyStart) + commandSpyText.substring(0, commandSpyStart)).trim();
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("reload")) {
			chatText = textArea.getText();
			firstUpdateFlag = true;
			

			String[] messages = chatText.split("\\r?\\n");
			for (String s : messages) {
				if (s.startsWith("setusername ")) {
					ChatHacks.username = s.substring(12).replaceAll("&", "§");
					ChatHacks.session = new Session(ChatHacks.username, "", "", "mojang");
					System.out.println("Username set to " + ChatHacks.username);
				}
			}
		}
	}
	
}