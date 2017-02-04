package pomodoroplus;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

/**
 * Classe que representa um programa de períodos de tempo.
 * @author Guilherme
 */
public class Programa{
    
    //Valor (em segundos) da hora de início do programa
    private long inicio;
    //Variável auxiliar. Guarda o valor do início digitado pelo usuário, antes
    //do "iniciar agora" ser ativado.
    private long inicioVelho;
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
    //Thread do objeto anterior
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
     * @param janelaPrinc Referência para a Janela Principal
     */
    public Programa(JLabel labelDuracao, JLabel labelAte, JFormattedTextField inicioTempo, Cronometro cronometro, JanelaPrincipal janelaPrinc){
        this.labelDuracao = labelDuracao;
        this.labelAte = labelAte;
        this.inicioTempo = inicioTempo;
        this.cronometro = cronometro;
        this.janelaPrinc = janelaPrinc;
        listaPaineis = new LinkedList<>();
        relInicio = new RelInicio(this, inicioTempo);
        
        if(!abreArquivo("Atual")){
            inicio = 0;
            inicioVelho = 0;
            duracao = 0;
            ate = 0;
            
            Painel novoPainel = new Painel(janelaPrinc.getProgramaPanel(), janelaPrinc.getProgramaRolagem(), janelaPrinc, this);
            janelaPrinc.getProgramaPanel().add(novoPainel);
            janelaPrinc.getProgramaPanel().setPreferredSize(new Dimension(novoPainel.getWidth(),janelaPrinc.getCont()*(40+(((FlowLayout)janelaPrinc.getProgramaPanel().getLayout()).getVgap()))));
        }
    }
    
    /**************************
     * MANIPULAÇÃO DE PERÍODOS
     **************************/
    
    /**
     * Atualiza os campos Início, Duração e Até da Janela Principal, bem como o 
     * campo Até de todos os Paineis afetados. Método para o contexto de 
     * atualização de um painel.
     * @param chamou Painel que chamou o método, pois teve seu campo Duração 
     * atualizado. Caso tenha sido chamado pela Janela Principal (modificação no 
     * campo Início) deve receber null.
     */
    public synchronized void atualiza(Painel chamou){
        long duracaotmp = 0;
        
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
        
        for(int i =0 ; i<indice; i++){
            duracaotmp += listaPaineis.get(i).getPeriodo().getDuracao();
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
             duracaotmp += periodo.getDuracao();
        }
        
        this.ate = periodo.getAte();
        this.labelAte.setText(Utils.longToString1(this.ate));
        this.duracao = duracaotmp;
        if(this.duracao<=359999){
            this.labelDuracao.setText(Utils.longToString1Duracao(this.duracao));
        }else{
           this.labelDuracao.setText("--:--:--");
        }
    }

     /**
     * Atualiza os campos Início, Duração e Até da Janela Principal, bem como o 
     * campo Até de todos os Paineis afetados. Método para o contexto de 
     * exclusão de um painel.
     * @param chamou Painel que chamou o método, pois será excluído.
     */
    public synchronized void atualizaExclusao(Painel chamou){
        long duracaotmp = 0;
        
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
        
        for(int i =0 ; i<indice; i++){
            duracaotmp += listaPaineis.get(i).getPeriodo().getDuracao();
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
             duracaotmp += periodo.getDuracao();
        }
        
        this.ate = periodo.getAte();
        this.labelAte.setText(Utils.longToString1(this.ate));
        this.duracao = duracaotmp;
        if(this.duracao<=359999){
            this.labelDuracao.setText(Utils.longToString1Duracao(this.duracao));
        }else{
           this.labelDuracao.setText("--:--:--");
        }
    }
    
        /**
     * Finaliza a programação e envia a lista de períodos para o objeto Cronometro (1ª tela).
     * @return True: Caso o programa tenha duração maior que zero / False: Caso seja igual a zero.
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
        salvaArquivo(listaPeriodos, "Atual", true);
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
    
    /*******************************************
     * MANIPULAÇÃO DA THREAD DO "INICIAR AGORA"
     *******************************************/
    
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
    
    /**********************************************
     * SALVAR E CARREGAR PERÍODOS PARA/DE ARQUIVOS
     **********************************************/
    
    /**
     * Salva os períodos da programação em um arquivo.
     * @param listaPeriodos Lista de períodos da programação.
     */
    private void salvaArquivo(LinkedList<Periodo> listaPeriodos, String nome, boolean atual){
        ObjectOutputStream output;
        File file;
        String diretorio;
        String barra;
        if(Utils.isLinux()){
            diretorio = "/home/" + System.getProperty("user.name") + "/Pomodoro";
            System.out.println(diretorio);
            barra = "/";
        }else{
            diretorio = "C:\\Pomodoro";
            barra = "\\";
        }
        
        
        try{
            file = new File(diretorio);
            file.mkdir();
            int i = 1;
            String novoNome = nome;
            while((new File(diretorio + barra + novoNome + ".pdp").exists() || novoNome.equals("Atual"))&& !atual){
                novoNome = nome + String.valueOf(i);
                i++;
            }
            output = new ObjectOutputStream(new FileOutputStream(diretorio + barra + novoNome + ".pdp") );
            output.writeObject(listaPeriodos);
            output.close();
        }catch(IOException ex){
            System.err.println( "Erro em salvar o arquivo" + ex.getMessage() );
            return;
        }
    }

    /**
     * Avalia se há um arquivo de um programa anterior e carrega tal programa.
     * @param nome Nome do arquivo a ser aberto.
     * @return True: Existe o arquivo / False: Não existe o arquivo.
     */
    public boolean abreArquivo(String nome){
        ObjectInputStream input;
        LinkedList<Periodo> listaPeriodos;
        
        String diretorio;
        String barra;
        
        if(Utils.isLinux()){
            diretorio = "/home/" + System.getProperty("user.name") + "/Pomodoro";
            System.out.println(diretorio);
            barra = "/";
        }else{
            diretorio = "C:\\Pomodoro";
            barra = "\\";
        }
        
        try{
            input = new ObjectInputStream(new FileInputStream(diretorio + barra + nome + ".pdp") );
            listaPeriodos = (LinkedList<Periodo>) input.readObject();
            input.close();
            mostrarPrograma(listaPeriodos);
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
    private void mostrarPrograma(LinkedList<Periodo> listaPeriodos){
        
        //Limpeza
        for(Painel pa: listaPaineis){
            pa.removeQuandoTodos();
        }
        listaPaineis.clear();
           
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
    
    /**
     * Salva um programa, ao clicar no botão Salvar
     * @param nome Nome do programa a ser salvo
     */
    public void salvaPrograma(String nome){
        LinkedList<Periodo> listaPeriodos = fazListaPeriodos(); 
        salvaArquivo(listaPeriodos, nome, false);
    }
    
    /**
     * Lista os programas salvos (Arquivos no diretório C:\\Pomodoro).
     * @return Lista de nomes dos programas salvos.
     */
    public String[] listarSalvos(){
        File file;
        
        String diretorio;
        
        if(Utils.isLinux()){
            diretorio = "/home/" + System.getProperty("user.name") + "/Pomodoro";
            System.out.println(diretorio);
        }else{
            diretorio = "C:\\Pomodoro";
        }
        
        file = new File(diretorio);
        if(file.exists()){
            String[] nomes = file.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name){
                    return name.endsWith(".pdp") && !name.contains("Atual.pdp");
                }
            });        
            for(int i = 0; i< nomes.length; i++){
               nomes[i] = nomes[i].replaceAll(".pdp", "");
            }
            return nomes;
        }else{
            return new String[0];
        }
        
    }
    
    /**
     * Exclui um programa (arquivo) salvo.
     * @param nome Nome do programa (arquivo).
     */
    public void excluir(String nome){
        
        String diretorio;
        String barra;
        
        if(Utils.isLinux()){
            diretorio = "/home/" + System.getProperty("user.name") + "/Pomodoro";
            System.out.println(diretorio);
            barra = "/";
        }else{
            diretorio = "C:\\Pomodoro";
            barra = "\\";
        }
        
        File file = new File(diretorio + barra + nome + ".pdp");
        file.delete();
    }
    
    /*********************
     * GETTERS E SETTER 
     *********************/
    
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

    public LinkedList<Painel> getListaPaineis(){
        return listaPaineis;
    }
    
}
