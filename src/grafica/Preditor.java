package grafica;

import opencv.ExtratorImagem;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class Preditor extends JDialog {
    private JPanel geral;
    private JButton btnSelecionarImagem;
    private JTextField txtCaminhoImagem;
    private JLabel lblImagem;
    private JButton btnClassificar;
    private JButton btnExtraicaracteristicas;
    private JLabel lblLaranjaBart;
    private JLabel lblAzulCalcao;
    private JLabel lblAzulSapato;
    private JLabel lblAzulHomer;
    private JLabel lblMarromHomer;
    private JLabel lblSapatoHomer;
    private JLabel lblNaiveBart;
    private JLabel lblNaiveHomer;
    private JLabel lblJ48Bart;
    private JLabel lblJ48Homer;
    private Instances instancias;

    public void carregaBaseWeka() throws Exception {
        DataSource ds = new DataSource("src/opencv/caracteristicas.arff");
        instancias = ds.getDataSet();
        instancias.setClassIndex(instancias.numAttributes() - 1);
    }

    public void defineCaracteristicas(Instance insta) {
        insta.setDataset(instancias);
        insta.setValue(0, Float.parseFloat(lblLaranjaBart.getText()));
        insta.setValue(1, Float.parseFloat(lblAzulCalcao.getText()));
        insta.setValue(2, Float.parseFloat(lblAzulSapato.getText()));
        insta.setValue(3, Float.parseFloat(lblAzulHomer.getText()));
        insta.setValue(4, Float.parseFloat(lblMarromHomer.getText()));
        insta.setValue(5, Float.parseFloat(lblSapatoHomer.getText()));
    }

    public void showResults(JLabel bart, JLabel homer, double result[]) {
        DecimalFormat df = new DecimalFormat("#,###.0000");
        bart.setText("Bart: " + df.format(result[0]));
        homer.setText("Homer: " + df.format(result[1]));
    }

    public void classifaNaiveBayes() throws Exception {
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(instancias);
        Instance novo = new DenseInstance(instancias.numAttributes());
        defineCaracteristicas(novo);
        double resultado[] = nb.distributionForInstance(novo);
        showResults(lblNaiveBart, lblNaiveHomer, resultado);
    }

    public void classificaJ48() throws Exception {
        J48 arvore = new J48();
        arvore.setUnpruned(true);
        arvore.buildClassifier(instancias);
        Instance novo = new DenseInstance(instancias.numAttributes());
        defineCaracteristicas(novo);
        double resultado[] = arvore.distributionForInstance(novo);
        showResults(lblJ48Bart, lblJ48Homer, resultado);
    }

    public Preditor() {
        setContentPane(geral);
        setModal(true);

        btnSelecionarImagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("src/imagens/");
                int retorno = fc.showDialog(fc, "Selecione a Imagem");
                if(retorno == JFileChooser.APPROVE_OPTION) {
                    File arquivo = fc.getSelectedFile();
                    txtCaminhoImagem.setText(arquivo.getAbsolutePath());

                    BufferedImage imagemBmp = null;
                    try {
                        imagemBmp = ImageIO.read(arquivo);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    ImageIcon imagemLabel = new ImageIcon(imagemBmp);
                    lblImagem.setIcon(
                            new ImageIcon(
                                    imagemLabel.getImage().getScaledInstance(
                                            lblImagem.getWidth(), lblImagem.getHeight(), Image.SCALE_DEFAULT
                                    )
                            )
                    );
                }
            }
        });
        btnExtraicaracteristicas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtratorImagem extrator = new ExtratorImagem();
                float[] caracteristicas = extrator.extrairCaracteristicaImagem(txtCaminhoImagem.getText());
                lblLaranjaBart.setText(String.valueOf(caracteristicas[0]));
                lblAzulCalcao.setText(String.valueOf(caracteristicas[1]));
                lblAzulSapato.setText(String.valueOf(caracteristicas[2]));
                lblAzulHomer.setText(String.valueOf(caracteristicas[3]));
                lblMarromHomer.setText(String.valueOf(caracteristicas[4]));
                lblSapatoHomer.setText(String.valueOf(caracteristicas[5]));
            }
        });
        btnClassificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carregaBaseWeka();
                    classifaNaiveBayes();
                    classificaJ48();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private static String getLookAndFeelClassName(String nameSnippet) {
        UIManager.LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info : plafs) {
            if (info.getName().contains(nameSnippet)) {
                return info.getClassName();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String className = getLookAndFeelClassName("Nimbus");
        try {
            UIManager.setLookAndFeel(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Preditor dialog = new Preditor();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }
}