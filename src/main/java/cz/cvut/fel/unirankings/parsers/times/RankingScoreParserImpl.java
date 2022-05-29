package cz.cvut.fel.unirankings.parsers.times;

import cz.cvut.fel.unirankings.parsers.times.model.UniversityRankingScore;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RankingScoreParserImpl implements RankingScoreParser {

  public List<UniversityRankingScore> parse(Document document) {
    if (document == null) {
      throw new IllegalArgumentException("The provided document is null.");
    }
    return parseRankingScores(document);
  }

  private List<UniversityRankingScore> parseRankingScores(Document document) {
    Element scoreTableElement = document.selectFirst("#datatable-1 tbody");

    if (scoreTableElement != null) {
      Elements rowElements = scoreTableElement.selectXpath("//tr[@role='row']");
      return IntStream.range(0, rowElements.size())
          .mapToObj(i -> parseRowElement(rowElements.get(i), i))
          .collect(Collectors.toList());
    } else {
      return new ArrayList<>();
    }
  }

  private UniversityRankingScore parseRowElement(Element rowElement, int originalRowElementIdx) {
    String rank = getRank(rowElement);
    String name = getName(rowElement);
    String detailLinkPath = getDetailLinkPath(rowElement);
    String country = getCountry(rowElement);
    String overallScore = getOverallScore(rowElement);
    String teachingScore = getTeachingScore(rowElement);
    String researchScore = getResearchScore(rowElement);
    String citationsScore = getCitationsScore(rowElement);
    String industryIncomeScore = getIndustryIncomeScore(rowElement);
    String internationalOutlookScore = getInternationalOutlookScore(rowElement);

    return new UniversityRankingScore(
        originalRowElementIdx,
        rank,
        name,
        detailLinkPath,
        country,
        overallScore,
        teachingScore,
        researchScore,
        citationsScore,
        industryIncomeScore,
        internationalOutlookScore);
  }

  /**
   * Wraps {@link Element#text()} with {@code null} check.
   *
   * @param tag tag to get text
   * @return the value of {@link Element#text()} if the element is not null
   */
  private String getString(Element tag) {
    if (tag != null) {
      return tag.text();
    }
    return null;
  }

  private String getRank(Element rowElement) {
    Element rankElement = rowElement.selectFirst(".rank");
    return getString(rankElement);
  }

  private String getName(Element rowElement) {
    Element nameElement = rowElement.selectFirst(".ranking-institution-title");
    return getString(nameElement);
  }

  private String getDetailLinkPath(Element rowElement) {
    Element detailLinkPathElement = rowElement.selectFirst(".ranking-institution-title");
    if (detailLinkPathElement != null) {
      return detailLinkPathElement.attr("href");
    }
    return null;
  }

  private String getCountry(Element rowElement) {
    Element countryElement = rowElement.selectFirst(".location");
    return getString(countryElement);
  }

  private String getOverallScore(Element rowElement) {
    Element overallScoreElement = rowElement.selectFirst(".overall-score");
    return getString(overallScoreElement);
  }

  private String getTeachingScore(Element rowElement) {
    Element teachingScoreElement = rowElement.selectFirst(".teaching-score");
    return getString(teachingScoreElement);
  }

  private String getResearchScore(Element rowElement) {
    Element researchScoreElement = rowElement.selectFirst(".research-score");
    return getString(researchScoreElement);
  }

  private String getCitationsScore(Element rowElement) {
    Element citationsScoreElement = rowElement.selectFirst(".citations-score");
    return getString(citationsScoreElement);
  }

  private String getIndustryIncomeScore(Element rowElement) {
    Element industryIncomeScoreElement = rowElement.selectFirst(".industry_income-score");
    return getString(industryIncomeScoreElement);
  }

  private String getInternationalOutlookScore(Element rowElement) {
    Element internationalOutlookScoreElement =
        rowElement.selectFirst(".international_outlook-score");
    return getString(internationalOutlookScoreElement);
  }
}
