package cl.automind.gameframework.ranking;

public interface IRankingContext {

	public abstract void addTeam(int teamId, String username, String colegio,
			String comuna, int nivel, String pais, int tiempo, int puntaje);

	public abstract void updateTeamRanking();

	/**
	 * Actualiza las columnas de " + RankingType.GENERAL.getName() + ",
	 * " + RankingType.COLEGIO.getName() + ", etc.
	 */
	public abstract void updateRanking();

	public abstract void setMode(RankingType mode);

	public abstract int getPositionAtRanking(RankingType key);

	public abstract int playersAtRanking(RankingType key);

	public abstract String getColegio();

	public abstract String getComuna();

	public abstract String getPais();

}