package nextstep.subway.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nextstep.subway.exception.BadRequestException;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        nameAndColorValidation(name, color);
        this.name = name;
        this.color = color;
        this.sections = new Sections(this, distance, upStation, downStation);
    }

    public List<Station> getStations(){
        return sections.getStation();
    }

    public List<Section> getSectionList(){
        return sections.getSections();
    }

    public void nameAndColorValidation(String name, String color){
        if(!StringUtils.hasText(name)){
            throw new BadRequestException("name을 입력하여 주십시오.");
        }
        if(!StringUtils.hasText(color)){
            throw new BadRequestException("color을 입력하여 주십시오.");
        }
    }

}
