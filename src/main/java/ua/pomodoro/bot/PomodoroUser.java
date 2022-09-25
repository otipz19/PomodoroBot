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
            msg = "��������� ����� ���� ������-���������!\n��������� ������ ������: " + workTimeDuration + " ������.\n��������� ������ ���������: " + breakTimeDuration + " ������.";
        }
        else if(!isTimerRunning){
            msg = "³���! �������� ���������.\n�� ������ ������ ����� ���� ������-��������� ������������ ������� /set";
        }
        else if(timerType == PomodoroBot.TimerType.WORK){
            msg = "������� " + breakTimeDuration + " ������. ��� ��������� ���������!\n��������� ����� ��������� " + workTimeDuration + " ������.\n�� ������!";
        }
        else{
            msg = "������� " + workTimeDuration + " ������. ��� ������ ���������!\n��������� ����� ��������� " + breakTimeDuration + " ������.\n������� ���������!";
        }

        PomodoroBot.S.sendMessage(chatId.toString(), msg);
    }
}
