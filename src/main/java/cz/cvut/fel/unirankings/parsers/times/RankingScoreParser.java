package cz.cvut.fel.unirankings.parsers.times;

import cz.cvut.fel.unirankings.parsers.times.model.UniversityRankingScore;
import org.jsoup.nodes.Document;

import java.util.List;

public interface RankingScoreParser {

  List<UniversityRankingScore> parse(Document document);
}
