package com.university.dms.Utils;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.ProposalMarking;

public class ProjectUtils {

    public static boolean isProposalNotGraded(ProposalMarking proposalMarking) {
        return proposalMarking.getIntroductionMark() == null || proposalMarking.getAimsObjectivesMark() == null
                || proposalMarking.getMethodologyMark() == null || proposalMarking.getProjectPlanMark() == null
                || proposalMarking.getSummaryConclusionMark() == null || proposalMarking.getPresentationAppendicesMark() == null;
    }

    public static boolean hasAnyProposalBeenSubmitted(Project project){
        return project.getProposal() != null;
    }

}
