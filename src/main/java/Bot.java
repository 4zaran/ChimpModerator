import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.channels.Channel;
import java.util.Locale;
import java.util.Objects;

class JDAService {
    private JDA jda;
    private final JDABuilder jdaBuild;

    public JDAService(){
        String TOKEN = "ODE5NTUwMTU3MTM3NTEwNDIx.YEoPjw.UZhG6THDOqYdyWuZmGiqDcL3_a0";
        jdaBuild = JDABuilder.createDefault(TOKEN)
                .addEventListeners(new Bot());
    }
    
    public void connect(){
        try {
            jda = jdaBuild.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException le) {
            System.out.println(le.getMessage());
        }
    }

    public void disconnect(){
        jda.shutdownNow();
        System.exit(0);
    }
    
    public JDA getJda(){
        return jda;
    }

    public TextChannel getTextChannel(String name){
        return jda.getTextChannelsByName(name, true).get(0);
    }
}

public class Bot extends ListenerAdapter {
    private static JDAService jda;
    private static MainWindow window;

    public static void main(String[] args) {
        window = new MainWindow();
        jda = new JDAService();
        jda.connect();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        window.printText("Connected!");

        window.sendButton.addActionListener(e -> sendMessage());

        window.messageTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        Member member = event.getMember();
        String messageContent = msg.getContentRaw();
        MessageChannel channel = event.getChannel();

        String[] messageContentParams = messageContent.split("\\s+");

        window.printText(event.getMember().getEffectiveName() + ": " + messageContent);

        if (messageContent.toLowerCase().startsWith("/kick")){
            if (msg.getMentionedMembers().isEmpty()){
                return;
            }
            if(messageContentParams.length < 3) {
                channel.sendMessage("Missing reason!").queue();
                return;
            }

            StringBuilder reason = new StringBuilder();
            for(int i = 2; i < messageContentParams.length; i++){
                reason.append(messageContentParams[i]).append(" ");
            }
            channel.sendMessage("Reason: " + reason).queue();

            Member target = msg.getMentionedMembers().get(0);
            if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
                channel.sendMessage("You need permission to kick!").queue();
                return;
            }

            final Member selfMember = event.getGuild().getSelfMember();

            if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
                event.getChannel().sendMessage("I don't have permission to kick!").queue();
                return;
            }


            event.getGuild()
                    .kick(target, reason.toString())
                    .reason(reason.toString())
                    .queue(
                            (__) -> event.getChannel().sendMessage("Kick was successful").queue(),
                            (error) -> event.getChannel().sendMessageFormat("Could not kick %s", error.getMessage()).queue()
                    );
        }
        if (messageContent.toLowerCase().startsWith("/ban")){
            if (msg.getMentionedMembers().isEmpty()){
                return;
            }
            if(messageContentParams.length < 3) {
                channel.sendMessage("Missing reason!").queue();
                return;
            }

            StringBuilder reason = new StringBuilder();
            for(int i = 2; i < messageContentParams.length; i++){
                reason.append(messageContentParams[i]).append(" ");
            }

            Member target = msg.getMentionedMembers().get(0);
            if (!member.canInteract(target) || !member.hasPermission(Permission.BAN_MEMBERS)) {
                channel.sendMessage("You need permission to ban members!").queue();
                return;
            }

            final Member selfMember = event.getGuild().getSelfMember();

            if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                event.getChannel().sendMessage("I don't have permission to ban members!").queue();
                return;
            }


            event.getGuild()
                    .ban(target,0, reason.toString())
                    .reason(reason.toString())
                    .queue(
                            (__) -> event.getChannel().sendMessage("Ban was successful").queue(),
                            (error) -> event.getChannel().sendMessageFormat("Could not ban %s", error.getMessage()).queue()
                    );
        }
    }

    private void sendMessage() {
        String messageToSend = window.messageTextField.getText();
        if(messageToSend.equals("/exit")){
            jda.disconnect();
        }
        window.messageTextField.setText("");
        TextChannel textChannel = jda.getTextChannel("general");
        if (!Objects.equals(messageToSend, ""))
            textChannel.sendMessage(messageToSend).queue();
    }
}