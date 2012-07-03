package cl.automind.gameframework.ranking;

public enum RankingType {
	GENERAL("ranking_general", new String[]{"countGeneral", "countNotNullGeneral", "getTeamGeneral", "rankingAll", "searchAllTeams", "updateRankingGeneral"}), 
	COLEGIO ("ranking_colegio", new String[]{"countColegio", "countNotNullColegio", "getTeamColegio", "rankingColegio", "searchColegioTeams", "updateRankingColegio"}), 
	COMUNA ("ranking_comuna", new String[]{"countComuna", "countNotNullComuna", "getTeamComuna", "rankingComuna", "searchComunaTeams", "updateRankingComuna"}), 
	NIVEL ("ranking_nivel", new String[]{"countNivel", "countNotNullNivel", "getTeamNivel", "rankingNivel", "searchNivelTeams", "updateRankingNivel"}), 
	PAIS ("ranking_pais", new String[]{"countPais", "countNotNullPais", "getTeamPais", "rankingPais", "searchPaisTeams", "updateRankingPais"}), 
	EQUIPO ("ranking_equipo", new String[]{"", "countNotNullEquipo", "getTeamEquipo", "", "searchPlayers", "updatePlayer"});
	RankingType(String name, String[] queries){
		this(new Descriptor(name, queries));
	}
	RankingType(Descriptor descriptor){
		this.descriptor = descriptor;
	}
	private Descriptor descriptor;
	public Descriptor getDescriptor(){
		return descriptor;
	}
	public final static class Descriptor {
		private String name;
		private String[] queries;
		public Descriptor(String name, String[] queries){
			this.name = name;
			this.queries = queries;
		}
		public String getName(){
			return name;
		}
		public String count(){
			return queries[0];
		}
		public String countNotNull(){
			return queries[1];
		}
		public String get(){
			return queries[2];
		}
		public String ranking(){
			return queries[3];
		}
		public String search(){
			return queries[4];
		}
		public String update(){
			return queries[5];
		}
	}
};
