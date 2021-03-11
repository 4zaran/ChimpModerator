import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Bot extends ListenerAdapter {
    private static String _token = "ODE5NTUwMTU3MTM3NTEwNDIx.YEoPjw.UZhG6THDOqYdyWuZmGiqDcL3_a0";
    private MainWindow window;
    private static JDA jda;

    public static void main(String[] args){
        try{
            jda = JDABuilder.createDefault(_token)
                    .addEventListeners(new Bot())
                    .build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException le) {
            //System.out.println(le.getMessage());
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("API ready");
        window = new MainWindow();

        window.sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageToSend = window.messageField.getText();
                window.messageField.setText("");
                TextChannel textChannel = jda.getTextChannelsByName("general",true).get(0);
                textChannel.sendMessage(messageToSend).queue();
            }
        });

        window.messageField.addKeyListener( new KeyListener() {
            @Override
            public void keyPressed( KeyEvent evt ) {
                if(evt.getKeyCode() == evt.VK_ENTER){
                    String messageToSend = window.messageField.getText();
                    window.messageField.setText("");
                    TextChannel textChannel = jda.getTextChannelsByName("general",true).get(0);
                    textChannel.sendMessage(messageToSend).queue();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased( KeyEvent evt ){}
        } );
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        //System.out.println(event.getMessage().getContentRaw());
        String currentText = window.logField.getText();
        currentText += (event.getMember().getEffectiveName() + ": " + event.getMessage().getContentRaw() + '\n');

        window.logField.setText(currentText);
    }
}
