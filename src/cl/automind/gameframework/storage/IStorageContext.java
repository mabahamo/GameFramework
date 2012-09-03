package cl.automind.gameframework.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IStorageContext {

	/**
	 * Reinicia la base de datos, eliminando todas las tablas y creandolas desde cero
	 */
	public abstract void reset();

	public abstract void addCaja(int turno, String alto, String ancho,
			String largo, String color, String sorpresa);


	public abstract void addPlayer(int casilla, String name, int puntaje);

	public abstract void setState(char c);

	public abstract char getState();

	public abstract int getTurno();

	public abstract ResultSet getSorpresa(int turno) throws SQLException;

	public abstract void updateSorpresa(int turno, String data);

	public abstract void storeRegla(Integer casilla, String rule);

	public abstract void removeRegla(Integer casilla);

	public abstract String getRegla(Integer casilla);

	public abstract void updateTeam(int teamId, int puntaje, int tiempo);

	public abstract void updatePlayer(int casilla, int puntaje);

	public abstract String getEquipo();

	public abstract void setUsername(String username);

	public abstract int getPuntaje();

	public abstract String getStatusMsg();

	public abstract String getLastResultado();

	public abstract void rankingUpdated();

	public abstract String getPlayerCasilla(int casilla);

	public abstract boolean jugadorApuestaEnTurno(int turno);

	public abstract int getApuesta(int turno, int casilla);

	public abstract int getResultadoTurnoCasilla(int turno, int casilla);

	public abstract void storeBet(int turno, String apuesta, String reglas,
			int delay, int puntaje);

	@Deprecated
	public abstract Connection getConnection();

	public abstract int getPuntajeTurno(int turno);

	public abstract int getPuntajeTurnoCasilla(int turno, int casilla);

	public abstract void setTeamId(int teamId);

	public abstract int getTeamId();


}