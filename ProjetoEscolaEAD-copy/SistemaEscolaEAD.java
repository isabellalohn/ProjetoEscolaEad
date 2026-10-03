import java.util.Scanner;

public class SistemaEscolaEAD {

    private static final int SEMESTRES = 2;
    private static final int CURSOS_POR_SEMESTRE = 3;
    private static final int PARCELAS = 12;

    private Scanner leitor = new Scanner(System.in);
    private ListaDeAlunos lista = new ListaDeAlunos(50);

    private Curso[][] matrizCursos = new Curso[SEMESTRES][CURSOS_POR_SEMESTRE];

    public static void main(String[] args) {
        SistemaEscolaEAD sistema = new SistemaEscolaEAD();
        sistema.cadastrarCursos();
        sistema.executar();
    }

    private int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(this.leitor.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, digite um número inteiro.");
            }
        }
    }

    private double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Double.parseDouble(this.leitor.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, digite um número.");
            }
        }
    }

    private String lerTexto(String mensagem) {
        String texto;
        do {
            System.out.print(mensagem);
            texto = this.leitor.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Campo obrigatório.");
            }
        } while (texto.isEmpty());
        return texto;
    }


    public void executar() {
        int opcao;
        do {
            System.out.println();
            System.out.println("=== MENU ===");
            System.out.println("1 - Visualizar Lista de Alunos");
            System.out.println("2 - Adicionar Aluno");
            System.out.println("3 - Sair");
            System.out.println("4 - Verificar Notas do Aluno");
            System.out.println("5 - Verificar Financeiro do Aluno");
            opcao = lerInt("Escolha: ");

            switch (opcao) {
                case 1:
                    this.lista.exibirLista();
                    exibirCursosEAlunos();
                    break;
                case 2:
                    adicionarAluno();
                    break;
                case 3:
                    System.out.println("Encerrando o sistema...");
                    break;
                case 4:
                    verificarNotas();
                    break;
                case 5:
                    verificarFinanceiro();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 3);
    }


    private void cadastrarCursos() {
        System.out.println("=== CADASTRO DE CURSOS ===");
        int total = SEMESTRES * CURSOS_POR_SEMESTRE;
        int quantidade;
        do {
            quantidade = lerInt("Quantos cursos deseja cadastrar (1 a " + total + ")? ");
        } while (quantidade < 1 || quantidade > total);

        for (int i = 0; i < quantidade; i++) {
            System.out.println("Curso " + (i + 1) + ":");

            int semestre;
            do {
                semestre = lerInt("  Semestre (1 a " + SEMESTRES + "): ") - 1;
            } while (semestre < 0 || semestre >= SEMESTRES);

            int coluna = proximaColunaLivre(semestre);
            if (coluna == -1) {
                System.out.println("  Esse semestre já está cheio. Escolha outro.");
                i--;
                continue;
            }

            int codigo = lerInt("  Código: ");
            if (buscarCurso(codigo) != null) {
                System.out.println("  Já existe curso com esse código. Tente novamente.");
                i--;
                continue;
            }

            String nome = lerTexto("  Nome: ");

            int duracao;
            do {
                duracao = lerInt("  Duração (horas): ");
                if (duracao <= 0) {
                    System.out.println("  A duração deve ser maior que zero.");
                }
            } while (duracao <= 0);

            this.matrizCursos[semestre][coluna] = new Curso(codigo, nome, duracao);
        }
        System.out.println("Cursos cadastrados com sucesso!");
    }


    private int proximaColunaLivre(int semestre) {
        for (int j = 0; j < CURSOS_POR_SEMESTRE; j++) {
            if (this.matrizCursos[semestre][j] == null) {
                return j;
            }
        }
        return -1;
    }

    private Curso buscarCurso(int codigo) {
        for (int i = 0; i < SEMESTRES; i++) {
            for (int j = 0; j < CURSOS_POR_SEMESTRE; j++) {
                if (this.matrizCursos[i][j] != null
                        && this.matrizCursos[i][j].getCodigo() == codigo) {
                    return this.matrizCursos[i][j];
                }
            }
        }
        return null;
    }

    private void listarCursosDisponiveis() {
        System.out.println("Cursos disponíveis:");
        for (int i = 0; i < SEMESTRES; i++) {
            for (int j = 0; j < CURSOS_POR_SEMESTRE; j++) {
                Curso curso = this.matrizCursos[i][j];
                if (curso != null) {
                    System.out.println("  [" + curso.getCodigo() + "] " + curso.getNome()
                            + " (" + curso.getDuracao() + "h) - Semestre " + (i + 1));
                }
            }
        }
    }


    private void exibirCursosEAlunos() {
        System.out.println();
        System.out.println("=== CURSOS E ALUNOS MATRICULADOS ===");
        boolean achou = false;
        for (int i = 0; i < SEMESTRES; i++) {
            for (int j = 0; j < CURSOS_POR_SEMESTRE; j++) {
                Curso curso = this.matrizCursos[i][j];
                if (curso == null) {
                    continue;
                }
                achou = true;
                curso.exibeDados();
                System.out.println("Alunos matriculados:");
                int contador = 0;
                for (int k = 0; k < this.lista.getTotalAlunos(); k++) {
                    Aluno aluno = this.lista.getAluno(k);
                    if (aluno.getCursoMatriculado() == curso) {
                        System.out.println("- " + aluno.getNome() + " (código " + aluno.getCodigo() + ")");
                        contador++;
                    }
                }
                if (contador == 0) {
                    System.out.println("(nenhum)");
                }
                System.out.println();
            }
        }
        if (!achou) {
            System.out.println("Nenhum curso cadastrado.");
        }
    }


    private void adicionarAluno() {
        int codigo = lerInt("Código: ");
        if (this.lista.buscarPorCodigo(codigo) != null) {
            System.out.println("Já existe aluno com esse código.");
            return;
        }
        String nome = lerTexto("Nome: ");
        String nascimento = lerTexto("Data de nascimento (dd/mm/aaaa): ");
        String email = lerTexto("Email: ");
        if (!email.contains("@")) {
            System.out.println("Email inválido. Aluno não cadastrado.");
            return;
        }
        String senha = lerTexto("Senha: ");
        String resposta = lerTexto("É bolsista? (s/n): ");

        Aluno novo;
        if (resposta.equalsIgnoreCase("s")) {
            String tipo = lerTexto("Tipo de bolsa: ");
            novo = new AlunoBolsista(codigo, nome, nascimento, email, senha, tipo);
        } else {
            novo = new Aluno(codigo, nome, nascimento, email, senha);
        }

 
        listarCursosDisponiveis();
        int codCurso = lerInt("Código do curso (0 para não matricular agora): ");
        if (codCurso != 0) {
            Curso curso = buscarCurso(codCurso);
            if (curso == null) {
                System.out.println("Curso não encontrado. Aluno ficará sem curso.");
            } else {
                novo.setCursoMatriculado(curso);
            }
        }

        double valor = lerDouble("Valor da mensalidade (" + PARCELAS + " parcelas): R$ ");
        if (valor <= 0) {
            System.out.println("Valor inválido. Aluno não cadastrado.");
            return;
        }
        double[] valores = new double[PARCELAS];
        for (int i = 0; i < valores.length; i++) {
            valores[i] = valor;
        }
        novo.adicionarMensalidades(valores);

        if (this.lista.adicionarAluno(novo)) {
            System.out.println("Aluno cadastrado com sucesso!");
        }
    }



    private void verificarNotas() {
        Aluno aluno = this.lista.buscarPorCodigo(lerInt("Código do aluno: "));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        aluno.exibirNotas();

        String resposta = lerTexto("Deseja lançar/atualizar as notas? (s/n): ");
        if (resposta.equalsIgnoreCase("s")) {
            double[] valores = new double[3];
            for (int i = 0; i < valores.length; i++) {
                valores[i] = lerDouble("Nota " + (i + 1) + " (0 a 10): ");
            }
            aluno.lancarNotas(valores);
            aluno.exibirNotas();
        }
    }


    private void verificarFinanceiro() {
        Aluno aluno = this.lista.buscarPorCodigo(lerInt("Código do aluno: "));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        aluno.exibirMensalidades();
        if (aluno.getNumParcelas() == 0) {
            return;
        }
        String resposta = lerTexto("Deseja pagar alguma parcela? (s/n): ");
        if (resposta.equalsIgnoreCase("s")) {
            int parcela = lerInt("Número da parcela: ");
            aluno.pagarMensalidade(parcela - 1);
            aluno.exibirMensalidades();
        }
    }
}