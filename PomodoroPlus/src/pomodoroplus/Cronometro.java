package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Classe que responsável pela atualização da 1ª janela (tabela de períodos, cronômetro, pausa, sons).
 * Responsavel ainda pelo controle de tempo de cada período (controlando objeto Regressivo).
 * @author Guilherme
 */
public class Cronometro{
    //Lista de períodos que estão atualmente sendo cronometrados na 1ª janela.
    private LinkedList<Periodo> listaPeriodos;
    //Horário (em segundos) programado para se iniciar o programa.
    private long inicioProgramado;
    //Referência para a tabela da 1ª janela.
    private JTable tabela;
    //Referência para o botão de pausar e continuar.
    private JButton botao;
    //Objeto cronômetro regressivo, usado para contar a passagem de tempo dos períodos.
    private Regressivo regressivo;
    //Thread do objeto Regressivo acima
    private Thread regressivoThread = null;

    /**
     * Cria novo objeto Cronometro.
     * 
     * @param janelaPrinc Referência para a janela principal do Pomodoro.
     */
    public Cronometro(JanelaPrincipal janelaPrinc){
        this.tabela = janelaPrinc.getTabela();
        this.botao = janelaPrinc.getPausaCont();
        this.regressivo = new Regressivo(janelaPrinc);
    }
    
    /**
     * Usada para programar o cronômetro com um novo programa, vindo da 2ª 
     * janela, a qual chama tal método.
     * 
     * @param listaPeriodos Lista de períodos do programa.
     * @param inicioProgramado Horas (em segundos) que o irá programa começar.
     */
    public void programaCronometro(LinkedList<Periodo> listaPeriodos, long inicioProgramado){
        this.listaPeriodos = listaPeriodos;
        this.inicioProgramado = inicioProgramado;
        this.regressivo.setListaPeriodos(listaPeriodos);
        avaliaInicio();
        escreveTabela();
        iniciaRegressivo();
    }

    /**
     * Avalia o início programado para o programa que foi passado. Caso ele não 
     * começe imediatamente, é criado um período de espera.
     */
    public void avaliaInicio(){
        long horaAtual = regressivo.getHorario();
        if(inicioProgramado != -1){
            Periodo espera = new Periodo(-1, "Espera", Utils.diferenca(horaAtual, inicioProgramado) , inicioProgramado);
            listaPeriodos.addFirst(espera);
        }
    }
    
    /**
     * Escreve os dados dos períodos do programa na tabela da interface gráfica.
     */
    private void escreveTabela(){
        Periodo p;
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);
        
        for(int i=0; i < listaPeriodos.size(); i++){
            p = listaPeriodos.get(i);
            model.addRow(new String[]{p.getNome(), Utils.longToString1(p.getDuracao()), 
                "--:--:--", Utils.longToString1(p.getAte())});
        }
    }

    /**
     * Inicia a thread do cronômetro regressivo para iniciar a execução do 
     * programa.
     */
    private void iniciaRegressivo(){
        if(regressivoThread !=null && regressivoThread.isAlive()){
            regressivoThread.interrupt();
        }
        regressivoThread = new Thread(regressivo);
        regressivoThread.start();
    }

    /**
     * Informa ao cronômetro regressivo para pausar o período atual.
     * @return True: A thread do contador regressivo estava rodando e foi
     * pausada / False: A thread ainda não estava rodando, portanto não foi
     * possível pausa-la.
     */
    public boolean pausar(){
        if(regressivoThread != null && regressivoThread.isAlive()){
            regressivo.pausar();
            return true;
        }else{
            return  false;
        }
    }
    
    /**
     * Informa ao cronômetro regressivo para continuar o período atual.
     */
    void continuar(){
       regressivo.continuar();
    }
    
    @Override
    public String toString(){
        String retorno;
        retorno = "Cronometro{" + "listaPeriodos=\n";
        for(Periodo p : this.listaPeriodos){
            retorno = retorno + p.toString() +"\n";
        } 
        retorno = retorno + "\ninicioProgramado=" + inicioProgramado + "}";
        return retorno;
    }
    
}
