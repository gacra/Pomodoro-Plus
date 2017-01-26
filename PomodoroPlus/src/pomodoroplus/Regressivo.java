package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

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
    JTable tabela;
    //Se true: painelHorario1 ; Se false: PainelHorario2
    boolean qualPainel;
    long tempo;
    boolean pausado;
    long ateAnterior;
    long horaPausa;
    long duracaoPausa, duracaoPausaParcial;
    
    public Regressivo(JanelaPrincipal janelaPrinc){
        this.janelaPrinc = janelaPrinc;
        this.suporteHorario = janelaPrinc.getSuporteHorario();
        this.painelHorario1 = janelaPrinc.getPainelHorario1();
        this.painelHorario2 = janelaPrinc.getPainelHorario2();
        this.tabela = janelaPrinc.getTabela();
        this.qualPainel = true;
        this.pausado = false;
        this.horaPausa = 0;
        this.duracaoPausa = 0;
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
            
            duracaoPausa = 0;
            ateAnterior = p.getAte();
            
            while(!Thread.interrupted()){
                try{
                    Thread.sleep(100);
                    atualizaRelogio();
                    if(pausado){
                        duracaoPausaParcial = Utils.diferenca(horaPausa, horario); 
                        p.setAte(duracaoPausaParcial + ateAnterior);
                        atualizaPausa(p);
                    }else{
                        ateAnterior = p.getAte();
                        tempo = Utils.diferenca(horario, p.getAte());
                        atualizaMostrador();
                        if(tempo == 0 || tempo > p.getDuracao()){
                            alarme();
                            Thread.sleep(400);
                            break;
                        }
                    }
                }catch(InterruptedException ex){
                    return;
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

    private void alarme(){
        if(janelaPrinc.isSom()){
            this.janelaPrinc.getAudio(janelaPrinc.getIndiceAlarme()).play();
        }
    }

   public void pausar(){
        pausado = true;
        atualizaRelogio();
        horaPausa = horario;
    }
    
    public void continuar(){
        pausado = false;
        duracaoPausa += duracaoPausaParcial;
    }

    private void atualizaPausa(Periodo chamou){
        int indice;

        indice = listaPeriodos.indexOf(chamou);
        
        Periodo periodo = null;
        long tempo, tempoAnt;
        
        tempoAnt = listaPeriodos.get(indice).getAte();
        
        tabela.setValueAt(Utils.longToString1(duracaoPausa+duracaoPausaParcial), indice, 2);
        tabela.setValueAt(Utils.longToString1(tempoAnt), indice, 3);

        for(int i = indice+1; i<listaPeriodos.size(); i++){
             periodo = listaPeriodos.get(i);
             tempo = periodo.getDuracao();
             periodo.setAte((tempo+tempoAnt)%86400); //Para não passar de 24h
             tabela.setValueAt(Utils.longToString1(periodo.getAte()), i, 3);
             tabela.repaint();
             tempoAnt = periodo.getAte();
        }
        
        
    }
    
}
