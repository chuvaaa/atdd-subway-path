package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    final Long 일호선ID    = 1L;
    final String 일호선이름 = "일호선";
    final String 라인색    = "blue";

    final int DEFAULT_DISTANCE = 10;

    Line 일호선 = new Line(일호선ID, 일호선이름, 라인색  );
    Station 개봉역_상행 = new Station(1L, "개봉역");
    Station 구일역_하행 = new Station(2L, "구일역");
    @Test
    @DisplayName("구간을 생성한다.")
    void addSection() {
        //given

        //when
        일호선.addSection(개봉역_상행, 구일역_하행, DEFAULT_DISTANCE);

        //then
        assertThat(일호선.getSections()).filteredOn(
                section -> section.equals(new Section(일호선, 개봉역_상행, 구일역_하행, DEFAULT_DISTANCE))
        );

    }

    @Test
    @DisplayName("구간목록을 조회한다.")
    void getStations() {
        //given
        일호선.addSection(개봉역_상행, 구일역_하행, DEFAULT_DISTANCE);

        //when
        List<Station> stations = 일호선.getStations();

        //then
        assertAll(
                () -> assertThat(stations).filteredOn(
                        station -> station.toString().contains(개봉역_상행.toString())
                ),
                () -> assertThat(stations).filteredOn(
                        station -> station.toString().contains(구일역_하행.toString())
                )
        );


    }

    @Test
    @DisplayName("구간을 제거한다.")
    void removeSection() {
//        Section section = new Section(일호선,개봉역_상행, 구일역_하행, DEFAULT_DISTANCE);
        Station 구로역 = new Station(3L, "구로역");
        //given
        일호선.addSection(1L, 개봉역_상행, 구일역_하행, DEFAULT_DISTANCE);
        일호선.addSection(2L, 구일역_하행, 구로역, DEFAULT_DISTANCE);

        Section 삭제대상구간 = 일호선.getSectionById(2L);
        //when
        일호선.removeSection(구로역.getId());

        //then
        assertThat(일호선.getSections()).hasSize(1);

    }

    //해당 테스트 기능 개선으로 인한 제거.
    @DisplayName("지하철 구간 삭제 실패")
    @Test
    void removeSectionFail() {
    }

}
