package ua.pomodoro;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.pomodoro.bot.PomodoroBot;

public class Start {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new PomodoroBot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
