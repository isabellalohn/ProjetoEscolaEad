public class ListaDeAlunos {

    private Aluno[] alunos;
    private int totalAlunos;

    public ListaDeAlunos(int capacidade) {
        this.alunos = new Aluno[capacidade];
        this.totalAlunos = 0;
    }

    public int getTotalAlunos() {
        return this.totalAlunos;
    }


    public Aluno getAluno(int i) {
        if (i < 0 || i >= this.totalAlunos) {
            return null;
        }
        return this.alunos[i];
    }


    public boolean adicionarAluno(Aluno a) {
        if (a == null) {
            return false;
        }
        if (this.totalAlunos >= this.alunos.length) {
            System.out.println("Lista cheia!");
            return false;
        }
        if (buscarPorCodigo(a.getCodigo()) != null) {
            System.out.println("Já existe aluno com o código " + a.getCodigo() + ".");
            return false;
        }
        this.alunos[this.totalAlunos] = a;
        this.totalAlunos++;
        return true;
    }


    public Aluno buscarPorCodigo(int codigo) {
        for (int i = 0; i < this.totalAlunos; i++) {
            if (this.alunos[i].getCodigo() == codigo) {
                return this.alunos[i];
            }
        }
        return null;
    }


    public void exibirLista() {
        if (this.totalAlunos == 0) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        for (int i = 0; i < this.totalAlunos; i++) {
            System.out.println("--------------------");
            this.alunos[i].exibeDados();
        }
        System.out.println("--------------------");
    }
}