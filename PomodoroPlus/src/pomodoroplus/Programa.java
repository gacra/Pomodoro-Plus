package pomodoroplus;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
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
    //Janela Principla
    private JanelaPrincipal janelaPrinc;

    /**
     * Cria novo objeto Programa
     * @param labelDuracao Label tempo de duração do programa na JanelaPrincipal
     * @param labelAte Label tempo final do programa na JanelaPrincipal
     * @param inicioTempo Referência para o campo inicioTempo da Janela Principal
     * @param cronometro Referência para o Cronômetro ao qual será enviado a programação
     */
    public Programa(JLabel labelDuracao, JLabel labelAte, JFormattedTextField inicioTempo, Cronometro cronometro, JanelaPrincipal janelaPrinc){
        this.labelDuracao = labelDuracao;
        this.labelAte = labelAte;
        this.inicioTempo = inicioTempo;
        this.cronometro = cronometro;
        this.janelaPrinc = janelaPrinc;
        listaPaineis = new LinkedList<>();
        relInicio = new RelInicio(this, inicioTempo);
        
        if(!avaliaAnterior()){
            inicio = 0;
            inicioVelho = 0;
            duracao = 0;
            ate = 0;
            
            Painel novoPainel = new Painel(janelaPrinc.getProgramaPanel(), janelaPrinc.getProgramaRolagem(), janelaPrinc, this);
            janelaPrinc.getProgramaPanel().add(novoPainel);
            janelaPrinc.getProgramaPanel().setPreferredSize(new Dimension(novoPainel.getWidth(),janelaPrinc.getCont()*(40+(((FlowLayout)janelaPrinc.getProgramaPanel().getLayout()).getVgap()))));
        }
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
    
    /**
     * Finaliza a programação e envia a lista de períodos para o objeto Cronometro (1ª tela).
     */
    public synchronized boolean finalizaProgramacao(){
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
        salvaArquivo(listaPeriodos, "Atual");
        this.cronometro.programaCronometro(listaPeriodos, inicioProgramado);
        return true;
    }
    
    /**
     * Monta a lista de períodos a partir da lista de Paineis
     */
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

    /**
     * Salva os períodos da programação em um arquivo.
     * @param listaPeriodos Lista de períodos da programação.
     */
    private void salvaArquivo(LinkedList<Periodo> listaPeriodos, String nome){
        ObjectOutputStream output;
        File file;
        
        try{
            file = new File("C:\\Pomodoro");
            file.mkdir();
            int i = 1;
            String novoNome = nome;
            while(new File("C:\\Pomodoro\\" + novoNome + ".pdp").exists()){
                novoNome = nome + String.valueOf(i);
                i++;
            }
            output = new ObjectOutputStream(new FileOutputStream("C:\\Pomodoro\\" + novoNome + ".pdp") );
            output.writeObject(listaPeriodos);
            output.close();
        }catch(IOException ex){
            System.err.println( "Erro em salvar o arquivo" + ex.getMessage() );
            return;
        }
    }

    /**
     * Avalia se há um arquivo de um programa anterior e carrega tal programa
     * @return True: Existe o arquivo / False: Não existe o arquivo
     */
    private boolean avaliaAnterior(){
        ObjectInputStream input;
        LinkedList<Periodo> listaPeriodos;
        
        try{
            input = new ObjectInputStream(new FileInputStream("C:\\Pomodoro\\Atual.pdp") );
            listaPeriodos = (LinkedList<Periodo>) input.readObject();
            input.close();
            mostraAnterior(listaPeriodos);
            return true;
        }catch(Exception ex){
            System.err.println( "Não há arquivo." + ex.getMessage());
            return false;
        }
    }

    /**
     * Usa uma lista de Períodos presente em um arquivos para montar os Paineis correspondentes.
     * @param  listaPeriodos Lista de Períodos
     */
    private void mostraAnterior(LinkedList<Periodo> listaPeriodos){
        
        //Limpeza
        for(Painel pa: listaPaineis){
            pa.removePainel();
        }
        
        Periodo pe = listaPeriodos.get(0);
        Painel novoPainel = new Painel(janelaPrinc.getProgramaPanel(), janelaPrinc.getProgramaRolagem(), janelaPrinc, this);
        novoPainel.setPeriodo(pe);
        janelaPrinc.getProgramaPanel().add(novoPainel);
        janelaPrinc.getProgramaPanel().setPreferredSize(new Dimension(novoPainel.getWidth(),janelaPrinc.getCont()*(40+(((FlowLayout)janelaPrinc.getProgramaPanel().getLayout()).getVgap()))));
        
        for(int i = 1; i < listaPeriodos.size(); i++){
            pe = listaPeriodos.get(i);
            novoPainel.ajustaPainel();
            novoPainel = novoPainel.criaNovoPainel();
            novoPainel.setPeriodo(pe);
            System.out.println("hey");
        }
        
        novoPainel.ajustaPainel();
        novoPainel = novoPainel.criaNovoPainel();
    }
    
    /**
     * Testa se todos os períodos do programa tem duração zero.
     * @return True: O programa tem duração zero / False: O programa tem duração maior que zero.
     */
    public boolean testaVazio(){
        for(Painel p : listaPaineis){
            if(p.getPeriodo().getDuracao() != 0){
                return true;
            }
        }
        return false;
    }
    
    public void salvaPrograma(String nome){
        LinkedList<Periodo> listaPeriodos = fazListaPeriodos(); 
        salvaArquivo(listaPeriodos, nome);
    }
    
}
