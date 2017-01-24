package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Guilherme
 */
public class Regressivo extends Relogio implements Runnable{
    LinkedList<Periodo> listaPeriodos;
    JPanel suporteHorario;
    PainelHorario1 painelHorario1;
    PainelHorario2 painelHorario2;
    //Se true: painelHorario1 ; Se false: PainelHorario2
    boolean qualPainel;
    long tempo;
    
    public Regressivo(LinkedList<Periodo> listaPeriodos, JPanel suporteHorario, PainelHorario1 painelHorario1, PainelHorario2 painelHorario2){
        this.listaPeriodos = listaPeriodos;
        this.suporteHorario = suporteHorario;
        this.painelHorario1 = painelHorario1;
        this.painelHorario2 = painelHorario2;
        System.out.println(painelHorario2);
        this.qualPainel = true;
    }
    
    @Override
    public void run(){
        long ate;
        
        for(Periodo p : listaPeriodos){
            ate = p.getAte();
            
            while(true){                
                try{
                    Thread.sleep(100);
                }catch(InterruptedException ex){
                    return;
                }
                atualizaRelogio();
                tempo = Utils.diferenca(horario, ate);
                atualizaMostrador();
                if(tempo == 0 || tempo > p.getDuracao()){
                    break;
                }
            }
        }
    }

    private void atualizaMostrador(){
        if(tempo>=3600){    //Mais que 1 hora
            if(!qualPainel){
                suporteHorario.removeAll();
                suporteHorario.add(painelHorario1);
                suporteHorario.updateUI();
            }
            painelHorario1.getHorMin().setText(Utils.longToHoraMin(tempo));
            painelHorario1.getSeg().setText(Utils.longToSeg(tempo));
        }else{
            if(qualPainel){
                suporteHorario.removeAll();
                suporteHorario.add(painelHorario2);
                suporteHorario.updateUI();
            }
            System.out.println(painelHorario2.getMinSeg());
            painelHorario2.getMinSeg().setText(Utils.longToMinSeg(tempo));
        }
    }
    
}
