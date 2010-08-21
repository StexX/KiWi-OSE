package kiwi.api.reasoning;

import java.util.List;
import java.util.Set;

import kiwi.model.kbase.KiWiTriple;
import kiwi.service.reasoning.CompiledRule;
import kiwi.service.reasoning.ReasoningTaskStatistics;

public interface ProgramProcessor {

	public void setProgramLoader(ProgramLoader programLoader);

	public ProgramLoader getProgramLoader();

	public ReasoningTaskStatistics processRemovedTriples(Set<KiWiTriple> removedTriples);

	public ReasoningTaskStatistics processAddedTriples(Set<KiWiTriple> addedTriples);

	public ReasoningTaskStatistics processNaively(List<CompiledRule> compiledRules);

	public ReasoningTaskStatistics process(Set<KiWiTriple> addedTriples);

}
