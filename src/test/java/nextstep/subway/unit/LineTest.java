package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    void addSection() {
        //given
        Station upStation = new Station(1L, "강남역");
        Station downStation = new Station(2L, "역삼역");

        Line line = new Line(1L, "1호선", "blue");

        line.addSection(new Section(upStation, downStation, 10));

        //when
        line.getSectionList().forEach(
                section -> System.out.println(section.toString())
        );



    }

    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        //given
        Station upStation = new Station(1L,"강남역");
        Station downStation = new Station(2L, "역삼역");

        Line line = new Line("1호선", "파랑", 10, upStation, downStation);

        //when
        List<Station> stations = line.getStations();

        //then
        assertThat(stations).hasSize(2);

    }

    @Test
    void removeSection() {
    }
}
