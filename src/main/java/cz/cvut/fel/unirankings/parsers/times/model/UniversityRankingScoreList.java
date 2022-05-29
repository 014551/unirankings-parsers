package cz.cvut.fel.unirankings.parsers.times.model;

import java.util.List;

public class UniversityRankingScoreList {

  private List<UniversityRankingScore> universityRankingScores;

  public UniversityRankingScoreList(List<UniversityRankingScore> universityRankingScores) {
    this.universityRankingScores = universityRankingScores;
  }

  public List<UniversityRankingScore> getUniversityRankingScores() {
    return universityRankingScores;
  }

  public void setUniversityRankingScores(List<UniversityRankingScore> universityRankingScores) {
    this.universityRankingScores = universityRankingScores;
  }
}
