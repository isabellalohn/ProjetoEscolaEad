public class Aluno {
    private int codigo;
    private String nome;
    private String dataNascimento;
    private String email;
    private String senha;

    private Curso cursoMatriculado;

    private double[] notas = new double[3];
    private boolean[] lancada = new boolean[3];

    private Mensalidade[] mensalidades;
    private int numParcelas;

    public Aluno(int codigo, String nome, String dataNascimento, String email, String senha) {
        this.codigo = codigo;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
        this.mensalidades = new Mensalidade[0];
        this.numParcelas = 0;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Curso getCursoMatriculado() { return cursoMatriculado; }
    public void setCursoMatriculado(Curso cursoMatriculado) { this.cursoMatriculado = cursoMatriculado; }
    public int getNumParcelas() { return numParcelas; }

    public void exibeDados() {
        System.out.println("Código: " + codigo);
        System.out.println("Nome: " + nome);
        System.out.println("Data de nascimento: " + dataNascimento);
        System.out.println("E-mail: " + email);
        System.out.println("Senha: " + senha);
        if (cursoMatriculado != null) {
            System.out.println("Curso: " + cursoMatriculado.getNome());
        } else {
            System.out.println("Curso: (não matriculado)");
        }
    }

    public boolean lancarNota(int posicao, double nota) {
        if (posicao < 0 || posicao >= notas.length) return false;
        if (nota < 0 || nota > 10) return false;
        notas[posicao] = nota;
        lancada[posicao] = true;
        return true;
    }

    public void lancarNotas(double[] valores) {
        if (valores == null || valores.length != 3) {
            System.out.println("Informe exatamente 3 notas.");
            return;
        }
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] < 0 || valores[i] > 10) {
                System.out.println("Nota inválida (use valores entre 0 e 10). Nada foi gravado.");
                return;
            }
        }
        for (int i = 0; i < valores.length; i++) {
            lancarNota(i, valores[i]);
        }
    }

    public double calcularMedia() {
        double soma = 0;
        for (int i = 0; i < notas.length; i++) {
            soma += notas[i];
        }
        return soma / notas.length;
    }

    public void exibirNotas() {
        System.out.println("Notas de " + nome + ":");
        for (int i = 0; i < notas.length; i++) {
            if (lancada[i]) {
                System.out.printf("  Nota %d: %.2f%n", i + 1, notas[i]);
            } else {
                System.out.printf("  Nota %d: (não lançada)%n", i + 1);
            }
        }
        System.out.printf("  Média: %.2f%n", calcularMedia());
    }

    public void adicionarMensalidades(double[] valores) {
        numParcelas = valores.length;
        mensalidades = new Mensalidade[numParcelas];
        for (int i = 0; i < numParcelas; i++) {
            mensalidades[i] = new Mensalidade(valores[i], false);
        }
    }

    public void exibirMensalidades() {
        if (numParcelas == 0) {
            System.out.println("Nenhuma mensalidade cadastrada para este aluno.");
            return;
        }
        System.out.println("Mensalidades de " + nome + ":");
        for (int i = 0; i < numParcelas; i++) {
            String status = mensalidades[i].isPago() ? "PAGO" : "EM ABERTO";
            System.out.printf("  Parcela %d | R$ %.2f | %s%n", i + 1, mensalidades[i].getValor(), status);
        }
    }

    public void pagarMensalidade(int indice) {
        if (indice < 0 || indice >= numParcelas) {
            System.out.println("Parcela inexistente.");
            return;
        }
        if (mensalidades[indice].isPago()) {
            System.out.println("Esta parcela já está paga.");
            return;
        }
        mensalidades[indice].darBaixa();
        System.out.println("Parcela " + (indice + 1) + " paga com sucesso.");
    }
}