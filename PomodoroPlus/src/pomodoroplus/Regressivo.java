package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Guilherme
 */
public class Regressivo extends Relogio implements Runnable{
    LinkedList<Periodo> listaPeriodos;
    JanelaPrincipal janelaPrinc;
    JPanel suporteHorario;
    PainelHorario1 painelHorario1;
    PainelHorario2 painelHorario2;
    //Se true: painelHorario1 ; Se false: PainelHorario2
    boolean qualPainel;
    long tempo;
    
    public Regressivo(JanelaPrincipal janelaPrinc){
        this.janelaPrinc = janelaPrinc;
        this.suporteHorario = janelaPrinc.getSuporteHorario();
        this.painelHorario1 = janelaPrinc.getPainelHorario1();
        this.painelHorario2 = janelaPrinc.getPainelHorario2();
        this.qualPainel = true;
    }

    public void setListaPeriodos(LinkedList<Periodo> listaPeriodos){
        this.listaPeriodos = listaPeriodos;
    }
    
    @Override
    public void run(){
        long ate;
        
        janelaPrinc.voltaLinha();
        
        for(Periodo p : listaPeriodos){
            janelaPrinc.incLinha();
            ate = p.getAte();
            
            while(!Thread.interrupted()){   
                try{
                    Thread.sleep(100);
                    atualizaRelogio();
                    tempo = Utils.diferenca(horario, ate);
                    atualizaMostrador();
                    if(tempo == 0 || tempo > p.getDuracao()){
                       Thread.sleep(400);
                       this.janelaPrinc.getAudio().play();
                       break;
                    }
                }catch(InterruptedException ex){
                    return;
                }
            }
        }
    }

    private void atualizaMostrador(){
        if(tempo>=3600){    //Mais que 1 hora
            System.out.println("Mais q uma hora");
            if(!qualPainel){
                suporteHorario.removeAll();
                suporteHorario.add(painelHorario1);
                suporteHorario.updateUI();
                qualPainel = true;
            }
            painelHorario1.getHorMin().setText(Utils.longToHoraMin(tempo));
            painelHorario1.getSeg().setText(Utils.longToSeg(tempo));
        }else{
            if(qualPainel){
                suporteHorario.removeAll();
                suporteHorario.add(painelHorario2);
                suporteHorario.updateUI();
                qualPainel = false;
            }
            painelHorario2.getMinSeg().setText(Utils.longToMinSeg(tempo));
        }
    }
    
}
