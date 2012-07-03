package cl.automind.gameframework;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javazoom.jl.player.Player;

public class SoundManager implements ISoundManager {

	private static ISoundManager instance = new SoundManager();
	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	private boolean running;
	private String local = ".AutoMind";

	private SoundManager() {

	}

	public static ISoundManager getInstance() {
		return instance;
	}
	
	@Override
	public String voicePath(String msg) {
		String base = System.getProperty("user.home")
				+ System.getProperty("file.separator") + local
				+ System.getProperty("file.separator");
		File f = new File(base);
		f.mkdirs();
		String file = base + hash(msg) + ".mp3";
		return file;
	}
	
	@Override
	public void playVoice(String msg){
		queue.add('0' + voicePath(msg));
		if (!running){
			start();
		}
	}

	@Override
	public void play(String absolutePath) {
		queue.add('0'+absolutePath);
		if (!running) {
			start();
		}
	}

	private void start() {
		running = true;
		Thread t = new Thread() {
			public void run() {
				System.out.println("Starting sound Manager");
				while (running) {
					if (queue.isEmpty()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						String data = queue.poll();
						char type = data.charAt(0);
						String filename = data.substring(1);
						try {
							BufferedInputStream bis;
							if (type == '0'){
								FileInputStream fis = new FileInputStream(filename);
								bis = new BufferedInputStream(fis);
							}
							else {
								bis = new BufferedInputStream(SoundManager.class.getResourceAsStream(filename));
							}
							Player player = new Player(bis);
							player.play();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}

		};
		t.setDaemon(true);
		t.setName("SoundPlayer");
		t.start();
	}
	
	private String hash(String aux){
		//el 00 que agrego al string es para crear un hash diferente segun la version del codificador
		byte[] defaultBytes = ("01" + clean(aux)).getBytes();
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
		}catch(NoSuchAlgorithmException nsae){
			nsae.printStackTrace();
		}
		return "tmpwav" + aux.length();

	}
	
	@Override
	public String clean(String txt){
		txt = txt.replaceAll("-", " guion ");

		//esta regla siempre debe ser la ultima
		txt = txt.replaceAll(" ", "+");
		return txt;
	}

	@Override
	public void playFromJar(String string) {
		playFromJar(string,true);
	}
	
	@Override
	public void playFromJar(String string, boolean always) {
		if (!always && queue.contains('1' + string)){
			return;
		}
		queue.add('1' + string);
		if (!running){
			start();
		}
	}

	
}
