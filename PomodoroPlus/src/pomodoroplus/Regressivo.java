package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Herdeira da classe Relogio que é responsável pela contagem regressiva dos
 * períodos na 1ª tela.
 * @author Guilherme
 */
public class Regressivo extends Relogio implements Runnable{
    //Referência para a lista de períodos (programa) que está atualmente sendo
    //executada.
    private LinkedList<Periodo> listaPeriodos;
    //Referência para a janela principal.
    private JanelaPrincipal janelaPrinc;
    //Referência para o painel que suporta os dois tipos de mostradores para o
    //cronômetro regressivo.
    private JPanel suporteHorario;
    //Painel no formato hh:mm:ss.
    private PainelHorario1 painelHorario1;
    //Painel no formado mm:ss.
    private PainelHorario2 painelHorario2;
    //Referência para a tabela da 1ª tela em que são mostradas as informações 
    //dos períodos do programa atualmente sendo executados.
    private JTable tabela;
    //Se true: painelHorario1 ; Se false: PainelHorario2
    private boolean qualPainel;
    //Variável auxiliar usada para guardar quanto tempo (em segundos) falta para
    //um período acabar.
    private long tempo;
    //Indica se o período está pausado (true) ou não (false).
    private boolean pausado;
    //Variável auxiliar. Guarda a hora que o período atual deveria acabar, antes
    //da pausa mais recente.
    private long ateAnterior;
    //Hora (em segundos) que a pausa mais recente se iniciou.
    private long horaPausa;
    //Duração somada (em segundos) de todas as pausas sofridas pelo período
    //atual.
    private long duracaoPausa;
    //Duração (em segundos) apenas da pausa mais recente sofridas pelo período
    //atual.
    private long duracaoPausaParcial;
    
    /**
     * Cria novo objeto Regressivo
     * @param janelaPrinc Janela Principal
     */
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

    /**
     * Passagem da lista de Períodos que o regressivo irá executar.
     * @param listaPeriodos 
     */
    public void setListaPeriodos(LinkedList<Periodo> listaPeriodos){
        this.listaPeriodos = listaPeriodos;
    }
    
    /**
     * Passa período por período cronometrando quando tempo falta para o término
     * do mesmo. Atualiza o mostrador do cronômetro, os dados de pausa na tabela,
     * bem como a indicação de qual o período atual.
     */
    @Override
    public void run(){
        
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

    /**
     * Atualiza o mostrador do cronômetro regressivo com o tempo correto.
     * Também escolhe o mostrador apropriado (mais ou menos que 1h).
     */
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

    /**
     * Toca o alarme.
     */
    private void alarme(){
        if(janelaPrinc.isSom()){
            this.janelaPrinc.getAudio(janelaPrinc.getIndiceAlarme()).play();
        }
    }

    /**
     * Pausa o período.
     */
    public void pausar(){
        pausado = true;
        atualizaRelogio();
        horaPausa = horario;
    }
    
    /**
     * Continua o período.
     */
    public void continuar(){
        pausado = false;
        duracaoPausa += duracaoPausaParcial;
    }

    /**
     * Atualiza os tempos de término (até) de todos os períodos afetados quando
     * um deles é pausado.
     * @param chamou Período pausado, devido ao qual este método foi chamado.
     */
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
