package com.chimp.services.moderation;

/**
 * Possible states of {@link BadUser}. When user violates, some kind of punishment is executed.
 * If this user received specified number of one punishment,
 * his behavioural state is changing and next punishment will be considered.
 * For example, {@code warnAmount = 1, kickAmount = 0} means that user will receive one warning
 * and with the next punishment he will be banned.
 */
public enum BehaviourState {
    CIVIL,WARNED,KICKED,BANNED
}
