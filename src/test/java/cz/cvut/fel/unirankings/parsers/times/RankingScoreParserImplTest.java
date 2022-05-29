package cz.cvut.fel.unirankings.parsers.times;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvException;
import cz.cvut.fel.unirankings.parsers.times.model.UniversityRankingScore;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

class RankingScoreParserImplTest {

  private static final String RESOURCE_PATH = "src/test/resources/times/parser";

  private RankingScoreParser parser;

  @BeforeEach
  public void setup() {
    parser = new RankingScoreParserImpl();
  }

  @Test
  public void test_standard_execution() throws Exception {
    String doc = getHtmlFromResources("TestUniversityRankings.html");
    List<UniversityRankingScore> expectedUniversities =
        getExpectedUniversities("TestUniversityRankings_expected.txt");

    List<UniversityRankingScore> actualUniversities = parser.parse(Jsoup.parse(doc));
    assertUniversitiesEqual(expectedUniversities, actualUniversities);
  }

  @Test
  public void test_document_without_table() throws Exception {
    String doc = getHtmlFromResources("TestNoTable.html");
    List<UniversityRankingScore> expectedUniversities =
        getExpectedUniversities("TestNoTable_expected.txt");

    List<UniversityRankingScore> actualUniversities = parser.parse(Jsoup.parse(doc));
    assertUniversitiesEqual(expectedUniversities, actualUniversities);
  }

  @Test
  public void test_null_document_throws_exception() {
    IllegalArgumentException ex =
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse(null));
    Assertions.assertEquals("The provided document is null.", ex.getMessage());
  }

  @Test
  public void test_table_with_rankings_is_empty() throws Exception {
    String doc = getHtmlFromResources("TestEmptyTable.html");
    List<UniversityRankingScore> expectedUniversities =
        getExpectedUniversities("TestEmptyTable_expected.txt");

    List<UniversityRankingScore> actualUniversities = parser.parse(Jsoup.parse(doc));
    assertUniversitiesEqual(expectedUniversities, actualUniversities);
  }

  @Test
  public void test_incomplete_data_in_table() throws Exception {
    String doc = getHtmlFromResources("TestIncompleteData.html");
    List<UniversityRankingScore> expectedUniversities =
        getExpectedUniversities("TestIncompleteData_expected.txt");

    List<UniversityRankingScore> actualUniversities = parser.parse(Jsoup.parse(doc));
    assertUniversitiesEqual(expectedUniversities, actualUniversities);
  }

  @Test
  public void test_missing_tag_in_table() throws Exception {
    String doc = getHtmlFromResources("TestMissingTagInTable.html");
    List<UniversityRankingScore> expectedUniversities =
        getExpectedUniversities("TestMissingTagInTable_expected.txt");

    List<UniversityRankingScore> actualUniversities = parser.parse(Jsoup.parse(doc));
    assertUniversitiesEqual(expectedUniversities, actualUniversities);
  }

  private void assertUniversitiesEqual(
      List<UniversityRankingScore> expected, List<UniversityRankingScore> actual) {
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(expected.size(), actual.size());

    for (int i = 0; i < expected.size(); i++) {
      Assertions.assertEquals(expected.get(i), actual.get(i));
    }
  }

  private String getHtmlFromResources(String fileName) throws IOException {
    File file = new File(RESOURCE_PATH + "/" + fileName);
    return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
  }

  private List<UniversityRankingScore> getExpectedUniversities(String fileName)
      throws IOException, CsvException {
    File file = new File(RESOURCE_PATH + "/" + fileName);

    CSVParser csvParser =
        new CSVParserBuilder()
            .withSeparator(';')
            .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
            .build();
    try (CSVReader reader =
        new CSVReaderBuilder(new FileReader(file.getAbsolutePath()))
            .withCSVParser(csvParser)
            .build()) {
      return reader.readAll().stream()
          .map(
              line ->
                  new UniversityRankingScore(
                      Integer.parseInt(line[0]),
                      line[1],
                      line[2],
                      line[3],
                      line[4],
                      line[5],
                      line[6],
                      line[7],
                      line[8],
                      line[9],
                      line[10]))
          .collect(Collectors.toList());
    }
  }
}
