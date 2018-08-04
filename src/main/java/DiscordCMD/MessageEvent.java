package DiscordCMD;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;

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

    public Member getMember () {
        return this.member;
    }

    public boolean isPrivateMessage() {
        return this.member == null;
    }
}
