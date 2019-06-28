package io.alangodoi.thundercast.Helper;

public interface dataUpdater {

    void updateProgressBar(int value);

    // 0 - Stopped | 1 - Playing | 2 - Paused
    void playerStatus(int value);
//        void processFinish(String output);
//        void placaEnviada(String output);
//        void dadosImpresao(
//                String SIS_PARAMETRO,
//                String SIS_PARAMETRO2,
//                String SIS_PARAMETRO3,
//                String SIS_PARAMETRO4,
//                String CABECALHO,
//                String RODAPE);
//        void limpaBanco(String output);
}
