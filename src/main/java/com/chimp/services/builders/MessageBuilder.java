package com.chimp.services.builders;

public interface MessageBuilder {
    void addAuthor();
    void addRawText();
    void addEmbed();
    void addAttachments();
    String getResult();
}
