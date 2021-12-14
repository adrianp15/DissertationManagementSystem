package com.university.dms.Utils;

import com.cloudmersive.client.ConvertDocumentApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.university.dms.model.discussions.Discussion;
import com.university.dms.model.discussions.DiscussionMessage;
import com.university.dms.model.forum.ForumPost;
import com.university.dms.model.forum.ForumTopic;
import com.university.dms.model.project.Project;
import com.university.dms.model.project.ProposalMarking;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProjectUtils {

    public static boolean isProposalNotGraded(ProposalMarking proposalMarking) {
        return proposalMarking.getIntroductionMark() == null || proposalMarking.getAimsObjectivesMark() == null
                || proposalMarking.getMethodologyMark() == null || proposalMarking.getProjectPlanMark() == null
                || proposalMarking.getSummaryConclusionMark() == null || proposalMarking.getPresentationAppendicesMark() == null;
    }

    public static boolean hasAnyProposalBeenSubmitted(Project project){
        return project.getProposal() != null;
    }

    public static DiscussionMessage getLatestMessage(Discussion discussion) {

        DiscussionMessage latestDiscussion = discussion.getMessages().get(discussion.getMessages().size()-1);

        return latestDiscussion;

    }

    public ForumPost getLatestForumPost(ForumTopic forumTopic){
        return forumTopic.getPosts().get(forumTopic.getPosts().size()-1);
    }

    public static byte[] convertWordToPDF(MultipartFile file) throws IOException {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setConnectTimeout(30000);
        defaultClient.setReadTimeout(30000);
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey("815982a1-05a5-426b-9638-ba085d169705");

        byte[] result = null;

        if (!file.isEmpty()) {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getName());
            file.transferTo(convFile);

            if (file.getOriginalFilename().contains("pdf")) {

                result = Files.readAllBytes(convFile.toPath());

            } else {

                ConvertDocumentApi apiInstance = new ConvertDocumentApi();
                try {

                    result = apiInstance.convertDocumentDocxToPdf(convFile);

                } catch (ApiException e) {
                    System.err.println("Exception when calling ConvertDocumentApi#convertDocumentDocxToPdf");
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

}
