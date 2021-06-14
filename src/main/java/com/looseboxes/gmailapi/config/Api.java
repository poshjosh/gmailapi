package com.looseboxes.gmailapi.config;

public class Api {

    private String tokensDirectoryPath;
    private String credentialsFilePath;

    public String getTokensDirectoryPath() {
        return tokensDirectoryPath;
    }

    public void setTokensDirectoryPath(String tokensDirectoryPath) {
        this.tokensDirectoryPath = tokensDirectoryPath;
    }

    public Api tokensDirectoryPath(String tokensDirectoryPath) {
        this.tokensDirectoryPath = tokensDirectoryPath;
        return this;
    }

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public Api credentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
        return this;
    }

    @Override
    public String toString() {
        return "Api{" +
                "tokensDirectoryPath='" + tokensDirectoryPath + '\'' +
                ", credentialsFilePath='" + credentialsFilePath + '\'' +
                '}';
    }
}
