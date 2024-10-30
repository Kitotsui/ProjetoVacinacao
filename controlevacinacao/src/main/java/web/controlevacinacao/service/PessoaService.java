package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.repository.PessoaRepository;

@Service
@Transactional
public class PessoaService {

    private PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository vacinaRepository) {
        this.pessoaRepository = vacinaRepository;
    }

    public void salvar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    public void alterar(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    public void remover(Pessoa pessoa) {
        pessoaRepository.delete(pessoa);
    }

}
