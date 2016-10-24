package GUI;

import Application.ClientApplication;
import javax.swing.JFrame;
import javax.swing.Timer;
import utils.MyUtils;

/**
 *
 * @author Javi
 */
public class MainFrame extends JFrame {
    
    private LoginDialog loginDialog;
    private Timer echoLoop;
    private ClientApplication application;
    
     public MainFrame(ClientApplication applicaction) {
        this.application = applicaction;
        initComponents();
        MyUtils.center((JFrame) this);
    }

    public MainFrame() {
    }
    
    public ClientApplication getApplication() {
        return application;
    }
    
    /**
     * Lanza dialogo de login o para ack, dependiendo de la condición que se cumpla.
     *
     * @param 
     * @return
     * @throws 
     */
    private void doLogin() {
        if (application.getSessionId() == null) {
            loginDialog = new LoginDialog(this, true);
//            if (!loginDialog.isLoginOk()) {
//                dispose();
//            }
        } else {
//            jMenuItemLogout.setEnabled(true);
//            stopACK();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        doLogin();
    }//GEN-LAST:event_formWindowActivated

    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
