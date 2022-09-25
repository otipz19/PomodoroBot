package ua.pomodoro.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PomodoroBot extends TelegramLongPollingBot {
    enum TimerType{
        WORK,
        BREAK
    }

    private ConcurrentHashMap<Long, PomodoroUser> users = new ConcurrentHashMap<>();

    public static PomodoroBot S;

    public PomodoroBot(){
        S = this;
    }

    @Override
    public String getBotUsername() {
        return "PomodorikBot";
    }

    @Override
    public String getBotToken() {
        return "5331052717:AAGVZwFlff8Ui1mBwAcjCLf2JvgYHix3ssM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            var chatId = update.getMessage().getChatId();
            if(update.getMessage().getText().equals("/start")){
                //Create new user's object
                if(!users.containsKey(chatId))
                    users.put(chatId, new PomodoroUser(chatId));

                sendMessage(chatId.toString(), """
                        Привіт! Я буду керувати твоїм часом роботи та відпочинку.
                        Введи час у хвилинах у форматі 10 5, де
                        10 - час роботи,
                        5 - час відпочинку.
                        """);
            }
            else{
                String[] args = update.getMessage().getText().split(" ");
                if(args.length == 2){
                    //Set user's timer
                    users.get(chatId).SetTimer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 1);
                }
                else{
                    sendMessage(chatId.toString(), "Не зрозумів повідомлення");
                }
            }
        }
    }

    public void sendMessage(String chatId, String text) {
        SendMessage msg = new SendMessage(chatId, text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public  void checkTimer() throws InterruptedException {
        while (true){
            for (Map.Entry<Long, PomodoroUser> user: users.entrySet()) {
                if(user.getValue().isTimerRunning && user.getValue().endTime.compareTo(Instant.now()) < 0){
                    user.getValue().TimerEnd();
                }
            }
            Thread.sleep(1000l);
        }
    }
}
