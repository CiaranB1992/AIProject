package ie.gmit.sw.ai.audio;

import java.net.*;
import javax.sound.sampled.*;
public enum SoundEffects {	
	MOVE("resources/move"), ALARM("resources/alarm");

	private Clip clip;
	SoundEffects(String fileName) {
		try {
			URL url = this.getClass().getClassLoader().getResource(fileName);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if (clip.isRunning()) clip.stop(); 
		clip.setFramePosition(0);
		clip.start(); 
	}
	 
	public static void init() {
		values();
	}
}