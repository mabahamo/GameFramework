package cl.automind.gameframework.network;

import cl.automind.gameframework.AbstractLevel;
import cl.automind.gameframework.modal.PasswordListenerInterface;

public interface INetworkManager {

	public abstract void doLogin(String username, char[] password,
			PasswordListenerInterface pl);


	public abstract void addListener(byte mask, AbstractLevel level);

	public abstract void joinRoom(long roomId);

	public abstract String getUsername();

	/**
	 * Datos necesarios para comenzar el juego:
	 * Datos de mi equipo (colegio, nombre de todos los integrantes)
	 * Lista de todos los equipos conectados
	 * Puntajes de cada uno de los equipos
	 * 
	 * Los datos se descargan de rails, se intenta cuantas veces sea necesario hasta recuperar la informaci�n. 
	 * Este m�todo retorna inmediatamente, y notifica al statusListener cuando termina de descargar todas las tareas.
	 */

	public abstract void setInstanceId(long instanceId);

	public abstract String getColegio();

	public abstract void downloadVoice(String msg) throws Exception;

	public abstract void send(byte[] msg);

	public abstract void fetchRanking();

	public abstract void fetchTeamRanking();


}