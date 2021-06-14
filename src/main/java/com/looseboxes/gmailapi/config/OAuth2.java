package com.looseboxes.gmailapi.config;

import java.util.List;

public class OAuth2 {

    /**
     * [online|offline]
     * offline mode will lead to a long lived refresh token
     */
    private String accessType = "offline";

    /**
     * [auto|force|consent]
     * auto -> request auto approval
     * force -> force the approval UI to show, null -> default behaviour
     * consent -> documented here https://developers.google.com/identity/protocols/oauth2/web-server
     */
    private String approvalPrompt;

    private List<String> accessTokenScopes;

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public OAuth2 accessType(String accessType) {
        this.accessType = accessType;
        return this;
    }

    public String getApprovalPrompt() {
        return approvalPrompt;
    }

    public void setApprovalPrompt(String approvalPrompt) {
        this.approvalPrompt = approvalPrompt;
    }

    public OAuth2 approvalPrompt(String approvalPrompt) {
        this.approvalPrompt = approvalPrompt;
        return this;
    }

    public List<String> getAccessTokenScopes() {
        return accessTokenScopes;
    }

    public void setAccessTokenScopes(List<String> accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
    }

    public OAuth2 accessTokenScopes(List<String> accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
        return this;
    }

    @Override
    public String toString() {
        return "OAuth2{" +
                "accessType='" + accessType + '\'' +
                ", approvalPrompt='" + approvalPrompt + '\'' +
                ", accessTokenScopes=" + accessTokenScopes +
                '}';
    }
}
