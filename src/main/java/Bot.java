import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        window.printText(event.getMember().getEffectiveName() + ": " + event.getMessage().getContentRaw());
    }

    private void sendMessage() {
        String messageToSend = window.messageTextField.getText();
        if(messageToSend.equals("exit")){
            jda.disconnect();
        }
        window.messageTextField.setText("");
        TextChannel textChannel = jda.getTextChannel("general");
        if (!Objects.equals(messageToSend, ""))
            textChannel.sendMessage(messageToSend).queue();
    }
}