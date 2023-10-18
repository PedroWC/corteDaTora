/*
Pedro Henrique Weber Carvalhaes
Ralson Rwan dos Santos Lima
 */

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.Random;

public class CorteDaTora {

    public static int receitaMaximaPD(int[] precos, int comprimento) {
        int[] dp = new int[comprimento + 1];
        dp[0] = 0;

        for (int i = 1; i <= comprimento; i++) {
            int valorMax = Integer.MIN_VALUE;
            for (int j = 1; j <= i; j++) {
                valorMax = Math.max(valorMax, precos[j - 1] + dp[i - j]);
            }
            dp[i] = valorMax;
        }

        return dp[comprimento];
    }

    public static int receitaMaximaGuloso(int[] precos, int comprimento) {
        int receita = 0;
        while (comprimento > 0) {
            int densidadeMax = Integer.MIN_VALUE;
            int melhorCorte = 0;

            for (int i = 1; i <= comprimento; i++) {
                int densidade = precos[i - 1] / i;
                if (densidade > densidadeMax) {
                    densidadeMax = densidade;
                    melhorCorte = i;
                }
            }

            receita += precos[melhorCorte - 1];
            comprimento -= melhorCorte;
        }

        return receita;
    }

    public static void main(String[] args) {
        int inc = 100;
        int fim = 2000;
        int stp = 100;

        Random rand = new Random();
        AsciiTable at = new AsciiTable();

        at.addRule();
        at.addRow("n", "vDP", "tDP", "vGreedy", "tGreedy", "%").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for (int n = inc; n <= fim; n += stp) {
            int[] precos = new int[n];
            for (int i = 0; i < n; i++) {
                precos[i] = rand.nextInt(10) + 1;  // Preços aleatórios entre 1 e 10
            }

            long inicioPD = System.nanoTime();
            int receitaPD = receitaMaximaPD(precos, n);
            long fimPD = System.nanoTime();

            long inicioGuloso = System.nanoTime();
            int receitaGuloso = receitaMaximaGuloso(precos, n);
            long fimGuloso = System.nanoTime();

            double tempoPD = (fimPD - inicioPD) / 1e6;
            double tempoGuloso = (fimGuloso - inicioGuloso) / 1e6;

            double porcentagemAcerto = ((double) receitaGuloso / receitaPD) * 100;

            at.addRow(
                    n,
                    receitaPD,
                    String.format("%.6f", tempoPD),
                    receitaGuloso,
                    String.format("%.6f", tempoGuloso),
                    String.format("%.2f", porcentagemAcerto)
            ).setTextAlignment(TextAlignment.CENTER);
        }
        at.addRule();

        String rend = at.render();
        System.out.println(rend);
    }
}
