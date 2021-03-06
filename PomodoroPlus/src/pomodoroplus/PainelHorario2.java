package pomodoroplus;

import javax.swing.JLabel;

/**
 * Painel usado pelo cronômetro regressivo para tempos menores que 1h.
 * Possui campos de minutos e segundos.
 * @author Guilherme
 */
public class PainelHorario2 extends javax.swing.JPanel{

    private boolean linux;
    
    /**
     * Creates new form PainelHorario2
     */
    public PainelHorario2(boolean linux){
        this.linux = linux;
        initComponents();
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        minSeg = new javax.swing.JLabel();

        setBackground(new java.awt.Color(238, 241, 246));

        if(!linux){
            minSeg.setFont(new java.awt.Font("Century Gothic", 1, 180)); // NOI18N
        }else{
            minSeg.setFont(new java.awt.Font("Century Gothic", 1, 144)); // NOI18N
        }
        minSeg.setText("00:00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(minSeg)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(minSeg, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public JLabel getMinSeg(){
        return minSeg;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel minSeg;
    // End of variables declaration//GEN-END:variables
}
