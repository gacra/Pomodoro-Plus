package pomodoroplus;

import javax.swing.JLabel;

/**
 * Classe que representa um período de tempo do Programa
 * @author Guilherme
 */
public class Periodo{
    //Número do período
    private int numero;
    //Nome do período
    private String nome;
    //Duração (em segundos) do período
    private long duracao;
    //Hora (em segundos) que o período acaba
    private long ate;
    //Ponteiro para labelAte do Painel
    public JLabel labelAte;

    /**
     * Cria um objeto Periodo.
     * @param numero Número do período.
     * @param labelAte Referência para a Label Ate da janela principal.
     */
    public Periodo(int numero, JLabel labelAte){
        this.numero = numero;
        duracao = 0;
        ate = 0;
        this.labelAte = labelAte;
    }
    
    public long getDuracao(){
        return duracao;
    }

    public long getAte(){
        return ate;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setDuracao(long duracao){
        this.duracao = duracao;
    }

    public void setAte(long ate){
        this.ate = ate;
    }

    @Override
    public String toString(){
        return "Periodo{" + "numero=" + numero + ", nome=" + nome + ", duracao=" + duracao + ", ate=" + ate + '}';
    }
    
    
}
