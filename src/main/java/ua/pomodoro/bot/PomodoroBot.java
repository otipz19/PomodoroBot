package ua.pomodoro.bot;

import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ConcurrentHashMap;

public class PomodoroBot extends TelegramLongPollingBot {
    enum TimerType{
        WORK,
        BREAK
    }

    record Timer(Instant timer, TimerType timerType){}

    private static ConcurrentHashMap<Timer, Long> timers = new ConcurrentHashMap<>();

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
                    //Get current time and add minutes that was input by user
                    var workTime = Instant.now().plus(Long.parseLong(args[0]), ChronoUnit.MINUTES);
                    timers.put(new Timer(workTime, TimerType.WORK), chatId);
                    var breakTime = Instant.now().plus(Long.parseLong(args[1]), ChronoUnit.MINUTES);
                    timers.put(new Timer(breakTime, TimerType.BREAK), chatId);
                }
                else{
                    sendMessage(chatId.toString(), "Не зрозумів повідомлення");
                }
            }
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage msg = new SendMessage(chatId, text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public  void checkTimer() throws InterruptedException {
        while (true){
            
            Thread.sleep(1000l);
        }
    }
}
