import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // informação de data deve
                                                                                             // ser exibido no formato
                                                                                             // dd/mm/aaaa;
    private static final DecimalFormat MOEDA = criarFormatoMoeda(); // informação de valor numérico deve ser exibida no
                                                                    // formatado com separador de milhar como ponto e
                                                                    // decimal como vírgula.
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public static void main(String[] args) {

        // 3.1 - Inserir todos os funcionários, na mesma ordem e informações da tabela
        // acima.
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios
                .add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios
                .add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        // 3.2 - Remover o funcionário “João” da lista.
        funcionarios.removeIf(funcionario -> "João".equals(funcionario.getNome()));

        // 3.3 - Imprimir todos os funcionários com todas as informações,
        System.out.println("\n===== 3.3 - Lista de funcionários e suas informações =====\n");
        funcionarios.forEach(funcionario -> System.out.println(formatar(funcionario)));

        // 3.4 - Os funcionários receberam 10% de aumento de salário, atualizar a lista
        // de funcionários com novo valor.
        funcionarios.forEach(funcionario -> funcionario.setSalario(aplicarAumento(funcionario.getSalario())));

        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.4 - Após aumento de 10% do salário =====\n");
        funcionarios.forEach(funcionario -> System.out.println(formatar(funcionario)));

        // 3.5 - Agrupar os funcionários por função em um MAP, sendo a chave a “função”
        // e o valor a “lista de funcionários”.
        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.5 - Funcionários agrupados =====\n");
        Map<String, List<Funcionario>> porFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        // 3.6 - Imprimir os funcionários, agrupados por função.
        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.6 - Funcionários agrupados por função =====\n");
        porFuncao.forEach((funcao, lista) -> {
            System.out.println(funcao + ":");
            lista.forEach(funcionario -> System.out.println("  " + formatar(funcionario)));
        });

        // 3.8 - Imprimir os funcionários que fazem aniversário no mês 10 e 12.
        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.8 - Funcionários que fazem aniversário em Outubro e Dezembro =====\n");
        funcionarios.stream()
                .filter(funcionario -> {
                    int mes = funcionario.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(funcionario -> System.out.println(formatar(funcionario)));

        // 3.9 - Imprimir o funcionário com a maior idade, exibir os atributos: nome e
        // idade.
        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.9 - Funcionário com maior idade =====\n");
        funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresent(funcionario -> System.out.println(
                        "Nome: " + funcionario.getNome() + " | Idade: "
                                + calcularIdade(funcionario.getDataNascimento())));

        // 3.10 - Imprimir a lista de funcionários por ordem alfabética.
        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.10 - Lista de funcionários em ordem alfabética =====\n");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(funcionario -> System.out.println(formatar(funcionario)));

        // 3.11 - Imprimir o total dos salários dos funcionários.
        BigDecimal total = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.11 - Total dos salários dos funcionários =====\n");
        System.out.println(MOEDA.format(total));

        // 3.12 - Imprimir quantos salários mínimos ganha cada funcionário, considerando
        // que o salário mínimo é R$1212.00.
        System.out.println("\n===============================================================================\n");
        System.out.println("\n===== 3.12 - Quantos salários mínimos por funcionário =====\n");
        funcionarios.forEach(funcionario -> {
            BigDecimal qtd = funcionario.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.println(funcionario.getNome() + ": " + MOEDA.format(qtd) + " salários mínimos");
        });
    }

    // Métodos

    private static DecimalFormat criarFormatoMoeda() {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        return new DecimalFormat("#,##0.00", simbolos);
    }

    private static BigDecimal aplicarAumento(BigDecimal salario) {
        return salario.multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);
    }

    private static int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private static String formatar(Funcionario funcionario) {
        return String.format("Nome: %s | Nascimento: %s | Salário: %s | Função: %s",
                funcionario.getNome(),
                funcionario.getDataNascimento().format(DATA),
                MOEDA.format(funcionario.getSalario()),
                funcionario.getFuncao());
    }
}