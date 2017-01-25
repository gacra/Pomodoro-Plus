package pomodoroplus;

import javax.swing.JFormattedTextField;

/**
 * Classe para atualizar o incício do programa com a hora do sistema.
 * @author Guilherme
 */
public class RelInicio extends Relogio implements Runnable{
    //Referência para o objeto programa
    Programa programa;
    //Referência para o campo inicioTempo da Janela principal
    JFormattedTextField inicioTempo;
    
    /**
     * Cria novo objeto RelInicio
     * @param programa Referência para o objeto programa
     * @param inicioTempo Referência para o campo inicioTempo da Janela principal
     */
    public RelInicio(Programa programa, JFormattedTextField inicioTempo){
        this.programa = programa;
        this.inicioTempo = inicioTempo;
    }
    
    /**
     * Atualiza periodicamente o tempo de início do programa com a hora atual do sistema.
     */
    @Override
    public void run(){
        while(!Thread.interrupted()){            
            try{
                Thread.sleep(250);
            }catch(InterruptedException ex){
                return;
            }
            super.atualizaRelogio();
            this.programa.setInicio(super.horario);
            this.programa.atualiza(null);
            inicioTempo.setText(Utils.longToString1(super.horario));
        }
    }
    
}
