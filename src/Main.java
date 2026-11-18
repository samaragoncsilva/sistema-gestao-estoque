import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String option = "";

        do {
            System.out.println("\n====== DISTRIBUIDORA - SISTEMA DE GESTÃO DE ESTOQUE ======\n");
            System.out.println("1 - Fazer login");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha uma opção: ");

            if (!input.hasNextLine()) {
                System.out.println("\nEntrada encerrada.");
                break;
            }
            option = input.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.println("\nAinda não implementado.");
                    break;
                case "0":
                    System.out.println("\nEncerrando o sistema.");
                    break;
                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }
        } while (!option.equals("0"));

        input.close();
    }
}