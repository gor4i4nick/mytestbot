package xyz.klopina.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.CurrencyModeService;

import java.util.*;

public class TestBot extends TelegramLongPollingBot {
    private final CurrencyModeService currencyModeService = CurrencyModeService.getInstance();
private String getCurrencyButton(Currency saved, Currency current){
    return saved == current ? current + "✔" : current.name();
}
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
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntities =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntities.isPresent()) {
                String command = message.getText().substring(commandEntities.get().getOffset(),
                        commandEntities.get().getLength());
                switch (command) {
                    case "/set_currency":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        Currency originalCurrency = currencyModeService.getOriginalCurrency(message.getChatId());
                        Currency targetCurrency = currencyModeService.getOriginalCurrency(message.getChatId());

                        for (Currency currency : Currency.values()) {
                            buttons.add(
                                    Arrays.asList(
                                            InlineKeyboardButton.builder()
                                                    .text(getCurrencyButton(originalCurrency,currency))
                                                    .callbackData("ORIGINAL :" + currency)
                                                    .build(),
                                            InlineKeyboardButton.builder()
                                                    .text(getCurrencyButton(targetCurrency,currency))
                                                    .callbackData("TARGET :" + currency)
                                                    .build()));



                        }
                        execute(SendMessage.builder()
                                .text("Please choose Original and Target currencies")
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());
                        return;
                }
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
