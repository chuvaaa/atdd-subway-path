package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotExistElementException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author a1101466 on 2022/07/13
 * @project subway
 * @description
 */
@Embeddable
@Getter
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("id asc")
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        List<Section> sectionsSortList = new ArrayList<>();
        if(sections.isEmpty()){
            return sectionsSortList;
        }
        Section firstSection = getFirstSection();
        sectionsSortList.add(firstSection);
        for(int i = 0; i < sections.size(); i++ ){
            Section nextSection = getNextSection(sectionsSortList.get(i));
            if(Objects.isNull(nextSection)){
                break;
            }
            sectionsSortList.add(nextSection);
        }
        return sectionsSortList;
    }

    public void add(Section newSection) {
        if(!sections.isEmpty()){
            validateAddSection(newSection);
            Section matchedSection = getBetweenSection(newSection);
            if (!Objects.isNull(matchedSection)){
                addBetweenSectionValidateAndModify(newSection, matchedSection);
            }
        }
        this.sections.add(newSection);
    }


    public Section getBetweenSection(Section newSection){
        return sections.stream().filter(
                section -> newSection.getUpStation().equals(section.getUpStation())
                        || newSection.getDownStation().equals(section.getDownStation())
        ).findFirst().orElse(null);
    }

    public void addBetweenSectionValidateAndModify(Section newSection, Section matchedSection){
//        역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음
        if( newSection.getDistance() >= matchedSection.getDistance()){
            throw new BadRequestException("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음");
        }
        matchedSection.betweenAddModifyStation(newSection);
    }

    public void validateAddSection(Section newSection){
        if(checkExistSection(newSection)){
            throw new BadRequestException("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음");
        }
        if( checkNotExistStation(newSection.getUpStation()) && checkNotExistStation(newSection.getDownStation())){
            throw new BadRequestException("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음");
        }
    }

    public Boolean checkNotExistStation(Station station){
        return getStations()
                .stream()
                .noneMatch(currentStation -> currentStation.equals(station));
    }

    public Boolean checkExistSection(Section section){
        return getSections()
                .stream()
                .anyMatch(currentSection -> currentSection.getUpStation().equals(section.getUpStation())
                            && currentSection.getDownStation().equals(section.getDownStation())
                );
    }


    public List<Station> getStations(){
        return getSections().stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void removeSection(Long stationId) {
        if(sections.size() == 1)
            throw new BadRequestException("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.");

        if(removeSectionWhenFirst(stationId)){
            return;
        }
        if(removeSectionWhenLast(stationId)){
            return;
        }

        removeSectionWhenBetWeen(stationId);
    }


    public Boolean removeSectionWhenFirst(Long stationId){
        Section firstSection = getFirstSection();
        if(Objects.equals(firstSection.getUpStation().getId(), stationId)){
            sections.remove(firstSection);
            return true;
        }
        return false;
    }

    public Boolean removeSectionWhenLast(Long stationId){
        Section lastSection = getLastSection();
        if(Objects.equals(lastSection.getDownStation().getId(), stationId)){
            sections.remove(lastSection);
            return true;
        }
        return false;
    }

    public void removeSectionWhenBetWeen(Long stationId){
        Section prevSection = getSectionByDownStationId(stationId);
        Section nextSection = getSectionByUpStationId(stationId);

        prevSection.plusDistance(nextSection.getDistance());
        prevSection.replaceDownStation(nextSection.getDownStation());

        sections.remove(nextSection);
    }

    public Section getLastSection(){
        if(sections.isEmpty()){
            throw new NotExistElementException("저장 된 section정보가 없습니다.");
        }
        return sections.stream()
                .filter(this::isLastSection)
                .findFirst()
                .orElseThrow(() -> new NotExistElementException("구간 정보를 찾을 수 없습니다."));

    }


    public Section getSectionById(Long sectionId){
        return sections.stream()
                .filter(section -> sectionId.equals(section.getId()))
                .findFirst()
                .orElseThrow(() -> new NotExistElementException("구간 정보를 찾을 수 없습니다."));

    }

    public Section getFirstSection(){
        if(sections.isEmpty()){
            throw new NotExistElementException("저장 된 section정보가 없습니다.");
        }
        return sections.stream()
                .filter(this::isFirstSection)
                .findFirst()
                .orElseThrow(() -> new NotExistElementException("구간 정보를 찾을 수 없습니다."));

    }

    public Section getNextSection(Section section){

        return sections.stream()
                .filter(thisSection -> section.getDownStation().equals(thisSection.getUpStation()))
                .findFirst()
                .orElse(null);

    }
    public Boolean isFirstSection(Section section){
        return sections.stream()
                .noneMatch(currentSection -> section.getUpStation().equals(currentSection.getDownStation()));
    }

    public Boolean isLastSection(Section section){
        return sections.stream()
                .noneMatch(currentSection -> section.getDownStation().getId().equals(currentSection.getUpStation().getId()));
    }

    public Section getSectionByUpStationId(Long stationId){
        return sections.stream()
                .filter(section -> stationId.equals(section.getUpStation().getId()))
                .findFirst()
                .orElseThrow(() -> new NotExistElementException("노선에 존재하지 않는 지하철역 입니다."));
    }

    public Section getSectionByDownStationId(Long stationId){
        return sections.stream()
                .filter(section -> stationId.equals(section.getDownStation().getId()))
                .findFirst()
                .orElseThrow(() -> new NotExistElementException("노선에 존재하지 않는 지하철역 입니다."));
    }


}