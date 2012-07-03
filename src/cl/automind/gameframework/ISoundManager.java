package cl.automind.gameframework;

public interface ISoundManager {

	public abstract String voicePath(String msg);

	public abstract void playVoice(String msg);

	public abstract void play(String absolutePath);

	public abstract String clean(String txt);

	public abstract void playFromJar(String string);

	/**
	 * Agrega un sonido a la cola de reproducci—n. Si @always es falso entonces se revisa la cola y s—lo se agrega si es que no exist’a antes.
	 * @param string
	 * @param always
	 */
	public abstract void playFromJar(String string, boolean always);

}