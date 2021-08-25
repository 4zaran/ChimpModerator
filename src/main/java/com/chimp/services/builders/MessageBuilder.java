package com.chimp.services.builders;

/**
 * Interface used for building a messages. It can be used to display messages in various ways.
 * For example, listing everything in a message as a string for use in simple log.
 * Builder design pattern.
 */
public interface MessageBuilder {
    void addAuthor();
    void addRawText();
    void addEmbed();
    void addAttachments();
    String getResult();
}
