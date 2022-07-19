package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line",
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			orphanRemoval = true)
	@OrderBy("id asc")
	private final List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	public Sections(Line line, int distance, Station upStation, Station downStation) {
		Section section = new Section(upStation, downStation, distance);
		section.setLine(line);
		sections.add(section);
	}

	public List<Station> getStation() {
		return sections.stream()
				.map(Section::getStations)
				.flatMap(List::stream)
				.distinct()
				.collect(Collectors.toList());
	}

	public void addSection(Line line, Section section) {
		sections.add(section);
		section.setLine(line);
	}

	public void add(Section addSection) {
		this.sections.add(addSection);
	}

}
