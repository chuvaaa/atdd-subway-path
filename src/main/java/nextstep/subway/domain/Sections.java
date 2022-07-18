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
	@OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
	@OrderBy("id asc")
	private final List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	public Sections(Line line, int distance, Station upStation, Station downStation) {
		Section section = new Section(distance, upStation, downStation);
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

	public void add(Section addSection) {
		this.sections.add(addSection);
	}

}
