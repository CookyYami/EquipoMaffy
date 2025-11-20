/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vista;

/**
 *
 * @author HP
 */
public class PanelInicio extends javax.swing.JPanel {

    /**
     * Creates new form PanelInicio
     */
    public PanelInicio() {
        initComponents();
    }

   
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        lblDesc = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        // Set overall panel background to baby-pink and adjust spacing
        setBackground(new java.awt.Color(255, 182, 193));

        jPanel1.setBackground(new java.awt.Color(255, 182, 193));
        jPanel1.setLayout(null);
        // Try local file first (user-specified); if missing, fall back to remote image; then to bundled resource; finally emoji.
        try {
            java.io.File localFile = new java.io.File("C:\\Users\\USUARIO\\Pictures\\fondologin.png");
            javax.swing.ImageIcon icon;
            if (localFile.exists() && localFile.isFile()) {
                icon = new javax.swing.ImageIcon(localFile.getAbsolutePath());
            } else {
                // fallback to remote image
                java.net.URL remote = new java.net.URL("https://katpetkovich.com/cdn/shop/files/3_4f834ad2-aef7-4e61-8170-258db7dbbdef_550x.jpg?v=1635204951");
                icon = new javax.swing.ImageIcon(remote);
            }

            java.awt.Image img = icon.getImage().getScaledInstance(940, 600, java.awt.Image.SCALE_SMOOTH);
            lblImage.setIcon(new javax.swing.ImageIcon(img));
            lblImage.setBounds(0, 0, 940, 600);
            lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            lblImage.setOpaque(true);
            jPanel1.add(lblImage);
        } catch (Exception ex) {
            // Try bundled resource next
            java.net.URL imgUrl = getClass().getResource("/welcome.png");
            if (imgUrl != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgUrl);
                java.awt.Image img = icon.getImage().getScaledInstance(940, 600, java.awt.Image.SCALE_SMOOTH);
                lblImage.setIcon(new javax.swing.ImageIcon(img));
                lblImage.setBounds(0, 0, 940, 600);
                lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblImage.setOpaque(true);
                jPanel1.add(lblImage);
            } else {
                lblImage.setFont(new java.awt.Font("Segoe UI Emoji", 0, 96));
                lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblImage.setText("🍰");
                lblImage.setBounds(410, 100, 120, 120);
                jPanel1.add(lblImage);
            }
        }

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 40));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Bienvenido a Maffy");
        lblTitle.setBounds(0, 20, 940, 60);
        // Ensure title appears above the background image
        jPanel1.setComponentZOrder(lblTitle, 0);
        jPanel1.add(lblTitle);

        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblSubtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitle.setText("Tu sistema de gestión de pedidos y productos");
        lblSubtitle.setBounds(0, 240, 940, 36);
        jPanel1.add(lblSubtitle);

        lblDesc.setFont(new java.awt.Font("Segoe UI", 0, 18));
        lblDesc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDesc.setText("Administra tu inventario, pedidos y clientes de forma sencilla.");
        lblDesc.setBounds(0, 290, 940, 28);
        jPanel1.add(lblDesc);

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 940, 600));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblDesc;
    // End of variables declaration//GEN-END:variables
}
