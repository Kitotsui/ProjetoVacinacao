package web.controlevacinacao.filter;

public class PessoaFilter {
    
    private Long codigo;
    private String nome;
    private String cpf;

    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    @Override
    public String toString() {
        return "codigo: " + codigo + "\nnome: " + nome + "\ncpf: " + cpf;
    }
}
