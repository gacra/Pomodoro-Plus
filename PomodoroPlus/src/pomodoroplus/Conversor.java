package pomodoroplus;

import java.text.DecimalFormat;

/**
 *
 * @author Guilherme
 */
public class Conversor{
    
    /**
    * Converte uma string no formato 00:00:00 no tempo correspondente em segundos.
    * @param string1 String no formato 00:00:00 a ser convertida.
    * @return Tempo em segundos.
    */
    static long string1ToLong(String string1){
        String aux;
        long tempo;
        
        aux = string1.substring(6, 8);
        tempo = Long.parseLong(aux);
        aux = string1.substring(3, 5);
        tempo = tempo + (60*Long.parseLong(aux));
        aux = string1.substring(0, 2);
        tempo = tempo + (3600*Long.parseLong(aux));
        return tempo;
    }
    
    /**
     * Converte uma string no formato 00 h 00 m 00 s no tempo correspondente em segundos.
     * @param string2 String no formato 00 h 00 m 00 s
     * @return Tempo em segundos.
     */
    static long string2ToLong(String string2){
        String aux;
        long tempo;
        
        aux = string2.substring(10, 12);
        tempo = Long.parseLong(aux);
        aux = string2.substring(5, 7);
        tempo = tempo + (60*Long.parseLong(aux));
        aux = string2.substring(0, 2);
        tempo = tempo + (3600*Long.parseLong(aux));
        return tempo;
    }
    
    /**
     * Converte um tempo em segundos (long) para uma string no formato 00 h 00 m 00 s.
     * @param tempo Tempo em segundos (long).
     * @return String no formato 00 h 00 m 00 s.
     */
    static String longToString2(long tempo){
        String retorno;
        long aux, seg, min, hora;
        
        seg = tempo%60;
        aux = tempo/60;
        
        min = aux%60;
        aux = aux/60;
        
        hora = aux%60;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = formato.format(hora) + " h " + formato.format(min) + " m " + formato.format(seg) + " s";
        return retorno;
    }
}
