package pomodoroplus;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Classe pai dos relógios.
 * @author Guilherme
 */
public class Relogio{
    //Horário em segundos
    protected long horario;
    
    /**
     * Atualiza o atributo horário com a hora atual do sistema em ms.
     */
    public void atualizaRelogio(){
        GregorianCalendar calendar = new GregorianCalendar();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int seg = calendar.get(Calendar.SECOND);
        //System.out.println("Hora: " + hora + " Min: " +  min + " Seg: " + seg);
        this.horario = (hora*3600) + (min*60) + seg;
    }
    
    
}
