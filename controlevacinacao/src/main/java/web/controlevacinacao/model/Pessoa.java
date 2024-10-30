package web.controlevacinacao.model;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="pessoa")
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="geradorPessoa", sequenceName="pessoa_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="geradorPessoa", strategy = GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message="O nome da pessoa é obrigatório")
    private String nome;
    @NotBlank(message="O CPF da pessoa é obrigatório")
    private String cpf;
    @Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

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

    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pessoa other = (Pessoa) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "codigo: " + codigo + "\nnome: " + nome + "\ncpf: " + cpf;
    }
}