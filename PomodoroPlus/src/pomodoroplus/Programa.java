package pomodoroplus;

import java.util.LinkedList;
import javax.swing.JLabel;

/**
 *
 * @author Guilherme
 */
public class Programa{
    
    //Valor (em segundos) da hora de início do programa
    private long inicio;
    //Valor (em segundos) da duração do programa
    private long duracao;
    //Label tempo de duração do programa na JanelaPrincipal
    private JLabel labelDuracao;
    //Valor (em segundos) da hora final do programa
    private long ate;
    //Label tempo final do programa na JanelaPrincipal
    private JLabel labelAte;
    //Lista de objetos Periodo que formam o programa
    private LinkedList<Periodo> conjPeriodos;

    /**
     * Cria novo objeto Programa
     * @param labelDuracao Label tempo de duração do programa na JanelaPrincipal
     * @param labelAte Label tempo final do programa na JanelaPrincipal
     */
    public Programa(JLabel labelDuracao, JLabel labelAte){
        this.labelDuracao = labelDuracao;
        this.labelAte = labelAte;
        inicio = 0;
        duracao = 0;
        ate = 0;
        conjPeriodos = new LinkedList<>();
    }

    public long getInicio(){
        return inicio;
    }

    public void setInicio(long inicio){
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

    public LinkedList<Periodo> getConjPeriodos(){
        return conjPeriodos;
    }
    
    public void atualiza(Periodo chamou){
        int indice = conjPeriodos.indexOf(chamou);
        Periodo periodo;
        long tempo, tempoAnt;
        
        if(indice == 0){
            tempoAnt = this.inicio;
            System.out.println("tempoAnt" + tempoAnt);
        }else{
            tempoAnt = conjPeriodos.get(indice-1).getAte();
        }
             
        for(int i = indice; i<conjPeriodos.size(); i++){
             periodo = conjPeriodos.get(i);
             tempo = periodo.getDuracao();
             periodo.setAte(tempo+tempoAnt);
             JLabel teste = periodo.labelAte;
             teste.setText(Conversor.longToString2(periodo.getAte()));
             teste.repaint();
             tempoAnt = tempo;
        }
        
    }
}
