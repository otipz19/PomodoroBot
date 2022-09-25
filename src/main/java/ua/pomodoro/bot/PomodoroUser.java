package ua.pomodoro.bot;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PomodoroUser {
    public Long chatId;
    public boolean isTimerRunning;
    public PomodoroBot.TimerType timerType;
    public Instant endTime;
    public int workTimeDuration;
    public int breakTimeDuration;
    public int repeatsLeft;
    public int multiplier;

    PomodoroUser(long chatId){
        this.chatId = chatId;
    }

    public void SetTimer(int workTimeDuration, int breakTimeDuration, int repeats){
        this.workTimeDuration = workTimeDuration;
        this.breakTimeDuration = breakTimeDuration;
        this.repeatsLeft = repeats;
        isTimerRunning = true;
        timerType = PomodoroBot.TimerType.WORK;
        //SHOULD BE CHANGED TO ChronoUnit.MINUTES lately
        endTime = Instant.now().plus(workTimeDuration, ChronoUnit.SECONDS);

        NotifyUser(true);
    }

    public void TimerEnd(){
        if(timerType == PomodoroBot.TimerType.BREAK){
            repeatsLeft--;
            if(repeatsLeft == 0){
                isTimerRunning = false;
                NotifyUser(false);
                return;
            }
            timerType = PomodoroBot.TimerType.WORK;
            //SHOULD BE CHANGED TO ChronoUnit.MINUTES lately
            endTime = Instant.now().plus(workTimeDuration, ChronoUnit.SECONDS);
        }
        else{
            timerType = PomodoroBot.TimerType.BREAK;
            //SHOULD BE CHANGED TO ChronoUnit.MINUTES lately
            endTime = Instant.now().plus(breakTimeDuration, ChronoUnit.SECONDS);
        }

        NotifyUser(false);
    }

    private void NotifyUser(boolean justStarted){
        String msg = new String();

        if(justStarted){
            msg = "Розпочато новий цикл роботи-відпочинку!\nТривалість періоду роботи: " + workTimeDuration + " хвилин.\nТривалість періоду відпочинку: " + breakTimeDuration + " хвилин.";
        }
        else if(!isTimerRunning){
            msg = "Вітаю! Помодоро завершено.\nВи можете задати новий цикл роботи-відпочинку використавши команду /set";
        }
        else if(timerType == PomodoroBot.TimerType.WORK){
            msg = "Пройшло " + breakTimeDuration + " хвилин. Час відпочинку завершено!\nНаступний період триватиме " + workTimeDuration + " хвилин.\nДо роботи!";
        }
        else{
            msg = "Пройшло " + workTimeDuration + " хвилин. Час роботи завершено!\nНаступний період триватиме " + breakTimeDuration + " хвилин.\nГарного відпочинку!";
        }

        PomodoroBot.S.sendMessage(chatId.toString(), msg);
    }
}
