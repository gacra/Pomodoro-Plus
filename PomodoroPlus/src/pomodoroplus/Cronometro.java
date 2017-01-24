package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Classe que representa o cronômetro regressivo (1ª janela).
 * @author Guilherme
 */
public class Cronometro{
    private LinkedList<Periodo> listaPeriodos;
    private long inicioProgramado;
    private JTable tabela;
    private JPanel suporteHorario;
    private PainelHorario1 painelHorario1;
    private PainelHorario2 painelHorario2;
    private JButton botao;
    private Regressivo regressivo;

    public Cronometro(JanelaPrincipal janelaPrinc){
        this.tabela = janelaPrinc.getTabela();
        this.botao = janelaPrinc.getPausaCont();
        this.suporteHorario = janelaPrinc.getSuporteHorario();
        this.painelHorario1 = janelaPrinc.getPainelHorario1();
        this.painelHorario2 = janelaPrinc.getPainelHorario2();
        System.out.println();
    }
    
    public void programaCronometro(LinkedList<Periodo> listaPeriodos, long inicioProgramado){
        this.listaPeriodos = listaPeriodos;
        this.inicioProgramado = inicioProgramado;
        this.regressivo = new Regressivo(listaPeriodos, suporteHorario, painelHorario1, painelHorario2);
        avaliaInicio();
        escreveTabela();
        iniciaRegressivo();
    }

    public void avaliaInicio(){
        long horaAtual = regressivo.getHorario();
        if(inicioProgramado != -1){
            Periodo espera = new Periodo(-1, "Espera", Utils.diferenca(horaAtual, inicioProgramado) , inicioProgramado);
            listaPeriodos.addFirst(espera);
        }
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

    private void iniciaRegressivo(){
        Thread regressivoThread = new Thread(regressivo);
        regressivoThread.start();
    }
    
}
