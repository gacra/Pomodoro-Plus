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
     * @param nome Nome do período
     * @param duracao Duração (em segundos) do período
     * @param ate Hora (em segundos) que o período acaba
     */
    public Periodo(int numero, String nome, long duracao, long ate){
        this.numero = numero;
        this.nome = nome;
        this.duracao = duracao;
        this.ate = ate;
    }
    
    /**
     * Cria um objeto Periodo.
     * @param numero Número do período.
     */
    public Periodo(int numero){
        this(numero, null, 0, 0);
    }
    
    public long getDuracao(){
        return duracao;
    }

    public long getAte(){
        return ate;
    }

    public String getNome(){
        return nome;
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
