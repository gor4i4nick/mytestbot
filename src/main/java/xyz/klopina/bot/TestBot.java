package xyz.klopina.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TestBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "@klopinabot";
    }

    @Override
    public String getBotToken() {
        return "1363999060:AAETuTK_i_xonIfZnlviFh-dTAMuGYcI4Ak";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("You sent: \n\n" + message.getText())
                        .build());
            }
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        TestBot bot = new TestBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);

    }
}
