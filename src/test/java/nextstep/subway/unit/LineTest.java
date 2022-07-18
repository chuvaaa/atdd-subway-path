package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

class LineTest {
    @Test
    void addSection() {
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
