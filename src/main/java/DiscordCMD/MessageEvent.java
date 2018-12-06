package DiscordCMD;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;

/**
 * A little bolt-on to JDA to be able to pass MessageReceived and MessageUpdate events to the same methods.
 *
 * (because the only common superclass is too general, and I don't want to double every method).
 */
public class MessageEvent {
    private Message message;
    private User author;
    private Member member;

    MessageEvent (MessageReceivedEvent mre) {
        this.message = mre.getMessage();
        this.author = mre.getAuthor();
        this.member = mre.getMember();
    }

    MessageEvent (MessageUpdateEvent mue) {
        this.message = mue.getMessage();
        this.author = mue.getAuthor();
        this.member = mue.getMember();
    }

    public Message getMessage () {
        return this.message;
    }

    public User getAuthor () {
        return this.author;
    }

    /**
     * Will return null if this is a private message, i.e. isPrivateMessage returns true.
     * @return the Member associated with the message
     */
    public Member getMember () {
        return this.member;
    }

    public boolean isPrivateMessage() {
        return this.member == null;
    }
}
