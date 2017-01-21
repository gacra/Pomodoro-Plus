package pomodoroplus;

import javax.swing.JLabel;

/**
 * Classe que representa um período de tempo do Programa
 * @author Guilherme
 */
public class Periodo implements Cloneable{
    //Número do período
    private int numero;
    //Nome do período
    private String nome;
    //Duração (em segundos) do período
    private long duracao;
    //Hora (em segundos) que o período acaba
    private long ate;

    /**
     * Cria um objeto Periodo.
     * @param numero Número do período.
     * @param labelAte Referência para a Label Ate da janela principal.
     */
    public Periodo(int numero){
        this.numero = numero;
        duracao = 0;
        ate = 0;
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
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        Periodo p = (Periodo) super.clone();
        p.nome = nome;
        return p;
    }
    
}
