package pomodoroplus;

import java.text.DecimalFormat;

/**
 * Classe de métodos abstratos de conversão.
 * @author Guilherme
 */
public class Utils{
    
    /**
    * Converte uma string no formato 00:00:00 no tempo correspondente em segundos.
    * @param string1 String no formato 00:00:00 a ser convertida.
    * @return Tempo em segundos.
    */
    static long string1ToLong(String string1){
        String aux;
        long tempo;
        
        aux = string1.substring(6, 8);
        if(testeMaxMinSeg(aux)){
            aux = "59";
        }
        tempo = Long.parseLong(aux);
        aux = string1.substring(3, 5);
        if(testeMaxMinSeg(aux)){
            aux = "59";
        }
        tempo = tempo + (60*Long.parseLong(aux));
        aux = string1.substring(0, 2);
        if(testeMaxHora(aux)){
            aux = "23";
        }
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
        if(testeMaxMinSeg(aux)){
            aux = "59";
        }
        tempo = Long.parseLong(aux);
        aux = string2.substring(5, 7);
        if(testeMaxMinSeg(aux)){
            aux = "59";
        }
        tempo = tempo + (60*Long.parseLong(aux));
        aux = string2.substring(0, 2);
        if(testeMaxHora(aux)){
            aux = "23";
        }
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
        
        hora = aux%24;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = formato.format(hora) + " h " + formato.format(min) + " m " + formato.format(seg) + " s";
        return retorno;
    }
    
     /**
     * Converte um tempo em segundos (long) para uma string no formato 00:00:00.
     * @param tempo Tempo em segundos (long).
     * @return String no formato 00:00:00.
     */
    static String longToString1(long tempo){
        String retorno;
        long aux, seg, min, hora;
        
        seg = tempo%60;
        aux = tempo/60;
        
        min = aux%60;
        aux = aux/60;
        
        hora = aux%24;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = formato.format(hora) + ":" + formato.format(min) + ":" + formato.format(seg);
        return retorno;
    }
    
     /**
     * Converte um tempo em segundos (long) para uma string no formato HH:MM, mostrando apenas as horas e os minutos.
     * @param tempo Tempo em segundos (long).
     * @return String no formato HH:MM.
     */
    static String longToHoraMin(long tempo){
        String retorno;
        long aux, min, hora;

        aux = tempo/60;
        
        min = aux%60;
        aux = aux/60;
        
        hora = aux%24;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = formato.format(hora) + ":" + formato.format(min);
        return retorno;
    }
    
     /**
     * Converte um tempo em segundos (long) para uma string no formato :SS, mostrando apenas os segundos.
     * @param tempo Tempo em segundos (long).
     * @return String no formato SS.
     */
    static String longToSeg(long tempo){
        String retorno;
        long seg;
        
        seg = tempo%60;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = ":" + formato.format(seg);
        return retorno;
    }
    
     /**
     * Converte um tempo em segundos (long) para uma string no formato MM:SS, mostrando apenas os minutos e os segundos.
     * @param tempo Tempo em segundos (long), que seja menor que 1 hora (ou 3600 segundos).
     * @return String no formato MM:SS.
     */
    static String longToMinSeg(long tempo){
        String retorno;
        long aux, seg, min;
        
        seg = tempo%60;
        aux = tempo/60;
        
        min = aux%60;
        aux = aux/60;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = formato.format(min) + ":" + formato.format(seg);
        return retorno;
    }
    
     /**
     * Converte um tempo em segundos (long) para uma string no formato 00:00:00 com as horas indo até 99.
     * @param tempo Tempo em segundos (long).
     * @return String no formato 00:00:00.
     */
    static String longToString1Duracao(long tempo){
        String retorno;
        long aux, seg, min, hora;
        
        seg = tempo%60;
        aux = tempo/60;
        
        min = aux%60;
        aux = aux/60;
        
        hora = aux%100;
        
        DecimalFormat formato = new DecimalFormat("00");
        retorno = formato.format(hora) + ":" + formato.format(min) + ":" + formato.format(seg);
        return retorno;
    }
    
    /**
     * Testa se a string no formato 00 h 00 m 00 s tem o tempo zero.
     * @param texto String no formato 00 h 00 m 00 s.
     * @return true se o tempo representado pela string é zero / false caso contrário
     */
    static boolean testeZero(String texto){
        long tempo = string2ToLong(texto);
        if(tempo == 0){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Testa se os dígitos dos minutos ou segundos digitados são maiores do que o possível (>=60).
     * @param digitos Dígitos dos minutos ou segundos digitados.
     * @return true: Os dígitos são maiores que o permitido / false: Os dígitos estão dentro do permitido.
     */
    static boolean testeMaxMinSeg(String digitos){
        if(Character.getNumericValue(digitos.charAt(0)) >= 6){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Testa se os dígitos das horas digitados são maiores do que o possível (>=24).
     * @param digitos Dígitos dos minutos ou segundos digitados.
     * @return true: Os dígitos são maiores que o permitido / false: Os dígitos estão dentro do permitido.
     */
    static boolean testeMaxHora(String digitos){
        if(Character.getNumericValue(digitos.charAt(0)) >= 3 ||
                (Character.getNumericValue(digitos.charAt(0)) >= 2 && 
                Character.getNumericValue(digitos.charAt(1)) >= 4)){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * Calcula diferença de tempo entre a hora atual e uma hora qualquer.
     * @param horaAtual Hora (em segundos) atual do sistema.
     * @param hora Hora (em segundos).
     * @return Diferença (em segundos).
     */
    static long diferenca(long horaAtual, long hora){
        if(horaAtual <= hora){
            return hora - horaAtual;
        }else{
            return (hora + 86400) - horaAtual;
        }
    }
}
