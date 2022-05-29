package cz.cvut.fel.unirankings.parsers;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import cz.cvut.fel.unirankings.parsers.times.RankingScoreParser;
import cz.cvut.fel.unirankings.parsers.times.RankingScoreParserImpl;
import cz.cvut.fel.unirankings.parsers.times.model.UniversityRankingScore;
import cz.cvut.fel.unirankings.parsers.times.model.UniversityRankingScoreList;
import org.jsoup.Jsoup;

import java.util.List;

public class TheParserFunction {

  @FunctionName("TheParser")
  public HttpResponseMessage run(
      @HttpTrigger(
              name = "req",
              methods = {HttpMethod.POST},
              authLevel = AuthorizationLevel.ANONYMOUS)
          HttpRequestMessage<String> request,
      final ExecutionContext context) {
    final String pageSource = request.getBody();

    if (pageSource == null || pageSource.isEmpty()) {
      return request
          .createResponseBuilder(HttpStatus.BAD_REQUEST)
          .body("Please pass the page source in the request body")
          .build();
    } else {
      RankingScoreParser parser = new RankingScoreParserImpl();
      List<UniversityRankingScore> universityRankingScores = parser.parse(Jsoup.parse(pageSource));
      return request
          .createResponseBuilder(HttpStatus.OK)
          .header("Content-Type", "application/json")
          .body(new UniversityRankingScoreList(universityRankingScores))
          .build();
    }
  }
}
