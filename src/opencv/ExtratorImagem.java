package opencv;


import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

public class ExtratorImagem {

    public float[] extrairCaracteristicaImagem(String caminho) {

        float laranjaCamisaBart = 0, azulCalcaoBart = 0, azulSapatoBart = 0;
        float azulCalcaHomer = 0, marromBocaHomer = 0, cinzaSapatoHomer = 0;
        double red, green, blue;
        float[] caracteristicas = new float[6];

        IplImage imagem = cvLoadImage(caminho);

        for (int altura = 0; altura < imagem.height(); altura++) {
            for (int largura = 0; largura < imagem.width(); largura++) {

                // Extração do RGB de cada pixel da imagem
                opencv_core.CvScalar scalarExtraiRgb = cvGet2D(imagem, altura, largura);
                blue = scalarExtraiRgb.val(0);
                green = scalarExtraiRgb.val(1);
                red = scalarExtraiRgb.val(2);

                // Camisa laranja do Bart
                if (blue >= 11 && blue <= 22 &&
                        green >= 85 && green <= 105 &&
                        red >= 240 && red <= 255) {
                    laranjaCamisaBart++;
                }

                // Calção azul do Bart (metade de baixo da imagem)
                if (altura > (imagem.height() / 2)) {
                    if (blue >= 125 && blue <= 170 && green >= 0 && green <= 12 && red >= 0 && red <= 20) {
                        azulCalcaoBart++;
                    }
                }

                // Sapato do Bart (parte inferior da imagem)
                if (altura > (imagem.height() / 2) + (imagem.height() / 3)) {
                    if (blue >= 125 && blue <= 140 && green >= 3 && green <= 12 && red >= 0 && red <= 20) {
                        azulSapatoBart++;
                    }
                }

                // Calça azul do Homer
                if (blue >= 150 && blue <= 180 && green >= 98 && green <= 120 && red >= 0 && red <= 90) {
                    azulCalcaHomer++;
                }

                // Boca do Homer (pouco mais da metade da imagem)
                if (altura < (imagem.height() / 2) + (imagem.height() / 3)) {
                    if (blue >= 95 && blue <= 140 && green >= 160 && green <= 185 && red >= 175 && red <= 200) {
                        marromBocaHomer++;
                    }
                }

                // Sapato do Homer (parte inferior da imagem)
                if (altura > (imagem.height() / 2) + (imagem.height() / 3)) {
                    if (blue >= 25 && blue <= 45 && green >= 25 && green <= 45 && red >= 25 && red <= 45) {
                        cinzaSapatoHomer++;
                    }
                }
            }
        }

        // Normaliza as características pelo número de pixels totais da imagem
        laranjaCamisaBart = (laranjaCamisaBart / (imagem.height() * imagem.width())) * 100;
        azulCalcaoBart = (azulCalcaoBart / (imagem.height() * imagem.width())) * 100;
        azulSapatoBart = (azulSapatoBart / (imagem.height() * imagem.width())) * 100;
        azulCalcaHomer = (azulCalcaHomer / (imagem.height() * imagem.width())) * 100;
        marromBocaHomer = (marromBocaHomer / (imagem.height() * imagem.width())) * 100;
        cinzaSapatoHomer = (cinzaSapatoHomer / (imagem.height() * imagem.width())) * 100;

        caracteristicas[0] = laranjaCamisaBart;
        caracteristicas[1] = azulCalcaoBart;
        caracteristicas[2] = azulSapatoBart;
        caracteristicas[3] = azulCalcaHomer;
        caracteristicas[4] = marromBocaHomer;
        caracteristicas[5] = cinzaSapatoHomer;

        return caracteristicas;

    }

}