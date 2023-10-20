/*
Pedro Henrique Weber Carvalhaes
Ralson Rwan dos Santos Lima
 */

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.Random;

public class CorteDaTora {

    private static void plotarGraficos(
            ArrayList<Integer> ns,
            ArrayList<Integer> valoresPD,
            ArrayList<Integer> valoresGreedy,
            ArrayList<Double> temposPD,
            ArrayList<Double> temposGreedy
    ) {

        // Convertendo ArrayList<Integer> para double[]
        double[] ns_double = ns.stream()
                .mapToDouble(Integer::doubleValue)
                .toArray();

        double[] valoresPD_double = valoresPD.stream()
                .mapToDouble(Integer::doubleValue)
                .toArray();
        double[] valoresGreedy_double = valoresGreedy.stream()
                .mapToDouble(Integer::doubleValue)
                .toArray();
        double[][] matriz1 = new double[][]{
                valoresPD_double,
                valoresGreedy_double
        };

        // Gráfico para Valor Total de Venda
        XYChart chart1 = QuickChart.getChart(
                "DP vs. Greedy",
                "n",
                "Valor total de venda",
                new String[]{"DP", "Greedy"},
                ns_double,  // Usando ns_double aqui
                matriz1
        );
        new SwingWrapper<>(chart1).displayChart();

        // Gráfico para Tempo de Processamento
        double[] temposPD_double = temposPD.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
        double[] temposGreedy_double = temposGreedy.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
        double[][] matriz2 = new double[][]{
                temposPD_double,
                temposGreedy_double
        };

        XYChart chart2 = QuickChart.getChart(
                "DP vs. Greedy",
                "n",
                "Tempo (ms)",
                new String[]{"DP", "Greedy"},
                ns_double,  // Usando ns_double aqui
                matriz2
        );
        new SwingWrapper<>(chart2).displayChart();
    }

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
        int inc = 1000;
        int fim = 20000;
        int stp = 1000;

        Random rand = new Random();
        AsciiTable at = new AsciiTable();

        ArrayList<Integer>  ns              = new ArrayList<>();
        ArrayList<Integer>  valoresPD       = new ArrayList<>();
        ArrayList<Integer>  valoresGreedy   = new ArrayList<>();
        ArrayList<Double>   temposPD        = new ArrayList<>();
        ArrayList<Double>   temposGreedy    = new ArrayList<>();

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


            ns.add(n);
            valoresPD.add(receitaPD);
            valoresGreedy.add(receitaGuloso);
            temposPD.add(tempoPD);
            temposGreedy.add(tempoGuloso);
        }
        at.addRule();

        String rend = at.render();
        System.out.println(rend);

        plotarGraficos(ns, valoresPD, valoresGreedy, temposPD, temposGreedy);
    }
}
