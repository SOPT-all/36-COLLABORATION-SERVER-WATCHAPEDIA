package or.sopt.soptwatcha.util;

public class TimeFormatUtil {
    
    public static String formatMinutesToHoursAndMinutes(int minutes) {
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        
        if (hours > 0 && remainingMinutes > 0) {
            return hours + "시간 " + remainingMinutes + "분";
        } else if (hours > 0) {
            return hours + "시간";
        } else {
            return remainingMinutes + "분";
        }
    }
}