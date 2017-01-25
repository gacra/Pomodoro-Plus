package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Classe que representa um programa de períodos de tempo.
 * @author Guilherme
 */
public class Programa{
    
    //Valor (em segundos) da hora de início do programa
    private long inicio, inicioVelho;
    //Valor (em segundos) da duração do programa
    private long duracao;
    //Label tempo de duração do programa na JanelaPrincipal
    private JLabel labelDuracao;
    //Valor (em segundos) da hora final do programa
    private long ate;
    //Label tempo final do programa na JanelaPrincipal
    private JLabel labelAte;
    //Lista de objetos Periodo que formam o programa
    private LinkedList<Painel> listaPaineis;
    //Objeto do relógio que altera o valor do horário de início
    private RelInicio relInicio;
    //Thread do objeto anterios
    private Thread relInicioThread;
    //Referência para o campo inicioTempo da Janela Principal
    JFormattedTextField inicioTempo;
    //Referência para o Cronômetro ao qual será enviado a programação
    Cronometro cronometro;

    /**
     * Cria novo objeto Programa
     * @param labelDuracao Label tempo de duração do programa na JanelaPrincipal
     * @param labelAte Label tempo final do programa na JanelaPrincipal
     * @param inicioTempo Referência para o campo inicioTempo da Janela Principal
     * @param cronometro Referência para o Cronômetro ao qual será enviado a programação
     */
    public Programa(JLabel labelDuracao, JLabel labelAte, JFormattedTextField inicioTempo, Cronometro cronometro){
        this.labelDuracao = labelDuracao;
        this.labelAte = labelAte;
        inicio = 0;
        inicioVelho = 0;
        duracao = 0;
        ate = 0;
        this.inicioTempo = inicioTempo;
        this.cronometro = cronometro;
        listaPaineis = new LinkedList<>();
        relInicio = new RelInicio(this, inicioTempo);
    }

    public long getInicio(){
        return inicio;
    }

    public synchronized void setInicio(long inicio){
        this.inicio = inicio;
    }

    public long getDuracao(){
        return duracao;
    }

    public void setDuracao(long duracao){
        this.duracao = duracao;
    }

    public long getAte(){
        return ate;
    }

    public void setAte(long ate){
        this.ate = ate;
    }

    public LinkedList<Painel> getConjPeriodos(){
        return listaPaineis;
    }
    
    /**
     * Atualiza os campos Início, Duração e Até da Janela Principal, bem como o campo Até de todos os Paineis afetados. Método para o contexto de atualização de um painel.
     * @param chamou Painel que chamou o método, pois teve seu campo Duração atualizado. Caso tenha sido chamado pela Janela Principal (modificação no campo Início) deve receber null.
     */
    public synchronized void atualiza(Painel chamou){
        //Atualizando os tempos finais dos períodos.
        int indice;
               
        if(chamou != null){
            indice = listaPaineis.indexOf(chamou);
        }else{
            indice = 0;
        }
        
        Painel painel = null;
        Periodo periodo = null;
        long tempo, tempoAnt;
        
        if(indice == 0){
            tempoAnt = this.inicio;
        }else{
            tempoAnt = listaPaineis.get(indice-1).getPeriodo().getAte();
        }
        for(int i = indice; i<listaPaineis.size(); i++){
             painel = listaPaineis.get(i);
             periodo = painel.getPeriodo();
             tempo = periodo.getDuracao();
             periodo.setAte((tempo+tempoAnt)%86400); //Para não passar de 24h
             JLabel teste = painel.getLabelAte();
             teste.setText(Utils.longToString2(periodo.getAte()));
             teste.repaint();
             tempoAnt = periodo.getAte();
        }
        
        this.ate = periodo.getAte();
        this.labelAte.setText(Utils.longToString1(this.ate));
        this.duracao = this.ate - this.inicio;
        if(this.duracao<=359999){
            this.labelDuracao.setText(Utils.longToString1Duracao(this.duracao));
        }else{
           this.labelDuracao.setText("--:--:--");
        }
    }

     /**
     * Atualiza os campos Início, Duração e Até da Janela Principal, bem como o campo Até de todos os Paineis afetados. Método para o contexto de exclusão de um painel.
     * @param chamou Painel que chamou o método, pois será excluído.
     */
    public synchronized void atualizaExclusao(Painel chamou){
        int indice;
               
        indice = listaPaineis.indexOf(chamou);
        
        Painel painel = null;
        Periodo periodo = null;
        long tempo, tempoAnt;
        
        if(indice == 0){
            tempoAnt = this.inicio;
        }else{
            tempoAnt = listaPaineis.get(indice-1).getPeriodo().getAte();
        }
        for(int i = indice + 1; i<listaPaineis.size(); i++){
             painel = listaPaineis.get(i);
             periodo = painel.getPeriodo();
             tempo = periodo.getDuracao();
             periodo.setAte((tempo+tempoAnt)%86400); //Para não passar de 24h
             JLabel teste = painel.getLabelAte();
             teste.setText(Utils.longToString2(periodo.getAte()));
             teste.repaint();
             tempoAnt = periodo.getAte();
        }
        
        this.ate = periodo.getAte();
        this.labelAte.setText(Utils.longToString1(this.ate));
        this.duracao = this.ate - this.inicio;
        if(this.duracao<=359999){
            this.labelDuracao.setText(Utils.longToString1Duracao(this.duracao));
        }else{
           this.labelDuracao.setText("--:--:--");
        }
    }
    
    /**
     * Inicia a thread que atualiza o tempo de início com a hora atual do sistema.
     */
    public void iniciaThreadRelogio(){
        inicioVelho = inicio;
        relInicioThread = new Thread(relInicio);
        relInicioThread.start();
    }
    
     /**
     * Para a thread que atualiza o tempo de início com a hora atual do sistema.
     */
    public void paraThreadRelogio(){
        relInicioThread.interrupt();
        inicio = inicioVelho;
        atualiza(null);
        inicioTempo.setText(Utils.longToString1(inicio));
    }
    
    public boolean finalizaProgramacao(){
        long inicioProgramado;
        if(relInicioThread != null && relInicioThread.isAlive()){
            relInicioThread.interrupt();
            inicioProgramado =-1;
        }else{
            inicioProgramado = inicio;
        }
        LinkedList<Periodo> listaPeriodos = fazListaPeriodos(); 
        if(listaPeriodos.size() == 0){
            return false;
        }
        this.cronometro.programaCronometro(listaPeriodos, inicioProgramado);
        return true;
    }
    
    private LinkedList<Periodo> fazListaPeriodos(){
        LinkedList<Periodo> retorno = new LinkedList<>();
        for(Painel p : listaPaineis){
            p.finalizaPainel();
            if(p.getPeriodo().getDuracao() == 0){
                continue;
            }
            retorno.add(p.getClonePeriodo());
        }
        return retorno;
    }
}
