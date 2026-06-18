public class ProgramacaoPorContratoExample {

    public static void main(String[] args) {
        ProgramacaoPorContratoExample example = new ProgramacaoPorContratoExample();
        example.emprestarLivro(1, 1, 14);
    }

    public void emprestarLivro(int usuarioId, int livroId, int maxDias) {
        assert usuarioId > 0 : "Usuário deve ser informado";
        assert livroId > 0 : "Livro deve ser informado";
        assert maxDias > 0 : "Quantidade máxima de dias deve ser positiva";

        System.out.println("Exemplo de programação por contrato executado com sucesso.");
    }
}