package nextstep.subway.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@ToString
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public Section() {

    }

    public Section(Line line, Station upStation, Station downStation, Integer distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Integer distance) {
        if (upStation.equals(downStation)) {
            throw new BadRequestException("상행종점역과 하행종점역의 아이디는 같을 수 없습니다.");
        }
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

}