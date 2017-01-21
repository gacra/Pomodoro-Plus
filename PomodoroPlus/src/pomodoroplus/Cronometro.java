package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JTable;

/**
 * Classe que representa o cronômetro regressivo (1ª janela).
 * @author Guilherme
 */
public class Cronometro{
    private LinkedList<Periodo> listaPeriodos;
    private long inicioProgramado;
    private JTable tabela;
    private JButton botao;
    private Regressivo regressivo;

    public Cronometro(JanelaPrincipal janelaPrinc){
        this.tabela = janelaPrinc.getTabela();
        this.botao = janelaPrinc.getPausaCont();
        this.regressivo = new Regressivo();
    }
    
    public void programaCronometro(LinkedList<Periodo> listaPeriodos, long inicioProgramado){
        this.listaPeriodos = listaPeriodos;
        this.inicioProgramado = inicioProgramado;
        avaliaInicio();
        System.out.println(this);
    }

    public void avaliaInicio(){
        long horaAtual = regressivo.getHorario();
        if(inicioProgramado == -1){
        
        }else{
        
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
    
}
